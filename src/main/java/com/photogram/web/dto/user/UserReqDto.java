package com.photogram.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserReqDto {

	@Getter @Setter
	@ToString
	public static class UserUpdateReqDto {
		
		@NotBlank(message = "이름은 공백일 수 없습니다.")
		private String name;
		
		@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
		private String password;
		private String website;
		private String bio;
		private String phone;
		private String gender;
		
	}
}
