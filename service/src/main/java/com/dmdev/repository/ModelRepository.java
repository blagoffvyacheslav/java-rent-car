package com.dmdev.repository;

import com.dmdev.entity.Model;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import static com.dmdev.entity.QModel.model;

import org.springframework.stereotype.Repository;

@Repository
public class ModelRepository extends BaseRepository<Long, Model> {
    public ModelRepository(EntityManager entityManager) {
        super(Model.class, entityManager);
    }

    public List<Model> findAllQueryDsl(Session session) {
        return new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .fetch();
    }


    public Optional<Model> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .where(model.id.eq(id))
                .fetchOne());
    }
}