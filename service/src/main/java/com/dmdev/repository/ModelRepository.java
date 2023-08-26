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
    public ModelRepository() {
        super(Model.class);
    }

    public List<Model> findAllQueryDsl() {
        return new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .fetch();
    }


    public Optional<Model> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .where(model.id.eq(id))
                .fetchOne());
    }
}