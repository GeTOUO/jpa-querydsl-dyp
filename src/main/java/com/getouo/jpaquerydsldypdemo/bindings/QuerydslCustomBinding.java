package com.getouo.jpaquerydsldypdemo.bindings;


import com.getouo.jpaquerydsldypdemo.supports.PredicateConnectLogic;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

public abstract class QuerydslCustomBinding<T extends Path<? extends S>, S> implements MultiValueBinding<T, S> {
    protected Optional<PredicateConnectLogic> predicateConnect = Optional.of(PredicateConnectLogic.DEFAULT_AND);

    public QuerydslCustomBinding() {
        this(PredicateConnectLogic.DEFAULT_AND);
    }

    public QuerydslCustomBinding(PredicateConnectLogic predicateConnect) {
        this.predicateConnect = Optional.ofNullable(predicateConnect);
    }

    protected boolean isAnd() {
        return !(predicateConnect.isPresent() && predicateConnect.get().equals(PredicateConnectLogic.OR));
    }

    private static String getClassBase(String shortName) {
        String[] parts = shortName.split("\\.");
        return parts.length < 2 ? "" : parts[0] + "_";
    }

    private static Optional<Field> getStaticFieldOfType(Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            boolean hasSameType = type.equals(field.getType());
            if (isStatic && hasSameType) {
                return Optional.of(field);
            }
        }
        Class<?> superclass = type.getSuperclass();
        return Object.class.equals(superclass) ? Optional.empty() : getStaticFieldOfType(superclass);
    }

    private static final String NO_CLASS_FOUND_TEMPLATE = "Did not find a query class %s for domain class %s!";
    private static final String NO_FIELD_FOUND_TEMPLATE = "Did not find a static field of the same type in %s!";

    public static <T> EntityPath<T> createPath(Class<T> domainClass) {
        String pathClassName = getQueryClassName(domainClass);
        try {
            Class<?> pathClass = ClassUtils.forName(pathClassName, domainClass.getClassLoader());
            return getStaticFieldOfType(pathClass)//
                    .map(it -> (EntityPath<T>) ReflectionUtils.getField(it, null))//
                    .orElseThrow(() -> new IllegalStateException(String.format(NO_FIELD_FOUND_TEMPLATE, pathClass)));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format(NO_CLASS_FOUND_TEMPLATE, pathClassName, domainClass.getName()),
                    e);
        }
    }

    private static String getQueryClassName(Class<?> domainClass) {

        String simpleClassName = ClassUtils.getShortName(domainClass);
        String packageName = domainClass.getPackage().getName();

        return String.format("%s%s.Q%s%s", packageName, "", getClassBase(simpleClassName),
                domainClass.getSimpleName());
    }
}
