package com.photogram.web.dto.image;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class ImageStoryListRespDto {

	private Page<ImageRespDto> images;
	private int totalCount;

	public ImageStoryListRespDto(Page<ImageRespDto> images, int totalCount) {
		this.images = images;
		this.totalCount = totalCount;
		
	}
}
