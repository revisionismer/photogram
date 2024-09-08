package com.photogram.config.oauth.dto;

public interface OAuth2Response {

	// 1-1. 제공자
	String getProvider();
	
	// 1-2. 제공자에게 발곱해주는 아이디(번호)
	String getProviderId();
	
	// 1-3. 이메일
	String getEmail();
	
	// 1-4. 사용자 이름
	String getName();
}
