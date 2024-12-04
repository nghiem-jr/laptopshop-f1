package com.junior.laptopshop.service;

import org.springframework.stereotype.Service;

import com.junior.laptopshop.domain.User;
import com.junior.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        User junior = this.userRepository.save(user);
        System.out.println(junior);
        return junior;
    }
}
