package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.resolvers.MultiValueMapAccessor;
import com.getouo.jpaquerydsldypdemo.resolvers.QueryParamAccessorUtil;
import com.getouo.jpaquerydsldypdemo.supports.PredicateConnectLogic;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BasicModelService<R extends BasicRepository<E, ID>, E, ID> {

    @Autowired
    public R repository;

    Page<E> anyPredicate(com.querydsl.core.types.Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    Page<E> anyPredicate(com.querydsl.core.types.Predicate eqPredicate, Pageable pageable, MultiValueMapAccessor customQuery) {

        Class<E> entityType = getEntityType();

        Set<Predicate> predicatesOfUserCustom = customQuery.groupPredicates(customQuery.multiValueMap(), PredicateConnectLogic.DEFAULT_AND, entityType);

        final MultiValueMap<String, String> orQueryValueMap = QueryParamAccessorUtil.accessorOfPrefix(PredicateConnectLogic.OR.type, customQuery.multiValueMap());
        final Set<com.querydsl.core.types.Predicate> orPredicates = orQueryValueMap.entrySet().stream().map(singleKv -> {
            final MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
            linkedMultiValueMap.put(singleKv.getKey(), singleKv.getValue());
            return linkedMultiValueMap;
        }).map(singleKVMap -> ExpressionUtils
                .anyOf(customQuery.groupPredicates(singleKVMap, PredicateConnectLogic.OR, entityType)))
                .collect(Collectors.toSet());
        predicatesOfUserCustom.add(eqPredicate);

        final com.querydsl.core.types.Predicate allOrPredicate = ExpressionUtils.anyOf(orPredicates);
        final com.querydsl.core.types.Predicate allAndPredicate = ExpressionUtils.allOf(predicatesOfUserCustom);
        final com.querydsl.core.types.Predicate allPredicate = ExpressionUtils.allOf(allAndPredicate, allOrPredicate);


        assert allPredicate != null;
        return repository.findAll(allPredicate, pageable);
    }

    abstract protected Class<E> getEntityType();

    Iterable<E> anyPredicate(com.querydsl.core.types.Predicate predicate) {
        return repository.findAll(predicate);
    }

}
