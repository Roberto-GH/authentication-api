package com.app.authentication.controllers;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.app.authentication.models.Asset;
import com.app.authentication.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AssetController.API + AssetController.ASSETS)
@CrossOrigin(origins = "${cross.origins}")
public class AssetController {

  public static final String API = "/api";
  public static final String ASSETS = "/assets";
  public static final String UPLOAD = "/upload";
  public static final String GET_OBJECT = "/get-object";
  public static final String DELETE_OBJECT = "/delete-object";

  @Autowired
  private S3Service s3Service;

  @PostMapping(AssetController.UPLOAD)
  public ResponseEntity<?> upload(@RequestParam MultipartFile file) {
    String key =  s3Service.putObject(file);
    Map<String, String> response = new HashMap<>();
    response.put("key", key);
    response.put("url", s3Service.getObjectUrl(key));
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping(value = AssetController.GET_OBJECT, params = "key")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getObject(@RequestParam String key) {
    Asset asset = null;
    ByteArrayResource resource = null;
    try{
      asset = s3Service.getObject(key);
      resource = new ByteArrayResource(asset.getContent());
    }catch (AmazonS3Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    return ResponseEntity
      .status(HttpStatus.OK)
      .header("Content-Type", asset.getContentType())
      .contentLength(asset.getContent().length)
      .body(resource);
  }

  @DeleteMapping(value = AssetController.DELETE_OBJECT, params = "key")
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteObject(@RequestParam String key) {
    s3Service.deleteObject(key);
  }

}
