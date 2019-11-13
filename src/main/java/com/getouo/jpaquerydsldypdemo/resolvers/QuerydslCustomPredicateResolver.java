package com.getouo.jpaquerydsldypdemo.resolvers;


import com.getouo.jpaquerydsldypdemo.bindings.QuerydslCustomBinding;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

//@Lazy
//@Component
public class QuerydslCustomPredicateResolver {

    private final QuerydslBindingsFactory bindingsFactory;
    private final QuerydslCustomPredicateBuilder predicateBuilder;

    public QuerydslCustomPredicateResolver(QuerydslCustomBinding customBinding) {
        this(null, customBinding);
    }

    public QuerydslCustomPredicateResolver(QuerydslBindingsFactory factory, QuerydslCustomBinding customBinding) {
        this(Optional.ofNullable(factory), Optional.empty(), customBinding);
    }

    /**
     * Creates a new {@link QuerydslPredicateArgumentResolver} using the given {@link ConversionService}.
     *
     * @param factory
     * @param conversionService defaults to {@link DefaultConversionService} if {@literal null}.
     */
    public QuerydslCustomPredicateResolver(Optional<QuerydslBindingsFactory> factory, Optional<ConversionService> conversionService, QuerydslCustomBinding customBinding) {
        this.bindingsFactory = factory == null ? defaultFactory() : factory.orElseGet(() -> defaultFactory());
//        EntityPathResolver
        this.predicateBuilder = new QuerydslCustomPredicateBuilder(
                conversionService.orElseGet(DefaultConversionService::new), bindingsFactory.getEntityPathResolver(), Optional.ofNullable(customBinding));
    }

    public Map<Object, Path<?>> getPath() {
        return this.predicateBuilder.getPaths();
    }


    QuerydslBindingsFactory defaultFactory() {
        return new QuerydslBindingsFactory(
                SimpleEntityPathResolver.INSTANCE);
    }

    /**
     * resolve and build {@link Predicate}.
     *
     * @param root
     *            root class
     * @param customizerClass
     *            the {@link QuerydslBinderCustomizer} class instance
     * @param parameters
     *            parameter map , value can be string or string[] object
     * @return
     * @throws Exception
     */
    public Predicate resolve(Class<?> root, Optional<Class<? extends QuerydslBinderCustomizer<?>>> customizerClass,
                             MultiValueMap<String, String> parameters) throws Exception {

        TypeInformation<?> domainType = extractTypeInfo(root).getActualType();

        if (ObjectUtils.isEmpty(domainType)) {
            throw new Exception("unable to find domainType");
        }

        QuerydslBindings bindings = customizerClass.map(has -> bindingsFactory.createBindingsFor(domainType, has)).orElseGet(() -> bindingsFactory.createBindingsFor(domainType));

        final Field pathSpecsField = bindings.getClass().getDeclaredField("pathSpecs");
        pathSpecsField.setAccessible(true);

        final Predicate predicate = predicateBuilder.getPredicate(domainType, parameters, bindings);
        final Map<Object, Path<?>> paths = predicateBuilder.getPaths();
        final Map<String, Object> pathSpecs = (Map<String, Object>) pathSpecsField.get(bindings);
        final Set<String> keySet = pathSpecs.keySet();
        return predicate;
    }
    public Predicate resolve(Class<?> root, MultiValueMap<String, String> parameters) throws Exception {
        return resolve(root, Optional.empty(), parameters);
    }

    /**
     *
     * @param root
     * @return
     */
    private static TypeInformation<?> extractTypeInfo(Class<?> root) {
        if (root != null && !Object.class.equals(root)) {
            return ClassTypeInformation.from(root);
        }
        return null;
    }
}