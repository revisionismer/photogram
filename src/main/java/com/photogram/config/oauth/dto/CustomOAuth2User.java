package com.photogram.config.oauth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private final OAuth2UserRespDto oAuth2UserRespDto;
	
	public CustomOAuth2User(OAuth2UserRespDto oAuth2UserRespDto) {
		this.oAuth2UserRespDto = oAuth2UserRespDto;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		authorities.add(new GrantedAuthority() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_" + oAuth2UserRespDto.getRole();	
			}
		});
		
		return authorities;
	}

	@Override
	public String getName() {
		
		return oAuth2UserRespDto.getName();
	}
	
	public String getUsername() {
		
		return oAuth2UserRespDto.getUsername();
	}
	
	public Long getId() {
		return oAuth2UserRespDto.getId();
	}

}
