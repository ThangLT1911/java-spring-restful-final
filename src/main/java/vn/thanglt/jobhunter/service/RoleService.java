package vn.thanglt.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Permission;
import vn.thanglt.jobhunter.domain.Role;
import vn.thanglt.jobhunter.repository.PermissionRepository;
import vn.thanglt.jobhunter.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Optional<Role> fetchRoleById(long id) {
        return this.roleRepository.findById(id);
    }

    public Role createRole(Role role) {
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }
}
