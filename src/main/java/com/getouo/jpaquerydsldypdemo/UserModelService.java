package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserModelService extends BasicModelService<UserRepository, UserModel, String> {

    /**
     * 这个函数可以优化一下， 动态反射出E类型
     * @return
     */
    @Override
    protected Class<UserModel> getEntityType() {
        return UserModel.class;
    }
}
