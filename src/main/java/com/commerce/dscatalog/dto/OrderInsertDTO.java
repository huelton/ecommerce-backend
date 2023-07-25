package com.commerce.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderInsertDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Instant createDate = Instant.now();
	private Long userId;
	@JsonIgnore
	private String statusOrder;
	
	private Set<OrderItemDTO> items = new HashSet<>();	

	public OrderInsertDTO() {
	}

	public OrderInsertDTO(Long userId,Set<OrderItemDTO> items,  String statusOrder) {
		this.userId = userId;
		this.items = items;
		this.statusOrder = statusOrder;
	}	

	public Instant getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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