package com.techready.meli.repository;

import com.techready.meli.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Order} entities.
 *
 * Provides CRUD database operations through spring data JPA.
 * */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Standard CRUD Methods inherited from JpaRepository

}
