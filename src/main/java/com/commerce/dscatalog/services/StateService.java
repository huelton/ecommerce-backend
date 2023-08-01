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
import com.commerce.dscatalog.dto.StateDTO;
import com.commerce.dscatalog.entities.City;
import com.commerce.dscatalog.entities.State;
import com.commerce.dscatalog.repositories.CityRepository;
import com.commerce.dscatalog.repositories.StateRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StateService {

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Transactional(readOnly = true)
	public Page<StateDTO> findAllPaged(Pageable pageable) {
		Page<State> list = stateRepository.findAll(pageable);
		return list.map(x -> new StateDTO(x, x.getCities()));
	}

	@Transactional(readOnly = true)
	public StateDTO findById(Long id) {
		Optional<State> obj = stateRepository.findById(id);
		State entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new StateDTO(entity, entity.getCities());
	}

	@Transactional
	public StateDTO insert(StateDTO dto) {
		State entity = new State();
		copyDtoToEntity(dto, entity);
		entity = stateRepository.save(entity);
		return new StateDTO(entity);
	}

	@Transactional
	public StateDTO update(Long id, StateDTO dto) {
		try {
			State entity = stateRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = stateRepository.save(entity);
			return new StateDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!stateRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		try {
			stateRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(StateDTO dto, State entity) {

		entity.setStateName(dto.getStateName());
		entity.getCities().clear();
		for (CityDTO cityDTO : dto.getCities()) {
			City city = cityRepository.getReferenceById(cityDTO.getId());
			entity.getCities().add(city);
		}
	}
	
	public void accessMethod(StateDTO dto, State entity) {
		copyDtoToEntity(dto, entity);
	}

}