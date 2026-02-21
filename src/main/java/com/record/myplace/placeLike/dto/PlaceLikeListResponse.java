package com.record.myplace.placeLike.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceLikeListResponse {
	private List<PlaceLikeResponse> items;
}
