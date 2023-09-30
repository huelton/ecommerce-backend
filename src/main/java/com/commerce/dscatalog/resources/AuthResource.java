package com.commerce.dscatalog.resources;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.resources.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.dscatalog.dto.EmailDTO;
import com.commerce.dscatalog.dto.NewPasswordDTO;
import com.commerce.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Authentication Endpoints")
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private AuthService authService;

	@Operation(
			summary = "Recover Token",
			description = "Send new token to Email . When request is done you will receive email with the new token")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO emailDTO) {
		authService.createRecoverToken(emailDTO);
		return ResponseEntity.noContent().build();
	}

	@Operation(
			summary = "New Password",
			description = "Send new password with Token. . When request is done your new password will update into database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PutMapping(value = "/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO) {
		authService.saveNewPassword(newPasswordDTO);
		return ResponseEntity.noContent().build();
	}

}
