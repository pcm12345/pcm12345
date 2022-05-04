package com.carrot.user.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carrot.admin.dao.ExpensesVO;
import com.carrot.common.service.CryptoUtilService;
import com.carrot.user.dao.UserFileVO;
import com.carrot.user.dao.UserVO;
import com.carrot.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "/api/v1/user/", tags = "직원계정 관리 부분")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private CryptoUtilService cryptoUtilService;

	/**
	 * id 중복체크
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/user/dup")
	@ApiOperation(value="id 중복체크", notes = "id 중복체크")
	public ResponseEntity<Map<Object, Object>> duplicatUser(@RequestParam("id") String id) throws Exception {
		log.info("#### UserController.dupUserId id : " + id);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.duplicatUser(id),
				HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 직원 정보 등록
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/user")
	@ApiOperation(value = "직원 정보 등록", notes = "관리자(ADMIN)/본원상담사(STAFF)/협약상담사(CENTERSTAFF)/센터장(CENTERDIRECOTR) 등록하기")
	public ResponseEntity<Map<Object, Object>> insertUser(@RequestBody UserVO vo) throws Exception {
		log.info("#### UserController.insertUser vo : " + vo.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.insertUser(vo),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 직원 전체 목록
	 * 
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/user/all")
	@ApiOperation(value = "직원전체 목록", notes = "직원 전체 목록 조회")
	public ResponseEntity<Map<Object, Object>> selectUserAll() throws Exception {
		log.info("#### UserController.selectUserAll");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUserAll(),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 직원 정보 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/user/{idx}")
	@ApiOperation(value = "직원 정보 조회", notes = "관리자/본원상담사/협약상담사/센터장 정보 조회하기")
	public ResponseEntity<Map<Object, Object>> selectUser(@PathVariable("idx") int idx, HttpServletRequest request, @RequestParam(value="reportYn", required = false, defaultValue = "") String reportYn) throws Exception {
		log.info("#### UserController.selectStaff idx : " + idx + " reportYn : " + reportYn);
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("STAFF".equals(map.get("authority"))) {
			idx = Integer.parseInt(map.get("idx").toString());
		} 
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUser(idx, reportYn),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 직원 정보 수정
	 * 
	 * @param idx
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/user/{idx}")
	@ApiOperation(value = "직원 정보 수정", notes = "관리자/본원상담사/협약상담사/센터장 정보 수정하기")
	public ResponseEntity<Map<Object, Object>> updateUser(@PathVariable("idx") int idx, @RequestBody UserVO vo)
			throws Exception {
		log.info("#### UserController.updateUser idx :  " + idx + " vo : " + vo.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.updateUser(vo),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 직원 삭제
	 * 
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/user/{idx}")
	@ApiOperation(value = "직원 정보 삭제", notes = "관리자/본원상담사/협약상담사/센터장 정보 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteUser(@PathVariable("idx") int idx) throws Exception {
		log.info("#### UserController.deleteUser idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.deleteUser(idx),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 사용자 목록 조회
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/user/list")
	@ApiOperation(value = "사용자 목록 조회(삭제예정)", notes = "조건에 맞는 사용자 목록을 조회한다.")
	public ResponseEntity<Map<Object, Object>> selectUserList(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "permission", defaultValue = "", required = false) String permission,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate) throws Exception {
		log.info("#### UserController.selectUserList idxCrmCenter : " + idxCrmCenter + " name : " + name + " permassion : "
				+ permission + " startDate : " + startDate + " endDate : " + endDate);
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();

		int center = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("CENTERDIRECTOR".equals(map.get("authority"))) {
			center = Integer.parseInt(map.get("idxCrmCenter").toString());
		} else {
			center = idxCrmCenter;
		}

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUserList(center, name, permission, startDate, endDate, userService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/user/listall")
	@ApiOperation(value = "사용자 전체 목록 조회(삭제예정)", notes = "조건에 맞는 사용자 목록을 조회한다.")
	public ResponseEntity<Map<Object, Object>> selectUserListAll(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "permission", defaultValue = "", required = false) String permission,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate,
			HttpServletRequest request) throws Exception {
		log.info("#### UserController.selectUserList idxCrmCenter : " + idxCrmCenter + " name : " + name + " permassion : "
				+ permission + " startDate : " + startDate + " endDate : " + endDate);

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();

		int center = -1;

		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("CENTERDIRECTOR".equals(map.get("authority"))) {
			center = Integer.parseInt(map.get("idxCrmCenter").toString());
		} else {
			center = idxCrmCenter;
		}

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUserListAll(center, name, permission, startDate, endDate), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v2/user/list")
	@ApiOperation(value = "사용자 목록 조회", notes = "조건에 맞는 사용자 목록을 조회한다.")
	public ResponseEntity<Map<Object, Object>> selectUserListv2(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "permission", defaultValue = "", required = false) String permission,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate) throws Exception {
		log.info("#### UserController.selectUserList idxCrmCenter : " + idxCrmCenter + " name : " + name + " permassion : "
				+ permission + " startDate : " + startDate + " endDate : " + endDate);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUserList(idxCrmCenter, name, permission, startDate, endDate, userService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v2/user/listall")
	@ApiOperation(value = "사용자 전체 목록 조회", notes = "조건에 맞는 사용자 목록을 조회한다.")
	public ResponseEntity<Map<Object, Object>> selectUserListAllv2(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "permission", defaultValue = "", required = false) String permission,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate,
			HttpServletRequest request) throws Exception {
		log.info("#### UserController.selectUserList idxCrmCenter : " + idxCrmCenter + " name : " + name + " permassion : "
				+ permission + " startDate : " + startDate + " endDate : " + endDate);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectUserListAll(idxCrmCenter, name, permission, startDate, endDate), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/user/test")
	public void test() throws Exception {

		String pwd = "carrot0000!";
		String temp = "";
		String temp1 = "";
		temp = cryptoUtilService.encryptAES256(pwd);
		log.info("##### 암호화 : " + temp);
		log.info("##### 복호화 : " + cryptoUtilService.decryptAES256(temp));
		temp1 = cryptoUtilService.encryptAES256(pwd);
		log.info("##### 암호화 : " + temp1);
		log.info("##### 복호화 : " + cryptoUtilService.decryptAES256(temp1));
		
		log.info("#### result : " + userService.checkEmailForm("yk0138@ccedu.kr"));
		log.info("#### result : " + userService.checkPasswordForm("kuder12345"));
	}

	// 파일 체크 해주는 API
	@PostMapping("/api/v1/user/file-check")
	@ApiOperation(value = "파일 체크 해주는 API", notes = "파일 체크 해주는 API")
	public ResponseEntity<Map<Object, Object>> checkFile(@RequestParam MultipartFile File, @RequestParam("type") String type) throws Exception {

		log.info("###### UserController.checkFile File.empty : " + File.isEmpty() + " type : " + type);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.checkFile(File, type),
				HttpStatus.OK);
		return rtn;
	}

	// 파일 값들 저장/수정/삭제 하는 API
	@PostMapping("/api/v1/user/files")
	@ApiOperation(value = "파일 값들 저장/수정/삭제 하는 API", notes = "파일 값들 저장/수정/삭제 하는 API")
	public ResponseEntity<Map<Object, Object>> uploadUserFile(
			@ApiParam(value = "강사의 IDX 값") @RequestParam("idxCrmStaff") int idxCrmStaff,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "profileFile", required = false) MultipartFile profileFile,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "profileFileUrl", required = false) String profileFileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type", defaultValue = "I") String type,

			@ApiParam(value = "기존의 업로드된 이미지의 IDX값, 수정/삭제시 사용") @RequestParam(name = "idxCertFile1", required = false) String idxCertFile1,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "certiFile1", required = false) MultipartFile certiFile1,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "certiFile1FileUrl", required = false) String certiFile1FileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type1", defaultValue = "I") String type1,

			@ApiParam(value = "기존의 업로드된 이미지의 IDX값, 수정/삭제시 사용") @RequestParam(name = "idxCertFile2", required = false) String idxCertFile2,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "certiFile2", required = false) MultipartFile certiFile2,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "certiFile2FileUrl", required = false) String certiFile2FileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type2", defaultValue = "I") String type2,

			@ApiParam(value = "기존의 업로드된 이미지의 IDX값, 수정/삭제시 사용") @RequestParam(name = "idxCertFile3", required = false) String idxCertFile3,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "certiFile3", required = false) MultipartFile certiFile3,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "certiFile3FileUrl", required = false) String certiFile3FileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type3", defaultValue = "I") String type3,

			@ApiParam(value = "기존의 업로드된 이미지의 IDX값, 수정/삭제시 사용") @RequestParam(name = "idxCertFile4", required = false) String idxCertFile4,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "certiFile4", required = false) MultipartFile certiFile4,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "certiFile4FileUrl", required = false) String certiFile4FileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type4", defaultValue = "I") String type4,

			@ApiParam(value = "기존의 업로드된 이미지의 IDX값, 수정/삭제시 사용") @RequestParam(name = "idxCertFile5", required = false) String idxCertFile5,
			@ApiParam(value = "업로드 파일, 없는경우 입력/수정시 동작안함") @RequestParam(name = "certiFile5", required = false) MultipartFile certiFile5,
			@ApiParam(value = "기존의 업로드된 파일의 전체 URL값, 수정/삭제시 기존 파일 삭제에 사용") @RequestParam(name = "certiFile5FileUrl", required = false) String certiFile5FileUrl,
			@ApiParam(value = "I-신규입력, U-기존파일 교체, D-기존파일/데이터 삭제") @RequestParam(name = "type5", defaultValue = "I") String type5

	) throws Exception {

		log.info("###### UserController.uploadUserFile type : " + type + " type1 : " + type1 + " type2 : " + type2 + " type3 : " + type3 
				+ " type4 : " + type4 + " type5 : " + type5);
		List<UserFileVO> userFileList = new ArrayList<UserFileVO>();
		UserFileVO vo = new UserFileVO();

		// 프로필
		if("D".equals(type)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("P"); //프로파일 이미지이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(profileFileUrl);

			vo.setType(type);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( ("I".equals(type) || "U".equals(type)) && profileFile != null && !profileFile.isEmpty()) {
			//입력이나 업데이트에 대한 처리이다.

			vo.setFileType("P"); //프로파일 이미지이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(profileFileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(profileFile); //필수적으로 있어야 한다.

			vo.setType(type);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}

		// 자격증1, 업데이트, 삭제시에는 idx 값이 꼭 필요하다.
		if("D".equals(type1) && idxCertFile1 != null && !"".equals(idxCertFile1)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);
			vo.setIdx(Integer.parseInt(idxCertFile1));

			vo.setFileUrl(certiFile1FileUrl);

			vo.setType(type1);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "U".equals(type1) && certiFile1 != null && !certiFile1.isEmpty() && idxCertFile1 != null && !"".equals(idxCertFile1)) {
			//입력이나 업데이트에 대한 처리이다.
			vo.setIdx(Integer.parseInt(idxCertFile1));

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile1FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile1); //필수적으로 있어야 한다.

			vo.setType(type1);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "I".equals(type1) && certiFile1 != null && !certiFile1.isEmpty() ) {
			//입력에 대한 처리이다.

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile1FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile1); //필수적으로 있어야 한다.

			vo.setType(type1);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}

		// 자격증2, 업데이트, 삭제시에는 idx 값이 꼭 필요하다.
		if("D".equals(type2) && idxCertFile2 != null && !"".equals(idxCertFile2)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);
			vo.setIdx(Integer.parseInt(idxCertFile2));

			vo.setFileUrl(certiFile2FileUrl);

			vo.setType(type2);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "U".equals(type2) && certiFile2 != null && !certiFile2.isEmpty() && idxCertFile2 != null && !"".equals(idxCertFile2)) {
			//입력이나 업데이트에 대한 처리이다.
			vo.setIdx(Integer.parseInt(idxCertFile2));

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile1FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile2); //필수적으로 있어야 한다.

			vo.setType(type2);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "I".equals(type2) && certiFile2 != null && !certiFile2.isEmpty() ) {
			//입력에 대한 처리이다.

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile2FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile2); //필수적으로 있어야 한다.

			vo.setType(type2);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}

		// 자격증3, 업데이트, 삭제시에는 idx 값이 꼭 필요하다.
		if("D".equals(type3) && idxCertFile3 != null && !"".equals(idxCertFile2)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);
			vo.setIdx(Integer.parseInt(idxCertFile3));

			vo.setFileUrl(certiFile3FileUrl);

			vo.setType(type3);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "U".equals(type3) && certiFile3 != null && !certiFile3.isEmpty() && idxCertFile3 != null && !"".equals(idxCertFile3)) {
			//입력이나 업데이트에 대한 처리이다.
			vo.setIdx(Integer.parseInt(idxCertFile3));

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile3FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile3); //필수적으로 있어야 한다.

			vo.setType(type3);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "I".equals(type3) && certiFile3 != null && !certiFile3.isEmpty() ) {
			//입력에 대한 처리이다.

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile3FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile3); //필수적으로 있어야 한다.

			vo.setType(type3);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}

		// 자격증4, 업데이트, 삭제시에는 idx 값이 꼭 필요하다.
		if("D".equals(type4) && idxCertFile4 != null && !"".equals(idxCertFile4)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);
			vo.setIdx(Integer.parseInt(idxCertFile4));

			vo.setFileUrl(certiFile4FileUrl);

			vo.setType(type4);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "U".equals(type4) && certiFile4 != null && !certiFile4.isEmpty() && idxCertFile4 != null && !"".equals(idxCertFile4)) {
			//입력이나 업데이트에 대한 처리이다.
			vo.setIdx(Integer.parseInt(idxCertFile4));

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile4FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile4); //필수적으로 있어야 한다.

			vo.setType(type4);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "I".equals(type4) && certiFile4 != null && !certiFile4.isEmpty() ) {
			//입력에 대한 처리이다.

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile4FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile4); //필수적으로 있어야 한다.

			vo.setType(type4);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}

		// 자격증5, 업데이트, 삭제시에는 idx 값이 꼭 필요하다.
		if("D".equals(type5) && idxCertFile5 != null && !"".equals(idxCertFile5)){
			//삭제하는 경우에 대한 처리.
			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);
			vo.setIdx(Integer.parseInt(idxCertFile5));

			vo.setFileUrl(certiFile5FileUrl);

			vo.setType(type5);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "U".equals(type5) && certiFile5 != null && !certiFile5.isEmpty() && idxCertFile5 != null && !"".equals(idxCertFile5)) {
			//입력이나 업데이트에 대한 처리이다.
			vo.setIdx(Integer.parseInt(idxCertFile5));

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile5FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile5); //필수적으로 있어야 한다.

			vo.setType(type5);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}
		else if ( "I".equals(type5) && certiFile5 != null && !certiFile5.isEmpty() ) {
			//입력에 대한 처리이다.

			vo.setFileType("C"); //자격증 유형이다.
			vo.setIdxCrmStaff(idxCrmStaff);

			vo.setFileUrl(certiFile5FileUrl); //값이 존재하지 않아도 처리에는 문제없다.
			vo.setFile(certiFile5); //필수적으로 있어야 한다.

			vo.setType(type5);//I-신규입력, U-업데이트(기존파일변경), D-삭제

			userFileList.add(vo);
			vo = new UserFileVO();
		}


		log.info("#### UserFileList : " + userFileList.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
				userService.uploadUserFile(userFileList), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 지출내역 등록하기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/user/expenses")
	@ApiOperation(value = "지출 등록", notes = "지출내역 등록하기")
	public ResponseEntity<Map<Object, Object>> insertExpenses(@RequestBody ExpensesVO vo) throws Exception {
		log.info("#### UserController.insertExpenses vo : " + vo.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.insertExpenses(vo),
				HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/api/v1/user/expenses/{idx}")
	@ApiOperation(value = "지출 조회", notes = "지출내역 상세조회하기")
	public ResponseEntity<Map<Object, Object>> selectExpenses(@PathVariable("idx") int idx) throws Exception {
		log.info("#### UserController.selectExpenses idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectExpenses(idx),
				HttpStatus.OK);
		return rtn;
	}
	
	@PutMapping("/api/v1/user/expenses/{idx}")
	@ApiOperation(value = "지출 수정", notes = "지출내역 수정하기")
	public ResponseEntity<Map<Object, Object>> updateExpenses(@PathVariable("idx") int idx, @RequestBody ExpensesVO vo) throws Exception {
		log.info("#### UserController.updateExpenses idx : " + idx + " vo : " + vo.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.updateExpenses(idx, vo),
				HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/api/v1/user/expenses/list")
	@ApiOperation(value = "지출 검색 목록", notes = "지출내역 검색목록 조회")
	public ResponseEntity<Map<Object, Object>> selectExpensesList(@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="type", required = false, defaultValue = "") String type
			, @RequestParam(name="value", required = false, defaultValue = "") String value
			, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
			, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize
			, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum ) throws Exception {
		log.info("#### UserController.selectExpensesList type + " + type + " idxCrmCenter : " + idxCrmCenter + " value : " + value + " startDate : " + startDate + " endDate : " + endDate + " pageSIze : " + pageSize + " pageNum : " + pageNum );
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectExpensesList(idxCrmCenter, value, type, startDate, endDate, userService.doPaging(pageNum, pageSize), pageSize),
				HttpStatus.OK);
		return rtn;
	}
	
	@DeleteMapping("/api/v1/user/expenses/{idx}")
	@ApiOperation(value = "지출 삭제", notes = "지출내역 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteExpenses(@PathVariable("idx") int idx) throws Exception {
		log.info("#### UserController.deleteExpenses idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.deleteExpenses(idx),
				HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/api/v1/user/address/all")
	@ApiOperation(value = "지역 전체 가져오기", notes = "EAP 등록 시 지역 가져오기")
	public ResponseEntity<Map<Object, Object>> selectAddressAll() throws Exception {
		log.info("#### UserController.selectAddressAll");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectAddressAll(),
				HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/api/v1/user/address")
	@ApiOperation(value = "지역 가져오기", notes = "EAP 등록 시 지역 가져오기")
	public ResponseEntity<Map<Object, Object>> selectAddress(@RequestParam(value="city", required = false, defaultValue = "") String city) throws Exception {
		log.info("#### UserController.selectAddress city : " + city);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectAddress(city),
				HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/user/staff/schedule")
	@ApiOperation(value = "상담사 상담시간 스케쥴 체크", notes = "상담사 예약된 시간 가져오기")
	public ResponseEntity<Map<Object, Object>> selectStaffSchedule(@RequestParam(value="idxCrmStaff", required = false, defaultValue = "-1") int idxCrmStaff
																   , @RequestParam(value="idxCrmClient", required = false, defaultValue = "-1") int idxCrmClient
																   , @RequestParam(value="consDate", required = false, defaultValue = "") String consDate) throws Exception {
		log.info("#### UserController.selectStaffSchedule idxCrmStaff : " + idxCrmStaff + " consDate : " + consDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(userService.selectStaffSchedule(idxCrmStaff, idxCrmClient, consDate),
				HttpStatus.OK);
		return rtn;
	}
	
}
