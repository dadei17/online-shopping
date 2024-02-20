package com.shop.online.demo.service.impl;

import com.shop.online.demo.model.Product;
import com.shop.online.demo.repository.ProductRepository;
import com.shop.online.demo.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private void resetSequence() {
        jdbcTemplate.execute("ALTER SEQUENCE PRODUCT_ID_SEQ RESTART WITH 1");
    }

    @BeforeEach
    public void setUp() {
        productRepository.deleteAllInBatch();
        resetSequence();
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAllInBatch();
        resetSequence();
    }

    @Test
    void findAll() {
        for (long i = 1; i <= 10; i++) {
            productRepository.save(new Product().setName("name" + i));
        }
        assertEquals(10, productService.findAll().size());
    }

    @Test
    void findById() {
        for (long i = 1; i <= 10; i++) {
            productRepository.save(new Product().setName("name" + i).setQuantity(i));
        }
        assertEquals("name5", productService.findById(5L).get().getName());
        assertEquals(9L, productService.findById(9L).get().getQuantity());
        assertEquals(Optional.empty(), productService.findById(11L));
    }

    @Test
    void save() {
        for (long i = 1; i <= 10; i++) {
            productService.save(new Product().setName("name" + i).setQuantity(i));
        }

        assertEquals("name5", productService.findById(5L).get().getName());
        assertEquals(9L, productService.findById(9L).get().getQuantity());
        assertEquals(Optional.empty(), productService.findById(11L));
    }

    @Test
    void deleteById() {
        for (long i = 1; i <= 10; i++) {
            productRepository.save(new Product().setName("name" + i).setQuantity(i));
        }

        for (long i = 7; i <= 10; i++) {
            productService.deleteById(i);
        }
        assertEquals(6, productService.findAll().size());
    }

    @Test
    void findProductsByIds() {
        for (long i = 1; i <= 10; i++) {
            productRepository.save(new Product().setName("name" + i).setQuantity(i));
        }

        assertEquals(3, productService.findProductsByIds(Set.of(1L, 6L, 2L, 11L, 15L)).size());
        assertEquals(0, productService.findProductsByIds(Set.of(11L, 15L)).size());
        assertEquals(2, productService.findProductsByIds(Set.of(1L, 2L)).size());
    }
}
