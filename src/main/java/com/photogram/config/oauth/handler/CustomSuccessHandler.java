package com.photogram.config.oauth.handler;

import java.io.IOException;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photogram.config.jwt.JwtProperties;
import com.photogram.config.jwt.service.JwtService;
import com.photogram.config.oauth.dto.CustomOAuth2User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();
	
	private JwtService jwtService;
	
	public CustomSuccessHandler(JwtService jwtService) {
		this.jwtService = jwtService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// 1-1. 인증객체에서 CustomOAuth2User형 인증객체 꺼내오기
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		
		Collection<? extends GrantedAuthority> authorities = customOAuth2User.getAuthorities();
		
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		
		GrantedAuthority auth = iterator.next();
		
		String role = auth.getAuthority();
		
		// 1-2. JWT 토큰 만들기 1 : Header값 생성
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// 1-3. JWT 토큰 만들기 2 : claims 부분 설정(토큰 안에 담을 내용)
		Map<String, Object> claims = new HashMap<>();;
		claims.put("username", customOAuth2User.getUsername());
		claims.put("name", customOAuth2User.getName());
		claims.put("role", role);
		
		// 1-4. JWT 토큰 만들기 3 : 만료 시간 설정(Access token) ->  1000 * 60L * 60L * 1 = 1시간, 500 * 60L * 60L * 1 = 30분
		Long expiredTime = 1000 * 60L * 60L * 1;
//		Long expiredTime = 8 * 60L * 60L * 1;
		
		Date date = new Date();
		date.setTime(date.getTime() + expiredTime);
		
		System.out.println("access_token 만료일자 : " + date);
		
		// 1-5. JWT 토큰 만들기 4 : hmacSha 형식 key 만들기 
		Key key = Keys.hmacShaKeyFor(secretKeyBytes);
	
		// 1-6. JWT 토큰 Builder : access_token
		String access_token = Jwts.builder()
				.setHeader(headers) 
				.setClaims(claims)
				.setSubject("access_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("access_token = " + access_token);	
		
		// 1-7. JWT 토큰 Builder : refresh token -> expiredTime을 24시간보다 약간 크게 설정함.
		expiredTime *= 23;
		expiredTime += 100000;
		date.setTime(System.currentTimeMillis() + expiredTime);
		
		String refresh_token = Jwts.builder()
				.setHeader(headers) 
				.setSubject("refresh_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("refresh_token = " + refresh_token);
		
		// 1-8. refresh token 저장.
		jwtService.setRefreshToken(customOAuth2User.getUsername(), refresh_token);		
		
		// 1-9. JWT 토큰 response header에 담음(주의 : Bearer 다음에 한칸 띄우고 저장해야함)
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token);
		
		// 1-10. access_token 쿠키에 저장.
		Cookie cookie = new Cookie("access_token", access_token);
		
		// 1-11. 쿠키는 항상 도메인 주소가 루트("/")로 설정되어 있어야 모든 요청에서 사용 가능.
		cookie.setPath("/");
		cookie.setSecure(true);
	
		response.addCookie(cookie);
		
		// 1-12. 로그인 성공시 응답 객체 만들어 주기 1 : response객체 기본 세팅
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		// 1-13. 응답해줄 Map형태의 객체
		Map<String, Object> responseData = new HashMap<>();
		
		// 1-14. 응답 데이터 셋팅
		responseData.put("code", 1);
		responseData.put("message", "jwt 인증 성공");
		responseData.put("username", customOAuth2User.getUsername());
		responseData.put("role", role);
		
		// 1-15. ObjectMapper를 이용해 json형태의 String으로 변환
		String result = new ObjectMapper().writeValueAsString(responseData);
		
		// 1-16. response에 write
		response.getWriter().write(result);
		
	}
}
