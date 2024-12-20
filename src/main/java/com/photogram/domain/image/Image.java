package com.photogram.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.photogram.domain.comment.Comment;
import com.photogram.domain.likes.Likes;
import com.photogram.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Getter @Setter
@Table(name = "image_tb")
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 2-1. PK
	
	private String caption;  // 2-2. 상세 설명.
	
	private String name; // 2-3. original 이미지 이름
	
	private String storyImageUrl;  // 2-4. 실제 사진이 저장된 pc내 폴더 경로값과 저장으로 생성된 이미지 파일의 이름을 더한 문자열

	@JoinColumn(name = "userId")  // @JoinColumn : 연관관계의 주인이라는 뜻, Object를 선언할때는 외래키의 이름을 지정
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;  // 2-5. 유저 정보
	
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Likes> likes; // 2-8. 좋아요 리스트
	
	@Transient  // DB에 컬럼을 만들지 않는다.
	private Boolean likeState;  // 2-9. 좋아요 상태
	
	@Transient  // DB에 컬럼을 만들지 않는다.
	private int likeCount;  // 2-10. 좋아요 수
	
	// 1-8. 댓글
	@OrderBy("id desc") // 1-11. 내림 차순으로 정렬해서 가져오기
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	@CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = false)
 	private LocalDateTime createdDate;  // 2-6. 생성일
	
	@LastModifiedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = true)
 	private LocalDateTime updatedDate;  // 2-7. 수정일
}
