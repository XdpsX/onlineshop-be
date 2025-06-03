package com.xdpsx.onlineshop.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.enums.RoleName;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);
}
