package com.dmdev.utils;

import com.dmdev.dto.UserFilterDto;
import com.querydsl.core.types.Predicate;
import lombok.experimental.UtilityClass;

import static com.dmdev.entity.QUser.user;

@UtilityClass
public class UserPredicate {
    public Predicate build(UserFilterDto requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getUsername(), user.username::eq)
                .add(requestFilter.getEmail(), user.email::eq)
                .add(requestFilter.getName(), user.userDetails.name::containsIgnoreCase)
                .add(requestFilter.getLastname(), user.userDetails.lastname::containsIgnoreCase)
                .add(requestFilter.getBirthday(), user.userDetails.birthday::eq)
                .add(requestFilter.getExpiredLicenseDriverDate(), user.userDetails.driverLicenses.any().expiredDate::loe)
                .buildAnd();
    }
}