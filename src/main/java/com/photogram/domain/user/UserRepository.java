package com.photogram.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 참고 : JpaRepository를 상속 했으면 @Repository같은 어노테이션을 안붙여줘도 IoC에 자동으로 등록된다.
public interface UserRepository extends JpaRepository<User, Long> {
	// 1-1. JPA Query Method
	Optional<User> findByUsername(String username);
	
	@Query(value = "SELECT * FROM user_tb WHERE id IN (SELECT toUserId FROM subscribe_tb WHERE fromUserId = :principalId)", nativeQuery = true)
	List<User> mSubscribeUserList(@Param(value = "principalId") Long principalId);
	
}
