package com.changgou.content.feign;
import com.changgou.content.pojo.Content;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.PermitAll;
import java.util.List;

/****
 * @Author:itheima
 * @Description:
 *****/
@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {

    /**
     * @description: 根据categoryId 查询广告列表
     * @author: QIXIANG LING
     * @date: 2020/7/12 20:53
     * @param: id
     * @return: com.changgou.util.Result
     */
    @GetMapping("/findByCategoryId/{id}")
    Result findByCategoryId(@PathVariable(value = "id") Long id);
}