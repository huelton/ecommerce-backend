package com.commerce.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
	
	State findByStateName(String name);
}
