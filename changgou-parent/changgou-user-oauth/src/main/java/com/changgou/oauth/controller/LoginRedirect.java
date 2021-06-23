package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 登录跳转控制器
 * @author: QIXIANG LING
 * @date: 2020/7/30 13:40
 */
@Controller
@RequestMapping("/oauth")
public class LoginRedirect {

    @GetMapping("/login")
    public String LoginRedirect(@RequestParam(value = "FROM",required = false) String from, Model model){
        model.addAttribute("from",from);
        return "login";
    }
}
