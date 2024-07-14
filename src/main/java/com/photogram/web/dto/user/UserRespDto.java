package com.photogram.web.dto.user;

import com.photogram.domain.user.User;

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
		private String email;
		private String role;
		private String profileImageUrl;
		
		public UserUpdateRespDto(User userEntity) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.email = userEntity.getEmail();
			this.role = userEntity.getRole().getValue();
			this.profileImageUrl = userEntity.getProfileImageUrl();
		}
	}
	
}
