package com.record.myplace.placeReview.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.record.myplace.placeReview.entity.PlaceReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlaceReviewResponseDto {

    private Long id;
    private String userEmail;
    private String nickname;
    private String placeId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PlaceReviewImageResponseDto> images;

    public static PlaceReviewResponseDto fromEntity(PlaceReview entity) {
        return PlaceReviewResponseDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .nickname(entity.getUser() != null ? entity.getUser().getNickname() : null)
                .placeId(entity.getPlaceId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .images(entity.getImages() == null ? List.of()
                        : entity.getImages().stream()
                                .map(PlaceReviewImageResponseDto::fromEntity)
                                .collect(Collectors.toList()))
                .build();
    }
}
