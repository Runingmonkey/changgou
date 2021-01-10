package com.changgou.seckill.controller;

import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
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
@Api(value = "SeckillOrderController")
@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * @description: 用户下单 , 把下单数据存到redis中 ; 时间段是为了 从redis 中取数据
     * @author: QIXIANG LING
     * @date: 2020/8/4 17:56
     * @param: time
     * @param: seckillId
     * @param: userId
     * @return: com.changgou.util.Result
     */
    @RequestMapping("/add")
    public Result addOrder(String time ,String seckillId){
        String userId = "changgou";
        Boolean orderBool = seckillOrderService.add(time, seckillId, userId);
        return new Result<>(true, StatusCode.OK,"正在排队中",orderBool);
    }


    /***
     * SeckillOrder分页条件搜索实现
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "SeckillOrder条件分页查询",notes = "分页条件查询SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) @ApiParam(name = "SeckillOrder对象",value = "传入JSON数据",required = false) SeckillOrder seckillOrder, @PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页条件查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * SeckillOrder分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "SeckillOrder分页查询",notes = "分页查询SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @ApiOperation(value = "SeckillOrder条件查询",notes = "条件查询SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @PostMapping(value = "/search" )
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false) @ApiParam(name = "SeckillOrder对象",value = "传入JSON数据",required = false) SeckillOrder seckillOrder){
        //调用SeckillOrderService实现条件查询SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID删除",notes = "根据ID删除SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用SeckillOrderService实现根据主键删除
        seckillOrderService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID修改",notes = "根据ID修改SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "SeckillOrder对象",value = "传入JSON数据",required = false) SeckillOrder seckillOrder,@PathVariable String id){
        //设置主键值
        seckillOrder.setId(id);
        //调用SeckillOrderService实现修改SeckillOrder
        seckillOrderService.update(seckillOrder);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增SeckillOrder数据
     * @param seckillOrder
     * @return
     */
//    @ApiOperation(value = "SeckillOrder添加",notes = "添加SeckillOrder方法详情",tags = {"SeckillOrderController"})
//    @PostMapping
//    public Result add(@RequestBody  @ApiParam(name = "SeckillOrder对象",value = "传入JSON数据",required = true) SeckillOrder seckillOrder){
//        //调用SeckillOrderService实现添加SeckillOrder
//        seckillOrderService.add(seckillOrder);
//        return new Result(true,StatusCode.OK,"添加成功");
//    }

    /***
     * 根据ID查询SeckillOrder数据
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID查询",notes = "根据ID查询SeckillOrder方法详情",tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result<SeckillOrder> findById(@PathVariable String id){
        //调用SeckillOrderService实现根据主键查询SeckillOrder
        SeckillOrder seckillOrder = seckillOrderService.findById(id);
        return new Result<SeckillOrder>(true,StatusCode.OK,"查询成功",seckillOrder);
    }

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @ApiOperation(value = "查询所有SeckillOrder",notes = "查询所SeckillOrder有方法详情",tags = {"SeckillOrderController"})
    @GetMapping
    public Result<List<SeckillOrder>> findAll(){
        //调用SeckillOrderService实现查询所有SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
