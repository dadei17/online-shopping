package com.shop.online.demo.config;

import com.shop.online.demo.model.User;
import com.shop.online.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.DigestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationProviderTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private BasicAuthenticationProvider authenticationProvider;

    public InOrder inOrder;

    @BeforeEach
    public void init() {
        inOrder = Mockito.inOrder(userService);
    }

    @Test
    void authenticateSuccessfulAuthentication() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User(1L, username, DigestUtils.md5DigestAsHex(password.getBytes()), "");
        when(userService.findByLogin(username)).thenReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication result = authenticationProvider.authenticate(authentication);

        assertTrue(result.isAuthenticated());
        assertEquals(user, result.getPrincipal());
        assertNull(result.getCredentials());
    }

    @Test
    void notFoundAuthentication() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User(1L, username, password, "");
        when(userService.findByLogin(username)).thenReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        assertThrows(InsufficientAuthenticationException.class, () -> authenticationProvider.authenticate(authentication));
    }

    @Test
    void wrongPasswordAuthentication() {
        String username = "testUser";
        String password = "testPassword";
        when(userService.findByLogin(username)).thenReturn(Optional.empty());

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        assertThrows(InsufficientAuthenticationException.class, () -> authenticationProvider.authenticate(authentication));
    }

}