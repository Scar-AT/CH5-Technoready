package com.techready.meli;

import com.techready.meli.model.Order;
import com.techready.meli.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTests {

    @Autowired
    private OrderRepository repository;

    @BeforeEach
    void setup() {
        Order o1 = new Order();
        o1.setCustomerName("Scarlett");
        o1.setProduct("Wireless Mouse");
        o1.setQuantity(2);
        o1.setPrice(129.99);
        o1.setOrderDate(LocalDateTime.now().minusDays(1));

        Order o2 = new Order();
        o2.setCustomerName("Daniel");
        o2.setProduct("Keyboard");
        o2.setQuantity(1);
        o2.setPrice(79.99);
        o2.setOrderDate(LocalDateTime.now());

        repository.save(o1);
        repository.save(o2);
    }

    @Test
    void testSearchOrders_FilterByCustomer() {
        Page<Order> result = repository.searchOrders(
                "Scarlett",
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 5)
        );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCustomerName()).isEqualTo("Scarlett");
    }

    @Test
    void testSearchOrders_Pagination() {
        Page<Order> page = repository.searchOrders(
                null,
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 1)
        );

        assertThat(page.getTotalElements()).isGreaterThan(1);
        assertThat(page.getContent().size()).isEqualTo(1);
    }
}
