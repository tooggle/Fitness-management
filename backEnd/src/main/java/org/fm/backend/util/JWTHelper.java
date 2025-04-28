package org.fm.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.fm.backend.dto.TokenValidationResult;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTHelper {

    private final String secretKey = "SE";
    private final String issuer = "FitFitProjectTeam";
    private final String audience = "FitnessEnthusiasts";

    // 生成Token
    public String generateToken(int userId, String role) {
        // 创建签名算法
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        // 创建JWT并设置Claims
        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withClaim("userId", userId) // 用户ID
                .withClaim("role", role) // 角色
                .withClaim("jti", UUID.randomUUID().toString()) // JWT ID
                .withIssuedAt(new Date()) // 签发时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 过期时间：1小时
                .sign(algorithm); // 签名
    }

    // 验证Token
    public TokenValidationResult validateToken(String token) {
        try {
            // 创建签名算法
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // 创建JWT验证器
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build(); // 生成验证器

            // 验证Token
            DecodedJWT decodedJWT = verifier.verify(token);

            // 获取Claims
            String role = decodedJWT.getClaim("role").asString();
            int userId = decodedJWT.getClaim("userId").asInt();

            // 返回验证结果
            return new TokenValidationResult(userId, role, true);
        } catch (Exception e) {
            // 捕获异常并返回验证失败结果
            System.out.println("Token validation failed: " + e.getMessage());
            return new TokenValidationResult(0, null, false);
        }
    }
}

