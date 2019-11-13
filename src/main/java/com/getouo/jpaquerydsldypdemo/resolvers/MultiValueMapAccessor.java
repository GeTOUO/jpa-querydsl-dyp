package com.getouo.jpaquerydsldypdemo.resolvers;

import com.getouo.jpaquerydsldypdemo.bindings.QuerydslBetweenBinding;
import com.getouo.jpaquerydsldypdemo.bindings.QuerydslLikeBinding;
import com.getouo.jpaquerydsldypdemo.bindings.QuerydslNullBinding;
import com.getouo.jpaquerydsldypdemo.supports.PredicateConnectLogic;
import com.getouo.jpaquerydsldypdemo.supports.QueryPredicateType;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 键值存取工具类, 避免null异常和支持类型转换
 */
public final class MultiValueMapAccessor {
    private final MultiValueMap<String, String> vm;

    public MultiValueMapAccessor(MultiValueMap vm) {
        this.vm = vm;
    }

    public <T> List<T> values(String keyName, Function<String, T> valueConverter) {
        return vm == null ? new ArrayList<>() : Optional.ofNullable(vm.get(keyName))
                .map(strList -> strList.stream().map(valueConverter).collect(Collectors.toList()))
                .orElseGet(() -> new ArrayList<>());
    }

    public static MultiValueMapAccessor of(MultiValueMap<String, String> vm) {
        return new MultiValueMapAccessor(vm);
    }

    public MultiValueMap<String, String> multiValueMap() {
        return Optional.ofNullable(vm).orElseGet(LinkedMultiValueMap::new);
    }

    public Set<Predicate> groupPredicates(MultiValueMap<String, String> repos, PredicateConnectLogic logic, Class targetEntityClz) {
        Set predicates = QueryParamAccessorUtil.createPredicates(targetEntityClz, repos,
                Tuple.of(QueryPredicateType.LIKE, new QuerydslLikeBinding(logic)),                     // 解析String类型字段的模糊查询条件
                Tuple.of(QueryPredicateType.NULL, new QuerydslNullBinding(logic)),                     // 解析任意字段空的验证, 集合以元素个数
                Tuple.of(QueryPredicateType.BETWEEN, new QuerydslBetweenBinding(logic))                // 和集合支持

                // 下面两个需要扩展数据库函数才能支持
//                Tuple.of(QueryPredicateType.BIT_HAS, new QuerydslBitwiseBinding(logic,true)),       // 解析数值类型字段的二进制位运算包含
//                Tuple.of(QueryPredicateType.BIT_NOTHAS, new QuerydslBitwiseBinding(logic,false))    // 解析数值类型字段的二进制位运算不包含
        );
        return predicates;
    }

}