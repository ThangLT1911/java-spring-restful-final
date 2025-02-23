package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        User newUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if(id >= 1500) {
            throw new IdInvalidException("id khong lon hoac bang hon 1500");
        }
        this.userService.handleDeleteUser(id);
//        return ResponseEntity.status(HttpStatus.OK).body("id: " + id);
        return ResponseEntity.ok("id: " + id);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User getUser = this.userService.handleFecthUserById(id);
//        return ResponseEntity.status(HttpStatus.OK).body(getUser);
        return ResponseEntity.ok(getUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(User user) {
        return ResponseEntity.ok(this.userService.handleGetAllUser(user));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User updateUser) {
        User updateUsers = this.userService.handleUpdateUser(updateUser);
//        return ResponseEntity.status(HttpStatus.OK).body(updateUsers);
        return ResponseEntity.ok(updateUsers);
    }
}
