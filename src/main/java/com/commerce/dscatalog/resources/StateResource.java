package com.commerce.dscatalog.resources;

import java.net.URI;

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

import com.commerce.dscatalog.dto.StateDTO;
import com.commerce.dscatalog.dto.UserDTO;
import com.commerce.dscatalog.dto.UserInsertDTO;
import com.commerce.dscatalog.dto.UserUpdateDTO;
import com.commerce.dscatalog.services.StateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/states")
public class StateResource {

	@Autowired
	private StateService service;
	
	@GetMapping
	public ResponseEntity<Page<StateDTO>> findAll(Pageable pageable) {
		Page<StateDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<StateDTO> findById(@PathVariable Long id) {
		StateDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<StateDTO> insert(@Valid @RequestBody StateDTO stateDTO) {
		StateDTO dto = service.insert(stateDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<StateDTO> update(@Valid @PathVariable Long id, @RequestBody StateDTO dto) {
		StateDTO newDTO = service.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
} 
