package com.demo.BlogDemo.Controller;

import com.demo.BlogDemo.DTO.JwtResponse;
import com.demo.BlogDemo.DTO.LoginRequest;
import com.demo.BlogDemo.DTO.SucessMessage;
import com.demo.BlogDemo.Model.Role;
import com.demo.BlogDemo.Model.UserEntity;
import com.demo.BlogDemo.Model.UserRepository;
import com.demo.BlogDemo.Security.Jwtutil;
import com.demo.BlogDemo.DTO.RegisterRequest;
import com.demo.BlogDemo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Jwtutil jwtutil;
    @Autowired
    private UserRepository userRepository;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid role case
            return ResponseEntity.badRequest().body("Invalid role provided.");
        }
        userService.registerUser(request.getUsername(), request.getPassword(), role);
        return ResponseEntity.ok(new SucessMessage("Successfull"));
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            UserEntity user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtutil.generateToken(request.getUsername());
            return ResponseEntity.ok(new JwtResponse(token,user.getRole()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}