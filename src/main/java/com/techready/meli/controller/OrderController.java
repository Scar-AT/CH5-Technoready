package com.techready.meli.controller;

import com.techready.meli.model.Order;
import com.techready.meli.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;



import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }


    @Operation(summary = "Create a new order", description = "Registers a new order with customer and product details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
    })
    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        Order saved = repository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }


    @Operation(summary = "List all orders", description = "Retrieves every order stored in the system.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping
    public Page<Order> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return repository.findAll(pageable);
    }


    @Operation(summary = "Get order by ID", description = "Retrieves an order by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Search orders with filters, pagination and sorting",
            description = "Filters by customer name, product, quantity range, price range, and date range. "
                    + "Supports sorting by any field and pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search executed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        if (customerName != null && customerName.isBlank()) customerName = null;
        if (product != null && product.isBlank()) product = null;

        Page<Order> resultPage = repository.searchOrders(
                customerName, product, minQuantity, maxPrice, startDate, endDate, pageable
        );

        Map<String, Object> response = new HashMap<>();
        response.put("orders", resultPage.getContent());
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPages", resultPage.getTotalPages());
        response.put("sortBy", sortBy);
        response.put("direction", direction);

        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Update an existing order", description = "Updates customer, product, quantity, price, or date.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
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



    @Operation(summary = "Delete an order", description = "Removes an order by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
