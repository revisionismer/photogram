package com.photogram.service.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.domain.user.User;
import com.photogram.web.dto.image.ImageUploadReqDto;
import com.photogram.web.dto.image.ImageUploadRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class ImageService {
	
	private final ImageRepository imageRepository;
	
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
}
