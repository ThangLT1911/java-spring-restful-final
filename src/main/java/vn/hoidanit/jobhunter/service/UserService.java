package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

<<<<<<< HEAD
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
=======
    public void handleCreateUser(User user) {
        this.userRepository.save(user);
>>>>>>> d595634b97035564f1a26a596d35f59a035a86b2
    }
}
