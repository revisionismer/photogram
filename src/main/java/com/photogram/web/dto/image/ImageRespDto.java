package com.photogram.web.dto.image;

import com.photogram.domain.image.Image;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ImageRespDto {

	private Long imageId;  // 2-1. PK
	
	private String caption;  // 2-2. 상세 설명.
	
	private String name; // 2-3. original 이미지 이름
	
	private String storyImageUrl;  // 2-4. 실제 사진이 저장된 pc내 폴더 경로
	
	private Long userId;
	
	private String username;
	
	private String profileImageUrl;
	
	public ImageRespDto(Image image) {
		this.imageId = image.getId();
		this.caption = image.getCaption();
		this.name = image.getName();
		this.storyImageUrl = image.getStoryImageUrl();
		this.userId = image.getUser().getId();
		this.username = image.getUser().getUsername();
		this.profileImageUrl = image.getUser().getProfileImageUrl();
		
	}
}
