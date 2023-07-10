package com.commerce.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class NewPasswordDTO {
    
    @NotBlank(message = "Campo Obrigatorio")
    private String token;   
    
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
