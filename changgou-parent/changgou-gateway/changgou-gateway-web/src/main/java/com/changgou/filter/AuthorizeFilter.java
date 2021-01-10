package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @description: 自定义过滤器
 * @author: QIXIANG LING
 * @date: 2020/7/26 10:01
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 定义常量
    private static final String AUTHORIZE_TOKEN = "Authorization";

    // 登录页面
    private static final String LOGIN_URL = "http://localhost:9001/oauth/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 判断访问的路径; 下面这些路径都   不需要权限就可以访问   /api/user   || URLFilter.hasAuthorize(url)
        // URLFilter.hasAuthorize(url) false 不需要访问权限 ; true 需要权限
        String url = request.getURI().getPath();
        if(url.startsWith("/api/user/login") || url.startsWith("/api/brand/search/")){
            // 放行
            return chain.filter(exchange);
        }

        // 如果访问的不是登录url ; 判断有没有token  从头部; cookie中 ; 请求参数 中获取

        //头文件
        String token = request.getHeaders().getFirst("Authorization");

        if(StringUtils.isEmpty(token)){
            // 从请求参数中获取
            token = request.getQueryParams().getFirst("Authorization");
        }
        // 从cookie 中获取
        if(StringUtils.isEmpty(token)){
            HttpCookie cookie = request.getCookies().getFirst("Authorization");
            // 不为空就从里面获取 token
            if (cookie == null) {
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return response.setComplete();
                // 用户未登录
                response.setStatusCode(HttpStatus.SEE_OTHER); // 重定向到另一个url
                String path = LOGIN_URL + "?FROM=" + request.getURI().toString();
                response.getHeaders().add("Location",path);
                return  response.setComplete();
            }
            token = cookie.getValue();
        }

        // 将token 存到 头文件中 ; 才能对其他微服务进行访问 ; 注意头文件中 要指定token 类型
        token = "bearer "+ token;
        request.mutate().header(AUTHORIZE_TOKEN,token);

        // 从请求参数; cookie , 头文件中都没有就不放行
        if(StringUtils.isEmpty(token)){
            // 用户未登录
            response.setStatusCode(HttpStatus.SEE_OTHER); // 重定向到另一个url
            String path = LOGIN_URL + "?FROM=" + request.getURI().toString();
            response.getHeaders().add("Location",path);
            return  response.setComplete();
        }
        // 解析 获取到的token
//        try {
//            // 解析成功
//            Claims claims = JwtUtil.parseJWT(token);
//            System.out.println(claims);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 解析失败
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }

        // 有token 网关不做校验; 直接放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
