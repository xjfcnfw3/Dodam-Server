package com.example.dodam.config;

import com.example.dodam.config.auth.MemberDetailsService;
import com.example.dodam.config.jwt.JwtFilter;
import com.example.dodam.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final MemberDetailsService memberDetailsService;
	private final CorsConfig corsConfig;
	private final TokenProvider tokenProvider;

	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(tokenProvider, memberDetailsService);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.addFilter(corsConfig.corsFilter())
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.httpBasic().disable()
			// 필터 추가
			.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
			// 권한 관리
			.authorizeRequests()
			.antMatchers("/diary/**").hasAnyRole("ROLE_USER")
			.antMatchers("/orders/**").hasAnyRole("ROLE_USER")
			.antMatchers("/order-details/**").hasAnyRole("ROLE_USER")
			.antMatchers("/inquiry/**").hasAnyRole("ROLE_USER")
			.antMatchers("/inquiries/**").hasAnyRole("ROLE_USER")
			.antMatchers("/user").hasAnyRole("ROLE_USER")
			.anyRequest().permitAll();	//다른 요청은 권한 허용
		return http.build();
	}
}
