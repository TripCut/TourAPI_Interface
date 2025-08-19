package com.tripcut.global.security.jwt;

import com.tripcut.global.security.jwt.dto.RefreshToken;
import com.tripcut.global.security.jwt.dto.TokenDto;
import com.tripcut.global.security.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
public class TokenProvider implements InitializingBean {

   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long accessTokenValidityInMilliseconds;
   private final long refreshTokenValidityInMilliseconds;
   private final RefreshTokenRepository refreshTokenRepository;
   private Key key;

   public TokenProvider(
           @Value("${jwt.secret}") String secret,
           @Value("${jwt.access-token-validity}") long accessTokenValidity,
           @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
           RefreshTokenRepository refreshTokenRepository) {
      this.secret = secret;
      this.accessTokenValidityInMilliseconds = accessTokenValidity * 1000;
      this.refreshTokenValidityInMilliseconds = refreshTokenValidity * 1000;
      this.refreshTokenRepository = refreshTokenRepository;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   /** AccessToken 생성 */
   public String createAccessToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = System.currentTimeMillis();
      Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

      return Jwts.builder()
              .setSubject(authentication.getName())
              .claim(AUTHORITIES_KEY, authorities)
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
   }

   /** RefreshToken 생성 (✅ 권한도 함께 보관) */
   public String createRefreshToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = System.currentTimeMillis();
      Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

      String refreshToken = Jwts.builder()
              .setSubject(authentication.getName())
              .claim(AUTHORITIES_KEY, authorities)
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();

      // Refresh Token 저장 (유저별 1개 유지라면 upsert 전략 권장)
      refreshTokenRepository.save(new RefreshToken(authentication.getName(), refreshToken));
      return refreshToken;
   }

   /** Access + Refresh 동시 발급 */
   public TokenDto createTokens(Authentication authentication) {
      String accessToken = createAccessToken(authentication);
      String refreshToken = createRefreshToken(authentication);
      // 토큰 자체 로그 출력은 비권장(보안). 필요 시 마스킹하세요.
      return new TokenDto(accessToken, refreshToken);
   }

   /** RefreshToken으로 새로운 AccessToken 발급 (권한 복원) */
   public String regenerateAccessToken(String refreshToken) {
      if (!validateToken(refreshToken)) {
         throw new RuntimeException("Invalid refresh token");
      }

      Claims claims = Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(refreshToken)
              .getBody();

      String username = claims.getSubject();

      RefreshToken storedToken = refreshTokenRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("Refresh Token Not Found"));

      if (!storedToken.getToken().equals(refreshToken)) {
         throw new RuntimeException("Refresh Token Mismatch");
      }

      // ✅ refresh 토큰에서 auth 복원 (null/빈값 대비)
      String authStr = claims.get(AUTHORITIES_KEY, String.class);
      Collection<? extends GrantedAuthority> authorities =
              (authStr == null || authStr.isBlank())
                      ? new ArrayList<>()
                      : Arrays.stream(authStr.split(","))
                      .filter(s -> s != null && !s.isBlank())
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

      Authentication authentication =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

      return createAccessToken(authentication);
   }

   public Authentication getAuthentication(String token) {
      Claims claims = Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      String authStr = claims.get(AUTHORITIES_KEY, String.class);
      Collection<? extends GrantedAuthority> authorities =
              (authStr == null || authStr.isBlank())
                      ? new ArrayList<>()
                      : Arrays.stream(authStr.split(","))
                      .filter(s -> s != null && !s.isBlank())
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

      UserDetails principal = new User(claims.getSubject(), "", authorities);
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
      } catch (Exception e) {
         logger.info("잘못된 JWT 토큰입니다.");
      }
      return false;
   }
}
