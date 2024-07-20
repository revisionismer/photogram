package com.photogram.web.dto.auth;

import com.photogram.constant.user.UserEnum;
import com.photogram.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SignUpRespDto {

	private Long id;
	private String username;
	private UserEnum role;
	private String email;
	
	public SignUpRespDto(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	public SignUpRespDto(User userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.email = userEntity.getEmail();
		this.role = userEntity.getRole();
	}
}
