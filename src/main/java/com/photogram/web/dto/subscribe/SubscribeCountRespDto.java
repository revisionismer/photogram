package com.photogram.web.dto.subscribe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
public class SubscribeCountRespDto {

	private int subscribeCount;

	public SubscribeCountRespDto(int subscribeCount) {
		this.subscribeCount = subscribeCount;
	}
	
}
