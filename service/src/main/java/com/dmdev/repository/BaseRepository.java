package com.dmdev.repository;

import com.dmdev.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<K extends Serializable, E extends BaseEntity<K>> {
    private final Class<E> clazz;
    @Getter
    @Autowired
    private EntityManager entityManager;

    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public void delete(E entity) {
        entityManager.remove(entityManager.find(clazz, entity.getId()));
        entityManager.flush();
    }

    public void update(E entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    public List<E> findAll() {
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return entityManager.createQuery(criteria)
                .getResultList();
    }
}