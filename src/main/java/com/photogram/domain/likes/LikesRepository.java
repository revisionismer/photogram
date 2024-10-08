package com.photogram.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

	@Modifying
	@Query(value = "INSERT INTO likes_tb(imageId, userId, createDate) VALUES(:imageId, :principalId, now())", nativeQuery = true)
	int mLikes(@Param(value = "imageId") Long imageId, @Param(value = "principalId") Long principalId);
	
	@Modifying
	@Query(value = "DELETE FROM likes_tb WHERE imageId = :imageId AND userId = :principalId", nativeQuery = true)
	int mUnLikes(@Param(value = "imageId") Long imageId, @Param(value = "principalId") Long principalId);
	
	@Query(value = "SELECT COUNT(*) FROM likes_tb WHERE imageId = :imageId", nativeQuery = true)
	int mTotalLikeCount(@Param(value = "imageId") Long imageId);
}
