package com.changgou.oauth.config;
import com.changgou.oauth.util.UserJwt;
import com.changgou.user.feign.UserFeign;
import com.changgou.util.Result;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/*****
 * 自定义授权认证类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private UserFeign userFeign;

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证,统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
//                return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式 (不用再加密了 , 因为数据库里面的就是 加密的数据)
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        //根据用户名查询用户信息;  请求发送到 认证 微服务oauth ; 这里 又把请求发送到了 用户 微服务请求用户信息
        Result<com.changgou.user.pojo.User> result = userFeign.findById(username);
        com.changgou.user.pojo.User user = result.getData();
        String pwd = user.getPassword();


        //设置权限
        String permissions = "goods_list,seckill_list,admin";

        /**
         * @description: 根据用户信息生成
         * @author: QIXIANG LING
         * @date: 2020/7/28 10:03
         * @param: username   用户名
         * @param: pwd   查询得到的用户密码
         * @param: List<GrantedAuthority>  用户所拥有的角色, 权限列表
         * @return: org.springframework.security.core.userdetails.UserDetails  返回List< GrantedAuthority>
         */
        UserJwt userDetails = new UserJwt(username,pwd,AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        return userDetails;
    }
}
