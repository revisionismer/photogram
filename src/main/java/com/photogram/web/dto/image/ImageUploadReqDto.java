package com.photogram.web.dto.image;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.photogram.domain.image.Image;
import com.photogram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@ToString
public class ImageUploadReqDto {

	private MultipartFile file;
	
	private String caption;
	
	public Image toEntity(String originalFileName, String storyImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.name(originalFileName)
				.storyImageUrl(storyImageUrl)
				.user(user)
				.createdDate(LocalDateTime.now())
				.build();
	}
}
