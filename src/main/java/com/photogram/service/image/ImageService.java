package com.photogram.service.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.comment.CommentRepository;
import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.domain.likes.LikesRepository;
import com.photogram.domain.user.User;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.image.ImageRespDto;
import com.photogram.web.dto.image.ImageStoryListRespDto;
import com.photogram.web.dto.image.ImageUploadReqDto;
import com.photogram.web.dto.image.ImageUploadRespDto;
import com.photogram.web.dto.likes.LikesRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	private final LikesRepository likesRepository;
	
	private final CommentRepository commentRepository;

	@Value("${storyImg.path}")
	private String uploadFolder;

	public ImageUploadRespDto uploadStory(ImageUploadReqDto imageUploadReqDto, User loginUser) {
		
		// 1-3. 파일명 + 확장자 가져오기
		String originalFileName = imageUploadReqDto.getFile().getOriginalFilename();
				
		// 1-4. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				
		// 1-5. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
				
		// 1-6. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
				
		// 1-7. 실제 파일이  저장될 경로 + 파일명 저장.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
				
		// 1-8. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
			Files.write(imageFilePath, imageUploadReqDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		// 1-6. 매개변수로 전달된 정보를 Image entity로 변환(Builder 패턴 이용)
		Image image = imageUploadReqDto.toEntity(originalFileName, imageFileName, loginUser);
				
		// 1-7. image 엔티티 영속화하고 반환받는다.
		Image storyImage = imageRepository.save(image);
		
		return new ImageUploadRespDto(storyImage, loginUser);
	}
	
	public ImageStoryListRespDto imageStoryList(User loginUser, Pageable pageable) {
		
		Page<Image> images = imageRepository.mStory(loginUser.getId(), pageable);
		
		// 2024-08-12 : 좋아요 서비스 진행중
		images.forEach((image) -> {
			// 4-2. 이미지에 담겨있는 좋아요 데이터 가져오기
			image.getLikes().forEach((like) -> {
				// 4-3. 해당 이미지를 좋아요한 사람이 현재 로그인한 유저라면
				if(like.getUser().getId() == loginUser.getId()) {
					// 4-4. 좋아요를 한 유저가 로그인한 유저라면 좋아요 상태 값을 true로 세팅 -> 로그인한 유저라면 story 페이지에서  이미지의 좋아요 하트 색깔을 빨간색으로 표시해주기 위해
					image.setLikeState(true);
				}
				
				image.setLikeCount(image.getLikes().size());
			});
			
		});
	
		// 2024-08-20 : 전체 댓글 정보 가져와서 dto에 추가 : 이미지 아이디로 해당 이미지 아이디값을 가지고 있는 comment 리스트만 가져온다.
		Page<ImageRespDto> result = images.map( image -> new ImageRespDto(image, commentRepository.findAllByImageId(image.getId())));
		
		if(result.getContent().size() == 0) {
			return new ImageStoryListRespDto();
		} 
		
		return new ImageStoryListRespDto(result, result.getContent().size());
		
	}
	
	public LikesRespDto likeImageStory(Long imageId, Long principalId) {
		// 2024-08-13 : 좋아요 서비스 구현
		Optional<Image> imageOp = imageRepository.findById(imageId);
		
		if(imageOp.isPresent()) {
		
			Image findImage = imageOp.get();
			
			likesRepository.mLikes(findImage.getId(), principalId);
			
			String message = principalId + "번 유저가 " + imageId + "번 스토리 좋아요 성공";
			
			int totalLikeCount = likesRepository.mTotalLikeCount(imageId);
			
			return new LikesRespDto(findImage.getId(), principalId, message, totalLikeCount);
		} else {
			throw new CustomApiException("이미지가 존재하지 않습니다.");
		}
		
	}
	
	public LikesRespDto unLikeImageStory(Long imageId, Long principalId) {

		Optional<Image> imageOp = imageRepository.findById(imageId);
		
		if(imageOp.isPresent()) {
		
			Image findImage = imageOp.get();
			
			likesRepository.mUnLikes(findImage.getId(), principalId);
			
			String message = principalId + "번 유저가 " + imageId + "번 스토리 좋아요 취소 성공";
			
			int totalLikeCount = likesRepository.mTotalLikeCount(imageId);
			
			return new LikesRespDto(findImage.getId(), principalId, message, totalLikeCount);
		} else {
			throw new CustomApiException("이미지가 존재하지 않습니다.");
		}
		
	}
	
	
	
}
