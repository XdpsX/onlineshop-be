package com.xdpsx.onlineshop.runner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.xdpsx.onlineshop.entities.Permission;
import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.PermissionName;
import com.xdpsx.onlineshop.entities.enums.RoleName;
import com.xdpsx.onlineshop.repositories.PermissionRepository;
import com.xdpsx.onlineshop.repositories.RoleRepository;
import com.xdpsx.onlineshop.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RolePermissionInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Set<PermissionName> ROLE_ADMIN_PERMISSIONS = new HashSet<>(Arrays.asList(
            PermissionName.VIEW_ALL, PermissionName.CREATE_ALL, PermissionName.UPDATE_ALL, PermissionName.DELETE_ALL));

    @Override
    public void run(ApplicationArguments args) {
        initPermission();
        initRole(RoleName.ADMIN, "Administrator with full access", ROLE_ADMIN_PERMISSIONS);
        initRole(RoleName.USER, "User", null);

        initUser(
                User.builder()
                        .name("Admin")
                        .email("admin@xdpsx.com")
                        .password(passwordEncoder.encode("12345678"))
                        .enabled(true)
                        .build(),
                new HashSet<>(List.of(RoleName.ADMIN)));
    }

    private void initPermission() {
        for (PermissionName permissionName : PermissionName.values()) {
            permissionRepository.findByName(permissionName).orElseGet(() -> {
                Permission permission = Permission.builder()
                        .name(permissionName)
                        .description(permissionName.name().replace('_', ' ').toLowerCase())
                        .build();
                log.info("Creating permission {}", permissionName);
                return permissionRepository.save(permission);
            });
        }
    }

    private void initRole(RoleName roleName, String description, Set<PermissionName> permissionNames) {
        if (roleRepository.existsByName(roleName)) {
            return;
        }

        log.info("Initializing role {}", roleName);
        List<Permission> permissions = permissionRepository.findByNameIn(permissionNames);

        Role role = Role.builder()
                .name(roleName)
                .description(description)
                .permissions(new HashSet<>(permissions))
                .build();

        roleRepository.save(role);
    }

    private void initUser(User user, Set<RoleName> roleNames) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return;
        }
        log.info("Initializing user: {}", user.getEmail());
        Set<Role> roles = new HashSet<>();
        for (RoleName roleName : roleNames) {
            Role role = roleRepository
                    .findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
