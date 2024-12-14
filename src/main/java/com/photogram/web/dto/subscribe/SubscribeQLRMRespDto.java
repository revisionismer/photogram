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
    
}
