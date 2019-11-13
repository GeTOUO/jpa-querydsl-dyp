package com.getouo.jpaquerydsldypdemo;

import com.getouo.jpaquerydsldypdemo.model.UserModel;
import com.getouo.jpaquerydsldypdemo.resolvers.MultiValueMapAccessor;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoController {

    @Autowired
    UserModelService userModelService;

    /**
     *
     * 参数是固定的格式
     * @param predicate 自动解析所有的 eq 条件
     * @param pageable 分页对象，可以做一些默认操作，比如 parameters 中未传递的时候，根据当前分页对象修改默认设置
     * @param parameters 实际原参数列表, 做自定义扩展
     * @return 符合条件的
     */
    @RequestMapping("/page")
    public Page<UserModel> userPageOnlyEqual(@QuerydslPredicate(root = UserModel.class) Predicate predicate, Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

        if (!parameters.containsKey("size")) {
            pageable = PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, pageable.getSort());
        }

        return predicate == null ? userModelService.repository.findAll(pageable) : userModelService.anyPredicate(predicate, pageable, MultiValueMapAccessor.of(parameters));
    }






}
