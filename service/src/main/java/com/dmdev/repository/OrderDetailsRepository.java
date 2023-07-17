package com.dmdev.repository;

import com.dmdev.dto.OrderDetailsFilter;
import com.dmdev.entity.OrderDetails;
import com.dmdev.entity.OrderDetails_;
import com.dmdev.entity.Order_;
import com.dmdev.utils.CriteriaPredicate;
import com.dmdev.utils.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QOrderDetails.orderDetails;
import static com.dmdev.entity.QOrder.order;

public class OrderDetailsRepository {
    private static final OrderDetailsRepository INSTANCE = new OrderDetailsRepository();

    public static OrderDetailsRepository getInstance() {
        return INSTANCE;
    }

    public List<OrderDetails> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(OrderDetails.class);
        var orderDetails = criteria.from(OrderDetails.class);

        criteria.select(orderDetails);

        return session.createQuery(criteria)
                .list();
    }

    public List<OrderDetails> findAllQueryDsl(Session session) {
        return new JPAQuery<OrderDetails>(session)
                .select(orderDetails)
                .from(orderDetails)
                .fetch();
    }

    public Optional<OrderDetails> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(OrderDetails.class);
        var orderDetails = criteria.from(OrderDetails.class);

        criteria.select(orderDetails)
                .where(cb.equal(orderDetails.get(OrderDetails_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public Optional<OrderDetails> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<OrderDetails>(session)
                .select(orderDetails)
                .from(orderDetails)
                .where(orderDetails.id.eq(id))
                .fetchOne());
    }

    public Optional<OrderDetails> findOrderDetailsByOrderIdCriteria(Session session, Long orderId) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(OrderDetails.class);
        var orderDetails = criteria.from(OrderDetails.class);
        var order = orderDetails.join(OrderDetails_.order);

        criteria.select(orderDetails)
                .where(cb.equal(order.get(Order_.id), orderId));

        return session.createQuery(criteria).uniqueResultOptional();
    }

    public Optional<OrderDetails> findOrderDetailsByOrderIdQueryDsl(Session session, Long orderId) {
        return Optional.ofNullable(new JPAQuery<OrderDetails>(session)
                .select(orderDetails)
                .from(orderDetails)
                .join(orderDetails.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }

    public List<OrderDetails> findOrderDetailsBetweenStartAndRentalDatesCriteria(Session session, OrderDetailsFilter orderDetailsFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(OrderDetails.class);
        var orderDetails = criteria.from(OrderDetails.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(orderDetailsFilter.getStartDate(), startDate -> cb.greaterThanOrEqualTo(orderDetails.get(OrderDetails_.startDate), startDate))
                .add(orderDetailsFilter.getEndDate(), endDate -> cb.lessThanOrEqualTo(orderDetails.get(OrderDetails_.endDate), endDate))
                .getPredicates();

        criteria.select(orderDetails)
                .where(predicates);

        return session.createQuery(criteria)
                .list();
    }

    public List<OrderDetails> findOrderDetailsBetweenStartAndRentalDatesQueryDsl(Session session, OrderDetailsFilter orderDetailsFilter) {
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

        return new JPAQuery<OrderDetails>(session)
                .select(orderDetails)
                .from(orderDetails)
                .where(predicateAll)
                .fetch();
    }
}