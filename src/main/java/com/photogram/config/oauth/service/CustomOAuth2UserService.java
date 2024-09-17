package com.photogram.config.oauth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.photogram.config.oauth.dto.CustomOAuth2User;
import com.photogram.config.oauth.dto.FacebookResponse;
import com.photogram.config.oauth.dto.GoogleResponse;
import com.photogram.config.oauth.dto.NaverResponse;
import com.photogram.config.oauth.dto.OAuth2Response;
import com.photogram.config.oauth.dto.OAuth2UserRespDto;
import com.photogram.constant.user.UserEnum;
import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		// 1-1.
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		System.out.println("oAuth2User : " + oAuth2User);
		
		// 1-2. 인증 요청이 온 외부 로그인 사이트들을 구분해주는 id 값(구글, 페이스북, 네이버 등)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		// 1-4. dto로 데이터를 받아올 바구니
		OAuth2Response oAuth2Response = null;
		
		// 1-3. 1-2가 네이버라면
		if(registrationId.equals("naver")) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes()); // 1-5. 
		} else if(registrationId.equals("google")) { // 1-6. 1-2가 google, secret-key : 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
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else if(registrationId.equals("facebook")) {
			oAuth2Response = new FacebookResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}
		
		// 1-6. 리소스 서버에서 발급 받은 정보로 중복이 안되는 특정한 사용자 아이디 값을 만든다.
		String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
		
		// 1-8. DB에 user정보 저장
		Optional<User> userEntity = userRepository.findByUsername(username);
		
		if(userEntity.isEmpty()) {
			User user = new User();
			user.setUsername(username);
			user.setName(oAuth2Response.getName());
			user.setPassword(new BCryptPasswordEncoder().encode("1234"));
			user.setEmail(oAuth2Response.getEmail());
			user.setRole(UserEnum.USER);
			user.setCreatedDate(LocalDateTime.now());
			
			User newUser = userRepository.save(user);
			
			OAuth2UserRespDto oAuth2UserRespDto = new OAuth2UserRespDto();
			oAuth2UserRespDto.setId(newUser.getId());
			oAuth2UserRespDto.setUsername(username);
			oAuth2UserRespDto.setName(oAuth2Response.getName());
		
			oAuth2UserRespDto.setRole(UserEnum.USER.toString());
			
			return new CustomOAuth2User(oAuth2UserRespDto);
		} else {
			User user = userEntity.get();
			
			user.setEmail(oAuth2Response.getEmail());
			user.setName(oAuth2Response.getName());
			user.setUpdatedDate(LocalDateTime.now());
			
			OAuth2UserRespDto oAuth2UserRespDto = new OAuth2UserRespDto();
			oAuth2UserRespDto.setId(user.getId());
			oAuth2UserRespDto.setUsername(user.getUsername());
			oAuth2UserRespDto.setName(user.getName());
			oAuth2UserRespDto.setRole(user.getRole().toString());
			
			return new CustomOAuth2User(oAuth2UserRespDto);
		}
		
		
	}
}
