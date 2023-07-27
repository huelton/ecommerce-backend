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

import com.commerce.dscatalog.dto.DeliveryAddressDTO;
import com.commerce.dscatalog.services.DeliveryAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/address")
public class DeliveryAddressResource {

	@Autowired
	private DeliveryAddressService service;
	
	@GetMapping
	public ResponseEntity<Page<DeliveryAddressDTO>> findAll(Pageable pageable) {
		Page<DeliveryAddressDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<DeliveryAddressDTO> findById(@PathVariable Long id) {
		DeliveryAddressDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<DeliveryAddressDTO> insert(@Valid @RequestBody DeliveryAddressDTO deliveryAddressDTO) {
		DeliveryAddressDTO dto = service.insert(deliveryAddressDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<DeliveryAddressDTO> update(@Valid @PathVariable Long id, @RequestBody DeliveryAddressDTO dto) {
		DeliveryAddressDTO newDTO = service.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
} 
