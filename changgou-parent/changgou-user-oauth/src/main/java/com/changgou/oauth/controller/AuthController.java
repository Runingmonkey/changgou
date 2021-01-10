package com.changgou.oauth.controller;

import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 16:42
 * @Description: com.changgou.oauth.controller
 ****/
@RestController
@RequestMapping(value = "/user")
public class AuthController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public Result login(String username, String password) {
        if(StringUtils.isEmpty(username)){
            throw new RuntimeException("用户名不允许为空");
        }
        if(StringUtils.isEmpty(password)){
            throw new RuntimeException("密码不允许为空");
        }
        //申请令牌
        AuthToken authToken =  authService.login(username,password,clientId,clientSecret);

        //用户身份令牌
        String access_token = authToken.getAccessToken();
        //将令牌存储到cookie
        saveCookie(access_token);

        return new Result(true, StatusCode.OK,"登录成功！");
    }

    /**
     * @description: 通过login 页面登录
     * @author: QIXIANG LING
     * @date: 2020/7/30 15:15
     * @param: username
     * @param: password
     * @param: response
     * @return: com.changgou.util.Result
     */
    @GetMapping("/login")
    public Result loginByPage(String username, String password ,HttpServletResponse response){
        try {
            AuthToken authToken = authService.login(username, password, clientId, clientSecret);
            return new Result(true, StatusCode.OK,"登录成功",authToken);
        } catch (Exception e) {
            return new Result<>(false, StatusCode.ERROR,"登陆失败");

        }
    }

    /***
     * 将令牌存储到cookie
     * @param token
     */
    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // cookie 在 addCookie的 时候不能有空格
//        CookieUtil.addCookie(response,cookieDomain,"/","Authorization","bearer " + token,cookieMaxAge,false);
        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",token,cookieMaxAge,false);
    }
}
