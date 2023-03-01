package org.example.crypto.service;


import com.mifmif.common.regex.Generex;
import lombok.RequiredArgsConstructor;
import org.example.crypto.Security.JwtService;
import org.example.crypto.dto.LoginDto;
import org.example.crypto.dto.RegisterDto;
import org.example.crypto.dto.ResponseDto;
import org.example.crypto.model.Role;
import org.example.crypto.model.UserEntity;
import org.example.crypto.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String generateKey() {
        Generex generex = new Generex("[a-zA-Z0-9]{40}");
        return generex.random();
    }

    public ResponseDto register(RegisterDto request) {

        String secretKey = generateKey();
        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(Role.USER)
                .secretKey(secretKey)
                .build();

        userRepo.save(user);


        var jwtToken = jwtService.generateToken(new User(user.getUsername(), user.getSecretKey(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));

        return ResponseDto
                .builder()
                .token(jwtToken)
                .key(secretKey)
                .build();
    }

    public ResponseDto auth(LoginDto request) {
        UserEntity user = findBySecretKey(request.getSecret_key());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getSecretKey()
        ));

        var jwtToken = jwtService.generateToken(new User(user.getUsername(), user.getSecretKey(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));

        return ResponseDto
                .builder()
                .token(jwtToken)
                .build();
    }

    public UserEntity findBySecretKey(String secretKey) {
        return userRepo.findBySecretKey(secretKey).orElseThrow(() -> new UsernameNotFoundException("key not found"));
    }


    public UserDetails loadUserBySecretKey(String key) {
        UserEntity user = userRepo.findBySecretKey(key).orElseThrow(() -> new UsernameNotFoundException("key not found"));

        return new User(user.getUsername(), user.getSecretKey(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }


    public Boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

}
