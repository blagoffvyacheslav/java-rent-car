package com.dmdev.repository;

import com.dmdev.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>, QuerydslPredicateExecutor<UserDetails> {

    UserDetails findByUserId(Long userId);

    List<UserDetails> findAllByNameContainingIgnoreCaseAndLastnameContainingIgnoreCase(String name, String lastname);

    List<UserDetails> findByRegistrationDate(LocalDate registrationDate);

    List<UserDetails> findByRegistrationDateBetween(LocalDate start, LocalDate end);
}