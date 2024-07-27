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
		
	private Long userId;
	private boolean subscribeState;  
	private boolean equalUserState;
	
	private int subscribeCount;
	
	public SubscribeRespDto(Subscribe subscribe, Long principalId, Long pageUserId, int subscribeCount) {
		this.id = subscribe.getId();
		this.username = subscribe.getToUser().getUsername();
		this.profileImageUrl = subscribe.getToUser().getProfileImageUrl();
		
		this.userId = subscribe.getToUser().getId();
		this.subscribeState = subscribe != null ? true : false;
		this.equalUserState = principalId == pageUserId ? true : false;
		
		this.subscribeCount = subscribeCount;
	}
}
