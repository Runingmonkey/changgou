package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/28 16:42
 */
@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

    /**
     * @description: 根据id查询用户信息
     * @author: QIXIANG LING
     * @date: 2020/7/28 16:42
     * @param: id 用户名
     * @return: com.changgou.util.Result<com.changgou.user.pojo.User>
     */
    @GetMapping("/{id}")
    Result<User> findById(@PathVariable(value = "id") String id);

    /**
     * @description: 增加用户积分
     * @author: QIXIANG LING
     * @date: 2020/8/1 18:30
     * @param: point
     * @return: com.changgou.util.Result
     */
    @PostMapping("/userinfo/{point}")
    Result addUserPoint(@PathVariable(value = "point") Integer point);

}
