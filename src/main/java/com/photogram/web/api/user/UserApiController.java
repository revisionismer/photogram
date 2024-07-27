package com.photogram.web.api.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.service.user.UserService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.user.UserReqDto.UserUpdateReqDto;
import com.photogram.web.dto.user.UserRespDto.UserInfoRespDto;
import com.photogram.web.dto.user.UserRespDto.UserProfileRespDto;
import com.photogram.web.dto.user.UserRespDto.UserUpdateRespDto;

import jakarta.validation.Valid;
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
		
		UserInfoRespDto userInfoRespDto = userService.readUserInfo(loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "로그인 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/s/{pageUserId}/info")
	public ResponseEntity<?> userInfoById(@PathVariable("pageUserId") Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.readUserInfoByUserId(pageUserId, loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, pageUserId + "번 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/list")
	public ResponseEntity<?> userList() {
		
		List<UserInfoRespDto> result = userService.readUserList();
		
		return new ResponseEntity<>(new ResponseDto<>(1, "유저 리스트 정보 조회 성공", result), HttpStatus.OK);
	}

	@PutMapping("/s/update")
	public ResponseEntity<?> userInfoUpdate(@RequestBody @Valid UserUpdateReqDto userUpdateReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserUpdateRespDto userUpdateRespDto = userService.updateUserInfo(userUpdateReqDto, loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "계정 정보 수정 성공", userUpdateRespDto), HttpStatus.OK);
	}
	
	// 2024-07-18 : 여기까지 완료
	@PutMapping("/s/update/profileImage")
	public ResponseEntity<?> profileImageUpdate(@RequestPart("file") MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 1-1. json formData에서 파일데이터에 매핑된 키값과 매핑해줘야 한다.(중요)
		
		User loginUser = principalDetails.getUser();
		
		UserProfileRespDto userProfileRespDto = userService.userProfilePictureUpdate(loginUser.getId(), profileImageFile);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "프로필 사진 변경 성공", userProfileRespDto), HttpStatus.OK);
	}
}
