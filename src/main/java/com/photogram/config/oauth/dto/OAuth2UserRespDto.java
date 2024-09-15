package com.photogram.config.oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OAuth2UserRespDto {
	
	private Long id;

	private String username;
	
	private String name;
	
	private String role;
}
