package com.changgou.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import com.changgou.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mike ling
 * @description
 * @date 2021/6/10 14:23
 */
@RestController
public class UserController {

    public static final String RESOURCE_NAME = "userInfo";

    //
    @Resource
    private UserService userService;


    // 抛出异常
    @GetMapping("/user-info")
    private String getUser() {
        String userInfo = "";
        Entry entry = null;
        try {
            SphU.entry(RESOURCE_NAME);
            userInfo = userService.getUserInfo();
        } catch (BlockException e) {
            // 若需要配置降级规则，需要通过这种方式记录业务异常
            Tracer.traceEntry(e, entry);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return userInfo;
    }


    public static final String RESOURCE_NAME_QUERY_USER_BY_ID = "queryUserById";

    //
    @RequestMapping("/get/{id}")
    public String queryUserById(@PathVariable("id") String id) {
        if (SphO.entry(RESOURCE_NAME_QUERY_USER_BY_ID)) {
            try {
                //被保护的逻辑
                //模拟数据库查询数据
                return JSONObject.toJSONString("User");
            } finally {
                //关闭资源
                SphO.exit();
            }
        } else {
            //资源访问阻止，被限流或被降级
            return "Resource is Block!!!";
        }
    }

}
