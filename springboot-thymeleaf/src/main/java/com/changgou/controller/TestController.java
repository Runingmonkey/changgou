package com.changgou.controller;

import com.changgou.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @description: 禁用Ctrl + ctrl
 * @author: QIXIANG LING
 * @date: 2020/7/21 19:36
 */
@Controller
public class TestController {

    @RequestMapping("/demo")
    public String demo(Model model){
        System.out.println("hello,thymeleaf");
        model.addAttribute("hello","hello,thymeleaf");
        return "demo";
    }

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("hello","hello welcome");
        // 集合数据
        List<User> users = new ArrayList<>();
        users.add(new User(1,"zhangsan","深圳"));
        users.add(new User(1,"lisi","深圳"));
        users.add(new User(1,"wangwu","上海"));
        model.addAttribute("users",users);

        // 设置Map
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("No","123");
        dataMap.put("address","深圳");
        model.addAttribute("dataMap",dataMap);

        //存储一个数组
        String[] names = {"张三","李四","王五"};
        model.addAttribute("names",names);

        // 日期
        model.addAttribute("date",new Date());

        return "demo1";
    }

    @RequestMapping("/add")
    public void methodName(String name, String address, Model model){
        System.out.println(name + "的住址是");

    }
}
