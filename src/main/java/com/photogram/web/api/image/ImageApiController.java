package com.photogram.web.api.image;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.service.image.ImageService;
import com.photogram.web.dto.ResponseDto;
import com.photogram.web.dto.image.ImageStoryListRespDto;
import com.photogram.web.dto.image.ImageUploadReqDto;
import com.photogram.web.dto.image.ImageUploadRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ImageApiController {
	
	private final ImageService imageService;

	@PostMapping("/s/story")
	public ResponseEntity<?> storyWrite(@RequestPart("file") MultipartFile storyImageFile, @RequestPart("caption") String caption, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		ImageUploadRespDto imageUploadRespDto = imageService.uploadStory(new ImageUploadReqDto(storyImageFile, caption), loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, imageUploadRespDto.getId() + "번 스토리 업로드 성공", imageUploadRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/s/story/all")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
	
		User loginUser = principalDetails.getUser();
		
		ImageStoryListRespDto imageStoryListRespDto = imageService.imageStoryList(loginUser, pageable);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "이미지 스토리 리스트 불러오기 성공", imageStoryListRespDto), HttpStatus.OK);
	}
}
