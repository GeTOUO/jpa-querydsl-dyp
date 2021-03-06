package com.getouo.jpaquerydsldypdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BasicRepository<E, ID> extends
        JpaRepository<E, ID>,
        JpaSpecificationExecutor<E>,
        QuerydslPredicateExecutor<E>,
        Serializable {
}
