package com.commerce.dscatalog.dto;

import java.io.Serializable;

import com.commerce.dscatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    @NotBlank(message = "Campo Obrigatorio")
    private String firstName;
    
    private String lastName;
    
    @Email(message = "Favor entrar com email valido")
    private String email;

    public UserOrderDTO(){}

    public UserOrderDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserOrderDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
