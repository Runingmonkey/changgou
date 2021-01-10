package com.changgou.search.controller;

import com.changgou.search.service.SkuInfoService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/16 14:24
 */
@RestController
@RequestMapping("/search")
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * @description: es导入数据
     * @author: QIXIANG LING
     * @date: 2020/7/17 0:05
     * @param:
     * @return: com.changgou.util.Result
     */
    @GetMapping("/import")
    public Result importData(){
        skuInfoService.importSkuInfoToEs();
        return new Result<>(true, StatusCode.OK,"成功");
    }

    /**
     * @description: 关键字搜索
     * @author: QIXIANG LING
     * @date: 2020/7/17 0:07
     * @param: searchMap
     * @return: com.changgou.util.Result
     */
    @GetMapping
    public Map<String, Object> search(@RequestParam(required = false) Map<String,String> searchMap){
        Map<String, Object> searchResultMap = skuInfoService.search(searchMap);
        return searchResultMap;
    }

}
