package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.model.QRoleModel;
import com.getouo.jpaquerydsldypdemo.model.RoleModel;
import com.getouo.jpaquerydsldypdemo.model.UserModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class JpaQuerydslDypDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaQuerydslDypDemoApplication.class, args);
    }

    /**
     * 初始化数据
     *
     * demo使用的是内存数据库, 很多where条件不支持。 最好部署mysql跑
     * @param repo
     * @return
     */
    @Bean
    InitializingBean saveData(UserRepository repo) {
        RoleModel admin = new RoleModel("管理员", "厉害了", 10);
        RoleModel simple = new RoleModel("运维", "可以啊", 20);
        return () -> {
            repo.save(new UserModel("1", "jack", 1, "admin", Arrays.asList(admin, simple)));
            repo.save(new UserModel("2", "ali", 2, "world", Arrays.asList(admin)));
            repo.save(new UserModel("3", "baba", 3, "123456", Arrays.asList(simple)));
            repo.save(new UserModel("4", "yo", 4, "78", new ArrayList<>()));
            repo.save(new UserModel("5", "wei", 5, "00", new ArrayList<>()));
            repo.save(new UserModel(UUID.randomUUID().toString(), "hello", 20, "world", new ArrayList<>()));
            repo.save(new UserModel(UUID.randomUUID().toString(), "hello", 20, "world", new ArrayList<>()));
            repo.save(new UserModel(UUID.randomUUID().toString(), "hello", 20, "world", new ArrayList<>()));
            repo.save(new UserModel(UUID.randomUUID().toString(), "hello", 20, "world", new ArrayList<>()));
            repo.save(new UserModel(UUID.randomUUID().toString(), "hello", 20, "world", new ArrayList<>()));
        };
    }
}
