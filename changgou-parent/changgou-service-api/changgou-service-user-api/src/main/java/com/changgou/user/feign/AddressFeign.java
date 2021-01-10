package com.changgou.user.feign;

import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/30 19:47
 */
@FeignClient(name = "user")
@RequestMapping("/address")
public interface AddressFeign {

    /**
     * @description: 根据用户名查询用户的 地址列表
     * @author: QIXIANG LING
     * @date: 2020/7/30 19:52
     */
    @GetMapping("/user/list")
    Result findAddressList(String username);
}
