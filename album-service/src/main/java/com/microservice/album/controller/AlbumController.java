package com.microservice.album.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.microservice.album.response.AlbumResponse;
import com.microservice.album.service.AlbumService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

  private final AlbumService albumService;

  public AlbumController(AlbumService albumService) {
    this.albumService = albumService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<AlbumResponse>> retrieveAlbumById(@PathVariable String id) {
    return new ResponseEntity<>(albumService.retrieveAlbums(id), HttpStatus.OK);
  }
  
}
