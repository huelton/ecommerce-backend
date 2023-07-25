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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.commerce.dscatalog.dto.OrderDTO;
import com.commerce.dscatalog.dto.OrderInsertDTO;
import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.projections.ProductProjection;
import com.commerce.dscatalog.services.OrderService;
import com.commerce.dscatalog.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

	@Autowired
	private OrderService service;
	
	@GetMapping
	public ResponseEntity<Page<OrderDTO>> findAll(
			Pageable pageable) {
		Page<OrderDTO> list = service.findPageOrder(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
		OrderDTO dto = service.findOrder(id);
		return ResponseEntity.ok().body(dto);
	}

	//@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
	@PostMapping
	public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderInsertDTO dto) {
		OrderDTO newDTO= service.insertOrder(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}

} 
