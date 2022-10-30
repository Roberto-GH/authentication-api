package com.app.authentication.services;

import com.app.authentication.models.Asset;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

  String putObject(MultipartFile multipartFile);
  Asset getObject(String key);
  void deleteObject(String key);
  String getObjectUrl(String key);

}
