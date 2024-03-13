package com.shop.online.demo.controller;

import com.shop.online.demo.model.Product;
import com.shop.online.demo.model.dto.ProductDto;
import com.shop.online.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/service/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok(productService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with ID: %s. Not Found", id))));
    }

    @PostMapping()
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid ProductDto productDto) {

        return new ResponseEntity<>(productService.save(ProductDto.toEntity(productDto)), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody @Valid ProductDto productDto) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with ID: %s. Not Found", id)));

        return ResponseEntity.ok(productService.save(ProductDto.toEntity(product, productDto)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with ID: %s. Not Found", id)));

        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
