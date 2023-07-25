package com.commerce.dscatalog.dto;

import java.io.Serializable;
import java.util.Objects;

import com.commerce.dscatalog.entities.OrderItem;

public class OrderItemDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long orderId;
	private Long productId;
	private Double discount;
	private Integer quantity;
	private Double price;	
	private Double subTotal;

	public OrderItemDTO() {
	}	

	public OrderItemDTO(Long orderId, Long productId, Double discount, Integer quantity, Double price) {
		this.orderId = orderId;
		this.productId = productId;
		this.discount = discount;
		this.quantity = quantity;
		this.price = price;
	}

	public OrderItemDTO(OrderItem orderItem) {
		orderId = orderItem.getId().getOrder().getId();
		productId = orderItem.getId().getProduct().getId();
		discount = orderItem.getDiscount();
		quantity = orderItem.getQuantity();
		price = orderItem.getPrice();
		subTotal = orderItem.getSubtotal();
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Double getSubTotal() {
		return subTotal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItemDTO other = (OrderItemDTO) obj;
		return Objects.equals(orderId, other.orderId) && Objects.equals(productId, other.productId);
	}
 
}