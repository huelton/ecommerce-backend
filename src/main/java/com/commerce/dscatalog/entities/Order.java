package com.commerce.dscatalog.entities;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_order")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant createDate;
	
	private Instant updateDate;
	
	@ManyToOne()
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne()
	@JoinColumn(name = "address_id")
	private DeliveryAddress deliveryAddress;

	@OneToMany(mappedBy = "id.order", fetch = FetchType.EAGER)
	private Set<OrderItem> items = new HashSet<>();

	public Order() {
	}

	public Order(Long id, Instant createDate,Instant updateDate, User user, DeliveryAddress deliveryAddress, Status status) {
		this.id = id;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.user = user;
		this.deliveryAddress = deliveryAddress;
		this.status = status;
	}

	public double getTotalValue() {
		double sum = 0.0;
		for (OrderItem orderItem : items) {
			sum += orderItem.getSubtotal();
		}
		return sum;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Set<OrderItem> getItems() {
		return items;
	}
	
	public void addItems(OrderItem item) {
		this.items.add(item);
	}
	
	public void removeItems(OrderItem item) {
		this.items.add(item);
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuilder builder = new StringBuilder();
		builder.append("Order Number: ");
		builder.append(getId());
		builder.append(", create date: ");
		builder.append(sdf.format(getCreateDate()));
		builder.append(", user: ");
		//builder.append(getUser().getUsername());
		builder.append("\nDetails:\n");
		for (OrderItem itemPedido : getItems()) {
			builder.append(itemPedido.toString());
		}
		builder.append("Total Value Order: ");
		builder.append(nf.format(getTotalValue()));
		return builder.toString();
	}

}