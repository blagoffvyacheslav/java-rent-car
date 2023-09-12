package com.dmdev.repository;

import com.dmdev.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long>, QuerydslPredicateExecutor<OrderDetails> {

    @Query(value = "SELECT od " +
            "FROM OrderDetails od " +
            "WHERE od.order.id  = :orderId ")
    Optional<OrderDetails> findByOrderId(@Param("orderId") Long orderId);
}