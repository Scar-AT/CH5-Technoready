package com.techready.meli.controller;

import com.techready.meli.model.Order;
import com.techready.meli.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller that exposes CRUD endpoints for managing orders.
 * Base path: /api/orders
 *
 * Provides CRUD for orders within the online store system.
 *
 * */

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repository;

    /**
     * Injects the order repository dependency.
     * @param repository repository used for database access.
     * */
    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new order.
     * @param order the order to save.
     * @return the saved order entity.
     * */
    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        Order saved = repository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    /**
     * Retrieves all orders.
     * @return a list of all orders in the database.
     * */
    @GetMapping
    public List<Order> list() {
        return repository.findAll();
    }

    /**
     * Retrieves a specific order by ID.
     * @param id the order ID.
     * @return the corresponding order if found.
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a specific order by ID.
     *
     * @param id the order ID.
     * @param incoming the order details to update.
     * @return the updated order entity.
     * */
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @Valid @RequestBody Order incoming) {
        return repository.findById(id).map(existing -> {
            existing.setCustomerName(incoming.getCustomerName());
            existing.setProduct(incoming.getProduct());
            existing.setQuantity(incoming.getQuantity());
            existing.setPrice(incoming.getPrice());
            existing.setOrderDate(incoming.getOrderDate());
            return ResponseEntity.ok(repository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an order by ID.
     * @param id the order ID.
     * @return empty 204 "No content response".
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        java.time.LocalDateTime start = null;
        java.time.LocalDateTime end = null;

        try {
            if (startDate != null && !startDate.isBlank()) {
                start = java.time.LocalDateTime.parse(startDate, formatter);
            }
            if (endDate != null && !endDate.isBlank()) {
                end = java.time.LocalDateTime.parse(endDate, formatter);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        final java.time.LocalDateTime startFinal = start;
        final java.time.LocalDateTime endFinal = end;

        List<Order> filtered = repository.findAll().stream()
                .filter(o -> customerName == null || o.getCustomerName().toLowerCase().contains(customerName.toLowerCase()))
                .filter(o -> product == null || o.getProduct().toLowerCase().contains(product.toLowerCase()))
                .filter(o -> minQuantity == null || o.getQuantity() >= minQuantity)
                .filter(o -> maxPrice == null || o.getPrice() <= maxPrice)
                .filter(o -> startFinal == null || !o.getOrderDate().isBefore(startFinal))
                .filter(o -> endFinal == null || !o.getOrderDate().isAfter(endFinal))
                .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                .toList();

        if (filtered.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(filtered);
    }



}
