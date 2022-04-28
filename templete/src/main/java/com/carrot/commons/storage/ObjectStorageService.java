package com.carrot.commons.storage;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ObjectStorageService {

	@Value("${naver.end-point}")
	private String endPoint;

	@Value("${naver.region-name}")
	private String regionName;

	@Value("${naver.access-key}")
	private String accessKey;

	@Value("${naver.secret-key}")
	private String secretKey;

	private ObjectStorage connectStorage() {
		return ObjectStorage.init(endPoint, regionName, accessKey, secretKey);
	}

	public boolean uploadObject(MultipartFile file, String bucketName, String folderName) {
		return connectStorage().uploadObject(file, bucketName, folderName);
	}

	public boolean downloadObject(String bucketName, String folderName, String objectName) {
		return connectStorage().downloadObject(bucketName, folderName, objectName);
	}
	
	public List<Bucket> selectBucketList() {
		return connectStorage().getBucketList();
	}
	
	public ObjectListing selectObjectList(String bucketName, boolean isTopLevel) {
		return connectStorage().getObjectList(bucketName, isTopLevel);
	}
	
}
