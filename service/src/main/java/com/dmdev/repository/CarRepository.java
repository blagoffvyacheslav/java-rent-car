package com.dmdev.repository;

import com.dmdev.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, QuerydslPredicateExecutor<Car> {

    Optional<Car> findBySerialNumber(String serialNumber);
    List<Car> findBySerialNumberContainingIgnoreCase(String serialNumber);

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "JOIN fetch c.orders o " +
            "JOIN fetch o.damages a " +
            "WHERE o.damages.size > 0 ")
    List<Car> findAllWithDamages();

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "JOIN fetch c.orders o " +
            "JOIN fetch o.damages a " +
            "WHERE o.damages.size = 0 ")
    List<Car> findAllWithoutDamages();

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "WHERE c.isNew = true ")
    List<Car> findAllIsNew();

    @Query(value = "SELECT count(o.id) = 0 " +
            "FROM orders o " +
            "JOIN car c on o.car_id = c.id " +
            "WHERE c.id = :id AND o.id IN (SELECT order_id FROM order_details od WHERE od.start_date <= :endDate AND " +
            "od.end_date >= :startDate)", nativeQuery = true)
    boolean isCarAvailable(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}