package com.shop.online.demo.service.impl;

import com.shop.online.demo.model.Order;
import com.shop.online.demo.model.OrderProduct;
import com.shop.online.demo.model.Product;
import com.shop.online.demo.model.dto.OrderDto;
import com.shop.online.demo.model.dto.OrderItem;
import com.shop.online.demo.repository.OrderRepository;
import com.shop.online.demo.service.OrderService;
import com.shop.online.demo.service.ProductService;
import com.shop.online.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    private List<OrderProduct> populateOrderItems(Order order, OrderDto orderDto) {
        List<OrderItem> orderItems = orderDto.getProducts();
        Set<Long> uniqueProductIds = orderItems.stream()
                .map(OrderItem::getProductId).collect(Collectors.toSet());
        if (orderItems.size() != uniqueProductIds.size()) {
            throw new RuntimeException("product Ids must be unique and not duplicated");
        }
        Map<Long, Product> productMap = productService.findProductsByIds(uniqueProductIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderProduct> orderProducts = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            Product product = productMap.get(orderItem.getProductId());
            if (product == null) {
                throw new NotFoundException("Product not found for ID: " + orderItem.getProductId());
            }

            if (orderItem.getQuantity() > product.getQuantity()) {
                throw new RuntimeException("Product quantity is more than actually is for ID: "
                        + orderItem.getProductId());
            }

            orderProducts.add(new OrderProduct().setProduct(product).setOrder(order)
                    .setQuantity(orderItem.getQuantity()));
        });

        return orderProducts;
    }

    @Override
    public Order save(OrderDto orderDto) {
        Order order = new Order().setClientId(userService.getCurrentUserId());
        return repository.save(order.setProducts(populateOrderItems(order, orderDto)));
    }

    @Override
    public Order update(Order order, OrderDto orderDto) {

        return repository.save(order.setProducts(populateOrderItems(order, orderDto)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
