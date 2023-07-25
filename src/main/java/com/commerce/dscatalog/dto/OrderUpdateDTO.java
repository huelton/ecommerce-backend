package com.commerce.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderUpdateDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private Instant updateDate = Instant.now();

	private String statusOrder;
	
	private Set<OrderItemDTO> items = new HashSet<>();	

	public OrderUpdateDTO() {
	}

	public OrderUpdateDTO(Instant updateDate, Long userId,Set<OrderItemDTO> items,  String statusOrder) {
		this.items = items;
		this.statusOrder = statusOrder;
		this.updateDate = updateDate;
	}	
	
	public Instant getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Instant updateDate) {
		this.updateDate = updateDate;
	}

	public Set<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<OrderItemDTO> items) {
		this.items = items;
	}
	
	public String getStatusOrder() {
		return statusOrder;
	}
	
	public void setStatusOrder(String statusOrder) {
		this.statusOrder = statusOrder;
	}
	
 
}