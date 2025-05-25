package com.xdpsx.onlineshop.runner;

import com.xdpsx.onlineshop.entities.Permission;
import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.enums.PermissionName;
import com.xdpsx.onlineshop.entities.enums.RoleName;
import com.xdpsx.onlineshop.repositories.PermissionRepository;
import com.xdpsx.onlineshop.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RolePermissionInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private static final Set<PermissionName> ROLE_ADMIN_PERMISSIONS = new HashSet<>(
            Arrays.asList(
                    PermissionName.VIEW_ALL,
                    PermissionName.CREATE_ALL,
                    PermissionName.UPDATE_ALL,
                    PermissionName.DELETE_ALL
            )
    );

    @Override
    public void run(ApplicationArguments args) {
        initPermission();
        initRole(RoleName.ADMIN, "Administrator with full access", ROLE_ADMIN_PERMISSIONS);
        initRole(RoleName.USER, "User", null);
    }

    private void initPermission() {
        for (PermissionName permissionName : PermissionName.values()) {
            permissionRepository.findByName(permissionName)
                    .orElseGet(() -> {
                        Permission permission = Permission.builder()
                                .name(permissionName)
                                .description(permissionName.name().replace('_', ' ').toLowerCase())
                                .build();
                        return permissionRepository.save(permission);
                    });
        }
    }

    private void initRole(RoleName roleName, String description, Set<PermissionName> permissionNames) {
        if (roleRepository.existsByName(roleName)) {
            return;
        }

        List<Permission> permissions = permissionRepository.findByNameIn(permissionNames);

        Role role = Role.builder()
                .name(roleName)
                .description(description)
                .permissions(new HashSet<>(permissions))
                .build();

        roleRepository.save(role);
    }
}
