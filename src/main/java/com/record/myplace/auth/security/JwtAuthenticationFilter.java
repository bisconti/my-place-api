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

        String token = resolveToken(request);

        if (StringUtils.hasText(token)
                && jwtTokenProvider.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String email = jwtTokenProvider.getEmail(token);

            // ✅ email로 User 조회
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                CustomUserDetails userDetails = new CustomUserDetails(user);

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, // ✅ principal = CustomUserDetails(User)
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            // user가 없으면 인증 세팅 안 하고 그냥 통과(= 이후 인증 실패 처리)
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearer)) return null;

        if (bearer.startsWith("Bearer ")) return bearer.substring(7);
        return null;
    }
}