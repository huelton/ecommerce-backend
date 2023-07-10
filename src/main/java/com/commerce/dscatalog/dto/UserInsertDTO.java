package com.commerce.dscatalog.dto;

import com.commerce.dscatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private static final long serialVersionUID = 1L;
    
    
    @NotBlank(message = "Campo Obrigatorio")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$", 
    message = "Deve conter ao menos um numero, "
    		+ "Deve conter ao menos uma letra minúscula, "
    		+ "Deve conter ao menos uma letra maiúscula, "
    		+ "Deve conter ao menos um caractere especial, "
    		+ "Deve conter no minino 8 caracteres" )
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
