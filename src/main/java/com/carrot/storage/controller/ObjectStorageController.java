package com.carrot.storage.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carrot.storage.service.ObjectStorageService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/storage")
@ApiOperation("네이버클라우드 오브젝트 서비스 API")
@Slf4j
/**
 * Bucket -> Object Stroage 내의 버킷 이름
 * Folder -> Bucket 안에 만든 폴더
 * Object -> Bucket 혹은 Folder안에 들어갈 파일
 * @author D83
 *
 */
public class ObjectStorageController {
	
	@Autowired
	private ObjectStorageService objectStorageService;
	
	/**
	 * 버킷리스트 조회
	 * @throws Exception
	 */
	@GetMapping("/bucket-list")
	@ApiOperation(value="버킷리스트 조회", notes="스토리지에 존재하는 버킷리스트 조회")
	public void selectBucketList() throws Exception {
		
		log.info("#### ObjectStorageController.selectBucketList");
		objectStorageService.selectBucketList();
	}
	
	/**
	 * 오브젝트리스트 조회
	 * @throws Exception
	 */
	@GetMapping("/object-list")
	@ApiOperation(value="오브젝트리스트 조회", notes="버킷리스트 안에 존재하는 오브젝트리스트 조회")
	public void selectObjectList() throws Exception {
		
		log.info("#### ObjectStorageController.selectObjectList");
		objectStorageService.selectObjectList();
	}
	
	/**
	 * 파일 업로드
	 * @param file
	 * @throws Exception
	 */
	@PostMapping("/upload-object")
	public void uploadObject(@RequestParam("file") MultipartFile file, @RequestParam("storedFileName")String storedFileName, @RequestParam("bucketName") String bucketName, @RequestParam("folderName") String folderName) throws Exception {
		log.info("#### ObjectStorageController.uploadObject");
		objectStorageService.uploadObject(file, storedFileName, bucketName, folderName);
	}
	
	/**
	 * 파일 다운로드
	 * @throws Exception
	 */
	@GetMapping("/download-object")
	public void downloadObject(HttpServletResponse response) throws Exception {
		log.info("#### ObjectStorageController.downloadObject");
		objectStorageService.downloadObject(response);
	}
	
	@GetMapping("/delete")
	public void deleteObject() throws Exception {
		
		String bucketName = "crm";
		String objectName = "05f2772ad9ce4921a4063fb575357e8c1603162498.png";
		
		objectStorageService.deleteObject(bucketName, objectName);
		
	}

}
