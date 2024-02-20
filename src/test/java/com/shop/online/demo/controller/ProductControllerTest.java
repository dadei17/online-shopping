package com.shop.online.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.online.demo.model.Product;
import com.shop.online.demo.model.dto.ProductDto;
import com.shop.online.demo.service.ProductService;
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

import java.math.BigDecimal;
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

@WithMockUser(roles = "PRODUCT")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public ProductService productService;

    public InOrder inOrder;

    @BeforeEach
    public void init() {
        inOrder = Mockito.inOrder(productService);
    }

    @Test
    void getProducts() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), quantity = random.nextLong();
        BigDecimal price = new BigDecimal(random.nextLong());
        String name = "hat";
        List<Product> products =
                List.of(new Product(id, name, price, quantity));

        doReturn(products).when(productService).findAll();

        mockMvc.perform(get("/rest/service/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].quantity", is(quantity)))
                .andExpect(status().isOk());

        inOrder.verify(productService).findAll();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getProductNotFound() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), quantity = random.nextLong();
        BigDecimal price = new BigDecimal(random.nextLong());
        String name = "hat";
        Product product = new Product(id, name, price, quantity);

        doReturn(Optional.of(product)).when(productService).findById(id);

        mockMvc.perform(get("/rest/service/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        inOrder.verify(productService).findById(1L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getProduct() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), quantity = random.nextLong();
        BigDecimal price = new BigDecimal(random.nextLong());
        String name = "hat";
        Product product = new Product(id, name, price, quantity);

        doReturn(Optional.of(product)).when(productService).findById(id);

        mockMvc.perform(get("/rest/service/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(status().isOk());

        inOrder.verify(productService).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void saveProduct() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), quantity = Math.abs(random.nextLong());
        BigDecimal price = new BigDecimal(Math.abs(random.nextLong()));
        String name = "hat";
        Product product = new Product(id, name, price, quantity);
        ProductDto productDto = new ProductDto(name, price, quantity);

        doReturn(product).when(productService).save(any());

        mockMvc.perform(post("/rest/service/products").with(csrf())
                .content(objectMapper.writeValueAsString(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(status().isCreated());

        inOrder.verify(productService).save(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updateProductNotFound() throws Exception {
        Random random = new Random();
        Long dbId = random.nextLong(), quantity = Math.abs(random.nextLong());
        BigDecimal price = new BigDecimal(Math.abs(random.nextLong()));
        String name = "hat";

        Product product = new Product(dbId, name, price, quantity);
        ProductDto productDto = new ProductDto(name, price, quantity);

        doReturn(Optional.empty()).when(productService).findById(dbId);
        doReturn(product).when(productService).save(any());

        mockMvc.perform(put("/rest/service/products/{id}", dbId).with(csrf())
                .content(objectMapper.writeValueAsString(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        inOrder.verify(productService).findById(dbId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updateProduct() throws Exception {
        Random random = new Random();
        Long dbId = random.nextLong(), quantity = Math.abs(random.nextLong());
        BigDecimal price = new BigDecimal(Math.abs(random.nextLong()));
        String name = "hat";
        Product productDB = new Product(dbId, name, price, quantity);

        Long id = random.nextLong();
        quantity = Math.abs(random.nextLong());
        price = new BigDecimal(Math.abs(random.nextLong()));
        name = "t-shirt";
        Product product = new Product(id, name, price, quantity);
        ProductDto productDto = new ProductDto(name, price, quantity);

        doReturn(Optional.of(productDB)).when(productService).findById(dbId);
        doReturn(product).when(productService).save(product);

        mockMvc.perform(put("/rest/service/products/{id}", dbId).with(csrf())
                .content(objectMapper.writeValueAsString(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        inOrder.verify(productService).findById(dbId);
        inOrder.verify(productService).save(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteProduct() throws Exception {
        Random random = new Random();
        Long id = random.nextLong(), quantity = random.nextLong();
        BigDecimal price = new BigDecimal(random.nextLong());
        String name = "hat";
        Product product = new Product(id, name, price, quantity);

        doReturn(Optional.of(product)).when(productService).findById(id);
        doNothing().when(productService).deleteById(id);

        mockMvc.perform(delete("/rest/service/products/{id}", id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        inOrder.verify(productService).findById(id);
        inOrder.verify(productService).deleteById(id);
        inOrder.verifyNoMoreInteractions();
    }
}