package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user")
    public User createNewUser(@RequestBody User postManUser) {
        User newUser = this.userService.handleCreateUser(postManUser);
        return newUser;
    }

    @DeleteMapping("user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "id: " + id;
    }

    @GetMapping("user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return this.userService.handleFecthUserById(id);
    }

    @GetMapping("user")
    public List<User> getAllUser(User user) {
        return this.userService.handleGetAllUser(user);
    }

    @PutMapping("user")
    public User updateUser(@RequestBody User updateUser) {
        User updateUsers = this.userService.handleUpdateUser(updateUser);
        return updateUsers;
    }
}
