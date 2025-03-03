package com.demo.BlogDemo.DTO;

import com.demo.BlogDemo.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Role role;

}
