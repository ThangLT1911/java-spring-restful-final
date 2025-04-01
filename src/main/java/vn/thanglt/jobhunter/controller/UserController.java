package vn.thanglt.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.User;
import vn.thanglt.jobhunter.domain.response.ResCreateUserDTO;
import vn.thanglt.jobhunter.domain.response.ResUpdateUserDTO;
import vn.thanglt.jobhunter.domain.response.ResUserDTO;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.service.UserService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.checkEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + postManUser.getEmail() + " da ton tai, vui long su dung email khac");
        }
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.handleFecthUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("Id " + id + " khong ton tai");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User getUser = this.userService.handleFecthUserById(id);
        if (getUser == null) {
            throw new IdInvalidException("Id " + id + " khong ton tai");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(getUser));
    }

    @GetMapping("/users")
    @ApiMessage("Get all User")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updateUser = this.userService.handleUpdateUser(user);
        if (updateUser == null) {
            throw new IdInvalidException("Id " + user.getId() + " khong ton tai");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(updateUser));
    }
}
