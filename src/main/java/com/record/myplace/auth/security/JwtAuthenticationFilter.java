package com.record.myplace.auth.security;

import java.io.IOException;
import java.util.List;

import com.record.myplace.auth.principal.CustomUserDetails;
import com.record.myplace.user.entity.User;
import com.record.myplace.user.repository.UserRepository; // 네 repo 경로에 맞춰
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        String bearer = request.getHeader("Authorization");
        String token = resolveToken(request);

        System.out.println("\n[JWT-FILTER] " + method + " " + uri);
        System.out.println("[JWT-FILTER] hasAuthorizationHeader=" + (bearer != null && !bearer.isBlank()));
        System.out.println("[JWT-FILTER] tokenResolved=" + (token != null && !token.isBlank()));
        System.out.println("[JWT-FILTER] beforeAuth=" + SecurityContextHolder.getContext().getAuthentication());

        try {
            // 1) 토큰 없으면 그냥 통과
            if (!StringUtils.hasText(token)) {
                System.out.println("[JWT-FILTER] SKIP: token is empty");
                filterChain.doFilter(request, response);
                return;
            }

            // 2) 이미 인증 있으면 통과
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("[JWT-FILTER] SKIP: authentication already exists");
                filterChain.doFilter(request, response);
                return;
            }

            // 3) 토큰 검증
            boolean valid = jwtTokenProvider.validateToken(token);
            System.out.println("[JWT-FILTER] validateToken=" + valid);

            if (!valid) {
                System.out.println("[JWT-FILTER] SKIP: invalid token");
                filterChain.doFilter(request, response);
                return;
            }

            // 4) 이메일 추출
            String email = jwtTokenProvider.getEmail(token);
            System.out.println("[JWT-FILTER] extractedEmail=" + email);

            if (!StringUtils.hasText(email)) {
                System.out.println("[JWT-FILTER] SKIP: email is empty");
                filterChain.doFilter(request, response);
                return;
            }

            // 5) DB 조회
            User user = userRepository.findByEmail(email).orElse(null);
            System.out.println("[JWT-FILTER] userFound=" + (user != null));

            if (user == null) {
                System.out.println("[JWT-FILTER] SKIP: no user in DB for email=" + email);
                filterChain.doFilter(request, response);
                return;
            }

            // 6) 인증 세팅
            CustomUserDetails userDetails = new CustomUserDetails(user);
            var auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("[JWT-FILTER] AUTH SET: principal=" + auth.getPrincipal().getClass().getName());

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("[JWT-FILTER] ERROR: " + e.getClass().getName() + " / " + e.getMessage());
            e.printStackTrace();
            filterChain.doFilter(request, response);
        } finally {
            System.out.println("[JWT-FILTER] afterAuth=" + SecurityContextHolder.getContext().getAuthentication());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearer)) return null;

        if (bearer.startsWith("Bearer ")) return bearer.substring(7);
        return null;
    }
}