package com.photogram.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.photogram.constant.user.UserEnum;
import com.photogram.domain.image.Image;
import com.photogram.web.dto.user.UserReqDto.UserUpdateReqDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // 1-3. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가(중요)
@Getter @Setter
@Table(name = "user_tb")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@Column(length = 100, unique = true, nullable = false)  // 1-16. OAuth2 로그인을 위해 20 -> 100자로 늘린다.
	private String username;  // 1-2. 계정 명
	
	@Column(nullable = false)
	private String password; // 1-3. 계정 비밀번호
	
	@Column(nullable = false)
	private String name;  // 1-4. 사용자 이름
	
	private String website; // 1-5. 웹사이트
	
	private String bio;  // 1-6. 자기소개
	
	@Column(nullable = false)
	private String email;  // 1-7. 이메일
	
	private String phone;  // 1-8. 휴대폰 번호
	
	private String gender;  // 1-9. 성별
	
	private String profileImageUrl;  // 1-10. 프로필 이미지 경로
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserEnum role; // ADMIN, USER // 1-11. 권한

	@CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = false)
 	private LocalDateTime createdDate;  // 1-12. 생성일자
 	 
 	@LastModifiedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = true)
 	private LocalDateTime updatedDate;  // 1-13. 수정일
 	
 	private String refreshToken;
	
	// 1-14. image 엔티티 양방향 매핑 mappedBy 속성의 의미는 나는 연관관계의 주인이 아니므로 DB에 테이블을 만들지 말라는 뜻.
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // 1-15. Lazy 전략은 image를 get할때만 같이 select 하라는 의미.
	@JsonIgnoreProperties({"user"})  // 1-16. image 엔티티에 있는 user는 또 불러오지 않는다.(무한참조)
	private List<Image> images;

	// 1-15. AOP 처리시 User 객체 따로 sysout 해보고 싶을 때 toString 커스터마이징(images 제거)
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createdDate=" + createdDate + "]";
	} 
	
	@Builder
	public User(Long id, String username, String password, String name, String email, UserEnum role, LocalDateTime createdDate, LocalDateTime updatedDate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.role = role;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	
	public void update(UserUpdateReqDto userUpdateReqDto, PasswordEncoder passwordEncoder) {
		this.name = userUpdateReqDto.getName();
		this.password = passwordEncoder.encode(userUpdateReqDto.getPassword());
		this.website = userUpdateReqDto.getWebsite();
		this.bio = userUpdateReqDto.getBio();
		this.phone = userUpdateReqDto.getPhone();
		this.gender = userUpdateReqDto.getGender();
		this.updatedDate = LocalDateTime.now();
	}
}