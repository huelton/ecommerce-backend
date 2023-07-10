package com.commerce.dscatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.commerce.dscatalog.dto.EmailDTO;
import com.commerce.dscatalog.entities.PasswordRecover;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.PasswordRecoveryRepository;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.services.exceptions.EmailException;

@Service
public class AuthService {

	private final static String SUBJECT_EMAIL = "Recuperação Senha";

	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;

	@Value("${email.password-recover.uri}")
	private String recoverUri;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordRecoveryRepository passwordRecoveryRepository;

	@Autowired
	private EmailService emailService;

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

}
