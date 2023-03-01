package org.example.crypto.controller;

import lombok.RequiredArgsConstructor;
import org.example.crypto.dto.LoginDto;
import org.example.crypto.dto.RegisterDto;
import org.example.crypto.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/register", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        if (authService.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("username already taken", HttpStatus.BAD_GATEWAY);
        }

        return ResponseEntity.ok(authService.register(registerDto));
    }


    @PostMapping(value = "/login", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> authenticate(@RequestBody LoginDto request) {

        return ResponseEntity.ok(Map.of("token", authService.auth(request).getToken()));
    }
}
