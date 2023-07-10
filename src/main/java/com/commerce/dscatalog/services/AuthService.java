package com.commerce.dscatalog.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.EmailDTO;
import com.commerce.dscatalog.dto.NewPasswordDTO;
import com.commerce.dscatalog.entities.PasswordRecover;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.PasswordRecoveryRepository;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.services.exceptions.EmailException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

	private final static String SUBJECT_EMAIL = "Recuperação Senha";

	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;

	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordRecoveryRepository passwordRecoveryRepository;

	@Autowired
	private EmailService emailService;

	@Transactional
	public void createRecoverToken(EmailDTO dto) {

		User user = userRepository.findByEmail(dto.getEmail());
		if (user == null) {
			throw new EmailException("Email not Found");
		}

		String token = UUID.randomUUID().toString();

		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(dto.getEmail());
		entity.setToken(UUID.randomUUID().toString());
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
		entity = passwordRecoveryRepository.save(entity);

		String body = "Acesse o link para definir uma nova senha\n\n" 
		         + recoverUri + token + ". \n\n"
		         +"Validade de "+ tokenMinutes + " minutos";

		emailService.sendEmail(dto.getEmail(), SUBJECT_EMAIL, body);

	}

	@Transactional
	public void saveNewPassword(NewPasswordDTO newPasswordDTO) {
		
		List<PasswordRecover> result = passwordRecoveryRepository.searchValidTokens(newPasswordDTO.getToken(), Instant.now());
		
		if(result.isEmpty()) {
			throw new ResourceNotFoundException("Token Not found");
		}
		
		User user =  userRepository.findByEmail(result.get(0).getEmail());
		user.setPassword(passwordEncoder.encode(newPasswordDTO.getPassword()));
		user = userRepository.save(user);
	}

}
