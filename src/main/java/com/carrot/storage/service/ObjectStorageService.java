package com.carrot.storage.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.carrot.common.CommonEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ObjectStorageService {
	
	final String endPoint = "https://kr.object.ncloudstorage.com";
	final String regionName = "kr-standard";
	final String accessKey = "E5A5A18C52E30340EB35";//
	final String secretKey = "564344AFCBF1873637D4ABA2B75CF60677EBDA42";//
	
	final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			.build();
	
	public String uploadObject(MultipartFile file, String storedFileName, String bucketName, String folderName) throws Exception {
		
		log.info("네이버 파일 업로드 bucketName : " + bucketName + " folderName : " + folderName);
		String url = "";
		// 업로드 될 파일 or 폴더 정보들 length, contentType 등
		/**
		 * 폴더 생성 해주는 부분인
		 */
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(0L);
		objectMetadata.setContentType("application/x-directory");
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);
		try {
			//s3.get
		    s3.putObject(putObjectRequest);
		    System.out.format("Folder %s has been created.\n", folderName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}

		// upload local file
		ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(file.getContentType());
        omd.setContentLength(file.getSize());
        
		try {
			s3.putObject(new PutObjectRequest(bucketName, folderName + "/" + storedFileName, file.getInputStream(), omd).withCannedAcl(CannedAccessControlList.PublicRead));
			log.info("#### URL : " + s3.getUrl(bucketName, storedFileName).toString());
		    System.out.format("Object %s has been created.\n", file.getOriginalFilename());
		    
		    url = CommonEnum.NAVER_OBJECT.getValue() + bucketName + "/" + folderName + "/" + storedFileName;
		    log.info("#### object storage url : " + url);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
		
		return url;
		
	}

	public void uploadObject2(MultipartFile file) throws Exception {
		
		String bucketName = "api-test"; // 파일이 저장될 버킷이름

		// create folder
		String folderName = "folder-test/"; // 파일이 저장될 폴더 이름

		// 업로드 될 파일 or 폴더 정보들 length, contentType 등
		/**
		 * 폴더 생성 해주는 부분인
		 */
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(0L);
		objectMetadata.setContentType("application/x-directory");
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);
		try {
			//s3.get
		    s3.putObject(putObjectRequest);
		    System.out.format("Folder %s has been created.\n", folderName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}

		// upload local file
//		String filePath = "/tmp/sample.txt";
		String originalFileName = file.getOriginalFilename();
		String objectName = originalFileName;
		String filePath = "C:\\Users\\D83\\Desktop\\" + originalFileName;
		log.info("#### filePath : " + filePath);
		try {
		    s3.putObject(bucketName, folderName+objectName, new File(filePath));
		    System.out.format("Object %s has been created.\n", objectName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
		
	}
	
	public void downloadObject(HttpServletResponse response) throws Exception{
		String bucketName = "api-test";
		String folderName = "folder-test/";
		String objectName = "80461f03ef324feb977ad54eaf8eb8181593763451.jpg";
		String downloadPath = "C:\\Users\\D83\\Desktop\\"+objectName;

		// download object
		try {
		    S3Object s3Object = s3.getObject(bucketName, folderName+objectName);
		    S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

//		    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadPath));
		    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		    response.setContentType("application/octet-stream");
		    response.setHeader("Content-Disposition","attachment;filename=" + objectName);
		    byte[] bytesArray = new byte[4096];
		    int bytesRead = -1;
		    while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
		        outputStream.write(bytesArray, 0, bytesRead);
		    }
		    

		    outputStream.flush();
		    outputStream.close();
		    s3ObjectInputStream.close();
		    System.out.format("Object %s has been downloaded.\n", objectName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
	}
	
	public void selectBucketList() {
		
		try {
			List<Bucket> buckets = s3.listBuckets();
			log.info("##### Bucket List : ");
			for(Bucket bucket : buckets) {
				log.info("   name : " + bucket.getName() + " , creation_date : " + bucket.getCreationDate() + " , owner :  " + bucket.getOwner().getId());
			}
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		}
	}
	
	public void selectObjectList() {
		String bucketName = "barobiz";

		// list all in the bucket
		try {
		    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
		        .withBucketName(bucketName)
		        .withMaxKeys(300);

		    ObjectListing objectListing = s3.listObjects(listObjectsRequest);

		    System.out.println("Object List:");
		    while (true) {
		        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		            System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
		        }

		        if (objectListing.isTruncated()) {
		            objectListing = s3.listNextBatchOfObjects(objectListing);
		        } else {
		            break;
		        }
		    }
		} catch (AmazonS3Exception e) {
		    System.err.println(e.getErrorMessage());
		    System.exit(1);
		}

		// top level folders and files in the bucket
		try {
		    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
		        .withBucketName(bucketName)
		        .withDelimiter("/")
		        .withMaxKeys(300);

		    ObjectListing objectListing = s3.listObjects(listObjectsRequest);

		    System.out.println("Folder List:");
		    for (String commonPrefixes : objectListing.getCommonPrefixes()) {
		        System.out.println("    name=" + commonPrefixes);
		    }

		    System.out.println("File List:");
		    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		        System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
		    }
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
	}
	
	public void deleteObject (String bucketName, String objectName) throws Exception {

		// delete object
		log.info("#### deleteObject objectName : " + objectName);
		//String value = "profile_photo";
		try {
		    s3.deleteObject(new DeleteObjectRequest(bucketName, objectName)); // objectName 은 '/' s는 제외하고 폴더명부터 시작된다.
		    System.out.format("Object %s has been deleted.\n", objectName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
	}

	public String replaceObjectName(String bucketName, String url){
		url = url.replaceAll(CommonEnum.NAVER_OBJECT.getValue(), ""); //스토리지 엔드포인트 부분을 제거한다.
		url = url.replaceFirst(bucketName+"/", ""); //버킷명을 제거한다.
		return url;
	}
	
}
