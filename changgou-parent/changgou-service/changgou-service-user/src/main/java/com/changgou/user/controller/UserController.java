package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.changgou.util.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:itheima
 * @Description:
 *****/
@Api(value = "UserController")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @description: 增加用户积分
     * @author: QIXIANG LING
     * @date: 2020/8/1 18:29
     * @param: point
     * @return: com.changgou.util.Result
     */
    @PostMapping("/userinfo/{point}")
    public Result addUserPoint(@PathVariable(value = "point") Integer point){
        String username = TokenDecode.getUserInfo().get("username");
        int points = userService.addUserPoints(username, point);
        return new Result<>(true, StatusCode.OK,"增加积分成功", points);
    }




    /**
     * @description: 登录 并设置 token 到 cookie
     * @author: QIXIANG LING
     * @date: 2020/8/1 16:43
     * @param: username
     * @param: password
     * @param: response
     * @return: com.changgou.util.Result
     */
    @GetMapping("/login")
    public Result login(String username, String password, HttpServletResponse response){
        // 根据用户名查询密码; 用户的主键就是 username字段 测试数据 szitheima
        User user = userService.findById(username);
        boolean checkpw = BCrypt.checkpw(password, user.getPassword());
        if (checkpw) {
            // 设置令牌信息
            Map<String,Object> map = new HashMap<>();
            map.put("role","USER");
            map.put("success","SUCCESS");
            map.put("username","USERNAME");
            String  token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(map), null);

            // 往响应头和 cookie 里面 设置令牌
            response.setHeader("Authorization",token);
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Result<>(checkpw, StatusCode.OK,"登录成功");
        }else{
            return new Result<>(checkpw, StatusCode.ERROR,"错误");
        }
    }



    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "User条件分页查询",notes = "分页条件查询User方法详情",tags = {"UserController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) @ApiParam(name = "User对象",value = "传入JSON数据",required = false) User user, @PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页条件查询User
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "User分页查询",notes = "分页查询User方法详情",tags = {"UserController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页查询User
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @ApiOperation(value = "User条件查询",notes = "条件查询User方法详情",tags = {"UserController"})
    @PostMapping(value = "/search" )
    public Result<List<User>> findList(@RequestBody(required = false) @ApiParam(name = "User对象",value = "传入JSON数据",required = false) User user){
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
//    @PreAuthorize("hasAnyAuthority('admin')")
    @ApiOperation(value = "User根据ID删除",notes = "根据ID删除User方法详情",tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @ApiOperation(value = "User根据ID修改",notes = "根据ID修改User方法详情",tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "User对象",value = "传入JSON数据",required = false) User user,@PathVariable String id){
        //设置主键值
        user.setUsername(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增User数据
     * @param user
     * @return
     */
    @ApiOperation(value = "User添加",notes = "添加User方法详情",tags = {"UserController"})
    @PostMapping
    public Result add(@RequestBody  @ApiParam(name = "User对象",value = "传入JSON数据",required = true) User user){
        //调用UserService实现添加User
        userService.add(user);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @ApiOperation(value = "User根据ID查询",notes = "根据ID查询User方法详情",tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable String id){
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true,StatusCode.OK,"查询成功",user);
    }

    /***
     * 查询User全部数据
     * @return
     */
    @ApiOperation(value = "查询所有User",notes = "查询所User有方法详情",tags = {"UserController"})
    @GetMapping
    public Result<List<User>> findAll(){
        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
