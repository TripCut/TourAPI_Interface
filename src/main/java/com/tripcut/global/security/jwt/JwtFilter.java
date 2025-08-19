package com.tripcut.global.security.jwt;// package com.tripcut.global.security.jwt;

import com.tripcut.global.security.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
   private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
   private final TokenProvider tokenProvider;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
           throws ServletException, IOException {

      String bearer = request.getHeader("Authorization");
      if (bearer != null && bearer.startsWith("Bearer ")) {
         String token = bearer.substring(7);
         if (tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            // log.debug("JWT auth set: {}", auth.getName());
         } else {
            // log.debug("JWT invalid/expired");
         }
      } else {
         // log.debug("No Bearer header");
      }
      chain.doFilter(request, response);
   }
}
