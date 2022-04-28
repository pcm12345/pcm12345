package com.carrot.commons.storage;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.carrot.commons.model.ResponseBuilder;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
@ApiOperation("네이버클라우드 오브젝트 서비스 API")
@Slf4j
public class ObjectStorageController {

  private final ObjectStorageService objectStorageService;

  public ObjectStorageController(ObjectStorageService objectStorageService) {
    this.objectStorageService = objectStorageService;
  }

  /**
   * 버킷리스트 조회
   */
  @GetMapping("/bucketList")
  @ApiOperation(value = "버킷리스트 조회", notes = "스토리지에 존재하는 버킷리스트 조회")
  public ResponseEntity<?> selectBucketList() {
    log.info("#### ObjectStorageController.selectBucketList");
    List<Bucket> buckets = objectStorageService.selectBucketList();
    return ResponseBuilder.init(Map.of("buckets", buckets)).execute();
  }

  /**
   * 오브젝트리스트 조회
   */
  @GetMapping("/objectList")
  @ApiOperation(value = "오브젝트리스트 조회", notes = "버킷리스트 안에 존재하는 오브젝트리스트 조회")
  public ResponseEntity<?> selectObjectList(
      @RequestParam(value = "bucketName") String bucketName
      , @RequestParam(value = "isTopLevel") boolean isTopLevel
  ) {
    log.info("#### ObjectStorageController.selectObjectList");
    ObjectListing objectListing = objectStorageService.selectObjectList(bucketName, isTopLevel);
    return ResponseBuilder.init(Map.of("objectList", objectListing)).execute();
  }

  /**
   * 파일 업로드
   */
  @PostMapping("/upload")
  @ApiOperation(value = "오브젝트 업로드", notes = "네이버 오브젝트 업로드")
  public ResponseEntity<?> uploadObject(
      @RequestParam("file") MultipartFile file
      , @RequestParam("bucketName") String bucketName
      , @RequestParam("folderName") String folderName
  ) {
    log.info("#### ObjectStorageController.uploadObject");
    boolean isUpload = objectStorageService.uploadObject(file, bucketName, folderName);
    return ResponseBuilder
        .init(isUpload ? bucketName + "/" + folderName : "")
        .message(isUpload ? "업로드에 성공했습니다." : "업로드에 실패했습니다.").execute();
  }

  /**
   * 파일 다운로드
   */
  @GetMapping("/download")
  @ApiOperation(value = "오브젝트 다운로드", notes = "네이버 오브젝트 다운로드")
  public ResponseEntity<?> downloadObject(
      @RequestParam("bucketName") String bucketName
      , @RequestParam("folderName") String folderName
      , @RequestParam("objectName") String objectName
  ) {
    log.info("#### ObjectStorageController.downloadObject");
    boolean isDownload = objectStorageService.downloadObject(bucketName, folderName, objectName);
    return ResponseBuilder.init().message(isDownload ? "다운로드에 성공했습니다." : "다운로드에 실패했습니다.").execute();
  }

}
