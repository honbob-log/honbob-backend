
package honbob.honbob.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret_key}")
    private String secretKeyString;

    private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24시간

    // JWT 토큰 생성
    public String generateToken(Long memberId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, memberId.toString());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        // 시크릿 키 생성 - 문자열을 바이트 배열로 변환하여 사용
        // HMAC-SHA 알고리즘은 적어도 256비트(32바이트) 이상의 키를 권장
        if (secretKeyString == null || secretKeyString.length() < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 characters long");
        }

        byte[] keyBytes;
        try {
            // Base64로 인코딩된 키라면 디코딩 시도
            keyBytes = Decoders.BASE64.decode(secretKeyString);
        } catch (Exception e) {
            keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret key must be at least 256 bits (32 bytes)");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰에서 memberId 추출
    public Long extractMemberId(String token) {
        return Long.parseLong(extractClaim(token, Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 유효성 검사
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }
}