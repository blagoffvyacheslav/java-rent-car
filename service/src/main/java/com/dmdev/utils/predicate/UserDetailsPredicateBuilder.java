package com.dmdev.utils.predicate;

import com.dmdev.dto.UserDetailsFilterDto;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.entity.QUserDetails.userDetails;

@Component
public class UserDetailsPredicateBuilder implements PredicateBuilder<UserDetailsFilterDto> {

    @Override
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