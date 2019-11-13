package com.getouo.jpaquerydsldypdemo.resolvers;

import com.getouo.jpaquerydsldypdemo.bindings.QuerydslCustomBinding;
import com.getouo.jpaquerydsldypdemo.resolvers.QuerydslCustomPredicateResolver;
import com.getouo.jpaquerydsldypdemo.supports.QueryPredicateType;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询参数解析工具
 *
 * 主要作用是对前端调用时传递的查询字符串转换为符合格式要求的结构
 */
public class QueryParamAccessorUtil {

    /**
     * 筛选出以指定后缀的kv条件对, 并在去掉指定后缀后返回新的 MultiValueMap
     * @param suffix 指定后缀
     * @param values 值组
     * @return 符合条件的去掉后缀的kv集合
     */
    public static MultiValueMap<String, String> accessorOfSuffix(String suffix, MultiValueMap<String, String> values) {
        final LinkedMultiValueMap<String, String> objectObjectLinkedMultiValueMap = new LinkedMultiValueMap<>();
        castNullable(values).ifPresent(v -> {
            v.entrySet().stream().filter(e -> StringUtils.endsWithIgnoreCase(e.getKey(), suffix)).forEach(e -> {
                objectObjectLinkedMultiValueMap.put(StringUtils.removeEndIgnoreCase(e.getKey(), suffix), e.getValue());
            });
        });
        return objectObjectLinkedMultiValueMap;
    }

    /**
     * 筛选出以指定前缀的kv条件对, 并在去掉指定前缀后返回新的 MultiValueMap
     * @param prefix 指定前缀
     * @param values 值组
     * @return 符合条件的去掉前缀的kv集合
     */
    public static MultiValueMap<String, String> accessorOfPrefix(String prefix, MultiValueMap<String, String> values) {
        final LinkedMultiValueMap<String, String> objectObjectLinkedMultiValueMap = new LinkedMultiValueMap<>();
        castNullable(values).ifPresent(v -> {
            v.entrySet().stream().filter(e -> StringUtils.startsWithIgnoreCase(e.getKey(), prefix)).forEach(e -> {
                objectObjectLinkedMultiValueMap.put(StringUtils.removeStartIgnoreCase(e.getKey(), prefix), e.getValue());
            });
        });
        return objectObjectLinkedMultiValueMap;
    }

    /**
     * 生成条件谓词
     * @param typeOfEntity 目标实体类型
     * @param accessor 路径表达式和条件值
     * @param customBinding 路径绑定映射处理器
     * @param <E> 实体类型
     * @return 谓词
     */
    public static <E> com.querydsl.core.types.Predicate createPredicate(
            Class<E> typeOfEntity, MultiValueMap<String, String> accessor, QuerydslCustomBinding customBinding) {
        try {
            return accessor.size() > 0 ? new QuerydslCustomPredicateResolver(customBinding).resolve(typeOfEntity, accessor) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成条件谓词集
     * @param typeOfEntity 目标实体类型
     * @param srcParams 带后缀的路径表达式和条件值
     * @param querys 路径绑定映射处理器和后缀映射集
     * @param <E> 实体类型
     * @return 谓词
     */
    @SafeVarargs
    public static <E> Set<Predicate> createPredicates(
            Class<E> typeOfEntity, MultiValueMap<String, String> srcParams, Tuple2<QueryPredicateType, QuerydslCustomBinding>...querys) {

        Set<Predicate> collect = Arrays.stream(querys).map(query ->
                createPredicate(typeOfEntity, accessorOfSuffix(query._1().type, srcParams), query._2()))
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return collect;
    }

    /**
     * 对象包装为Optional
     * @param element
     * @param <T>
     * @return
     */
    private static <T> Optional<T> castNullable(T element) {
        return Optional.ofNullable(element);
    }


}