package com.changgou.user.controller;

import com.changgou.user.pojo.Comments;
import com.changgou.user.service.CommentsService;
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
@Api(value = "CommentsController")
@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    /***
     * Comments分页条件搜索实现
     * @param comments
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "Comments条件分页查询",notes = "分页条件查询Comments方法详情",tags = {"CommentsController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) @ApiParam(name = "Comments对象",value = "传入JSON数据",required = false) Comments comments, @PathVariable  int page, @PathVariable  int size){
        //调用CommentsService实现分页条件查询Comments
        PageInfo<Comments> pageInfo = commentsService.findPage(comments, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Comments分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "Comments分页查询",notes = "分页查询Comments方法详情",tags = {"CommentsController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用CommentsService实现分页查询Comments
        PageInfo<Comments> pageInfo = commentsService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param comments
     * @return
     */
    @ApiOperation(value = "Comments条件查询",notes = "条件查询Comments方法详情",tags = {"CommentsController"})
    @PostMapping(value = "/search" )
    public Result<List<Comments>> findList(@RequestBody(required = false) @ApiParam(name = "Comments对象",value = "传入JSON数据",required = false) Comments comments){
        //调用CommentsService实现条件查询Comments
        List<Comments> list = commentsService.findList(comments);
        return new Result<List<Comments>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Comments根据ID删除",notes = "根据ID删除Comments方法详情",tags = {"CommentsController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用CommentsService实现根据主键删除
        commentsService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Comments数据
     * @param comments
     * @param id
     * @return
     */
    @ApiOperation(value = "Comments根据ID修改",notes = "根据ID修改Comments方法详情",tags = {"CommentsController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "Comments对象",value = "传入JSON数据",required = false) Comments comments,@PathVariable String id){
        //设置主键值
        comments.setId(id);
        //调用CommentsService实现修改Comments
        commentsService.update(comments);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Comments数据
     * @param comments
     * @return
     */
    @ApiOperation(value = "Comments添加",notes = "添加Comments方法详情",tags = {"CommentsController"})
    @PostMapping
    public Result add(@RequestBody  @ApiParam(name = "Comments对象",value = "传入JSON数据",required = true) Comments comments){
        //调用CommentsService实现添加Comments
        commentsService.add(comments);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Comments数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Comments根据ID查询",notes = "根据ID查询Comments方法详情",tags = {"CommentsController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result<Comments> findById(@PathVariable String id){
        //调用CommentsService实现根据主键查询Comments
        Comments comments = commentsService.findById(id);
        return new Result<Comments>(true,StatusCode.OK,"查询成功",comments);
    }

    /***
     * 查询Comments全部数据
     * @return
     */
    @ApiOperation(value = "查询所有Comments",notes = "查询所Comments有方法详情",tags = {"CommentsController"})
    @GetMapping
    public Result<List<Comments>> findAll(){
        //调用CommentsService实现查询所有Comments
        List<Comments> list = commentsService.findAll();
        return new Result<List<Comments>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
