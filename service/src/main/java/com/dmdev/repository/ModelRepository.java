package com.dmdev.repository;

import com.dmdev.dto.ModelFilter;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.Model;
import com.dmdev.entity.Model_;
import com.dmdev.utils.CriteriaPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QModel.model;

public class ModelRepository extends BaseRepository<Long, Model> {
    public ModelRepository(EntityManager entityManager) {
        super(Model.class, entityManager);
    }

    public List<Model> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);

        criteria.select(model);

        return session.createQuery(criteria)
                .list();
    }

    public List<Model> findAllQueryDsl(Session session) {
        return new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .fetch();
    }

    public Optional<Model> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);

        criteria.select(model)
                .where(cb.equal(model.get(Model_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public Optional<Model> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .where(model.id.eq(id))
                .fetchOne());
    }

    public List<Model> findModelsByModelAndBrandNameCriteria(Session session, ModelFilter modelFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(modelFilter.getName(), mod -> cb.equal(model.get(Model_.name), mod))
                .getPredicates();

        criteria.select(model)
                .where(predicates);

        return session
                .createQuery(criteria)
                .list();
    }
}