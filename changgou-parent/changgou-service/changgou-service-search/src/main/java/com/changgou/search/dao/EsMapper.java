package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:  主键的类型为 String
 * @author: QIXIANG LING
 * @date: 2020/7/16 11:41
 */
@Repository
public interface EsMapper extends ElasticsearchRepository<SkuInfo,String> {
}
