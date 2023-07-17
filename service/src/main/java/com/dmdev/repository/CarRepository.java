package com.dmdev.repository;

import com.dmdev.entity.Car;
import com.dmdev.entity.Car_;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QCar.car;

public class CarRepository{
    private static final CarRepository INSTANCE = new CarRepository();

    public static CarRepository getInstance() {
        return INSTANCE;
    }

    public List<Car> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car);

        return session.createQuery(criteria)
                .list();
    }

    public List<Car> findAllQueryDsl(Session session) {
        return new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .fetch();
    }

    public Optional<Car> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car)
                .where(cb.equal(car.get(Car_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public Optional<Car> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .where(car.id.eq(id))
                .fetchOne());
    }

    public Optional<Car> findCarByNumberCriteria(Session session, String serialNumber) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car)
                .where(cb.equal(car.get(Car_.serialNumber), serialNumber));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }
}