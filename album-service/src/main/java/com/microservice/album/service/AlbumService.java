package com.microservice.album.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.google.common.reflect.TypeToken;
import com.microservice.album.entity.AlbumEntity;
import com.microservice.album.response.AlbumResponse;

@Service
public class AlbumService implements IAlbumService {

  @Override
  public List<AlbumResponse> retrieveAlbums(String userId) {
    List<AlbumEntity> returnValue = new ArrayList<>();

    AlbumEntity albumEntity = new AlbumEntity();
    albumEntity.setUserId(userId);
    albumEntity.setAlbumId("album1Id");
    albumEntity.setDescription("album 1 description");
    albumEntity.setId(1L);
    albumEntity.setName("album 1 name");

    AlbumEntity albumEntity2 = new AlbumEntity();
    albumEntity2.setUserId(userId);
    albumEntity2.setAlbumId("album2Id");
    albumEntity2.setDescription("album 2 description");
    albumEntity2.setId(2L);
    albumEntity2.setName("album 2 name");

    returnValue.add(albumEntity);
    returnValue.add(albumEntity2);

    Type listType = new TypeToken<List<AlbumResponse>>() {
      /**
       * 
       */
      private static final long serialVersionUID = -5639688331093239174L;
    }.getType();

    return new ModelMapper().map(returnValue, listType);
  }

}
