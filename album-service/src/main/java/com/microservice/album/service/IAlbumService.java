package com.microservice.album.service;

import java.util.List;
import com.microservice.album.response.AlbumResponse;

public interface IAlbumService {
  
  List<AlbumResponse> retrieveAlbums(String userId);
}
