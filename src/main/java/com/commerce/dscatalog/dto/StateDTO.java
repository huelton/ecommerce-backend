package com.commerce.dscatalog.dto;

import java.util.ArrayList;
import java.util.List;

import com.commerce.dscatalog.entities.City;
import com.commerce.dscatalog.entities.State;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StateDTO {

	private Long id;
	
	@Size(min=5, max=60, message = "Campo deve ter entre 3 e 60 caracteres")
	@NotBlank(message = "Campo Obrigatorio")
	private String stateName;
	
	private List<CityDTO> cities = new ArrayList<>();
	
	public StateDTO() {
	}

	public StateDTO(Long id, String stateName) {
		this.id = id;
		this.stateName = stateName;

	}
	
	public StateDTO(State state) {
		id = state.getId();
		stateName = state.getStateName();
	}
	
	public StateDTO(State state, List<City> cities) {
		this(state);
		cities.forEach(city -> this.cities.add(new CityDTO(city)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public List<CityDTO> getCities() {
		return cities;
	}
	
	public void setCities(List<CityDTO> cities) {
		this.cities = cities;
	}
	
}