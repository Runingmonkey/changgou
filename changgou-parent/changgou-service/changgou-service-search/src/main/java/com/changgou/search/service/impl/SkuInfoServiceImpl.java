package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.EsMapper;
import com.changgou.search.dao.SkuInfoMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuInfoService;
import com.changgou.util.Result;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/16 11:43
 */
@Service
public class SkuInfoServiceImpl implements SkuInfoService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsMapper esMapper;

    @Autowired
    private SkuFeign skuFeign;


    /**
     * @description: 导入数据到es中
     * @author: QIXIANG LING
     * @date: 2020/7/21 11:49
     * @param:
     * @return: void
     */
    @Override
    public void importSkuInfoToEs() {
        // 获取已上架商品
        Result<List<Sku>> result = skuFeign.findSkuByStatus("1");
        List<Sku> data = result.getData();

        // 把数据转化成SkuInfo
        String json = JSON.toJSONString(data);
        List<SkuInfo> skuInfos = JSON.parseArray(json, SkuInfo.class);

        // 处理specMap 字段
        for (SkuInfo skuInfo : skuInfos) {
            String spec = skuInfo.getSpec();
            Map<String,Object>  specMap = JSON.parseObject(spec);
            skuInfo.setSpecMap(specMap);
        }

        // 将数据导入es中
        esMapper.saveAll(skuInfos);

    }

    /**
     * @description:  搜索结果; 返回一个 map
     * @author: QIXIANG LING
     * @date: 2020/7/16 23:05
     * @param: map
     * @return: java.util.List<com.changgou.pojo.SkuInfo>
     */
    @Override
    public Map<String,Object> search(Map<String, String> searchMap) {

        // 根据关键字搜索
        NativeSearchQueryBuilder builder = builderBasicQuery(searchMap);
        // 执行搜索; 封装结果并返回 高亮显示
//        Map<String,Object> resultMap = searchForPage(builder);
        /* // 分类统计 搜索
        List<String> list = searchCategoryList(builder);
        resultMap.put("categoryList",list);
        // 品牌查询 搜索
        List<String> brandList = searchBrandNameList(builder);
        resultMap.put("brandList",brandList);
        // 规格数据查询 搜搜
        Map<String, Set<String>> specMap = searchSpecMap(builder);
        resultMap.put("specMap",specMap);*/
        Map<String, Object> map = getSearch(builder, searchMap);
        return map;
    }

    /**
     * @description: 构建搜索条件;  相当于写sql
     * @author: QIXIANG LING
     * @date: 2020/7/16 23:18
     * @param: searchMap
     * @return: org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
     */
    private NativeSearchQueryBuilder builderBasicQuery(Map<String, String> searchMap) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 查询
        if (searchMap != null) {
            // 搜索条件不为空, 获取关键字
            String keyword = searchMap.get("keywords");

            // 封装搜索条件 就相当于sql 语句封装的 and 条件
            if(!StringUtils.isEmpty(keyword)){
                // 根据关键字查询 名字相关的
                builder.withQuery(QueryBuilders.matchQuery("name",keyword));
            }
            // 根据规格查询 matchQuery 查询所有
            String category = searchMap.get("category");
            if(!StringUtils.isEmpty(category)){
                boolQuery.must(QueryBuilders.matchQuery("categoryName",category));
            }
            String brandName = searchMap.get("brand");
            if(!StringUtils.isEmpty(brandName)){
                boolQuery.must(QueryBuilders.matchQuery("brandName",brandName));
            }
            // 根据规格数据过滤 map.keySet 方法可以获取所有的key 值
            Set<String> stringSet = searchMap.keySet();
            for (String set : stringSet) {
                // 如果开头为"spec_" 就把它添加到查询条件
                if (set.startsWith("spec_")){
                    String[] split = set.split("spec_");
                    // 获取到规格数据信息
                    String specValue = split[1];
                    boolQuery.must(QueryBuilders.matchQuery("specMap."+specValue+".keyword",searchMap.get(set)));
                }
            }
            // 根据价格区间查询;
            String priceRange = searchMap.get("priceRange");
            if(!StringUtils.isEmpty(priceRange)){
                // 转换成 数值; 处理数据  0-550 元 / 3000元以上
                String getPrice = priceRange.replace("元", "").replace("元以上", "");
                String[] split = getPrice.split("-");
                boolQuery.must(QueryBuilders.rangeQuery("price").gte( split[0]));
                // 若搜索条件长度 不为1 ;3000元以上
                if (split.length != 1) {
                    boolQuery.must(QueryBuilders.rangeQuery("price").lte(split[1]));
                }
            }

            // 聚合条件的封装
            // 分类
            builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
            // 品牌
            builder.addAggregation(AggregationBuilders.terms("brandNameList").field("brandName"));
            //规格
            builder.addAggregation(AggregationBuilders.terms("specMap").field("spec.keyword"));

        }

        // builder  封装查询条件
        // and 条件
        builder.withFilter(boolQuery);

        // 分页, 获取页码
        String pageNum = searchMap.get("pageNum");
        // 获取每页大小
        String pageSize = searchMap.get("pageSize");
        int page = 1;
        int size = 30;
        if(!StringUtils.isEmpty(pageNum)){
            page = Integer.valueOf(pageNum);
        }
        if(!StringUtils.isEmpty(pageSize)){
            size = Integer.valueOf(pageSize);
        }
        // es 分页从0开始
        Pageable pageable  = PageRequest.of(page - 1 , size);
        builder.withPageable(pageable);

        // 排序; 设置根据哪个字段排序 以及排序的规则
        // 获取排序的规则和 排序的 字段
        String sortField = searchMap.get("sortField");
        String sortRule = searchMap.get("sortRule");
        if(!StringUtils.isEmpty(sortField)){
            builder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }

        return builder;
    }

    /**
     * @description: 优化成一次查询; 执行查询; 得到查询结果
     * @author: QIXIANG LING
     * @date: 2020/7/21 16:13
     * @param: builder
     * @param: searchMap
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String,Object> getSearch(NativeSearchQueryBuilder builder , Map<String,String> searchMap){
        // 里面封装了 关键字; 分组分类; 品牌; 规格数据 的结果
        Map<String, Object> map = new HashMap<>();

        // 高亮显示 builder 需要进行高亮的 字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
//        field.preTags("< font color = 'red' >");
//        field.postTags("</font>");
        field.preTags("<span style = 'color:red'>");
        field.postTags("</span>");
        // 高亮显示条件
        builder.withHighlightFields(field);

        // 获取   searchResultMapper 重写 mapResult方法
        SearchResultMapper searchResultMapper = new SearchResultMapper() {
            /**
             * @description:
             * @author: QIXIANG LING
             * @date: 2020/7/21 10:52
             * @param: response  查询结果
             * @param: clazz
             * @param: pageable  分页对象
             * @return: org.springframework.data.elasticsearch.core.aggregation.AggregatedPage<T>
             */
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                // 获取高亮结果集
                SearchHits hits = response.getHits();
                // 封装高亮结果集
                List<T> content = new ArrayList<>();
                // 结果不为空
                if (hits != null) {
                    for (SearchHit hit : hits) {
                        // 获取结果
                        String result = hit.getSourceAsString();
                        // 转化成pojo
                        SkuInfo skuInfo = JSON.parseObject(result, SkuInfo.class);
                        HighlightField highlightField = hit.getHighlightFields().get("name");
                        if (highlightField != null) {
                            // 获取高亮结果集
                            Text[] fragments = highlightField.getFragments();
                            // 替换普通结果集
                            skuInfo.setName(fragments[0].toString());
                        }
                        content.add((T) skuInfo);
                    }
                }
                // 这里返回的结果是   new 的一个对象 我们新封装的
                Aggregations aggregations = response.getAggregations();
                return new AggregatedPageImpl<>(content,pageable,hits.getTotalHits(),aggregations);
            }
        };

        // 执行查询
        NativeSearchQuery build = builder.build();
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(build, SkuInfo.class, searchResultMapper);
        // 结果集
        List<SkuInfo> content = page.getContent();
        // 总条数
        long totalElements = page.getTotalElements();
        // 总页数
        int totalPages = page.getTotalPages();
        Pageable pageable = page.getPageable();
        // 每页大小和当前页码
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        map.put("content",content);
        map.put("totalElements",totalElements);
        map.put("totalPages",totalPages);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNumber);


        // 拿到所有的聚合数据; 为什么这里聚合查询的结果是空的?
        Aggregations aggs = page.getAggregations();

        // 类别聚合的结果
        String category = searchMap.get("category");
        if(StringUtils.isEmpty(category)){
            List<String> categoryList = getList(aggs, "skuCategory");
            map.put("categoryList",categoryList);
        }
        // 品牌聚合查询的结果  先判断是否为空; 为空的话 才需要获取聚合查询的结果; 不为空就 已经根据品牌查询了
        String brand = searchMap.get("brand");
        if(StringUtils.isEmpty(brand)){
            List<String> brandList = getList(aggs, "brandNameList");
            map.put("brandList",brandList);
        }
        // 规格聚合的结果
        List<String> skuSpec = getList(aggs, "specMap");
        Map<String, Set<String>> specMap = specPutAll(skuSpec);
        map.put("specList",specMap);
        return map;
    }

    /**
     * @description: 获取 分类, 品牌聚合查询的结果
     * @author: QIXIANG LING
     * @date: 2020/7/21 14:57
     * @param: aggs
     * @param: brand
     * @return: java.util.List<java.lang.String>
     */
    private List<String> getList(Aggregations aggregations, String name) {
        List<String> list = new ArrayList<>();
        // 取出结果 name 为聚合的字段
        StringTerms aggregation = aggregations.get(name);

        // 取出结果放到List中
        for (StringTerms.Bucket bucket : aggregation.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            list.add(keyAsString);
        }
        return list;
    }

    // List<String>   ={"网络":"全网通","颜色":"素皮色","芯片":"高通865","操作系统":"Android","机身内存":"264G","像素":"4800万"}
    //          map----->>> {"网络":"全网通","颜色":"素皮色","芯片":"高通865","操作系统":"Android","机身内存":"128G","像素":"5200万"}
    /**
     * @description: 转化处理 规格数据 返回规格数据查询的结果
     * @author: QIXIANG LING
     * @date: 2020/7/18 20:55
     * @param: specList
     * @return: java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
     */
    public Map<String,Set<String>> specPutAll(List<String> specList){
        Map<String,Set<String>> resultMap = new HashMap<>();
        for (String spec : specList) {
            // 转化成map
            Map<String,String> map = JSON.parseObject(spec, Map.class);

            // 获取 数据 存到 resultMap
            for (Map.Entry<String, String> entry : map.entrySet()) {
                // 规格
                String key = entry.getKey();
                // 规格数据
                String value = entry.getValue();
                // 获取规格数据
                Set<String> set = resultMap.get(key);
                // 判断是否已经有了该规格
                if (set == null) {
                    set = new HashSet<>();
                }
                set.add(value);
                // 存入resultMap
                resultMap.put(key, set);
            }
        }
        return resultMap;
    }

    /**
     * @description: 执行搜索 分页查询高亮显示
     * @author: QIXIANG LING
     * @date: 2020/7/16 23:19
     * @param: builder
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> searchForPage(NativeSearchQueryBuilder builder) {
       /* // 使用es的操作对象 esTemplate
        NativeSearchQuery build = builder.build();
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(build, SkuInfo.class);
        // 结果集
        List<SkuInfo> content = page.getContent();
        // 总条数 ; 总页数
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content",content);
        map.put("totalPages",totalPages);
        map.put("totalElements",totalElements);*/

        // 高亮显示 builder 需要进行高亮的 字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("< font color = 'red' >");
        field.postTags("</font>");
        // 高亮显示条件
        builder.withHighlightFields(field);

        // 获取   searchResultMapper 重写 mapResult方法
        SearchResultMapper searchResultMapper = new SearchResultMapper() {
            /**
             * @description:
             * @author: QIXIANG LING
             * @date: 2020/7/21 10:52
             * @param: response  查询结果
             * @param: clazz
             * @param: pageable  分页对象
             * @return: org.springframework.data.elasticsearch.core.aggregation.AggregatedPage<T>
             */
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                // 获取高亮结果集
                SearchHits hits = response.getHits();
                // 封装高亮结果集
                List<T> content = new ArrayList<>();
                // 结果不为空
                if (hits != null) {
                    for (SearchHit hit : hits) {

                        // 获取结果
                        String result = hit.getSourceAsString();
                        // 转化成pojo
                        SkuInfo skuInfo = JSON.parseObject(result, SkuInfo.class);
                        HighlightField highlightField = hit.getHighlightFields().get("name");
                        if (highlightField != null) {
                            // 获取高亮结果集
                            Text[] fragments = highlightField.getFragments();
                            // 替换普通结果集
                            skuInfo.setName(fragments[0].toString());
                        }
                        content.add((T) skuInfo);
                    }
                }
                return new AggregatedPageImpl<>(content,pageable,hits.getTotalHits());
            }
        };

        // 执行查询
        NativeSearchQuery build = builder.build();
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(build, SkuInfo.class, searchResultMapper);
        // 结果集
        List<SkuInfo> content = page.getContent();
        // 总条数
        long totalElements = page.getTotalElements();
        // 总页数
        int totalPages = page.getTotalPages();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content",content);
        map.put("totalPages",totalPages);
        map.put("totalElements",totalElements);
        return map;
    }

    /**
     * @description: 分类分组查询; 聚合查询
     * @author: QIXIANG LING
     * @date: 2020/7/17 1:19
     * @param: builder
     * @return: java.util.List<java.lang.String>
     */
    private List<String> searchCategoryList(NativeSearchQueryBuilder builder){
        // builder 封装查询条件; 增加一个聚合查询的条件group by
        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        // 掉es对象执行分页查询
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

        // 获取结果集
        Aggregations aggregations = page.getAggregations();

        // 处理结果集
        // 存放到列表中
        List<String> stringList = new ArrayList<>();
        // 返回指定名称 关联的聚合
        StringTerms stringTerms = aggregations.get("skuCategory");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            stringList.add(bucket.getKeyAsString());
        }
        return  stringList;

    }

    /**
     * @description: 品牌列表查询
     * @author: QIXIANG LING
     * @date: 2020/7/18 10:09
     * @param: builder
     * @return: java.util.List<java.lang.String>
     */
    public List<String> searchBrandNameList(NativeSearchQueryBuilder builder){
        // 添加聚合查询
        builder.addAggregation(AggregationBuilders.terms("brandNameList").field("brandName"));
        // 执行查询
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        // 获取查询结果
        Aggregations aggregations = page.getAggregations();
        // 获取指定字段的聚合结果
        StringTerms stringTerms = aggregations.get("brandNameList");
        // 处理结果
        List<String> brandList = new ArrayList<>();
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            brandList.add(bucket.getKeyAsString());
        }
        return brandList;
    }

    /**
     * @description: 规格数据查询
     * @author: QIXIANG LING
     * @date: 2020/7/18 20:01
     * @param: builder
     * @return: java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
     */
    public Map<String, Set<String>> searchSpecMap(NativeSearchQueryBuilder builder){
        // 添加聚合查询
        builder.addAggregation(AggregationBuilders.terms("specMap").field("spec.keyword"));

        // 执行查询
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

        // 获取聚合查询结果
        Aggregations aggregations = page.getAggregations();

        // 获取 指定字段的聚合结果;
        StringTerms stringTerms = aggregations.get("specMap");

        // 处理结果   bucket 是个啥啊? 里面放了什么 ???  设想 --- slq 语句查询结果  mapjosn ?
        List<String> specList = new ArrayList<>();
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            specList.add(bucket.getKeyAsString());
        }
        // 处理数据转化成Map
        Map<String, Set<String>> specMap = specPutAll(specList);
        return specMap;
    }


}
