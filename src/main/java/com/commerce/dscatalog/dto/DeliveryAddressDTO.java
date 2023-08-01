package com.commerce.dscatalog.dto;

import java.io.Serializable;

import com.commerce.dscatalog.entities.DeliveryAddress;

public class DeliveryAddressDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String zipCode;
	
	private Boolean avaliable;
	private Long userId;
	private Long cityId;

	public DeliveryAddressDTO() {
	}

	public DeliveryAddressDTO(Long id, String street, String number, String complement, String neighborhood,
			String zipCode, Boolean avaliable, Long userId, Long cityId) {

		this.id = id;
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.neighborhood = neighborhood;
		this.zipCode = zipCode;
		this.avaliable = avaliable;
		this.userId = userId;
		this.cityId = cityId;
	}
	
	public DeliveryAddressDTO(DeliveryAddress da) {

		id = da.getId();
		street = da.getStreet();
		number = da.getNumber();
		complement = da.getComplement();
		neighborhood = da.getNeighborhood();
		zipCode = da.getZipCode();
		avaliable = da.getAvaliable();
		userId = da.getUser().getId();
		cityId = da.getCity().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Boolean getAvaliable() {
		return avaliable;
	}
	
	public void setAvaliable(Boolean avaliable) {
		this.avaliable = avaliable;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
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
		DeliveryAddressDTO other = (DeliveryAddressDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
 
}