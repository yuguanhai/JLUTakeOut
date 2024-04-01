package com.sky.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTRequest {

    public static String generateToken(String subject, String secretKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("userId",101000021);
        Date now = Date.from(Instant.now());
        Date expiryDate = Date.from(Instant.now().plusSeconds(60 * 60)); // 设置过期时间为1小时

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // 使用HS256算法和密钥
                .compact();
    }


    // 验证JWT
    public static boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true; // 解析成功则认为有效
        } catch (Exception e) {
            return false; // 任何异常都视为无效
        }
    }

    public static void main(String[] args) {
        // 假设的用户标识
        String subject = "user123";
        // 生成密钥

//        String secretKey = "abhfdaihfdasodfhsauhfdahifhausifhauifhsauhfu";
        String secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256).toString();


        // 生成JWT
        String jwtToken = generateToken(subject, secretKey);
        System.out.println("Generated JWT: " + jwtToken);

        // 验证JWT
        boolean isValid = validateToken(jwtToken, secretKey);
        System.out.println("Is JWT valid? " + isValid);
    }
}
