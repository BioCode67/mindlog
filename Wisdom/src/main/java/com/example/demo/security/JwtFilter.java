package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. 요청 헤더에서 "Authorization"을 찾습니다. (여기에 토큰이 들어있음)
		String authorizationHeader = request.getHeader("Authorization");

		// [디버깅용 로그] 헤더가 제대로 들어오는지 확인
		System.out.println("헤더 수신: " + authorizationHeader);

		// 2. 헤더가 있고, "Bearer "로 시작하는지 확인합니다.
		// (토큰은 보통 "Bearer eyJhb..." 이런 식으로 보냅니다)
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

			// "Bearer " 글자(7글자)를 떼어내고 순수 토큰만 남깁니다.
			String token = authorizationHeader.substring(7);

			// [디버깅용 로그] 토큰만 잘 잘렸는지 확인
			System.out.println("추출된 토큰: " + token);

			// 3. 토큰이 유효한지 검사합니다. (위조되지 않았는지)
			if (jwtUtil.validateToken(token)) {

				// 4. 유효하다면 토큰에서 아이디(username)를 꺼냅니다.
				String username = jwtUtil.getUsername(token);

				// 5. "이 사용자는 인증되었습니다!"라고 임시 신분증(Authentication)을 만듭니다.
				// (비밀번호는 null, 권한은 일단 비워둠 new ArrayList<>())
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, new ArrayList<>());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// 6. SecurityContext(보안 구역)에 신분증을 걸어둡니다. (통과!)
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				// ... (기존 인증 로직) ...
	            System.out.println("토큰 검증 성공!"); // [추가]
			} else {
		        System.out.println("헤더가 없거나 Bearer로 시작하지 않음"); // [추가]
		    }
		}

		// 다음 단계로 넘어가라 (컨트롤러 등)
		filterChain.doFilter(request, response);
	}
}