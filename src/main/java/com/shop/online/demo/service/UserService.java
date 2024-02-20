package com.shop.online.demo.service;

import com.shop.online.demo.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);

    Long getCurrentUserId();
}
