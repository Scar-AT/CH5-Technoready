package com.techready.meli.controller;

import com.techready.meli.model.Order;
import com.techready.meli.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




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
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> ordersPage = repository.searchOrders(
                customerName,
                product,
                minQuantity,
                maxPrice,
                startDate,
                endDate,
                pageable
        );

        Map<String, Object> response = new HashMap<>();
        response.put("data", ordersPage.getContent());
        response.put("currentPage", ordersPage.getNumber());
        response.put("totalItems", ordersPage.getTotalElements());
        response.put("totalPages", ordersPage.getTotalPages());
        response.put("pageSize", ordersPage.getSize());
        response.put("sortBy", sortBy);
        response.put("direction", direction);

        return ResponseEntity.ok(response);
    }


}
