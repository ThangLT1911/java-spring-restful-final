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

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    public Role createRole(Role role) {
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(Permission::getId).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }

    public Role updateRole(Role role) {
        // check role
        Role roleDb = this.fetchRoleById(role.getId());
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(Permission::getId).collect(Collectors.toList());
            List<Permission> dbPermission = this.permissionRepository.findByIdIn(reqPermissions);
            roleDb.setPermissions(dbPermission);
        }

        roleDb.setName(role.getName());
        roleDb.setDescription(role.getDescription());
        roleDb.setActive(role.isActive());
        roleDb.setPermissions(role.getPermissions());
        roleDb = this.roleRepository.save(roleDb);
        return roleDb;
    }
}
