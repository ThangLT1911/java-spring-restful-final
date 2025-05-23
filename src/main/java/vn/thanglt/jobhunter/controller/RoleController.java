package vn.thanglt.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Role;
import vn.thanglt.jobhunter.service.RoleService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        boolean isRoleExist = this.roleService.existByName(role.getName());
        if (isRoleExist) {
            throw new IdInvalidException("Role " + role.getName() + " da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role currentRole = this.roleService.fetchRoleById(role.getId());
        if (currentRole == null) {
            throw new IdInvalidException("Role not found");
        }

        boolean isRoleExist = this.roleService.existByName(role.getName());
        if (isRoleExist) {
            throw new IdInvalidException("Role " + role.getName() + " da ton tai");
        }
        return ResponseEntity.ok().body(this.roleService.updateRole(role));
    }
}
