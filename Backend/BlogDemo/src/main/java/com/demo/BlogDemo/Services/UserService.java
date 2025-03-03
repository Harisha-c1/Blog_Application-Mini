package com.demo.BlogDemo.Services;

import com.demo.BlogDemo.Model.Role;
import com.demo.BlogDemo.Model.UserEntity;
import com.demo.BlogDemo.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


    @Service
    public class UserService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public void registerUser(String username, String password, Role role) {

            if (userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username already exists!");
            }
            UserEntity users = new UserEntity();
            users.setUsername(username);
            users.setPassword(passwordEncoder.encode(password));
            users.setRole(role !=null ? role: Role.USER);
            userRepository.save(users);
        }
    }


