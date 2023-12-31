package com.commerce.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.commerce.dscatalog.dto.UserInsertDTO;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.resources.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Autowired
	private UserRepository repository;
	
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		//Coloque aqui seus teste de validação. acrescentando objetos FieldMessage a lista
		
		User user = repository.findByEmail(dto.getEmail());
		
		if(user != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
			.addPropertyNode(e.getFieldName())
			.addConstraintViolation();
		}
		
		return list.isEmpty();
	}

}
