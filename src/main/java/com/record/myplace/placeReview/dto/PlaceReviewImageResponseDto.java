package com.record.myplace.placeReview.dto;

import com.record.myplace.placeReview.entity.PlaceReviewImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlaceReviewImageResponseDto {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Long fileSize;
    private Integer sortOrder;

    public static PlaceReviewImageResponseDto fromEntity(PlaceReviewImage entity) {
        return PlaceReviewImageResponseDto.builder()
                .id(entity.getId())
                .originalFileName(entity.getOriginalFileName())
                .storedFileName(entity.getStoredFileName())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
