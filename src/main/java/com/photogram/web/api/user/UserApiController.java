package com.photogram.web.api.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.service.user.UserService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.user.UserRespDto.UserInfoRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class UserApiController {
	
	private final UserService userService;

	@GetMapping("/s/info")
	public ResponseEntity<?> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.readUserInfo(loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "로그인 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/list")
	public ResponseEntity<?> userList() {
		
		List<UserInfoRespDto> result = userService.readUserList();
		
		return new ResponseEntity<>(new ResponseDto<>(1, "유저 리스트 정보 조회 성공", result), HttpStatus.OK);
	}
	
	// 2024-07-12 : @PathVariable 사용시 이름을 항시 명시해줘야 한다.
	@GetMapping("/{userId}")
	public ResponseEntity<?> userInfoByUserId(@PathVariable("userId") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		UserInfoRespDto userInfoRespDto = userService.readUserInfoByUserId(id);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "유저 리스트 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
}
