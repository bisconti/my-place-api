package com.record.myplace.auth.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.record.myplace.auth.principal.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_ATTRIBUTE = "traceId";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String traceId = resolveTraceId(request);
        String token = resolveToken(request);

        request.setAttribute(TRACE_ID_ATTRIBUTE, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);
        MDC.put(TRACE_ID_ATTRIBUTE, traceId);

        try {
            if (!StringUtils.hasText(token)) {
                log.debug("[traceId={}] Authorization header가 없어 인증 없이 통과합니다. {} {}",
                        traceId, request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("[traceId={}] 기존 인증 정보가 있어 JWT 인증을 건너뜁니다. {} {}",
                        traceId, request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                log.debug("[traceId={}] 유효하지 않은 access token입니다. {} {}",
                        traceId, request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtTokenProvider.isRefreshToken(token)) {
                log.debug("[traceId={}] refresh token은 인증 필터에서 사용하지 않습니다. {} {}",
                        traceId, request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtTokenProvider.getEmail(token);
            String username = jwtTokenProvider.getUsername(token);

            if (!StringUtils.hasText(email) || !StringUtils.hasText(username)) {
                log.warn("[traceId={}] access token claim이 부족해 인증을 건너뜁니다. emailPresent={}, usernamePresent={}, {} {}",
                        traceId, StringUtils.hasText(email), StringUtils.hasText(username),
                        request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            CustomUserDetails userDetails = new CustomUserDetails(email, username, "");
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("[traceId={}] JWT 인증 성공. userEmail={}, {} {}",
                    traceId, email, request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.warn("[traceId={}] JWT 인증 필터 처리 중 예외가 발생했습니다. {} {}",
                    traceId, request.getMethod(), request.getRequestURI(), ex);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_ATTRIBUTE);
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String headerTraceId = request.getHeader(TRACE_ID_HEADER);
        if (StringUtils.hasText(headerTraceId)) {
            return headerTraceId;
        }

        Object attributeTraceId = request.getAttribute(TRACE_ID_ATTRIBUTE);
        if (attributeTraceId instanceof String && StringUtils.hasText((String) attributeTraceId)) {
            return (String) attributeTraceId;
        }

        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearer)) {
            return null;
        }

        return bearer.startsWith("Bearer ") ? bearer.substring(7) : null;
    }
}
