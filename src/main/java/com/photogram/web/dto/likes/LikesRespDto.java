package com.photogram.web.dto.likes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class LikesRespDto {

	private Long imageId;
	
	private Long principalId;
	
	private int totalLikeCount;
	
	private String message;
	
	public LikesRespDto(Long imageId, Long principalId, String message, int totalLikeCount) {
		
		this.imageId = imageId;
		this.principalId = principalId;
		this.message = message;
		this.totalLikeCount = totalLikeCount;
	}
	
}
