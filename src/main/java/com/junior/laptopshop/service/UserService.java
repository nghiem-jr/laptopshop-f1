package com.junior.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.junior.laptopshop.domain.User;
import com.junior.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleSaveUser(User user) {
        User junior = this.userRepository.save(user);
        System.out.println(junior);
        return junior;
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }
}
