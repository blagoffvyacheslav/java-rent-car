package com.dmdev.repository;

import com.dmdev.dto.OrderDetailsFilter;
import com.dmdev.entity.OrderDetails;
import com.dmdev.utils.CriteriaPredicate;
import com.dmdev.utils.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QOrderDetails.orderDetails;
import static com.dmdev.entity.QOrder.order;

@Repository
public class OrderDetailsRepository extends BaseRepository<Long, OrderDetails> {
    public OrderDetailsRepository() {
        super(OrderDetails.class);
    }


    public List<OrderDetails> findAllQueryDsl() {
        return new JPAQuery<OrderDetails>(getEntityManager())
                .select(orderDetails)
                .from(orderDetails)
                .fetch();
    }

    public Optional<OrderDetails> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<OrderDetails>(getEntityManager())
                .select(orderDetails)
                .from(orderDetails)
                .where(orderDetails.id.eq(id))
                .fetchOne());
    }

    public Optional<OrderDetails> findOrderDetailsByOrderIdQueryDsl(Long orderId) {
        return Optional.ofNullable(new JPAQuery<OrderDetails>(getEntityManager())
                .select(orderDetails)
                .from(orderDetails)
                .join(orderDetails.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }


    public List<OrderDetails> findOrderDetailsBetweenStartAndRentalDatesQueryDsl(OrderDetailsFilter orderDetailsFilter) {
        var predicateOrStart = QPredicate.builder()
                .add(orderDetailsFilter.getStartDate(), orderDetails.startDate::eq)
                .add(orderDetailsFilter.getStartDate(), orderDetails.startDate::gt)
                .buildOr();

        var predicateOrEnd = QPredicate.builder()
                .add(orderDetailsFilter.getEndDate(), orderDetails.startDate::eq)
                .add(orderDetailsFilter.getEndDate(), orderDetails.startDate::lt)
                .buildOr();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOrStart)
                .addPredicate(predicateOrEnd)
                .buildAnd();

        return new JPAQuery<OrderDetails>(getEntityManager())
                .select(orderDetails)
                .from(orderDetails)
                .where(predicateAll)
                .fetch();
    }
}