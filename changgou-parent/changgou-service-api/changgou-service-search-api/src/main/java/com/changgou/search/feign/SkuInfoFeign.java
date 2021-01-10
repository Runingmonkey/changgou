package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/22 10:45
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SkuInfoFeign {

    @GetMapping
    Map<String, Object> search(@RequestParam(required = false) Map<String, String> searchMap);
}
