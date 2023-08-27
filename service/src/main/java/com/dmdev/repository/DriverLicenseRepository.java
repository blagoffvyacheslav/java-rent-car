package com.dmdev.repository;

import com.dmdev.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long>, QuerydslPredicateExecutor<DriverLicense> {

    Optional<DriverLicense> findByNumberContainingIgnoreCase(String number);

    @Query(value = "SELECT dl " +
            "FROM DriverLicense dl " +
            "JOIN fetch dl.userDetails ud " +
            "JOIN fetch ud.user u " +
            "WHERE u.id  = :id ")
    Optional<DriverLicense> findByUserId(@Param("id") Long userId);

    List<DriverLicense> findByExpiredDateLessThanEqual(LocalDate expiredDate);
}