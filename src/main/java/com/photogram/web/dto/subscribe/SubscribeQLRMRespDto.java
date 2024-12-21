package com.photogram.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscribeQLRMRespDto {
	private Long id; 

    private String username;
    private String profileImageUrl;
   
    private Long subscribeState;
    private Long equalUserState; 
    
    private Long toUserId;
    
    // 2024-12-16 : mariaDB 버전에 따라 Long 형으로 매개변수를 전달 받을 수도 있고 Integer형태로 전달 받을 수도 있어서 Integer로 들어올 경우 생성자도 만들어 줌
    public SubscribeQLRMRespDto(Long id, String username, String profileImageUrl, Integer subscribeState, Integer equalUserState, Long toUserId) {
    	this.id= id;
    	this.username = username;
    	this.profileImageUrl = profileImageUrl;
    	this.subscribeState = Long.parseLong(String.valueOf(subscribeState));
    	this.equalUserState = Long.parseLong(String.valueOf(subscribeState));
    	this.toUserId = toUserId;
    	
    }
    
}
