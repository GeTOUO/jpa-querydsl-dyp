package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.model.QUserModel;
import com.getouo.jpaquerydsldypdemo.model.RoleModel;
import com.getouo.jpaquerydsldypdemo.model.UserModel;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface RoleRepository extends BasicRepository<RoleModel, String> {

}
