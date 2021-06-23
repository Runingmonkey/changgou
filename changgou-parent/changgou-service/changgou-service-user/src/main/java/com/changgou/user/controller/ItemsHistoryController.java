package com.changgou.user.controller;

import com.changgou.user.pojo.ItemsHistory;
import com.changgou.user.service.ItemsHistoryService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:itheima
 * @Description:
 *****/
@Api(value = "ItemsHistoryController")
@RestController
@RequestMapping("/itemsHistory")
@CrossOrigin
public class ItemsHistoryController {

    @Autowired
    private ItemsHistoryService itemsHistoryService;

    /***
     * ItemsHistory分页条件搜索实现
     * @param itemsHistory
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "ItemsHistory条件分页查询",notes = "分页条件查询ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) @ApiParam(name = "ItemsHistory对象",value = "传入JSON数据",required = false) ItemsHistory itemsHistory, @PathVariable  int page, @PathVariable  int size){
        //调用ItemsHistoryService实现分页条件查询ItemsHistory
        PageInfo<ItemsHistory> pageInfo = itemsHistoryService.findPage(itemsHistory, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * ItemsHistory分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "ItemsHistory分页查询",notes = "分页查询ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用ItemsHistoryService实现分页查询ItemsHistory
        PageInfo<ItemsHistory> pageInfo = itemsHistoryService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param itemsHistory
     * @return
     */
    @ApiOperation(value = "ItemsHistory条件查询",notes = "条件查询ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @PostMapping(value = "/search" )
    public Result<List<ItemsHistory>> findList(@RequestBody(required = false) @ApiParam(name = "ItemsHistory对象",value = "传入JSON数据",required = false) ItemsHistory itemsHistory){
        //调用ItemsHistoryService实现条件查询ItemsHistory
        List<ItemsHistory> list = itemsHistoryService.findList(itemsHistory);
        return new Result<List<ItemsHistory>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "ItemsHistory根据ID删除",notes = "根据ID删除ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用ItemsHistoryService实现根据主键删除
        itemsHistoryService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改ItemsHistory数据
     * @param itemsHistory
     * @param id
     * @return
     */
    @ApiOperation(value = "ItemsHistory根据ID修改",notes = "根据ID修改ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "ItemsHistory对象",value = "传入JSON数据",required = false) ItemsHistory itemsHistory,@PathVariable String id){
        //设置主键值
        itemsHistory.setId(id);
        //调用ItemsHistoryService实现修改ItemsHistory
        itemsHistoryService.update(itemsHistory);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增ItemsHistory数据
     * @param itemsHistory
     * @return
     */
    @ApiOperation(value = "ItemsHistory添加",notes = "添加ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @PostMapping
    public Result add(@RequestBody  @ApiParam(name = "ItemsHistory对象",value = "传入JSON数据",required = true) ItemsHistory itemsHistory){
        //调用ItemsHistoryService实现添加ItemsHistory
        itemsHistoryService.add(itemsHistory);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询ItemsHistory数据
     * @param id
     * @return
     */
    @ApiOperation(value = "ItemsHistory根据ID查询",notes = "根据ID查询ItemsHistory方法详情",tags = {"ItemsHistoryController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result<ItemsHistory> findById(@PathVariable String id){
        //调用ItemsHistoryService实现根据主键查询ItemsHistory
        ItemsHistory itemsHistory = itemsHistoryService.findById(id);
        return new Result<ItemsHistory>(true,StatusCode.OK,"查询成功",itemsHistory);
    }

    /***
     * 查询ItemsHistory全部数据
     * @return
     */
    @ApiOperation(value = "查询所有ItemsHistory",notes = "查询所ItemsHistory有方法详情",tags = {"ItemsHistoryController"})
    @GetMapping
    public Result<List<ItemsHistory>> findAll(){
        //调用ItemsHistoryService实现查询所有ItemsHistory
        List<ItemsHistory> list = itemsHistoryService.findAll();
        return new Result<List<ItemsHistory>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
