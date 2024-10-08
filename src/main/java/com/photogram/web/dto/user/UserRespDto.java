package com.photogram.web.dto.user;

import java.util.List;

import com.photogram.domain.user.User;
import com.photogram.web.dto.image.ImageRespDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {

	@Getter @Setter
	@ToString
	public static class UserInfoRespDto {

		private Long id;
		private String username;
		private String email;
		private String role;
		private String name;
		private String profileImageUrl;
		private String website;
		private String bio;
		private String phone;
		private String gender;
		
		private boolean isPageOwner;
		
		private boolean subscribeState;
		private int subscribeCount;
		
		private int totalStoryCount;
		
		private List<ImageRespDto> images;
		
		public UserInfoRespDto() {}
		
		public UserInfoRespDto(User userEntity) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.email = userEntity.getEmail();
			this.role = userEntity.getRole().getValue();
			this.profileImageUrl = userEntity.getProfileImageUrl();
			this.name = userEntity.getName();
			this.website = userEntity.getWebsite();
			this.bio = userEntity.getBio();
			this.phone = userEntity.getPhone();
			this.gender = userEntity.getGender();
		}
		
		public UserInfoRespDto(User userEntity, boolean subscribeState, int subscribeCount, boolean isPageOwner, List<ImageRespDto> images) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.email = userEntity.getEmail();
			this.role = userEntity.getRole().getValue();
			this.profileImageUrl = userEntity.getProfileImageUrl();
			this.name = userEntity.getName();
			this.website = userEntity.getWebsite();
			this.bio = userEntity.getBio();
			this.phone = userEntity.getPhone();
			this.gender = userEntity.getGender();
			
			this.subscribeState = subscribeState;
			this.subscribeCount = subscribeCount;
			
			this.isPageOwner = isPageOwner;
			
			this.totalStoryCount = images.size();
			this.images = images;
			
		}
	}
	
	@Getter @Setter
	@ToString
	public static class UserProfileRespDto {
		private Long id;
		private String username;
		private String email;
		private String role;
		private String name;
		private String profileImageUrl;
		private String website;
		private String bio;
		private String phone;
		private String gender;
		
		public UserProfileRespDto(User userEntity) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.email = userEntity.getEmail();
			this.role = userEntity.getRole().getValue();
			this.profileImageUrl = userEntity.getProfileImageUrl();
			this.name = userEntity.getName();
			this.website = userEntity.getWebsite();
			this.bio = userEntity.getBio();
			this.phone = userEntity.getPhone();
			this.gender = userEntity.getGender();
		}
	}
	
	@Getter @Setter
	@ToString
	public static class UserUpdateRespDto {
		private Long id;
		private String username;
		private String name;
		private String password;
		private String website;
		private String bio;
		private String phone;
		private String gender;
		private String email;
		private String role;
		private String profileImageUrl;
		
		public UserUpdateRespDto(User userEntity) {
			
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.password = userEntity.getPassword();
			this.name = userEntity.getName();
			this.website = userEntity.getWebsite();
			this.bio = userEntity.getBio();
			this.email = userEntity.getEmail();
			this.phone = userEntity.getPhone();
			this.gender = userEntity.getGender();
			this.profileImageUrl = userEntity.getProfileImageUrl();
			this.role = userEntity.getRole().getValue();

		}
	}
	
}
