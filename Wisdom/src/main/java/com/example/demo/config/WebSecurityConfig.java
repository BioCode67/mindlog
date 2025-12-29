package com.example.demo.config; //commit test

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtFilter;

@Configuration
public class WebSecurityConfig {

	private final JwtFilter jwtFilter;

	// 생성자 주입 (우리가 만든 필터를 받아옵니다)
	public WebSecurityConfig(JwtFilter jwtFilter) {
		System.out.println(">>> 보안 설정 파일 로드됨! <<<"); // 이 한 줄 추가
		this.jwtFilter = jwtFilter;
	}

	// 1. 비밀번호 암호화 도구
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 2. 보안 필터 체인 설정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// (1) CSRF 비활성화 (JWT 사용 시 불필요)
				.csrf(csrf -> csrf.disable())

				// (2) 세션 끄기 (Stateless 모드)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// (3) 요청 권한 설정
				.authorizeHttpRequests(auth -> auth
						// 로그인, 회원가입, 문서 관련은 허용
						.requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/").permitAll()
						// 나머지는 인증 필요
						.anyRequest().authenticated())

				// (4) [핵심] JWT 필터를 먼저 실행하도록 설정
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}