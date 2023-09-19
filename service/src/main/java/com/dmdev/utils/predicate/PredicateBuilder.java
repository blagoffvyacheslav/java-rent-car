package com.dmdev.utils.predicate;

import com.querydsl.core.types.Predicate;

public interface PredicateBuilder<F> {

    Predicate build (F requestFilter);
}
