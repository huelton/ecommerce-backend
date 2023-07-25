package com.commerce.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	//@Query("SELECT obj FROM Order obj JOIN FETCH obj.user WHERE obj.status.statusType = :status AND obj.user.id = :userId ")
	Page<Order> findByStatus(String status, Long userId, Pageable pageable);
}