package com.record.myplace.jwt;

import com.record.myplace.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스입니다.
 * 'jwt.secret' 및 'jwt.expiration' 설정을 application.properties/yml에서 읽어옵니다.
 */
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds; // 토큰 유효 시간 (ms)

    // @Value 어노테이션을 사용하여 application.properties의 값을 주입받습니다.
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") long validityInMilliseconds) {
        // base64로 인코딩된 시크릿 키를 Key 객체로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    /**
     * 사용자 정보를 기반으로 JWT Access Token을 생성합니다.
     * @param user 인증된 사용자 객체
     * @return 생성된 JWT 문자열
     */
    public String createToken(User user) {
        // 클레임 (Claims): 토큰에 담을 정보
        String userEmail = String.valueOf(user.getEmail());
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userEmail) // 토큰의 제목 (일반적으로 사용자 ID)
                .claim("username", user.getUsername()) // 추가 정보 (이메일)
                // user 엔티티에 닉네임 필드가 있다면 추가 가능: .claim("nickname", user.getNickname())
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 서명 알고리즘과 비밀 키
                .compact();
    }
}
