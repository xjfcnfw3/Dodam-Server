package com.example.dodam.config.jwt;

import com.example.dodam.config.auth.MemberDetailsService;
import com.example.dodam.security.TokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";

    private final TokenProvider tokenProvider;
    private final MemberDetailsService memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);

        try {
            authenticate(token);
        } catch (Exception e) {
            log.error("Error={}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        if (StringUtils.hasText(token) && tokenProvider.validateAccessToken(token)) {
            UserDetails member = getUserDetails(token);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(
                new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities()));
        }
    }

    private UserDetails getUserDetails(String token) {
        String email = tokenProvider.getAttribute(token);
        return memberDetailsService.loadUserByUsername(email);
    }
}
