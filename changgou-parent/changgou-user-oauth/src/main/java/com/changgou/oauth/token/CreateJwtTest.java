package com.changgou.oauth.token;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/27 10:11
 */
public class CreateJwtTest {


    /**
     * @description:  获取秘钥对; 根据秘钥和自定义载荷 通过JwtHelper 生成jwt
     * @author: QIXIANG LING
     * @date: 2020/7/27 10:41
     * @param:
     * @return: void
     */
    @Test
    public void testDemo(){
        // 证书文件路径
        String key_location = "changgou.jks";
        // 秘钥库 密码
        String key_password = "changgou";
        // 秘钥密码
        String keypwd = "changgou";
        // 秘钥别名
        String alias = "changgou";

        // 访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        // 创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());

        // 读取秘钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());

        // 获取公钥
        PublicKey publickey = keyPair.getPublic();
//        System.out.println("publicKey: " + publickey);

        // 获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        // 定义载荷
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id",666);
        tokenMap.put("name","itheima");
        tokenMap.put("words","千古兴亡何限错，百年生死本来齐");

        // 生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));

        // 取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }
// eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiaXRoZWltYSIsIndvcmRzIjoi5Y2D5Y-k5YW05Lqh5L2V6ZmQ6ZSZ77yM55m-5bm055Sf5q275pys5p2l6b2QIiwiaWQiOjY2Nn0.SYhXtowPzLmTqt2AtD8oT-20prGxcy0dbJA1HMEV8zYHB2oog-Clq6FVYrUch2bKG_P6v8FgPkict1NwytEiThS2ubWQwA9KSnfuZ5CglD77__PVfd0vhU6cW1jyMRpczS9aHXVUn-fNeZ38dYKfSUzSjSaoTsoKtEuHFhQ2LtkUXFNwobGBPaeJXwHz1DcYIl0o0xqXVGoHzpkoV24NqOoFNtAhIfFWUiqULlvqZp1XGl_ND9a2igeR49EELHF56KW1qOM4xaPuF4Hls7Cm7OOonuEW6RKL9uJK5x2LqaEqKsdhYUcRDUuT3xqApeEFmFM_i2iPsdHQG5x2dq1Nzw

    /**
     * @description: 解析Jwt
     * @author: QIXIANG LING
     * @date: 2020/7/27 10:43
     * @param:
     * @return: void
     */
    @Test
    public void parseJwt(){
        // 令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiaXRoZWltYSIsIndvcmRzIjoi5Y2D5Y-k5YW05Lqh5L2V6ZmQ6ZSZ77yM55m-5bm055Sf5q275pys5p2l6b2QIiwiaWQiOjY2Nn0.SYhXtowPzLmTqt2AtD8oT-20prGxcy0dbJA1HMEV8zYHB2oog-Clq6FVYrUch2bKG_P6v8FgPkict1NwytEiThS2ubWQwA9KSnfuZ5CglD77__PVfd0vhU6cW1jyMRpczS9aHXVUn-fNeZ38dYKfSUzSjSaoTsoKtEuHFhQ2LtkUXFNwobGBPaeJXwHz1DcYIl0o0xqXVGoHzpkoV24NqOoFNtAhIfFWUiqULlvqZp1XGl_ND9a2igeR49EELHF56KW1qOM4xaPuF4Hls7Cm7OOonuEW6RKL9uJK5x2LqaEqKsdhYUcRDUuT3xqApeEFmFM_i2iPsdHQG5x2dq1Nzw";

        // 公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqGUti1Ni1mMPpyD5b8YOt16IIIwZYw/c/pdfZcbkLdgNkAylL9rDdQZm/CKN9os4SWV/hi32exro9iZ+Vi7OrCcldqTZTzWAipgdQ2tVuKYF6XJAUkCvw/dysrMnkT1trcqEQLP2yWkHCPKVrOrDJFlV+4kKGkC20UqBS71FhgxBh348A1HiIj3CaDGHem1/NZPpx0vbu7uF0mC7H063a8qAAi2sNN4tvu07nxE5CB1GztUvejZxkeYCGNm+9wpop/jkm5+K7v0KVAXsGPjlqXv+VBcPcynmCAiI7tShp8bppzFmZlmd7lqSOCGJhSAtujH8EhiHH13j23aIY5zOOwIDAQAB-----END PUBLIC KEY-----";

        // 校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        // 获取Jwt 原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);

        // jwt 令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }


}
