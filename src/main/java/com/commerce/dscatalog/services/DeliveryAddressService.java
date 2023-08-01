package com.commerce.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.DeliveryAddressDTO;
import com.commerce.dscatalog.entities.City;
import com.commerce.dscatalog.entities.DeliveryAddress;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.CityRepository;
import com.commerce.dscatalog.repositories.DeliveryAddressRepository;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DeliveryAddressService {

	@Autowired
	private DeliveryAddressRepository deliveryAddressRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public Page<DeliveryAddressDTO> findAllPaged(Pageable pageable) {
		Page<DeliveryAddress> list = deliveryAddressRepository.findAll(pageable);
		return list.map(x -> new DeliveryAddressDTO(x));
	}

	@Transactional(readOnly = true)
	public DeliveryAddressDTO findById(Long id) {
		Optional<DeliveryAddress> obj = deliveryAddressRepository.findById(id);
		DeliveryAddress entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new DeliveryAddressDTO(entity);
	}

	@Transactional
	public DeliveryAddressDTO insert(DeliveryAddressDTO dto) {
		DeliveryAddress entity = new DeliveryAddress();
		copyDtoToEntity(dto, entity);
		entity = deliveryAddressRepository.save(entity);
		return new DeliveryAddressDTO(entity);
	}

	@Transactional
	public DeliveryAddressDTO update(Long id, DeliveryAddressDTO dto) {
		try {
			DeliveryAddress entity = deliveryAddressRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = deliveryAddressRepository.save(entity);
			return new DeliveryAddressDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!deliveryAddressRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		try {
			deliveryAddressRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(DeliveryAddressDTO dto, DeliveryAddress entity) {

		entity.setStreet(dto.getStreet());
		entity.setNumber(dto.getNumber());
		entity.setComplement(dto.getComplement());
		entity.setNeighborhood(dto.getNeighborhood());
		entity.setZipCode(dto.getZipCode());
		entity.setAvaliable(dto.getAvaliable());

		Optional<City> obj1 = cityRepository.findById(dto.getCityId());
		City city = obj1.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		entity.setCity(city);

		Optional<User> obj2 = userRepository.findById(dto.getUserId());
		User user = obj2.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		entity.setUser(user);

	}
	
	public void accessMethod(DeliveryAddressDTO dto, DeliveryAddress entity) {
		copyDtoToEntity(dto, entity);
	}

}