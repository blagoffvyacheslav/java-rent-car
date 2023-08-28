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

    public CarRepository() {
        super(Car.class);
    }


    public List<Car> findAllQueryDsl() {
        return new JPAQuery<Car>(getEntityManager())
                .select(car)
                .from(car)
                .fetch();
    }


    public Optional<Car> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Car>(getEntityManager())
                .select(car)
                .from(car)
                .where(car.id.eq(id))
                .fetchOne());
    }

}