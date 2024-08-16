package com.photogram.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query(value = "SELECT * FROM image_tb WHERE userId IN (SELECT toUserId FROM subscribe_tb WHERE fromUserId = :principalId)", nativeQuery = true)
	Page<Image> mStory(@Param(value = "principalId") Long principalId, Pageable pageable);
	
	@Query(value = "SELECT * FROM image_tb WHERE userId IN (SELECT toUserId FROM subscribe_tb WHERE fromUserId = :principalId)", nativeQuery = true)
	List<Image> mStory(@Param(value = "principalId") Long principalId);
	
	
	List<Image> findAllByUserId(Long userId);
	
}
