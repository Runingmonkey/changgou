package com.itheima.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalPacket;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.util.Result;
import com.netflix.discovery.converters.Auto;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/12 21:06
 */
@CanalEventListener
public class CanalSyncContent {
    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 自定监听器; 监听类型, 范围  changgou_content库 tb_content表的 insert 和 update变化
    @ListenPoint(
            destination = "example", // 实例
            schema = "changgou_content",
            table = {"tb_content"},
            eventType = {CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE}
    )
    public void onEventChangeContent(CanalEntry.RowData rowData, CanalEntry.EntryType entryType){
        // 获取分类id
        String categoryId = getColumnValue(rowData, "category_id");
        // 根据分类id查询广告列表
        Result<List<Content>> contentList = contentFeign.findByCategoryId(Long.parseLong(categoryId));

        // 存入redis
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(contentList.getData()));
    }



    // 获取对应列的值
     public String getColumnValue(CanalEntry.RowData rowData, String columnName){
         List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
         for (CanalEntry.Column column : afterColumnsList) {
             if(columnName.equals(column.getName())){
                 // 返回该列的值
                 return column.getValue();
             }
         }
         return null;
     }

}
