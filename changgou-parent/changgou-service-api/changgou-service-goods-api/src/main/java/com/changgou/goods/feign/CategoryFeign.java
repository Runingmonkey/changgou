package com.changgou.goods.feign;

import com.changgou.goods.pojo.Category;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/23 12:07
 */
@FeignClient(name = "goods")
@RequestMapping("/category")
public interface CategoryFeign {

    /**
     * @description: 根据id查询分类
     * @author: QIXIANG LING
     * @date: 2020/7/23 12:09
     * @param: id
     * @return: com.changgou.util.Result<com.changgou.goods.pojo.Category>
     */
    @GetMapping("/{id}")
    Result<Category> findById(@PathVariable Integer id);
}
