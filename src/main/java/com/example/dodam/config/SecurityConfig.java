package com.example.dodam.config;


import com.example.dodam.config.jwt.JwtAuthenticationFilter;
import com.example.dodam.config.jwt.JwtAuthorizationFilter;
import com.example.dodam.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{	
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CorsConfig corsConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.addFilter(corsConfig.corsFilter())
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.formLogin().disable()
				.httpBasic().disable()
				// 필터 추가
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
				// 권한 관리
				.authorizeRequests()
				.antMatchers("/diary/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/orders/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/order-details/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/inquiry/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/inquiries/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/user")
				.access("hasRole('ROLE_USER')")
				.anyRequest().permitAll();	//다른 요청은 권한 허용
	}
}






