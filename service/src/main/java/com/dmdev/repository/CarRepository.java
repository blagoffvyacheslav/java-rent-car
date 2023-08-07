package com.dmdev.repository;

import com.dmdev.entity.Car;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import static com.dmdev.entity.QCar.car;

@Repository
public class CarRepository extends BaseRepository<Long, Car>{

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }


    public List<Car> findAllQueryDsl(Session session) {
        return new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .fetch();
    }


    public Optional<Car> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .where(car.id.eq(id))
                .fetchOne());
    }

}