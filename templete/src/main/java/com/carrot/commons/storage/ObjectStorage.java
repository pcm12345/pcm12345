package com.carrot.commons.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.carrot.commons.common.Commons;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public class ObjectStorage {

  private AmazonS3 s3;

  private ObjectStorage() {

  }

  public static ObjectStorage init(String endPoint, String regionName, String accessKey,
      String secretKey) {
    ObjectStorage objectStorage = new ObjectStorage();
    objectStorage.s3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new EndpointConfiguration(endPoint, regionName))
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .build();
    return objectStorage;
  }

  /**
   * 파일 업로드
   * @param file 실제 파일
   * @param bucketName 버킷명
   * @param folderName 폴더이름
   * @return 파일명
   */
  public boolean uploadObject(MultipartFile file, String bucketName, String folderName) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(0L);
    objectMetadata.setContentType("application/x-directory");
    try {
      String location = bucketName + "/" + folderName;
      String fileName = file.getOriginalFilename();
      ObjectMetadata omd = new ObjectMetadata();
      omd.setContentType(file.getContentType());
      omd.setContentLength(file.getSize());

      PutObjectRequest objectRequest = new PutObjectRequest(location, fileName, file.getInputStream(), omd).withCannedAcl(
          CannedAccessControlList.PublicRead);
      s3.putObject(objectRequest);
      return true;
    } catch (SdkClientException | IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean downloadObject(String bucketName, String folderName, String objectName) {
    try {
      HttpServletResponse response = Commons.recallResponse();
      S3Object s3Object = s3.getObject(bucketName, folderName + objectName);
      S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

      OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + objectName);
      byte[] bytesArray = new byte[4096];
      int bytesRead;
      while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
        outputStream.write(bytesArray, 0, bytesRead);
      }
      outputStream.flush();
      outputStream.close();
      s3ObjectInputStream.close();
      //log.info(String.format("Object %s has been downloaded.\n", objectName));
      return true;
    } catch (SdkClientException | IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Bucket> getBucketList() {
    try {
      return s3.listBuckets();
    } catch (SdkClientException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ObjectListing getObjectList(String bucketName, boolean isTopLevel) {
    try {
      ListObjectsRequest listObjectsRequest = new
          ListObjectsRequest().withBucketName(bucketName).withMaxKeys(300);
      if (isTopLevel) {
        listObjectsRequest.withDelimiter("/");
      }
      return s3.listObjects(listObjectsRequest);
    } catch (AmazonS3Exception e) {
      System.err.println(e.getErrorMessage());
      return null;
    }
  }

}
