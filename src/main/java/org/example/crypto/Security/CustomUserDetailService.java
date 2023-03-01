package org.example.crypto.Security;

import lombok.RequiredArgsConstructor;
import org.example.crypto.model.UserEntity;
import org.example.crypto.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new User(user.getUsername(), user.getSecretKey(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

    }
}
