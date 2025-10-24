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
/**
 * Represents an order entity within the online store system.  This class is mapped to a DB table and stores information
 * about the customer, product, entity, entity, product quantity, price, order date.
 * */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Unique identifier for each order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the customer placing the order.
     */
    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    /**
    * Product name or description.
    */
    @NotBlank(message = "product cannot be blank")
    private String product;

    /**
     * Quantity of the product ordered.
     * */
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


    /**
    * Price per unit or total depending on business rules.
    */
    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    /**
     * Date and time when the order was placed.
     * */
    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate = LocalDateTime.now();

    /**
     *
     * Date is ensured to be set only once at creation time.
     * Won't be overwritten after re-saving an existing record.
     * */
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }
}
