package com.shop.online.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"Id", "clientId", "status", "products"})
@Entity(name = "CLIENT_ORDER")
public class Order {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;

    @JsonProperty("clientId")
    Long clientId;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @JsonProperty("products")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products = new ArrayList<>();

    public Order setProducts(List<OrderProduct> products) {
        this.products.clear();
        this.products.addAll(products);
        return this;
    }

    @PrePersist
    public void prePersist() {
        if (this.getStatus() == null) {
            this.setStatus(OrderStatus.IN_PROGRESS);
        }
    }
}
