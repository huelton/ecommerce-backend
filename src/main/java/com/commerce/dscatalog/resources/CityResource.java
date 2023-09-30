package com.commerce.dscatalog.resources;

import java.net.URI;

import com.commerce.dscatalog.resources.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.commerce.dscatalog.dto.CityDTO;
import com.commerce.dscatalog.services.CityService;

import jakarta.validation.Valid;

@Tag(name = "Cities", description = "Cities Endpoints")
@RestController
@RequestMapping(value = "/cities")
public class CityResource {

	@Autowired
	private CityService service;

	@Operation(
			summary = "Retrieve all Cities",
			description = "Get City object . The response is a List with object City with id, name.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CityDTO.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@GetMapping
	public ResponseEntity<Page<CityDTO>> findAll(Pageable pageable) {
		Page<CityDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@Operation(
			summary = "Retrieve City by Id",
			description = "Get City object . The response is a one object City with id, name.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CityDTO.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@GetMapping(value = "/{id}")
	public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
		CityDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@Operation(
			summary = "Insert City",
			description = "Post City object . The request with object City with name and response the object City with id, name")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CityDTO.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping
	public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO cityDTO) {
		CityDTO dto = service.insert(cityDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@Operation(
			summary = "Update City",
			description = "Put City object . The request with object City with name and response is Update object City with id, name with parameter ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CityDTO.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<CityDTO> update(@Valid @PathVariable Long id, @RequestBody CityDTO dto) {
		CityDTO newDTO = service.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}

	@Operation(
			summary = "Delete City",
			description = "Delete City object . The service remove the object City with parameter ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
} 
