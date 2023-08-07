package com.dmdev.repository;

import com.dmdev.dto.UserDto;
import com.dmdev.dto.UserFilter;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.entity.UserDetails_;
import com.dmdev.entity.User_;
import com.dmdev.utils.CriteriaPredicate;
import com.dmdev.utils.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QUser.user;
import static com.dmdev.entity.QUserDetails.userDetails;

public class UserRepository extends BaseRepository<Long, User>{
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria)
                .list();
    }

    public List<User> findAllQueryDsl(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    public Optional<User> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user)
                .where(cb.equal(user.get(User_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public Optional<User> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    public Optional<User> findUsersByEmailAndPasswordCriteria(Session session, UserFilter userFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(userFilter.getEmail(), email -> cb.equal(user.get(User_.email), email))
                .add(userFilter.getPassword(), password -> cb.equal(user.get(User_.password), password))
                .getPredicates();

        criteria.select(user)
                .where(predicates);

        return session.createQuery(criteria)
                .uniqueResultOptional();
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

    public List<User> findUsersByBirthdayCriteria(Session session, UserFilter userFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user)
                .where(cb.equal(user.get(User_.userDetails).get(UserDetails_.birthday), userFilter.getBirthday()));

        return session.createQuery(criteria)
                .list();
    }

    public List<User> findUsersByBirthdayQueryDsl(Session session, UserFilter userFilter) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.userDetails.birthday.eq(userFilter.getBirthday()))
                .fetch();
    }

    public List<UserDto> findUsersWithShortDataOrderedByEmailCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(UserDto.class);
        var user = criteria.from(User.class);
        var userDetails = user.join(User_.userDetails);

        criteria.select(
                        cb.construct(UserDto.class,
                                user.get(User_.email),
                                userDetails.get(UserDetails_.name),
                                userDetails.get(UserDetails_.lastname),
                                userDetails.get(UserDetails_.birthday))

                )
                .orderBy(cb.asc(user.get(User_.email)));

        return session.createQuery(criteria)
                .list();
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

    public List<UserDto> findUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailCriteria(Session session, UserFilter userFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(UserDto.class);
        var user = criteria.from(User.class);
        var userDetails = user.join(User_.userDetails);
        Predicate predicateForName = cb.equal(userDetails.get(UserDetails_.name), userFilter.getName());
        Predicate predicateForSurname = cb.equal(userDetails.get(UserDetails_.lastname), userFilter.getLastname());
        Predicate[] predicates = CriteriaPredicate.builder()
                .buildOr(predicateForName, predicateForSurname, cb)
                .add(userFilter.getBirthday(), birthday -> cb.equal(user.get(User_.userDetails).get(UserDetails_.birthday), birthday))
                .getPredicates();

        criteria.select(
                        cb.construct(UserDto.class,
                                user.get(User_.email),
                                userDetails.get(UserDetails_.name),
                                userDetails.get(UserDetails_.lastname),
                                userDetails.get(UserDetails_.birthday))
                )
                .where(predicates)
                .orderBy(cb.asc(user.get(User_.email)));

        return session.createQuery(criteria)
                .list();
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