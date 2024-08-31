package com.photogram.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.domain.subscribe.SubscribeRepository;
import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.web.dto.image.ImageRespDto;
import com.photogram.web.dto.user.UserReqDto.UserUpdateReqDto;
import com.photogram.web.dto.user.UserRespDto.UserInfoRespDto;
import com.photogram.web.dto.user.UserRespDto.UserProfileRespDto;
import com.photogram.web.dto.user.UserRespDto.UserUpdateRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	private final SubscribeRepository subscribeRepository;
	
	private final ImageRepository imageRepository;
	
	@Value("${profileImg.path}")
	private String uploadFolder;
	
	@Transactional(readOnly = true)
	public UserInfoRespDto readUserInfo(Long principalId) {
		
		// 2-1. 전달 받은 User entity 정보가 있는지 확인
		Optional<User> userOp = userRepository.findById(principalId);
						
		if(userOp.isPresent()) {
			User findUser = userOp.get();
			
			int subscribeCount = subscribeRepository.mSubscribeCount(principalId);
			int subscribeState = subscribeRepository.mSubscribeState(principalId, principalId);
			
			List<Image> images = imageRepository.findAllByUserId(principalId);
			
			// 2024-08-25 : 본인이 올려놓은 스토리의 좋아요 정보 담기.
			images.forEach((image) -> {
				// 4-2. 이미지에 담겨있는 좋아요 데이터 가져오기
				image.getLikes().forEach((like) -> {
					// 4-3. 해당 이미지를 좋아요한 사람이 현재 로그인한 유저라면
					if(like.getUser().getId().equals(findUser.getId())) {
						// 4-4. 좋아요를 한 유저가 로그인한 유저라면 좋아요 상태 값을 true로 세팅 -> 로그인한 유저라면 story 페이지에서  이미지의 좋아요 하트 색깔을 빨간색으로 표시해주기 위해
						image.setLikeState(true);
					}
					
				});

				// 2024-08-29 : 위치 변경 -> .getLikes().forEach 안에서 밖으로 이동
				image.setLikeCount(image.getLikes().size());
				
			});
		
			// 2024-08-01
			List<ImageRespDto> imagesDto = images.stream().map( (image) -> new ImageRespDto(image)).toList();
			
			return new UserInfoRespDto(findUser, subscribeState == 1 ? true : false, subscribeCount, true, imagesDto);
							
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<UserInfoRespDto> readUserList() {
				
		List<UserInfoRespDto> result = new ArrayList<>(); 
	
		List<User> list = userRepository.findAll();
		
		for(User user : list) {
			result.add(new UserInfoRespDto(user));
		}
		
		return result;
		
	}
	
	public UserInfoRespDto readUserInfoByUserId(Long pageUserId, Long principalId) {
		
		Optional<User> userOp = userRepository.findById(pageUserId);
		
		if(userOp.isPresent()) {
			User findUser = userOp.get();
			
			List<Image> images = imageRepository.findAllByUserId(pageUserId);
			
			int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
			int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
			
			images.forEach((image) -> {
				// 4-2. 이미지에 담겨있는 좋아요 데이터 가져오기
				image.getLikes().forEach((like) -> {
					// 4-3. 해당 이미지를 좋아요한 사람이 현재 로그인한 유저라면
					if(like.getUser().getId().equals(findUser.getId())) {
						// 4-4. 좋아요를 한 유저가 로그인한 유저라면 좋아요 상태 값을 true로 세팅 -> 로그인한 유저라면 story 페이지에서  이미지의 좋아요 하트 색깔을 빨간색으로 표시해주기 위해
						image.setLikeState(true);
					}
					
				});
				
				// 2024-08-29 : 위치 변경 -> .getLikes().forEach 안에서 밖으로 이동
				image.setLikeCount(image.getLikes().size());
				
			});
			
			// 2024-08-01
			List<ImageRespDto> imagesDto = images.stream().map( (image) -> new ImageRespDto(image)).toList();
			
			return new UserInfoRespDto(findUser, subscribeState == 1 ? true : false, subscribeCount, principalId == pageUserId ? true : false, imagesDto);
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	public UserUpdateRespDto updateUserInfo(UserUpdateReqDto userUpdateReqDto, Long principalId) {
		
		Optional<User> userOp = userRepository.findById(principalId);
		
		if(userOp.isPresent()) {
			User loginUser = userOp.get();
			
			if(passwordEncoder.matches(userUpdateReqDto.getPassword(), loginUser.getPassword())) {

				loginUser.update(userUpdateReqDto, passwordEncoder);
				
			} else {
				throw new CustomApiException("비밀번호가 일치하지 않습니다.");
			}
			
			return new UserUpdateRespDto(loginUser);
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	public UserProfileRespDto userProfilePictureUpdate(Long principalId, MultipartFile profileImageFile) {
		// 3-1. 파일명 + 확장자 가져오기
		String originalFileName = profileImageFile.getOriginalFilename();
				
		// 3-2. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				
		// 3-3. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
				
		// 3-4. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
		
		// 3-5. 3-4에서 가져온 실제 파일이  저장될 경로에 3-3에서 만든 UUID 파일명을 더해서 Path를 만든다.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		System.out.println(imageFilePath);
		
		// 3-6. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
			
			Files.write(imageFilePath, profileImageFile.getBytes());
			
		} catch (Exception e) {
			throw new MaxUploadSizeExceededException(1000);
		}
		
		// 3-7. 로그인 유저 정보를 가져온다.
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});		
			
		// 3-8. 변경감지를 이용해 set을 호출하면 업데이트
		userEntity.setProfileImageUrl(imageFileName);
		
		return new UserProfileRespDto(userEntity);
				
	}

}
