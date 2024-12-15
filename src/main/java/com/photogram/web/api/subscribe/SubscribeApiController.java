package com.photogram.web.api.subscribe;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.service.subscribe.SubscribeService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.subscribe.SubscribeCountRespDto;
import com.photogram.web.dto.subscribe.SubscribeRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscribes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class SubscribeApiController {

	private final SubscribeService subscribeService;
	
	// 2024-07-22 : 구독하기, 구독취소 api
	@PostMapping("/s/{toUserId}")
	public ResponseEntity<?> subscribe(@PathVariable("toUserId") Long toUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		subscribeService.mSubscribe(loginUser.getId(), toUserId);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "구독하기 성공", null), HttpStatus.OK);
	}
	
	// 2024-07-26 : 구독 취소 구현
	@DeleteMapping("/s/{toUserId}")
	public ResponseEntity<?> unsubscribe(@PathVariable("toUserId") Long toUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		SubscribeCountRespDto subscribeCountRespDto = subscribeService.mUnSubscribe(loginUser.getId(), toUserId);
	
		return new ResponseEntity<>(new ResponseDto<>(1, "구독 취소하기 성공", subscribeCountRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/s/{pageUserId}/all")
	public ResponseEntity<?> subscribeList(@PathVariable("pageUserId") Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
//		List<SubscribeRespDto> subscribeRespDtos = subscribeService.subscribeList(loginUser, pageUserId);
		
		List<SubscribeRespDto> subscribeQLRMRespDtos = subscribeService.subscribeListByQLRM(loginUser, pageUserId);
		
		return new ResponseEntity<>(new ResponseDto<>(1, pageUserId + "번 유저의 구독자 정보 리스트 불러오기 성공", subscribeQLRMRespDtos), HttpStatus.OK);
	}
	
}
