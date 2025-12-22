package com.presight.gateway.config.security;

import com.presight.common_lib.security.LocalUsersProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalUsersProperties.class)
public class YamlUserDetailsService implements ReactiveUserDetailsService {

    private final LocalUsersProperties localUsersProperties;
    private final PasswordEncoder passwordEncoder; // injected

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.justOrEmpty(
                        localUsersProperties.getUsers().stream()
                                .filter(u -> u.getUsername().equals(username))
                                .findFirst()
                )
                .map(u -> User.withUsername(u.getUsername())
                        .password(passwordEncoder.encode(u.getPassword())) // See note below on encoding
                        .roles(u.getRoles().split(","))
                        .build()
                )
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username)));
    }
}

