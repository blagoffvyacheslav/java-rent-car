package com.dmdev.repository;

import com.dmdev.dto.OrderFilter;
import com.dmdev.entity.*;
import com.dmdev.utils.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QCar.car;
import static com.dmdev.entity.QDamage.damage;
import static com.dmdev.entity.QModel.model;
import static com.dmdev.entity.QOrder.order;

@Repository
public class OrderRepository extends BaseRepository<Long, Order>{

    public OrderRepository() {
        super(Order.class);
    }

    public List<Order> findAllQueryDsl() {
        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .fetch();
    }

    public Optional<Order> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .where(order.id.eq(id))
                .fetchOne());
    }

    public List<Tuple> findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
        return new JPAQuery<Tuple>(getEntityManager())
                .select(order.date, order.amount.avg())
                .from(order)
                .groupBy(order.date)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(OrderFilter orderFilter) {
        var predicates = QPredicate.builder()
                .add(orderFilter.getModelName(), model.name::eq)
                .buildAnd();

        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .join(order.car, car)
                .join(car.model, model)
                .where(predicates)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl() {
        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .join(order.damages, damage)
                .groupBy(order.id)
                .having(damage.amount.avg().gt(
                        new JPAQuery<BigDecimal>(getEntityManager())
                                .select(damage.amount.avg())
                                .from(damage)
                ))
                .orderBy(order.date.asc())
                .fetch();
    }
}