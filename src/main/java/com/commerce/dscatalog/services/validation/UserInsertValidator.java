package com.commerce.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import com.commerce.dscatalog.dto.UserInsertDTO;
import com.commerce.dscatalog.resources.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Override
	public boolean isValid(UserInsertDTO value, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		//Coloque aqui seus teste de validação. acrescentando objetos FieldMessage a lista
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
			.addPropertyNode(e.getFieldName())
			.addConstraintViolation();
		}
		
		return list.isEmpty();
	}

}
