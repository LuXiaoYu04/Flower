package com.flowers.shopping.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 使用 JJWT 工具生成一个符合 HS256 要求的安全密钥
    private static final Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Long expire = 43200000L; // 12 小时

    /**
     * 生成JWT令牌
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(signKey)  // 使用安全密钥
                .compact();
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
