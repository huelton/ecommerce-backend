package com.commerce.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderRemoveItemDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private Instant updateDate = Instant.now();
	
	private Set<OrderItemDTO> items = new HashSet<>();	

	public OrderRemoveItemDTO() {
	}

	public OrderRemoveItemDTO(Instant updateDate, Set<OrderItemDTO> items,  String statusOrder) {
		this.updateDate = updateDate;		
		this.items = items;
	}	

	public Set<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<OrderItemDTO> items) {
		this.items = items;
	}
	
	public Instant getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Instant updateDate) {
		this.updateDate = updateDate;
	}
 
}