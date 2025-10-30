package com.techready.meli.repository;

import com.techready.meli.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Order} entities.
 *
 * Extends JpaRepository to provide CRUD operations and
 * custom search/filtering queries for Sprint 3.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Dynamic search query combining multiple optional filters.
     *
     * Allows filtering by:
     * - Customer name (partial match)
     * - Product (partial match)
     * - Minimum quantity
     * - Maximum price
     * - Order date range
     */
    @Query("""
    SELECT o FROM Order o
    WHERE (:customerName IS NULL OR LOWER(o.customerName) LIKE LOWER(CONCAT('%', :customerName, '%')))
      AND (:product IS NULL OR LOWER(o.product) LIKE LOWER(CONCAT('%', :product, '%')))
      AND (:minQuantity IS NULL OR o.quantity >= :minQuantity)
      AND (:maxPrice IS NULL OR o.price <= :maxPrice)
      AND (:startDate IS NULL OR o.orderDate >= :startDate)
      AND (:endDate IS NULL OR o.orderDate <= :endDate)
    ORDER BY o.orderDate DESC
    """)
    List<Order> searchOrders(
            @Param("customerName") String customerName,
            @Param("product") String product,
            @Param("minQuantity") Integer minQuantity,
            @Param("maxPrice") Double maxPrice,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate
    );
}
