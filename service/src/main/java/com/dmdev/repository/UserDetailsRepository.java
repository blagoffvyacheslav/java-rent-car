package com.dmdev.repository;

import com.dmdev.dto.UserDetailsFilter;
import com.dmdev.entity.*;
import com.dmdev.utils.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QUser.user;
import static com.dmdev.entity.QUserDetails.userDetails;
import org.springframework.stereotype.Repository;


@Repository
public class UserDetailsRepository extends BaseRepository<Long, UserDetails> {
    public UserDetailsRepository() {
        super(UserDetails.class);
    }

    public List<UserDetails> findAllQueryDsl() {
        return new JPAQuery<UserDetails>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .fetch();
    }

    public Optional<UserDetails> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<User>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .where(userDetails.id.eq(id))
                .fetchOne());
    }

    public List<UserDetails> findUserDetailsByNameAndSurnameQueryDsl(UserDetailsFilter userDetailsFilter) {
        var predicate = QPredicate.builder()
                .add(userDetailsFilter.getName(), userDetails.name::eq)
                .add(userDetailsFilter.getSurname(), userDetails.lastname::eq)
                .buildAnd();

        return new JPAQuery<UserDetails>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .where(predicate)
                .fetch();
    }

    public List<Tuple> findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(LocalDate localDate) {
        var predicate = QPredicate.builder()
                .add(localDate.getMonth().getValue(), userDetails.birthday.month().intValue()::eq)
                .add(localDate.getDayOfMonth(), userDetails.birthday.dayOfMonth()::eq)
                .buildAnd();

        return new JPAQuery<Tuple>(getEntityManager())
                .select(userDetails.lastname, userDetails.name, userDetails.birthday, user.email)
                .from(userDetails)
                .join(userDetails.user, user)
                .where(predicate)
                .orderBy(userDetails.lastname.asc(), userDetails.name.asc())
                .fetch();
    }
}