package com.photogram.web.api.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.service.comment.CommentService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.comment.CommentReqDto;
import com.photogram.web.dto.comment.CommentRespDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class CommentApiController {

	private final CommentService commentService;
	
	@PostMapping("")
	public ResponseEntity<?> saveComment(@RequestBody @Valid CommentReqDto commentReqDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		CommentRespDto commentRespDto = commentService.writeComment(commentReqDto.getContent(), commentReqDto.getImageId(), commentReqDto.getUserId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, commentRespDto.getCommentId() + "번 comment 작성 성공", commentRespDto), HttpStatus.CREATED);
	}
	
	// 2024-08-20 : 댓글 삭제 api 구현중 
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
 	
		CommentRespDto commentRespDto = commentService.deleteComment(commentId);
		
		return new ResponseEntity<>(new ResponseDto<>(1, commentRespDto.getCommentId() + "번 comment 삭제 성공", null), HttpStatus.CREATED);
	}
	
}
