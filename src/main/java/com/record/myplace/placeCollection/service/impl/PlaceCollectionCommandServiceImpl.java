package com.record.myplace.placeCollection.service.impl;

import com.record.myplace.place.service.PlaceCommandService;
import com.record.myplace.placeCollection.dto.PlaceCollectionCreateRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceRequest;
import com.record.myplace.placeCollection.dto.PlaceCollectionSavePlaceResponse;
import com.record.myplace.placeCollection.entity.PlaceCollection;
import com.record.myplace.placeCollection.entity.PlaceCollectionItem;
import com.record.myplace.placeCollection.repository.PlaceCollectionItemRepository;
import com.record.myplace.placeCollection.repository.PlaceCollectionRepository;
import com.record.myplace.placeCollection.service.PlaceCollectionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceCollectionCommandServiceImpl implements PlaceCollectionCommandService {

    private static final String DEFAULT_COLLECTION_COLOR = "#dc2626";
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9a-fA-F]{6}$");

    private final PlaceCollectionRepository placeCollectionRepository;
    private final PlaceCollectionItemRepository placeCollectionItemRepository;
    private final PlaceCommandService placeCommandService;

    @Override
    public PlaceCollectionResponse createCollection(String useremail, PlaceCollectionCreateRequest request) {
        String name = request != null && request.getName() != null ? request.getName().trim() : "";

        if (name.isBlank()) {
            throw new IllegalArgumentException("리스트 이름은 필수입니다.");
        }

        if (placeCollectionRepository.existsByUseremailAndName(useremail, name)) {
            throw new IllegalArgumentException("같은 이름의 저장 리스트가 이미 있습니다.");
        }

        PlaceCollection collection = new PlaceCollection();
        collection.setUseremail(useremail);
        collection.setName(name);
        collection.setColor(resolveColor(request.getColor()));

        PlaceCollection saved = placeCollectionRepository.save(collection);
        return toResponse(saved);
    }

    @Override
    public PlaceCollectionSavePlaceResponse savePlace(String useremail, Long collectionId, PlaceCollectionSavePlaceRequest request) {
        if (request == null || request.getPlaceId() == null || request.getPlaceId().isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        PlaceCollection collection = getOwnedCollection(useremail, collectionId);

        placeCommandService.ensurePlaceExists(
                request.getPlaceId(),
                request.getPlaceName(),
                request.getAddress(),
                request.getRoadAddress(),
                request.getCategory(),
                request.getPhone()
        );

        PlaceCollectionItem existing = placeCollectionItemRepository.findByCollectionIdAndPlaceId(collectionId, request.getPlaceId())
                .orElse(null);

        if (existing != null) {
            touchCollection(collection);
            return toSaveResponse(collectionId, existing.getPlaceId(), existing.getCreatedAt());
        }

        PlaceCollectionItem item = new PlaceCollectionItem();
        item.setCollectionId(collectionId);
        item.setPlaceId(request.getPlaceId());

        try {
            PlaceCollectionItem saved = placeCollectionItemRepository.save(item);
            touchCollection(collection);
            return toSaveResponse(collectionId, saved.getPlaceId(), saved.getCreatedAt());
        } catch (DataIntegrityViolationException e) {
            PlaceCollectionItem duplicated = placeCollectionItemRepository.findByCollectionIdAndPlaceId(collectionId, request.getPlaceId())
                    .orElseThrow(() -> e);
            touchCollection(collection);
            return toSaveResponse(collectionId, duplicated.getPlaceId(), duplicated.getCreatedAt());
        }
    }

    @Override
    public void removePlace(String useremail, Long collectionId, String placeId) {
        if (placeId == null || placeId.isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        PlaceCollection collection = getOwnedCollection(useremail, collectionId);
        placeCollectionItemRepository.deleteByCollectionIdAndPlaceId(collectionId, placeId);
        touchCollection(collection);
    }

    @Override
    public void deleteCollection(String useremail, Long collectionId) {
        PlaceCollection collection = getOwnedCollection(useremail, collectionId);
        placeCollectionItemRepository.deleteByCollectionId(collectionId);
        placeCollectionRepository.delete(collection);
    }

    private PlaceCollection getOwnedCollection(String useremail, Long collectionId) {
        return placeCollectionRepository.findByIdAndUseremail(collectionId, useremail)
                .orElseThrow(() -> new IllegalArgumentException("저장 리스트를 찾을 수 없습니다."));
    }

    private void touchCollection(PlaceCollection collection) {
        collection.setUpdatedAt(LocalDateTime.now());
        placeCollectionRepository.save(collection);
    }

    private PlaceCollectionResponse toResponse(PlaceCollection collection) {
        PlaceCollectionResponse response = new PlaceCollectionResponse();
        response.setCollectionId(collection.getId());
        response.setName(collection.getName());
        response.setColor(collection.getColor());
        response.setPlaceCount(0L);
        response.setSaved(false);
        response.setCreatedAt(collection.getCreatedAt());
        response.setUpdatedAt(collection.getUpdatedAt());
        return response;
    }

    private PlaceCollectionSavePlaceResponse toSaveResponse(Long collectionId, String placeId, LocalDateTime savedAt) {
        PlaceCollectionSavePlaceResponse response = new PlaceCollectionSavePlaceResponse();
        response.setCollectionId(collectionId);
        response.setPlaceId(placeId);
        response.setSaved(true);
        response.setSavedAt(savedAt);
        return response;
    }

    private String resolveColor(String color) {
        String normalizedColor = color != null ? color.trim() : "";

        if (normalizedColor.isBlank()) {
            return DEFAULT_COLLECTION_COLOR;
        }

        if (!HEX_COLOR_PATTERN.matcher(normalizedColor).matches()) {
            throw new IllegalArgumentException("리스트 색상 형식이 올바르지 않습니다.");
        }

        return normalizedColor.toLowerCase();
    }
}
