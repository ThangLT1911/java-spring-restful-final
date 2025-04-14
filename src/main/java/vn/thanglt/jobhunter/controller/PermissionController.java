package vn.thanglt.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Permission;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.service.PermissionService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a new permission")
    public ResponseEntity<Permission> handleCreateSkill(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if (this.permissionService.existsByModuleAndApiPathAndMethod(permission)) {
            throw new IdInvalidException("Permission " + permission.getName() + " da ton tai");
        }
        Permission newPermission = this.permissionService.handleCreatePermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
    }

    @PutMapping("/permissions")
    @ApiMessage("update a permission")
    public ResponseEntity<Permission> handleUpdatePermission(@RequestBody Permission updatePermission) throws IdInvalidException {
        // check id
        if (this.permissionService.handleGetPermissionById(updatePermission.getId()) == null) {
            throw new IdInvalidException("Permission co id = " + updatePermission.getId() + " khong ton tai");
        }

        // check module, apiPath, method
        if (this.permissionService.existsByModuleAndApiPathAndMethod(updatePermission)) {
            // check name
            if (this.permissionService.isSameName(updatePermission)) {
                throw new IdInvalidException("Permission đã tồn tại.");
            }
        }

        return ResponseEntity.ok().body(this.permissionService.handleUpdatePermission(updatePermission));
    }


    @GetMapping("/permissions")
    @ApiMessage("Get all permission")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> specification, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.getAllPermission(specification, pageable));
    }
}
