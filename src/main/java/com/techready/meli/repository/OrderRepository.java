package com.techready.meli.repository;

import com.techready.meli.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
        SELECT * FROM orders o
        WHERE (CAST(:customerName AS TEXT) IS NULL OR o.customer_name ILIKE CONCAT('%', CAST(:customerName AS TEXT), '%'))
          AND (CAST(:product AS TEXT) IS NULL OR o.product ILIKE CONCAT('%', CAST(:product AS TEXT), '%'))
          AND (CAST(:minQuantity AS INTEGER) IS NULL OR o.quantity >= CAST(:minQuantity AS INTEGER))
          AND (CAST(:maxPrice AS DOUBLE PRECISION) IS NULL OR o.price <= CAST(:maxPrice AS DOUBLE PRECISION))
          AND (CAST(:startDate AS TIMESTAMP) IS NULL OR o.order_date >= CAST(:startDate AS TIMESTAMP))
          AND (CAST(:endDate AS TIMESTAMP) IS NULL OR o.order_date <= CAST(:endDate AS TIMESTAMP))
        ORDER BY o.order_date DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM orders o
        WHERE (CAST(:customerName AS TEXT) IS NULL OR o.customer_name ILIKE CONCAT('%', CAST(:customerName AS TEXT), '%'))
          AND (CAST(:product AS TEXT) IS NULL OR o.product ILIKE CONCAT('%', CAST(:product AS TEXT), '%'))
          AND (CAST(:minQuantity AS INTEGER) IS NULL OR o.quantity >= CAST(:minQuantity AS INTEGER))
          AND (CAST(:maxPrice AS DOUBLE PRECISION) IS NULL OR o.price <= CAST(:maxPrice AS DOUBLE PRECISION))
          AND (CAST(:startDate AS TIMESTAMP) IS NULL OR o.order_date >= CAST(:startDate AS TIMESTAMP))
          AND (CAST(:endDate AS TIMESTAMP) IS NULL OR o.order_date <= CAST(:endDate AS TIMESTAMP))
        """,
            nativeQuery = true)
    Page<Order> searchOrders(
            @Param("customerName") String customerName,
            @Param("product") String product,
            @Param("minQuantity") Integer minQuantity,
            @Param("maxPrice") Double maxPrice,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
