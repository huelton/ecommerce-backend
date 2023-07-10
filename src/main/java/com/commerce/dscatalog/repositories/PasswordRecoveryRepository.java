package com.commerce.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.dscatalog.entities.PasswordRecover;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecover, Long> {

}
