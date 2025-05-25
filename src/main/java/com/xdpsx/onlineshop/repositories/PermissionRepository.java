package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Permission;
import com.xdpsx.onlineshop.entities.enums.PermissionName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {
    Optional<Permission> findByName(PermissionName name);
    List<Permission> findByNameIn(Set<PermissionName> names);
}
