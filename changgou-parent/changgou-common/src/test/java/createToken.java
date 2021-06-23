import io.jsonwebtoken.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/24 18:49
 */
public class createToken {

    @Test
    public void testDemo(){
        // 创建JWT
        JwtBuilder builder = Jwts.builder();
        // 头信息 类型; 签名加密算法
        Map<String,Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("keyId","JWT");
        builder.setHeader(map);
        // 载荷
        builder.setId("001");
        builder.setIssuer("有缘千里来相会，无缘对面不相逢");
        builder.setIssuedAt(new Date());
        // 设置过期时间
//        builder.setExpiration(new Date(System.currentTimeMillis()+ 1500));

        // 自定义载荷信息 setClaims
        Map<String, Object> payloadmap = new HashMap<>();
        payloadmap.put("name","夏海里伽子");
        payloadmap.put("words","东风夜放花千树，更吹落，星如雨。");
        builder.setClaims(payloadmap);

        // 添加签名
        builder.signWith(SignatureAlgorithm.HS256,"itheima");

        // 生成token
        String token = builder.compact();
        System.out.println("token:  " + token);
    }


    @Test
    public void parseToken(){
        String token = "eyJrZXlJZCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJuYW1lIjoi5aSP5rW36YeM5Ly95a2QIiwid29yZHMiOiLkuJzpo47lpJzmlL7oirHljYPmoJHvvIzmm7TlkLnokL3vvIzmmJ_lpoLpm6jjgIIifQ.mB65p0Qt8pHKGb2DQbMimCntXc5mvX63vFLmvDaMLuc";
        JwtParser parser = Jwts.parser();
        parser.setSigningKey("itheima");
        // 载荷里面的内容
        Claims claims = parser.parseClaimsJws(token).getBody();
        System.out.println(claims);

    }



    @Test
    public void parseTokenInDb(){
        String token = "$2a$10$mqei0b3NW6uRRGSMI19D4OwmKJET4VJm5q7Nn4.EU9gnwUC/i2SR.";
        JwtParser parser = Jwts.parser();
        parser.setSigningKey("itheima");
        // 载荷里面的内容
        Claims claims = parser.parseClaimsJws(token).getBody();
        System.out.println(claims);

    }

    // 校验 token 包含用户信息
    @Test
    public void verify(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MjAyODAxNzU2NywiYXV0aG9yaXRpZXMiOlsic2Vja2lsbF9saXN0IiwiYWRtaW4iLCJnb29kc19saXN0Il0sImp0aSI6Ijg3Nzc2MDdhLWZjMjQtNGY4ZS05NWM4LTVhMDlmOWM2OTg4NyIsImNsaWVudF9pZCI6ImNoYW5nZ291IiwidXNlcm5hbWUiOiJjaGFuZ2dvdSJ9.NK5_W_YyEXlEquuFNK4j7vJHh-pNnzW5BCsx1iui-pC4G7FtY3GcQ930pAa_fSQVcxiuPkwTkF9pPcHwmOIVEht-lewnE50S8SL1-w-FoWdcfQgZKXFLkXr72vrDoty5x8oMYlL6CRrLjjxuga8730FhwexiWkkCGK7_IR7VMO5hNlPX_qXy5vDjPG_fABobnTRPKjl9ucxI1QhOBZRZZorknWgcJ_UflpE4B91Wlws87ZQq2N3KVzGZOJffsJWuNRoqXv_wplknQkqC9OEDbXsRfAYmToCwKFISSJiJA5_S5CU2xgP7vDuBaBHhV-e21AMbAFcdxof4Jv74BsOcgA";

        // 公钥
        String pk = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqGUti1Ni1mMPpyD5b8YOt16IIIwZYw/c/pdfZcbkLdgNkAylL9rDdQZm/CKN9os4SWV/hi32exro9iZ+Vi7OrCcldqTZTzWAipgdQ2tVuKYF6XJAUkCvw/dysrMnkT1trcqEQLP2yWkHCPKVrOrDJFlV+4kKGkC20UqBS71FhgxBh348A1HiIj3CaDGHem1/NZPpx0vbu7uF0mC7H063a8qAAi2sNN4tvu07nxE5CB1GztUvejZxkeYCGNm+9wpop/jkm5+K7v0KVAXsGPjlqXv+VBcPcynmCAiI7tShp8bppzFmZlmd7lqSOCGJhSAtujH8EhiHH13j23aIY5zOOwIDAQAB-----END PUBLIC KEY-----";

        // 校验 jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(pk));

        // 获取jwt初始内容
        String claims = jwt.getClaims();
        System.out.println(claims);

    }




}
