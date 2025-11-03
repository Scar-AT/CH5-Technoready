package com.techready.meli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techready.meli.model.Order;
import com.techready.meli.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    private Order sampleOrder;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        sampleOrder = new Order();
        sampleOrder.setCustomerName("Daniela Morales");
        sampleOrder.setProduct("USB-C Docking Station");
        sampleOrder.setQuantity(2);
        sampleOrder.setPrice(899.99);
        sampleOrder.setOrderDate(LocalDateTime.now().minusDays(1));
        orderRepository.save(sampleOrder);
    }

    @Test
    @DisplayName("Should create a new order successfully")
    void testCreateOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setCustomerName("Carlos Jimenez");
        newOrder.setProduct("Noise Cancelling Headphones");
        newOrder.setQuantity(1);
        newOrder.setPrice(1999.00);
        newOrder.setOrderDate(LocalDateTime.now());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Carlos Jimenez"))
                .andExpect(jsonPath("$.product").value("Noise Cancelling Headphones"));
    }

    @Test
    @DisplayName("Should list all orders")
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }


    @Test
    @DisplayName("Should get order by ID")
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", sampleOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Daniela Morales"));
    }


    @Test
    @DisplayName("Should update existing order")
    void testUpdateOrder() throws Exception {
        sampleOrder.setQuantity(5);

        mockMvc.perform(put("/api/orders/{id}", sampleOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    @DisplayName("Should delete an order by ID")
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", sampleOrder.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should search orders by product name")
    void testSearchOrders() throws Exception {
        mockMvc.perform(get("/api/orders/search")
                        .param("product", "Docking")
                        .param("customerName", "")
                        .param("minQuantity", "")
                        .param("maxPrice", "")
                        .param("startDate", "")
                        .param("endDate", "")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].product").value("USB-C Docking Station"))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }


    @Test
    @DisplayName("Should return 404 when getting non-existent order")
    void testGetNonExistentOrder() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", 9999))
                .andExpect(status().isNotFound());
    }
}
