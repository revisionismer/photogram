package com.photogram.web.dto.subscribe;

import com.photogram.domain.subscribe.Subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeRespDto {

	private Long id;
	private String username;
	private String profileImageUrl;
		
	private Long toUserId;
	private boolean subscribeState;  
	private boolean equalUserState;

	public SubscribeRespDto(Subscribe subscribe, Long principalId, Long pageUserId) {
		this.id = subscribe.getId();
		this.username = subscribe.getToUser().getUsername();
		this.profileImageUrl = subscribe.getToUser().getProfileImageUrl();
		
		this.toUserId = subscribe.getToUser().getId();
		this.subscribeState = subscribe != null ? true : false;
		this.equalUserState = principalId == pageUserId ? true : false;

	}
	
	public SubscribeRespDto(SubscribeQLRMRespDto subscribeQLRMRespDto) {
		this.id = subscribeQLRMRespDto.getId();
		this.username = subscribeQLRMRespDto.getUsername();
		this.profileImageUrl = subscribeQLRMRespDto.getProfileImageUrl();
		
		this.toUserId = subscribeQLRMRespDto.getToUserId();
		this.subscribeState = subscribeQLRMRespDto.getSubscribeState() == 1 ? true : false;
		this.equalUserState = subscribeQLRMRespDto.getEqualUserState() == 1 ? true : false;
	}
}
