package com.photogram.domain.subscribe;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.photogram.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(  // 4-5. 유니크 제약조건 컬럼 두개 이상 거는 방법(중복이 안되는 유일성을 가지기 위해)
	name = "subscribe_tb",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "subscribe_uk",
			columnNames = {"fromUserId", "toUserId"}
		)
	}
)
public class Subscribe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 4-1. PK
	
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	private User fromUser;  // 4-2. 구독하는 유저
	
	@ManyToOne
	@JoinColumn(name = "toUserId")
	private User toUser; // 4-3. 구독받는 유저
	
	@CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")  // 4-4. 생성일
 	@Column(nullable = false)
 	private LocalDateTime createdDate;
}
