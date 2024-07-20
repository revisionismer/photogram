package com.photogram.service.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.auth.SignUpReqDto;
import com.photogram.web.dto.auth.SignUpRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class AuthService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public SignUpRespDto join(SignUpReqDto signUpReqDto) {
		log.info("회원가입");
		
		// 1-1. 동일 회원 아이디가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(signUpReqDto.getUsername());
		
		if(userOp.isPresent()) {
			throw new CustomApiException("중복된 아이디입니다.");
		}
		
		// 1-2. 패스워드 인코딩 + 회원가입
		User userPS = userRepository.save(signUpReqDto.toEntity(passwordEncoder));
		
		// 1-4. dto 응답
		return new SignUpRespDto(userPS);
	}
}
