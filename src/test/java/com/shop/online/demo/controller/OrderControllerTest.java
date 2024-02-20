package com.shop.online.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.online.demo.model.Order;
import com.shop.online.demo.model.OrderStatus;
import com.shop.online.demo.model.dto.OrderDto;
import com.shop.online.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "ORDER")
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public OrderService orderService;

    public InOrder inOrder;

    @BeforeEach
    public void init() {
        inOrder = Mockito.inOrder(orderService);
    }

    @Test
    void getOrders() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), clientId = random.nextLong();
        List<Order> orders =
                List.of(new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList()));

        doReturn(orders).when(orderService).findAll();

        mockMvc.perform(get("/rest/service/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].clientId", is(clientId)))
                .andExpect(jsonPath("$[0].status", is(OrderStatus.IN_PROGRESS.name())))
                .andExpect(status().isOk());

        inOrder.verify(orderService).findAll();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getOrderNotFound() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), clientId = random.nextLong();
        Order order = new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());

        doReturn(Optional.of(order)).when(orderService).findById(id);

        mockMvc.perform(get("/rest/service/orders/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        inOrder.verify(orderService).findById(1L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getOrder() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), clientId = random.nextLong();
        Order order = new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());

        doReturn(Optional.of(order)).when(orderService).findById(id);

        mockMvc.perform(get("/rest/service/orders/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.clientId", is(clientId)))
                .andExpect(jsonPath("$.status", is(OrderStatus.IN_PROGRESS.name())))
                .andExpect(status().isOk());

        inOrder.verify(orderService).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void saveOrder() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), clientId = random.nextLong();
        Order order = new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());
        OrderDto orderDto = new OrderDto();

        doReturn(order).when(orderService).save(any());

        mockMvc.perform(post("/rest/service/orders").with(csrf())
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.clientId", is(clientId)))
                .andExpect(jsonPath("$.status", is(OrderStatus.IN_PROGRESS.name())))
                .andExpect(status().isCreated());

        inOrder.verify(orderService).save(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updateOrder() throws Exception {
        Random random = new Random();
        Long dbId = random.nextLong(), clientId = random.nextLong();
        Order orderDb = new Order(dbId, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());

        OrderDto orderDto = new OrderDto();
        Long id = random.nextLong();
        Order order = new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());

        doReturn(Optional.of(orderDb)).when(orderService).findById(dbId);
        doReturn(order).when(orderService).update(any(), any());

        mockMvc.perform(put("/rest/service/orders/{id}", dbId).with(csrf())
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        inOrder.verify(orderService).findById(dbId);
        inOrder.verify(orderService).update(any(), any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteOrder() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), clientId = random.nextLong();
        Order order = new Order(id, clientId, OrderStatus.IN_PROGRESS, Collections.emptyList());

        doReturn(Optional.of(order)).when(orderService).findById(id);
        doNothing().when(orderService).deleteById(id);

        mockMvc.perform(delete("/rest/service/orders/{id}", id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        inOrder.verify(orderService).findById(id);
        inOrder.verify(orderService).deleteById(id);
        inOrder.verifyNoMoreInteractions();
    }
}