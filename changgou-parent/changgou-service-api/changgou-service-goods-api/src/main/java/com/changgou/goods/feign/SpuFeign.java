package com.changgou.goods.feign;

import com.changgou.goods.pojo.Spu;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/23 12:27
 */
@FeignClient(name="goods")
@RequestMapping("/spu")
public interface SpuFeign {

    /**
     * @description: 根据spuid 查询spu信息
     * @author: QIXIANG LING
     * @date: 2020/7/23 12:28
     * @param: id
     * @return: com.changgou.util.Result<com.changgou.goods.pojo.Spu>
     */
    @GetMapping("/{id}")
    Result<Spu> findById(@PathVariable(value = "id") String id);
}
