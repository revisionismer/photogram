package com.photogram.web.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.jwt.service.JwtService;
import com.photogram.service.auth.AuthService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.auth.SignUpReqDto;
import com.photogram.web.dto.auth.SignUpRespDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class AuthApiController {
	
	private final AuthService authService;
	private final JwtService jwtService;

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody @Valid SignUpReqDto signUpReqDto, BindingResult bindingResult) {
		
		SignUpRespDto signUpRespDto = authService.join(signUpReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", signUpRespDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		
		jwtService.logout(request, response);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 성공", null), HttpStatus.OK);
	}
	
}
