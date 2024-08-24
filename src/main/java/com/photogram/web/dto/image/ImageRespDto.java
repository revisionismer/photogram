package com.photogram.web.dto.image;

import java.util.List;

import com.photogram.domain.comment.Comment;
import com.photogram.domain.image.Image;
import com.photogram.web.dto.comment.CommentRespDto;

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
	
	private Boolean likeState;  // 2-5. 게시글 좋아요 여부(본인)
	
	private int totalLikeCount; // 2-6. 게시글 좋아요 총 갯수
	
	private List<CommentRespDto> comments;  // 2-7. 댓글
	
	public ImageRespDto(Image image) {
		this.imageId = image.getId();
		this.caption = image.getCaption();
		this.name = image.getName();
		this.storyImageUrl = image.getStoryImageUrl();
		this.userId = image.getUser().getId();
		this.username = image.getUser().getUsername();
		this.profileImageUrl = image.getUser().getProfileImageUrl();
		this.likeState = image.getLikeState();
		this.totalLikeCount = image.getLikeCount();		
	}
	
	public ImageRespDto(Image image, List<Comment> comments) {
		this.imageId = image.getId();
		this.caption = image.getCaption();
		this.name = image.getName();
		this.storyImageUrl = image.getStoryImageUrl();
		this.userId = image.getUser().getId();
		this.username = image.getUser().getUsername();
		this.profileImageUrl = image.getUser().getProfileImageUrl();
		this.likeState = image.getLikeState();
		this.totalLikeCount = image.getLikeCount();
		this.comments = comments.stream().map( (comment) -> new CommentRespDto(comment)).toList();
	}
}
