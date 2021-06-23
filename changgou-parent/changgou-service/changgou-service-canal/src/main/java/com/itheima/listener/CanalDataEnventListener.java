/*
package com.itheima.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;
import com.xpand.starter.canal.annotation.UpdateListenPoint;

import java.util.List;

*/
/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/12 17:54
 *//*

@CanalEventListener  // canal 事件监听器 监听的表是? 所有的表
public class CanalDataEnventListener {

    @InsertListenPoint
    public void onEnventInsert(CanalEntry.Entry entryType, CanalEntry.RowData rowData){
        // 获取变更之后的行数据
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            // 获取行的名称 以及对应的值
            String name = column.getName();
            // 获取列的名称 以及对应的值
            String value = column.getValue();
            System.out.println("columName:" + name +";"+ "columnValue: " +value);
        }
    }

    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.Entry eventntry, CanalEntry.RowData rowData){
        // 更新前的数据
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println("更新前" + column.getName() +":"+column.getValue());
        }
        // 更新后的数据
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println("更新后" + column.getName() +":"+column.getValue());
        }
    }

}
*/
