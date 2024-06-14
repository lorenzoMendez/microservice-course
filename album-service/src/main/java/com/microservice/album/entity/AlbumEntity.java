package com.microservice.album.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumEntity {
  
  private Long id;
  private String albumId;
  private String userId;
  private String name;
  private String description;
}
