package com.shop.online.demo.service.impl;

import com.shop.online.demo.model.Order;
import com.shop.online.demo.model.OrderProduct;
import com.shop.online.demo.model.OrderStatus;
import com.shop.online.demo.model.Product;
import com.shop.online.demo.model.dto.OrderDto;
import com.shop.online.demo.model.dto.OrderItem;
import com.shop.online.demo.repository.OrderProductRepository;
import com.shop.online.demo.repository.OrderRepository;
import com.shop.online.demo.repository.ProductRepository;
import com.shop.online.demo.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private void resetSequence() {
        jdbcTemplate.execute("ALTER SEQUENCE ORDER_PRODUCT_ID_SEQ RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE PRODUCT_ID_SEQ RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE ORDER_ID_SEQ RESTART WITH 1");
    }

    @BeforeEach
    public void setUp() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        resetSequence();
    }

    @AfterEach
    public void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        resetSequence();
    }

    @Test
    void findAll() {
        for (long i = 1; i <= 10; i++) {
            orderRepository.save(new Order().setClientId(i));
        }

        assertEquals(10, orderService.findAll().size());
    }

    @Test
    void findById() {
        for (long i = 1; i <= 10; i++) {
            orderRepository.save(new Order().setClientId(i));
        }

        assertEquals(1L, orderService.findById(1L).get().getClientId());
        assertEquals(Optional.empty(), orderService.findById(100L));
    }

    @Test
    void saveProductIdDuplicate() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(14L), 5L));

        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L),
                        new OrderItem(product.getId(), 3L)))));
        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L),
                        new OrderItem(1L, 3L),
                        new OrderItem(5L, 3L)))));
        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L),
                        new OrderItem(15L, 3L),
                        new OrderItem(15L, 3L)))));
    }

    @Test
    void saveProductIdNotFound() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(14L), 5L));

        assertThrows(NotFoundException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(10L, 2L)))));
    }

    @Test
    void saveProductQuantityMore() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(9L), 5L));

        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 22L)))));
        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L),
                        new OrderItem(product.getId(), 10L)))));
        assertThrows(RuntimeException.class, () ->
                orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 12L),
                        new OrderItem(product.getId(), 4L)))));
    }

    @Test
    void save() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(14L), 5L));

        Order expected = new Order().setId(1L).setStatus(OrderStatus.IN_PROGRESS);
        Order actual = orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L))));

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());

    }

    @Test
    void update() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(14), 15L));
        Order savedOrder = orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L))));

        Order actual = orderService.update(savedOrder, new OrderDto(List.of(new OrderItem(product.getId(), 12L))));
        product.setOrderProducts(List.of(new OrderProduct(2L, 12L, product, actual)));

        Order expected = new Order().setId(1L).setStatus(OrderStatus.IN_PROGRESS).setProducts(
                List.of(new OrderProduct(2L, 12L, product, actual))
        );

        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getStatus(), expected.getStatus());
        assertEquals(actual.getProducts().get(0).getQuantity(), expected.getProducts().get(0).getQuantity());
    }

    @Test
    void deleteById() {
        Product product = productRepository.save(new Product(null, "name", new BigDecimal(14L), 5L));

        for (long i = 1; i <= 10; i++) {
            orderService.save(new OrderDto(List.of(new OrderItem(product.getId(), 2L))));
        }

        for (long i = 1; i <= 4; i++) {
            orderService.deleteById(i);
        }
        assertEquals(6, orderRepository.findAll().size());
    }
}