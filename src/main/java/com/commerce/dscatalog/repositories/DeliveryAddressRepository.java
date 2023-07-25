package com.commerce.dscatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.DeliveryAddress;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

@Query("SELECT da FROM DeliveryAddress da WHERE da.user.id = :userId")
 Optional<DeliveryAddress> findAddressFromUserId(Long userId);

}
