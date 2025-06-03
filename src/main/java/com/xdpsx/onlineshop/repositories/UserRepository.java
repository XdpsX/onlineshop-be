package com.xdpsx.onlineshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xdpsx.onlineshop.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(
            """
		SELECT u FROM User u
		LEFT JOIN FETCH u.roles r
		LEFT JOIN FETCH r.permissions
		WHERE u.email = :email
		""")
    Optional<User> findByEmailWithAuthorities(@Param("email") String email);
}
