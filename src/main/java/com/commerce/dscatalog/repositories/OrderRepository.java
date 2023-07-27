package com.commerce.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	Page<Order> findByStatus(String status, Long userId, Pageable pageable);
}