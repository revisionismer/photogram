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
	
	public LikesRespDto(Long imageId, Long principalId, int totalLikeCount) {
		
		this.imageId = imageId;
		this.principalId = principalId;
		this.message = principalId + "번 유저가 " + imageId + "번 스토리 좋아요 성공";
		this.totalLikeCount = totalLikeCount;
	}
	
}
