package com.record.myplace.placeCollection.mapper;

import com.record.myplace.placeCollection.dto.PlaceCollectionItemResponse;
import com.record.myplace.placeCollection.dto.PlaceCollectionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceCollectionQueryMapper {

    List<PlaceCollectionResponse> selectMyCollections(@Param("useremail") String useremail,
                                                      @Param("placeId") String placeId);

    PlaceCollectionResponse selectCollectionDetail(@Param("useremail") String useremail,
                                                   @Param("collectionId") Long collectionId);

    List<PlaceCollectionItemResponse> selectCollectionItems(@Param("useremail") String useremail,
                                                            @Param("collectionId") Long collectionId);
}
