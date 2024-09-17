package com.photogram.config.oauth.dto;

import java.util.Map;

public class FacebookResponse implements OAuth2Response {
	
	private final Map<String, Object> attribute;
	
	public FacebookResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getProvider() {
		return "facebook";
	}

	@Override
	public String getProviderId() {
		return (String) attribute.get("id");
	}

	@Override
	public String getEmail() {
		return (String) attribute.get("email");
	}

	@Override
	public String getName() {
		return (String) attribute.get("name");
	}
	
}
