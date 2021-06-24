package changgou.dao;

import tk.mybatis.mapper.additional.aggregation.AggregationMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 10:31
 */
public interface Mapper<T> extends tk.mybatis.mapper.common.Mapper<T>, MySqlMapper<T>, AggregationMapper<T> {

}

