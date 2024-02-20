package com.shop.online.demo.controller;

import com.shop.online.demo.model.Order;
import com.shop.online.demo.model.dto.OrderDto;
import com.shop.online.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/rest/service/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Order with ID: %s. Not Found", id))));
    }

    @PostMapping()
    public ResponseEntity<Order> saveOrder(@RequestBody @Valid OrderDto orderDto) {

        return new ResponseEntity<>(orderService.save(orderDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @RequestBody @Valid OrderDto orderDto) {

        Order order = orderService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Order with ID: %s. Not Found", id)));

        return ResponseEntity.ok(orderService.update(order, orderDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Order with ID: %s. Not Found", id)));

        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
