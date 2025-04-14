package vn.thanglt.jobhunter.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Permission;
import vn.thanglt.jobhunter.domain.Skill;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.repository.PermissionRepository;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean existsByModuleAndApiPathAndMethod(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
    }

    public Permission handleCreatePermission(@Valid Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission handleGetPermissionById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        return permissionOptional.orElse(null);
    }

    public Permission handleUpdatePermission(Permission permission) {
        Permission currentPermission = this.handleGetPermissionById(permission.getId());
        if (currentPermission != null) {
            currentPermission.setName(permission.getName());
            currentPermission.setApiPath(permission.getApiPath());
            currentPermission.setMethod(permission.getMethod());
            currentPermission.setModule(permission.getModule());

            currentPermission = this.permissionRepository.save(currentPermission);
            return currentPermission;
        }
        return null;
    }

    public boolean isSameName(Permission permission) {
        Permission permissionDB = this.handleGetPermissionById(permission.getId());
        if (permissionDB != null) {
            if (permissionDB.getName().equals(permission.getName())) ;
            return true;
        }
        return false;
    }

    public ResultPaginationDTO getAllPermission(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pagePermission.getContent());
        return rs;
    }
}
