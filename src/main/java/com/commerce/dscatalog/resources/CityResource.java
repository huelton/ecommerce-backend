package com.commerce.dscatalog.resources;

import java.net.URI;

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
	
	@GetMapping
	public ResponseEntity<Page<CityDTO>> findAll(Pageable pageable) {
		Page<CityDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
		CityDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO cityDTO) {
		CityDTO dto = service.insert(cityDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<CityDTO> update(@Valid @PathVariable Long id, @RequestBody CityDTO dto) {
		CityDTO newDTO = service.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
} 
