package com.techready.meli.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name cannot be blank")
    @Column(name = "customer_name", length = 255, columnDefinition = "VARCHAR(255)")
    private String customerName;

    @NotBlank(message = "product cannot be blank")
    @Column(name = "product", length = 255, columnDefinition = "VARCHAR(255)")
    private String product;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private int quantity;

    @Min(value = 0, message = "Price must be non-negative")
    @Column(name = "price")
    private double price;

    @NotNull(message = "Order date cannot be null")
    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }
}