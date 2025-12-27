package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 1. 비밀 키 (실무에선 application.properties에 숨겨야 합니다!)
    // 32글자 이상이어야 안전합니다.
    private static final String SECRET_KEY = "mindlog-secret-key-mindlog-secret-key-must-be-long";
    
    // 키 객체 생성
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 2. 토큰 생성 (로그인 성공 시 호출)
    public String createToken(String username) {
        long validTime = 1000 * 60 * 60; // 1시간 동안 유효

        return Jwts.builder()
                .setSubject(username) // 토큰 주인 (ID)
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + validTime)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 (도장 쾅!)
                .compact();
    }

    // 3. 토큰에서 아이디 꺼내기 (검증용)
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 4. 토큰 유효성 검사 (만료되었거나 조작되었는지 확인)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 위조되거나 만료된 토큰
        }
    }
}