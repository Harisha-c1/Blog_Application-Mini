package com.demo.BlogDemo.Services;


import com.demo.BlogDemo.Model.UserEntity;
import com.demo.BlogDemo.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database using UserRepository (returns Optional<UserEntity>)
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        UserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        // Map UserEntity to Spring Security User (which implements UserDetails)
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
