package com.photogram.web.dto.image;

import com.photogram.domain.image.Image;
import com.photogram.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ImageUploadRespDto {

	private Long id;
	private String originalFileName;
	private String storyImageUrl;
	private Long userId;
	
	public ImageUploadRespDto(Image image, User loginUser) {
		this.id = image.getId();
		this.originalFileName = image.getName();
		this.storyImageUrl = image.getStoryImageUrl();
		this.userId = loginUser.getId();
	}
}
