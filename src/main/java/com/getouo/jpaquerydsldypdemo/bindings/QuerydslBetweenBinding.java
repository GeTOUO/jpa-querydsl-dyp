package com.getouo.jpaquerydsldypdemo.bindings;

import com.getouo.jpaquerydsldypdemo.supports.PredicateConnectLogic;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class QuerydslBetweenBinding extends QuerydslCustomBinding<Path<? extends Object>, Object> {
    public QuerydslBetweenBinding() {
    }

    public QuerydslBetweenBinding(PredicateConnectLogic predicateConnect) {
        super(predicateConnect);
    }

    @Override
    public Optional<Predicate> bind(Path<?> path, Collection<?> value) {
        Assert.notNull(path, "Path must not be null!");
        Assert.notNull(value, "Value must not be null!");

        if (value.isEmpty() || value.size() < 2) {
            return Optional.empty();
        }
        try {
            final Iterator<?> iterator = value.iterator();
            Long left = Long.valueOf(iterator.next().toString());
            Long right = Long.valueOf(iterator.next().toString());
            if (left > right) {
                Long iBuffer = left;
                left = right;
                right = iBuffer;
            }

            if (path instanceof CollectionPathBase) {
                return Optional.of(((CollectionPathBase) path).size().between(left, right));
            } else if (path instanceof NumberExpression) {
//                Expressions.numberTemplate()
                return Optional.of(((NumberExpression) path).between(left, right));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
