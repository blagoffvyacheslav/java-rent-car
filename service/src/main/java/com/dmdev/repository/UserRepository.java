package com.dmdev.repository;

import com.dmdev.dto.UserFilter;
import com.dmdev.entity.User;
import com.dmdev.utils.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QUser.user;
import static com.dmdev.entity.QUserDetails.userDetails;

@Repository
public class UserRepository extends BaseRepository<Long, User>{
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findAllQueryDsl(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    public Optional<User> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne());
    }


    public Optional<User> findUsersByEmailAndPasswordQueryDsl(Session session, UserFilter userFilter) {
        var predicate = QPredicate.builder()
                .add(userFilter.getEmail(), user.email::eq)
                .add(userFilter.getPassword(), user.password::eq)
                .buildAnd();
        return Optional.ofNullable(new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(predicate)
                .fetchOne()
        );
    }


    public List<User> findUsersByBirthdayQueryDsl(Session session, UserFilter userFilter) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.userDetails.birthday.eq(userFilter.getBirthday()))
                .fetch();
    }


    public List<Tuple> findUsersTupleWithShortDataOrderedByEmailQueryDsl(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(user.email, userDetails.name,
                        userDetails.lastname, userDetails.birthday)
                .from(user)
                .join(user.userDetails, userDetails)
                .orderBy(user.email.asc())
                .fetch();
    }

    public List<Tuple> findUsersTupleByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl(Session session, UserFilter userFilter) {
        var predicateOr = QPredicate.builder()
                .add(userFilter.getName(), user.userDetails.name::eq)
                .add(userFilter.getLastname(), user.userDetails.lastname::eq)
                .buildOr();

        var predicateAnd = QPredicate.builder()
                .add(userFilter.getBirthday(), user.userDetails.birthday::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOr)
                .addPredicate(predicateAnd);

        return new JPAQuery<Tuple>(session)
                .select(user.email, userDetails.name,
                        userDetails.lastname, userDetails.birthday)
                .from(user)
                .join(user.userDetails, userDetails)
                .where(predicateAll.buildAnd())
                .orderBy(user.email.asc())
                .fetch();
    }
}