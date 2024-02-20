package com.shop.online.demo.service.impl;

import com.shop.online.demo.config.RestSecurityConfiguration.RoleType;
import com.shop.online.demo.model.User;
import com.shop.online.demo.repository.UserRepository;
import com.shop.online.demo.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @PostConstruct
    public void initialize() {
        // admin - admin
        repository.save(new User().setLogin("admin").setPassword("21232f297a57a5a743894a0e4a801fc3")
                .setRoles(RoleType.ORDER.name() + "," + RoleType.PRODUCT.name()));
        // product_user - user
        repository.save(new User().setLogin("product_user").setPassword("ee11cbb19052e40b07aac0ca060c23ee")
                .setRoles(RoleType.PRODUCT.name()));
        // order_user - user
        repository.save(new User().setLogin("order_user").setPassword("ee11cbb19052e40b07aac0ca060c23ee")
                .setRoles(RoleType.ORDER.name()));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((User) authentication.getPrincipal()).getId();
        }

        return null;
    }
}
