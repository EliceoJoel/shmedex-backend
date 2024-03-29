package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.exception.RegistrationException;
import com.eherbas.shmedex.model.AuthResponse;
import com.eherbas.shmedex.service.implement.AuthService;
import com.eherbas.shmedex.dto.LoginDTO;
import com.eherbas.shmedex.dto.RegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        try {
            AuthResponse response =authService.register(request);
            return  ResponseEntity.ok(response);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
