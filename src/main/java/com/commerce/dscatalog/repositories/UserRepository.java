package com.commerce.dscatalog.repositories;

import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.entities.projetcions.UserDetailsProjection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
	
	@Query(nativeQuery = true, value = """ 
			SELECT tb_user.email as username, tb.user.password, tb_role.id as roleId, tb_role.authority
			FROM tb_user
			INNER_JOIN tb_user_role ON tb_user.id =  tb_user_role.user_id
			INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
			WHERE tb_user.email = : email
		""")
	List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
