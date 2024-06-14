package com.microservice.album.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumResponse {
  
  private String albumId;
  private String userId; 
  private String name;
  private String description;
}
