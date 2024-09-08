package com.photogram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.photogram.config.jwt.filter.JwtAuthenticationFilter;
import com.photogram.config.jwt.filter.JwtAuthorizationFilter;
import com.photogram.config.jwt.service.JwtService;
import com.photogram.config.oauth.handler.CustomSuccessHandler;
import com.photogram.config.oauth.service.CustomOAuth2UserService;
import com.photogram.constant.user.UserEnum;
import com.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration  // 1-1. IOC에 SecurityConfig 파일을 등록.
@EnableWebSecurity  // 1-16. 시큐리티 활성화 -> 기본 스프링 필터체인에 등록, 주의 : SecurityConfig를 테스트할땐 @EnableWebSecurity 주석
@RequiredArgsConstructor
public class SecurityConfig {
	
	// 1-2. @Slf4j 어노테이션으로 사용해도 된다.
	private final Logger log = LoggerFactory.getLogger(getClass());

	// 3-1. AuthenticationConfiguration 객체 만들기 1
	private AuthenticationConfiguration configuration; 
	
	// 3-4. 
	private final JwtService jwtService;
		
	// 3-2. WebSecurityConfigurerAdapter를 상속해서 AuthenticationManager를 bean으로 등록했던걸 직접 등록. -> 이걸 등록해주면 콘솔에 비밀번호 뜨는게 안뜬다.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		this.configuration = authenticationConfiguration;  // 3-3. authenticationManager로 전달되는 AuthenticationConfiguration을 셋팅(변수명 겹치는거 유의)
		return configuration.getAuthenticationManager();
	}
	
	// 4-1. 
	private final UserRepository userRepository;
	
	// 1-3. 비밀번호 해시 -> 참고 : @Bean 어노테이션은 상위 클래스에 @Configuraion 어노테이션이 붙어 있는 곳에서만 동작
	@Bean 
	public BCryptPasswordEncoder encode() throws Exception {
		log.info("BCryptPasswordEncoder 빈 등록 완료.");
		return new BCryptPasswordEncoder();
	}
	
	// 5-2. 
	private final CustomOAuth2UserService customOAuth2UserService;
	
	// 5-3. 
	private final CustomSuccessHandler customSuccessHandler;
	
	// 1-4. 기존 SecurityConfig에서 configure 메소드 기능을 한다.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("filterChain 빈 등록 완료");
		return http
				.headers( (header) -> header.frameOptions( (frameOption) -> frameOption.disable() ) )  // 1-5. http iframe 허용 X
				.csrf( (csrf) -> csrf.disable() )  // 1-6. csrf 비활성화
				.cors( (cors) -> cors.configurationSource(configurationSource()) )  // 1-7. custom cors 설정 등록
				.sessionManagement( (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )  // 1-8. jSessionId를 서버에서 관리하지 않는다는 뜻으로, 세션 응답이 종료되면 사라진다는 뜻.(무상태성 설정) : jwt인증을 구현할거기 때문에 추가
				.formLogin( (formLogin) -> formLogin.disable() )  // 1-9. 폼 로그인 방식을 사용하지 않는다고 선언
				.httpBasic( (httpBasic) -> httpBasic.disable() )  // 1-10. httpSecurity가 제공하는 기본인증 기능 disable(브라우저가 팝업창을 하나 띄워 인증을 진행하는걸 막음)
				.oauth2Login( (oauth2) -> oauth2.userInfoEndpoint( (userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))  // 5-1. oauth2 설정 : .oauth2Login(Customizer.withDefaults()) 초기 상태(이 설정을 넣어줘야 콘솔에 에러가 안남, 세팅 : 5-2로 받아온 customOAuth2UserService 넘겨준다.
												.successHandler(customSuccessHandler)) // 5-4. successHandler 등록.  
				.authorizeHttpRequests(   // 1-11. 인증 Request를 정의(이 설정이 들어가면 org.springframework.web.util.pattern.PatternParseException이 발생하는데 application.properties에 설정하나를 더 넣어줘야한다.(requestMatchers의 default를 antMatcher로 바꿔줘야한다. 스프링부트 3점대 버전부터 antMatcher가 default가 아니라 path_pattern_parser라서 모든 요청이 restAPI형태로만 들어와야됨)
		  				  				  // 그래서 application.properties에 spring.mvc.pathmatch.matching-strategy=ant_path_matcher 추가해줘야 정상동작.
						(authorizeReqeust) -> authorizeReqeust.requestMatchers("/api/**/s/**").authenticated()  // 1-12. /api/** 형태로 들어오는 url은 인증이 필요하다.
											   				  .requestMatchers("/api/admin/**").hasRole(""  + UserEnum.ADMIN)  // 1-13. /api/admin/**을 호출하기 위해선 설정된 Role이 필요하다.
											   				  .anyRequest()  // 1-14. 1-12, 1-13가 아닌 요청은			
											   				  .permitAll()  // 1-15. 모두 허용
				)
				.addFilterAt(new JwtAuthenticationFilter(authenticationManager(configuration), jwtService), UsernamePasswordAuthenticationFilter.class) // 1-17. 폼로그인을 사용하지 않기 때문에 UsernamePasswordAuthenticationFilter 재정의한 JwtAuthenticationFilter를 등록헤서 인증처리를 진행한다.
				.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(configuration), userRepository, jwtService), UsernamePasswordAuthenticationFilter.class)  // 4-2. 권한 관리 필터 등록. -> SecurityFilterChain 앞에 addFilterBefore로 필터를 등록.
				.build();
				
	}
	
	public CorsConfigurationSource configurationSource() {  // 2-1. CorsConfigurationSource로 cors 설정 -> 기존엔 filter로 만들어서 등록했었는데 이번엔 CorsConfigurationSource 객체를 이용해 등록.
		log.info("CorsConfigurationSource cors 설정 생성 후 SecurityFilterChain에 등록 후 실행 중");
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");  // 2-2. 모든 header 응답 허용
		configuration.addAllowedMethod("*");  // 2-3. GET, POST, PUT, DELETE 허용
		configuration.addAllowedOriginPattern("*");  // 2-4. 모든 IP 주소 허용
		configuration.setAllowCredentials(true);  // 2-5. 클라이언트쪽에서 쿠키 요청하는걸 허용(사용자 자격 증명이 지원되는지 여부)
		configuration.addExposedHeader("Authorization");  // 2-6. 브라우저 버전이 바뀌면 default가 아닐 수도 있기 때문에 넣어준다.(2023-07-29)
		
		// 2-6. UrlBasedCorsConfigurationSource 객체 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);  // 2-7. 모든 주소 요청시 CorsConfiguration 설정을 적용.
	
		
		return source;
	}
}
