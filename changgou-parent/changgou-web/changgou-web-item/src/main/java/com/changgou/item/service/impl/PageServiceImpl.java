package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import com.changgou.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/23 11:34
 */
@Service
public class PageServiceImpl implements PageService {

    // 静态文件存储的位置
    @Value("${pagepath}")
    private String pagePath;

    // 模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SpuFeign spuFeign;

    public Map<String,Object> buildDataModel(String spuid){
        Map<String, Object> map = new HashMap<>();

        // 根据spuid获取spu,并存储
        Result<Spu> spuResult = spuFeign.findById(spuid);
        Spu spu = spuResult.getData();

        // 根据spuid查询sku列表
        Sku sku = new Sku();
        sku.setSpuId(spuid);
        List<Sku> skuList = skuFeign.findList(sku).getData();


        // 获取分类信息
        map.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        map.put("category2",categoryFeign.findById(spu.getCategory1Id()).getData());
        map.put("category3",categoryFeign.findById(spu.getCategory1Id()).getData());

        // 获取图片地址
        map.put("images",spu.getImages().split(","));
        map.put("spu",spu);
        map.put("skuList",skuList);
        // 规格数据  {"适合人群":["零基础"],"书籍分类":["科技","软件编程","网络营销"]}
        String specItems = spu.getSpecItems();
        Map<String,List<String>> specMap = JSON.parseObject(specItems, Map.class);
        map.put("specList",specMap);
        return map;
    }



    // 根据skuid生成静态页面
    @Override
    public void createPageHtml(String spuid) {
        try {
            // 上下文对象
            Context context = new Context();
            // 数据模型; 用来存储需要填充到 页面的 数据
            Map<String, Object> dataModel = buildDataModel(spuid);
            context.setVariables(dataModel);

            // 用来存放静态页面的数据和 页面的名称
            File dest = new File(pagePath, spuid + ".html");

            // 生成静态页面
            PrintWriter printWriter = new PrintWriter(dest, "UTF-8");

            templateEngine.process("item",context,printWriter);

        } catch (Exception e) {
            throw new RuntimeException("生成静模板文件失败");
        }
    }




}
