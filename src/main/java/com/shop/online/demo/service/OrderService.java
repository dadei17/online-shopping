package com.shop.online.demo.service;

import com.shop.online.demo.model.Order;
import com.shop.online.demo.model.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();

    Optional<Order> findById(Long id);

    Order save(OrderDto orderDto);

    Order update(Order order, OrderDto orderDto);

    void deleteById(Long id);
}
