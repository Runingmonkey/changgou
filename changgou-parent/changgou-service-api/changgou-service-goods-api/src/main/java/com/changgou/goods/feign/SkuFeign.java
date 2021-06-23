package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import com.changgou.util.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/14 19:28
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * @description: 根据状态查询 商品
     * @author: QIXIANG LING
     * @date: 2020/7/15 17:21
     * @param: status
     * @return: com.changgou.util.Result<java.util.List<com.changgou.goods.pojo.Sku>>
     */
    @GetMapping("/findSkuByStatus/{status}")
    Result<List<Sku>> findSkuByStatus(@PathVariable(value = "status") String status);

    /**
     * @description: 根据spuid查询 sku 列表
     * @author: QIXIANG LING
     * @date: 2020/7/23 12:22
     * @param: null
     * @return:
     */
    @PostMapping(value = "/search" )
    Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku);

    /**
     * @description: 根据id 查询 sku
     * @author: QIXIANG LING
     * @date: 2020/7/29 10:28
     * @param: id
     * @return: com.changgou.util.Result<com.changgou.goods.pojo.Sku>
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable(value = "id") String id);

    /**
     * @description: 下单成功扣减库存
     * @author: QIXIANG LING
     * @date: 2020/8/1 10:01
     * @param: username
     * @return: com.changgou.util.Result
     */
    @GetMapping("/decr/{username}")
    Result decrSkuNum(@PathVariable(value = "username" ,required = false)String username);
}
