package com.shop.online.demo.repository;

import com.shop.online.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM PRODUCT WHERE id IN :ids", nativeQuery = true)
    List<Product> findProductsByIds(Set<Long> ids);
}
