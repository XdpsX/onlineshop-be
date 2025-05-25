package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.enums.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
