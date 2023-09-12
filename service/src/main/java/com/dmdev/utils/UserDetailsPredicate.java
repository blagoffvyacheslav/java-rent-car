package com.dmdev.utils;

import com.dmdev.dto.UserDetailsFilterDto;
import com.querydsl.core.types.Predicate;
import lombok.experimental.UtilityClass;

import static com.dmdev.entity.QUserDetails.userDetails;

@UtilityClass
public class UserDetailsPredicate {
    public Predicate build(UserDetailsFilterDto requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getName(), userDetails.name::containsIgnoreCase)
                .add(requestFilter.getLastname(), userDetails.lastname::containsIgnoreCase)
                .add(requestFilter.getBirthday(), userDetails.birthday::eq)
                .add(requestFilter.getPhone(), userDetails.phone::eq)
                .add(requestFilter.getAddress(), userDetails.address::containsIgnoreCase)
                .buildAnd();
    }
}