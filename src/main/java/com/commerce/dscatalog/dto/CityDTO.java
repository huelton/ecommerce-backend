package com.commerce.dscatalog.dto;

import com.commerce.dscatalog.entities.City;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CityDTO {

	private Long id;
	
	@Size(min=5, max=60, message = "Campo deve ter entre 3 e 60 caracteres")
	@NotBlank(message = "Campo Obrigatorio")
	private String cityName;
	private String stateName;
	
	public CityDTO(Long id, String cityName) {
		this.id = id;
		this.cityName = cityName;

	}
	
	public CityDTO(City city) {
		id = city.getId();
		cityName = city.getCityName();
		stateName = city.getState().getStateName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getStateName() {
		return stateName;
	}
	
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	
}
