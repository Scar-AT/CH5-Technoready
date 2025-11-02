package com.techready.meli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techready.meli.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setCustomerName("Scarlett");
        order.setProduct("Headphones");
        order.setQuantity(2);
        order.setPrice(199.99);
        order.setOrderDate(LocalDateTime.now());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Scarlett"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchOrders() throws Exception {
        mockMvc.perform(get("/api/orders/search")
                        .param("customerName", "Scarlett")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());
    }
}
