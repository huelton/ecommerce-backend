package com.commerce.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.dscatalog.dto.EmailDTO;
import com.commerce.dscatalog.dto.NewPasswordDTO;
import com.commerce.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private AuthService authService;

	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> createRevocerToken(@Valid @RequestBody EmailDTO emailDTO) {
		authService.createRecoverToken(emailDTO);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO) {
		authService.saveNewPassword(newPasswordDTO);
		return ResponseEntity.noContent().build();
	}

}
