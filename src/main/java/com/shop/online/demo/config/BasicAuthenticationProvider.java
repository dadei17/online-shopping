package com.shop.online.demo.config;

import com.shop.online.demo.model.User;
import com.shop.online.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> userFromDb = userService.findByLogin(name);
        if (userFromDb.isEmpty()) {
            log.warn("User is not found: User name: {}", name);
            throw new InsufficientAuthenticationException("Invalid user credentials");
        }
        User user = userFromDb.get();
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            log.warn("Invalid user credentials: User name: {}", name);
            throw new InsufficientAuthenticationException("Invalid user credentials");
        }

        return new UsernamePasswordAuthenticationToken(
                user, null,
                Arrays.stream(user.getRoles().split(",")).toList()
                        .stream()
                        .map(processStrategy -> ROLE_PREFIX + processStrategy)
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

