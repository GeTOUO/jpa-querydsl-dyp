package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.model.QUserModel;
import com.getouo.jpaquerydsldypdemo.model.UserModel;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserRepository extends BasicRepository<UserModel, String>, QuerydslBinderCustomizer<QUserModel>  {
    @Override
    default void customize(QuerydslBindings querydslBindings, QUserModel qUserModel) {
        querydslBindings.excluding(qUserModel.password);// 不允许对password建立查询条件
    }
}
