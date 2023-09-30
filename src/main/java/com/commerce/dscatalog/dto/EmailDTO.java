package com.commerce.dscatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Email DTO Information")
public class EmailDTO {

	@Schema(nullable = true, description = "User Email", example = "aplicacao@provider.com")
	@NotBlank(message="Campo obrigatorio")
	@Email(message="Email invalido")
	private String email;
	
	public EmailDTO() {
	}

	public EmailDTO(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}
