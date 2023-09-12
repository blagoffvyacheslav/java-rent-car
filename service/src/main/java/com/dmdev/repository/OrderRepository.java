package com.dmdev.repository;

import com.dmdev.entity.Order;
import com.dmdev.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.car c " +
            "WHERE c.serialNumber  = :serialNumber")
    List<Order> findAllBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.car c " +
            "WHERE c.id  = :carId")
    List<Order> findAllByCarId(@Param("carId") Long carId);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.user u " +
            "WHERE u.id  = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);

    List<Order> findAllByDateBetween(LocalDate start, LocalDate end);

    List<Order> findAllByDate(LocalDate date);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "WHERE o.damages.size > 0")
    List<Order> findAllWithDamages();
}