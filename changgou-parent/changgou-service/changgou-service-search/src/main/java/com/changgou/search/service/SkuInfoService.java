package com.changgou.search.service;


import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/16 11:43
 */
public interface SkuInfoService {
    // 将数据库中已上架商品的数据导入 到 es中
    void importSkuInfoToEs();

    // 搜索; 返回的数据也用Map封装起来; 为什么返回的数据也要用map封装起来?
     Map<String,Object> search(Map<String, String> map);

}
