package com.commerce.dscatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	
	Optional<Status> findByStatusType(String name);
}
