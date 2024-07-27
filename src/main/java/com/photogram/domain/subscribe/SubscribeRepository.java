package com.photogram.domain.subscribe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

	// 4-1. native query
	@Query(value = "SELECT * FROM subscribe_tb WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	Optional<Subscribe> findByfromUserIdAndtoUserId(@Param(value = "fromUserId") Long fromUserId, @Param(value = "toUserId") Long toUserId);
	
	@Modifying  // 4-2. @Modifying 어노테이션은 INSERT, DELETE, UPDATE를 네이트브 쿼리로 작성하려면 해당 어노테이션을 넣어줘야 한다.
	@Query(value = "INSERT INTO subscribe_tb(fromUserId, toUserId, createdDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	int mSubscribe(@Param(value = "fromUserId") Long fromUserId, @Param(value = "toUserId") Long toUserId);

	@Modifying
	@Query(value = "DELETE FROM subscribe_tb WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	int mUnSubscribe(@Param(value = "fromUserId") Long fromUserId, @Param(value = "toUserId") Long toUserId);

	@Query(value = "SELECT COUNT(*) FROM subscribe_tb WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(@Param(value = "principalId") Long principalId, @Param(value = "pageUserId") Long pageUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe_tb WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(@Param(value = "pageUserId") Long pageUserId);
	
	
	List<Subscribe> findByfromUserId(Long fromUserId);
	// 4-3. 반환형이 int일 경우 
	//  - 성공 시(1) : 변경된 행의 갯수 만큼 return
	//  - 실패 시(0) : insert시에는 변경된 행이 없음, delete시에는 삭제할 데이터가 없음.
	//  - 쿼리가 잘못되었을 시(-1)
}
