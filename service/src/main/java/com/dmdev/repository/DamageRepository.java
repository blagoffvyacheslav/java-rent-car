package com.dmdev.repository;

import com.dmdev.entity.Damage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DamageRepository extends JpaRepository<Damage, Long>, QuerydslPredicateExecutor<Damage> {


    @Query(value = "SELECT d " +
            "FROM Damage d " +
            "WHERE d.amount >= :amount " +
            "ORDER BY d.amount DESC ")
    List<Damage> findAllByAmount(@Param("amount") BigDecimal amount);

    @Query(value = "SELECT d " +
            "FROM Damage d " +
            "JOIN fetch d.order o " +
            "WHERE o.id = :orderId")
    List<Damage> findAllByOrderId(@Param("orderId") Long orderId);

    @Query(value = "SELECT d " +
            "FROM Damage d " +
            "JOIN fetch d.order o " +
            "JOIN fetch o.user u " +
            "JOIN fetch u.userDetails ud " +
            "WHERE lower(ud.name) = lower(:name) AND lower(ud.lastname) = lower(:lastname)")
    List<Damage> findAllByNameAndLastname(@Param("name") String name, @Param("lastname") String lastname);

    @Query(value = "SELECT d " +
            "FROM Damage d " +
            "JOIN fetch d.order o " +
            "JOIN fetch o.car c " +
            "WHERE c.serialNumber like %:serialNumber% " +
            "order by d.amount")
    List<Damage> findAllBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT a " +
            "FROM Damage a " +
            "WHERE a.amount > (SELECT avg(va.amount) FROM Damage va)")
    List<Damage> findAllByAvgAmountMore();
}