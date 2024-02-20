package com.shop.online.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ORDER_PRODUCT")
public class OrderProduct {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}