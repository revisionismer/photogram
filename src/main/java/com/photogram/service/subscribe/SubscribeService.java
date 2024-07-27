package com.photogram.service.subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.subscribe.Subscribe;
import com.photogram.domain.subscribe.SubscribeRepository;
import com.photogram.domain.user.User;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.subscribe.SubscribeCountRespDto;
import com.photogram.web.dto.subscribe.SubscribeRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;

	public int mSubscribe(Long fromUserId, Long toUserId) {
		
		Optional<Subscribe> subscribeOp = subscribeRepository.findByfromUserIdAndtoUserId(fromUserId, toUserId);
		
		if(subscribeOp.isEmpty()) {
			
			if(fromUserId.equals(toUserId)) {
				throw new CustomApiException("자기 자신을 구독할 수 없습니다.");
			}
			
			int result = subscribeRepository.mSubscribe(fromUserId, toUserId);
			
			return result;
			
		} else {
			throw new CustomApiException("이미 구독을 한 유저입니다.");
		}
		
	}
	
	public SubscribeCountRespDto mUnSubscribe(Long fromUserId, Long toUserId) {
		
		Optional<Subscribe> subscribeOp = subscribeRepository.findByfromUserIdAndtoUserId(fromUserId, toUserId);
		
		if(subscribeOp.isPresent()) {
			
			subscribeRepository.mUnSubscribe(fromUserId, toUserId);
			
			List<Subscribe> subscribes = subscribeRepository.findByfromUserId(fromUserId);
			
			int result = subscribes.size();
			
			return new SubscribeCountRespDto(result);
			
		} else {
			throw new CustomApiException("구독 정보가 없습니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<SubscribeRespDto> subscribeList(User loginUser, Long pageUserId) {
		
		List<Subscribe> subscribes = subscribeRepository.findByfromUserId(pageUserId);
		
		List<SubscribeRespDto> result = new ArrayList<>(); 
		
		for(Subscribe subscribe : subscribes) {
			
			result.add(new SubscribeRespDto(subscribe, loginUser.getId(), pageUserId, subscribes.size()));
			
		}
		
		return result;
	}
	
}
