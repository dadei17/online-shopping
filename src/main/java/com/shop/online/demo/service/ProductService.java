package com.shop.online.demo.service;

import com.shop.online.demo.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    List<Product> findProductsByIds(Set<Long> ids);
}
