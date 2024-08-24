package com.photogram.service.comment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.comment.Comment;
import com.photogram.domain.comment.CommentRepository;
import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.comment.CommentRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	
	public CommentRespDto writeComment(String content, Long imageId, Long principalId) {
		
		Image imageEntity = imageRepository.findById(imageId).orElseThrow(() -> {
			throw new CustomApiException("해당 이미지를 찾을 수 없습니다.");
		});
		
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
	
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(imageEntity);
		comment.setUser(userEntity);
		comment.setCreatedDate(LocalDateTime.now());
		
		return new CommentRespDto(commentRepository.save(comment));
	}
	
	public CommentRespDto deleteComment(Long commentId) {
		
		Comment commentEntity = commentRepository.findById(commentId).orElseThrow(() -> {
			throw new CustomApiException("해당 댓글을 찾을 수 없습니다.");
		});
		
		commentRepository.deleteById(commentId);
		
		return new CommentRespDto(commentEntity);
	}
}
