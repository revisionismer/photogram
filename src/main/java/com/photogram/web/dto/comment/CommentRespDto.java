package com.photogram.web.dto.comment;

import com.photogram.domain.comment.Comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class CommentRespDto {

	private Long commentId;
	private String content;
	private Long imageId;
	private Long userId;
	private String username;
	
	public CommentRespDto(Comment commentEntity) {
		this.commentId = commentEntity.getId();
		this.content = commentEntity.getContent();
		this.imageId = commentEntity.getImage().getId();
		this.userId = commentEntity.getUser().getId();
		this.username = commentEntity.getUser().getUsername();
	}
}
