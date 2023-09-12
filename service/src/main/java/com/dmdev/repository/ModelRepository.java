package com.dmdev.repository;

import com.dmdev.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long>, QuerydslPredicateExecutor<Model> {

    List<Model> findModelsByName(String name);
}