package com.commerce.dscatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "NewPassword DTO Information")
public class NewPasswordDTO {

	@Schema(nullable = true, description = "Token", example = "gyCzZSh0YQsWsYF5nXF6ZFJH0sRiY#6qCGsGYaAGCUr6wJQdt5gfhDOKrAIu0bPT0x3#eiR*1mF")
    @NotBlank(message = "Campo Obrigatorio")
    private String token;

	@Schema(nullable = true, description = "User New Password", example = "r6wJQdt5gfhD2VcsaQ")
    @NotBlank(message = "Campo Obrigatorio")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$", 
    message = "Deve conter ao menos um numero, "
    		+ "Deve conter ao menos uma letra minúscula, "
    		+ "Deve conter ao menos uma letra maiúscula, "
    		+ "Deve conter ao menos um caractere especial, "
    		+ "Deve conter no minino 8 caracteres" )
    private String password;

    public NewPasswordDTO() {
    }

	public NewPasswordDTO(String token, String password) {
		this.token = token;
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public String getPassword() {
		return password;
	}    

}
