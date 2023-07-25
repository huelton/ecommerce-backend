package com.commerce.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.CityDTO;
import com.commerce.dscatalog.entities.City;
import com.commerce.dscatalog.entities.State;
import com.commerce.dscatalog.repositories.CityRepository;
import com.commerce.dscatalog.repositories.StateRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	@Transactional(readOnly = true)
	public Page<CityDTO> findAllPaged(Pageable pageable) {
		Page<City> list = cityRepository.findAll(pageable);
		return list.map(x -> new CityDTO(x));
	}

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		Optional<City> obj = cityRepository.findById(id);
		City entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		copyDtoToEntity(dto, entity);
		entity = cityRepository.save(entity);
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = cityRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = cityRepository.save(entity);
			return new CityDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!cityRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		try {
			cityRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(CityDTO dto, City entity) {
		entity.setCityName(dto.getCityName());		
		State state = stateRepository.findByStateName(dto.getStateName());
		if(state == null) {
			throw new ResourceNotFoundException("State not found " + dto.getStateName());
		}
		entity.setState(state);

	}

}