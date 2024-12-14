package com.photogram.service.subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.subscribe.Subscribe;
import com.photogram.domain.subscribe.SubscribeRepository;
import com.photogram.domain.user.User;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.subscribe.SubscribeCountRespDto;
import com.photogram.web.dto.subscribe.SubscribeQLRMRespDto;
import com.photogram.web.dto.subscribe.SubscribeRespDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	
	private final EntityManager em;

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
	
	@Transactional(readOnly = true)
	public List<SubscribeRespDto> subscribeListByQLRM(User loginUser, Long pageUserId) {
		
		StringBuffer sb = new StringBuffer();
	
		// 1-1. 쿼리 준비(주의 : 세미콜론 들어가면 안된다.) 
		sb.append("SELECT ");
		sb.append(	"u.id, u.username, u.profileImageUrl, ");
		sb.append(	"if((SELECT 1 FROM subscribe_tb WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append(	"if((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM ");
		sb.append(	"user_tb u INNER JOIN subscribe_tb s ON u.id = s.toUserId ");
		sb.append("WHERE ");
		sb.append(	"s.fromUserId = ?");
		
		// 1-5. 첫번째 물음표 : principalId 
		// 1-6. 두번째 물음표 : principalId
		// 1-7. 마지막 물음표 : pageUserId
		
		// 1-2. 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
						.setParameter(1, loginUser.getId())
						.setParameter(2, loginUser.getId())
						.setParameter(3, pageUserId);
		
		// 1-3. 쿼리 실행(qlrm 라이브러리 이용, dto에 DB 결과값을 매핑시키기 위해서)
		JpaResultMapper jrm = new JpaResultMapper();
		
		// 2024-12-13 : 여기서부터 다시
		List<SubscribeQLRMRespDto> list = jrm.list(query, SubscribeQLRMRespDto.class);

		List<SubscribeRespDto> result = new ArrayList<>();
		
		for(SubscribeQLRMRespDto subscribeQLRMRespDto : list) {
			result.add(new SubscribeRespDto(subscribeQLRMRespDto));
		}
		
		return result;
	}
	
}
