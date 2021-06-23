package com.changgou.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.changgou.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author mike ling
 * @description
 * @date 2021/6/10 14:52
 */
@Service
public class UserService implements IUserService {

    @Override
    public String getUserInfo() {
        return "User--01";
    }


    //资源名称
    public static final String RESOURCE_NAME_QUERY_USER_BY_NAME = "queryUserByUserName";

    //value是资源名称，是必填项。blockHandler填限流处理的方法名称
    @Override
    @SentinelResource(value = RESOURCE_NAME_QUERY_USER_BY_NAME, blockHandler = "queryUserByUserNameBlock")
    public String queryByUserName(String userName) {
        return "mike";
    }

    //注意细节，一定要跟原函数的返回值和形参一致，并且形参最后要加个BlockException参数
    //否则会报错，FlowException: null
    public String queryUserByUserNameBlock(String userName, BlockException ex) {
        //打印异常
        ex.printStackTrace();
        return "用户mike资源访问被限流";
    }

}
