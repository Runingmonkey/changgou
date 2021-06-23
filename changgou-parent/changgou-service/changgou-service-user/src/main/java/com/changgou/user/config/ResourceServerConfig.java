package com.changgou.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @description:  user 微服务权限认证
 * @author: QIXIANG LING
 * @date: 2020/7/27 13:04
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String PUBLIC_KEY = "pk.key";

    /**
     * @description: tokenStore
     * @author: QIXIANG LING
     * @date: 2020/7/27 13:13
     * @param: jwtAccessTokenConverter
     * @return: org.springframework.security.oauth2.provider.token.TokenStore
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * @description: JwtAccessTokenConverter 设置公钥
     * @author: QIXIANG LING
     * @date: 2020/7/27 15:45
     * @param:
     * @return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        return converter;

    }

    /**
     * @description: 读取配置文件 拿到公钥
     * @author: QIXIANG LING
     * @date: 2020/7/27 15:37
     * @param:
     * @return: java.lang.String
     */
    private String getPubKey() {
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return  br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @description: 所有http 请求必须经过权限校验
     * @author: QIXIANG LING
     * @date: 2020/7/27 15:21
     * @param: http
     * @return: void
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 所有的请求必须通过验证
        http.authorizeRequests()
                .antMatchers("/user/add") // 这里放行 增加请求
                .permitAll()
                .anyRequest()
                .authenticated();
    }



}
