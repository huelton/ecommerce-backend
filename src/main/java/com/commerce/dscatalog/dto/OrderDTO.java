package com.commerce.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Instant createDate;
	private Instant updateDate;
	private String statusOrder;
	private DeliveryAddressDTO deliveryAddress;
	private UserOrderDTO user;
	private Set<OrderItemDTO> items = new HashSet<>();	
	private Double total;

	public OrderDTO() {
	}

	public OrderDTO(Long id, Instant createDate, Instant updateDate,String statusOrder, DeliveryAddressDTO deliveryAddress, UserOrderDTO user,
			Set<OrderItemDTO> items, Double total) {
		this.id = id;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.statusOrder = statusOrder;
		this.deliveryAddress = deliveryAddress;
		this.user = user;
		this.items = items;
		this.total = total;
	}
	
	public OrderDTO(OrderInsertDTO orderInsertDTO) {
		createDate = orderInsertDTO.getCreateDate();
		statusOrder = orderInsertDTO.getStatusOrder();	
		orderInsertDTO.getItems().forEach(oi -> this.items.add(oi));	
	}
	
	public OrderDTO(OrderUpdateDTO orderUpdateDTO) {
		statusOrder = orderUpdateDTO.getStatusOrder();
		updateDate = orderUpdateDTO.getUpdateDate();
		orderUpdateDTO.getItems().forEach(oi -> this.items.add(oi));	
	}
	
	public OrderDTO(OrderRemoveItemDTO orderRemoveItemDTO) {
		updateDate = orderRemoveItemDTO.getUpdateDate();
		orderRemoveItemDTO.getItems().forEach(oi -> this.items.add(oi));	
	}
	
	public OrderDTO(Order order) {
		id = order.getId();
		createDate = order.getCreateDate();
		updateDate = order.getUpdateDate();
		statusOrder = order.getStatus().getStatusType();
		deliveryAddress = new DeliveryAddressDTO(order.getDeliveryAddress());
		user = new UserOrderDTO(order.getUser());
		total = order.getTotalValue();		
	}
	
	public OrderDTO(Order entity, Set<OrderItem> orderItem) {
		this(entity);
		orderItem.forEach(oi -> this.items.add(new OrderItemDTO(oi)));	
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}
	
	public Instant getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Instant updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(String statusOrder) {
		this.statusOrder = statusOrder;
	}

	public DeliveryAddressDTO getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddressDTO deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public UserOrderDTO getUser() {
		return user;
	}

	public void setUser(UserOrderDTO user) {
		this.user = user;
	}

	public Set<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<OrderItemDTO> items) {
		this.items = items;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDTO other = (OrderDTO) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
 
}