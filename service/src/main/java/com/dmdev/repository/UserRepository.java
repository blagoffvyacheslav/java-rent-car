package com.dmdev.repository;

import com.dmdev.entity.User;
import com.dmdev.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    @Query(value = "SELECT u " +
            "FROM User u " +
            "JOIN fetch u.userDetails ud " +
            "WHERE ud.registrationDate  = :registrationDate")
    List<User> findAllByRegistrationDate(@Param("registrationDate") LocalDate registrationDate);

    @EntityGraph(attributePaths = {"orders"})
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.orders.size > 0")
    List<User> findAllWithOrders();

    @EntityGraph(attributePaths = {"orders"})
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.orders.size = 0")
    List<User> findAllWithoutOrders();
}