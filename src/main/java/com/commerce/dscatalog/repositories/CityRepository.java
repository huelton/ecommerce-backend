package com.commerce.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
