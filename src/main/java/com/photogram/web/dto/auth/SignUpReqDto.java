package com.photogram.web.dto.auth;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.photogram.constant.user.UserEnum;
import com.photogram.domain.user.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SignUpReqDto {

	@Size(min = 2, max = 20)
	@NotBlank
	private String username;
	
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	private String password;
	
	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	private String email;
	
	@NotBlank(message = "이름은 공백일 수 없습니다.")
	private String name;
	
	public User toEntity(BCryptPasswordEncoder passwordEncoder) {
		return User.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.role(username.equals("admin") ? UserEnum.ADMIN : UserEnum.USER)
				.name(name)
				.email(email)
				.createDate(LocalDateTime.now())
				.build();
	}
	
}
