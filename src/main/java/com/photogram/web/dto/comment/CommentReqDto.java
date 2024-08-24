package com.photogram.web.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentReqDto {

	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String content;
	
	private Long imageId;
	
	private Long userId;
	
}
