package com.changgou.controller;

import com.changgou.search.feign.SkuInfoFeign;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/22 10:12
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SkuInfoFeign skuInfoFeign;


    /**
     * @description:
     * @author: QIXIANG LING
     * @date: 2020/7/22 10:18
     * @param: 
     * @return: java.lang.String
     */
    @GetMapping("/list")
    public String  list(@RequestParam(required = false) Map<String,String> searchMap , Model model){
        // 掉search 微服务 获取查询结果
        Map<String, Object> resultMap = skuInfoFeign.search(searchMap);

        model.addAttribute("searchMap",searchMap);
        model.addAttribute("resultMap",resultMap);

        // 设置路径
        model.addAttribute("url",getUrl(searchMap));

        // 分页结果
        Page<SkuInfo> page = new Page<>(
                Long.parseLong(resultMap.get("totalElements").toString()),
                 Integer.parseInt(resultMap.get("pageNum").toString()) + 1,
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page",page);
        // 返回搜索页面
        return "search";
    }

    // 根据搜索条件组装url
    // http://localhost:18085/search?keywords=手机&category=手机
    // &brand=小米&Spec_颜色=紫色&priceRange=0-1500&sortField=price&sortRule=DESC
    private String getUrl(Map<String,String> searchMap){
        String url = "/search/list";
        // 若查询条件不为空 , 就拼接查询条件
        if(searchMap.size() != 0){
            url = url + "?";
            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                // 如果是页码,则不需要拼接
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("pageNum")){
                    continue;
                }
                url = url + key + "=" + value + "?";
            }
            // 去掉最后一个 &
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }



}
