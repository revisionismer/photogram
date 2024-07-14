package com.photogram.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.user.UserRespDto.UserInfoRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public UserInfoRespDto readUserInfo(User loginUser) {
		// 2-1. 전달 받은 User entity 정보가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(loginUser.getUsername());
						
		if(userOp.isPresent()) {
			User findUser = userOp.get();
							
			return new UserInfoRespDto(findUser);
							
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<UserInfoRespDto> readUserList() {
				
		List<UserInfoRespDto> result = new ArrayList<>(); 
	
		List<User> list = userRepository.findAll();
		
		for(User user : list) {
			result.add(new UserInfoRespDto(user));
		}
		
		return result;
		
	}
	
	public UserInfoRespDto readUserInfoByUserId(Long id) {
		
		Optional<User> userOp = userRepository.findById(id);
		
		if(userOp.isPresent()) {
			User findUser = userOp.get();
			
			return new UserInfoRespDto(findUser);
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}

}
