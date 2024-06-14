package com.microservice.user.client.album;

import static com.microservice.user.constants.Constants.ALBUM_SERVICE_NAME;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.microservice.user.model.response.AlbumResponse;

@FeignClient(name = ALBUM_SERVICE_NAME)
public interface IAlbumServiceClient {
  
  @GetMapping("/albums/{id}")
  public List<AlbumResponse> getAlbums(@PathVariable("id") String id);
  
}
