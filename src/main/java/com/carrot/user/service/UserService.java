package com.carrot.user.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.carrot.cons.dao.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.admin.dao.ExpensesVO;
import com.carrot.common.CommonEnum;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.common.service.CryptoUtilService;
import com.carrot.cons.dao.ReportVO;
import com.carrot.storage.service.ObjectStorageService;
import com.carrot.user.dao.UserFileVO;
import com.carrot.user.dao.UserVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserService extends AbstractService {

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private ObjectStorageService objectStorageService;

	@Autowired
	private CryptoUtilService cryptoUtilService;

	/**
	 * ID 중복체크
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> duplicatUser(String id) throws Exception {

		log.info("#### duplicatUser id : " + id);
		String sql ="";
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		sql = "SELECT count(*) FROM crm_staff WHERE id = #{param.id}";
		param.put("id", id);

		int count = commonMapper.selectInt(sql, param);

		if(count < 1) {
			result.put("err", 0);
			result.put("result", "사용할 수 있는 ID입니다.");
		} else {
			result.put("err", 100);
			result.put("result", "이미 사용중인 ID 입니다");
		}

		return result;
	}

	public Map<Object, Object> uploadUserFile(List<UserFileVO> fileList) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> file = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		String url = "";
		// crm_staff 에 profile 정보 insert 해야 되니까 update 해줌
		// crm_certificate 에 certiFile 정보 insert

		for(int i=0;i<fileList.size();i++) {

			file.clear();
			param.clear();
			data.clear();
			log.info("##### fileList.get(i).getType() : " + fileList.get(i).getType());

			if(!"".equals(fileList.get(i).getType()) && !"D".equals(fileList.get(i).getType())) {
				file = checkFile(fileList.get(i).getFile(), fileList.get(i).getFileType());
				fileList.get(i).setFileName(file.get("originalFileName").toString());
			}

			if("P".equals(fileList.get(i).getFileType())) {							// 프로필 사진

				if("I".equals(fileList.get(i).getType())) {

					// 사진 저장

					//파일명 재설정
					String extension = StringUtils.getFilenameExtension(fileList.get(i).getFile().getOriginalFilename());
					String storedFileName = new SimpleDateFormat("yyyyMMddhhmmsszzzyyyy").format(new Date());
					storedFileName = storedFileName + "." + extension;

					url = objectStorageService.uploadObject(fileList.get(i).getFile(), storedFileName, "crm", "profile_photo");

					fileList.get(i).setFileUrl(url);
					param.put("idx", fileList.get(i).getIdxCrmStaff());
					data.put("photo_name", fileList.get(i).getFile().getOriginalFilename());
					data.put("photo_url", url);

					commonMapper.update("crm_staff", data, " idx = #{param.idx}", param);
					fileList.get(i).setFile(null);
				}
				else if ("U".equals(fileList.get(i).getType())) {
					//기존의 파일을 삭제하고, 새로 입력해준다.
					//fileUrl 에 업로드된 주소값이 존재해야 한다.

					//오브젝틑 스토리지의 기존 파일을 삭제한다.
					if(fileList.get(i).getFileUrl() != null && !"".equals(fileList.get(i).getFileUrl())){
						String prevObjectName = objectStorageService.replaceObjectName("crm", fileList.get(i).getFileUrl());
						objectStorageService.deleteObject("crm", prevObjectName);
					}

					//파일명 재설정
					String extension = StringUtils.getFilenameExtension(fileList.get(i).getFile().getOriginalFilename());
					String storedFileName = new SimpleDateFormat("yyyyMMddhhmmsszzzyyyy").format(new Date());
					storedFileName = storedFileName + "." + extension;

					//오브젝트 스토리지에 새로운 파일을 업로드한다.
					url = objectStorageService.uploadObject(fileList.get(i).getFile(), storedFileName, "crm", "profile_photo");

					fileList.get(i).setFileUrl(url);
					param.put("idx", fileList.get(i).getIdxCrmStaff());
					data.put("photo_name", fileList.get(i).getFile().getOriginalFilename());
					data.put("photo_url", url);
					commonMapper.update("crm_staff", data, " idx = #{param.idx}", param);

				}
				else if ("D".equals(fileList.get(i).getType())) {
					//오브젝틑 스토리지의 기존 파일을 삭제한다.
					//fileUrl 값이 존재해야 오브젝트 스토리지의 파일 삭제가 가능하다.
					//오브젝틑 스토리지의 기존 파일을 삭제한다.
					if(fileList.get(i).getFileUrl() != null && !"".equals(fileList.get(i).getFileUrl())){
						String prevObjectName = objectStorageService.replaceObjectName("crm", fileList.get(i).getFileUrl());
						objectStorageService.deleteObject("crm", prevObjectName);
					}

					param.put("idx", fileList.get(i).getIdxCrmStaff());
					data.put("photo_name", "");
					data.put("photo_url", "");
					commonMapper.update("crm_staff", data, " idx = #{param.idx}", param);

				}

			} else { 																		// 자격증 사진

				if("I".equals(fileList.get(i).getType())) {

					url = objectStorageService.uploadObject(fileList.get(i).getFile(), file.get("storedFileName").toString(), "crm", "certificate");
					fileList.get(i).setFileUrl(url);
					commonMapper.insertCert(fileList.get(i));

				} else if ("U".equals(fileList.get(i).getType())) {

					url = objectStorageService.uploadObject(fileList.get(i).getFile(), file.get("storedFileName").toString(), "crm", "certificate");
					fileList.get(i).setFileUrl(url);
					commonMapper.updateCert(fileList.get(i));

				} else if ("D".equals(fileList.get(i).getType())) {

					param.put("idx", fileList.get(i).getIdx());
					commonMapper.delete("crm_certificate", param);
					// 오브젝트에서 삭제 하는것도 넣어야 하려나..~?
				}
			}
			//전달받은 파일 바이너리 데이터는 삭제한다.
			fileList.get(i).setFile(null);
		}

		result.put("err", 0);
		result.put("result", "OK");
		result.put("fileList", fileList); //저장된 파일들의 목록을 다시 리턴해준다.

		return result;
	}

	public Map<Object, Object> selectUserAll() throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<Object, Object>> userList = new ArrayList<Map<Object,Object>>();
		String sql = "";

		sql = "SELECT" +
				"	a.idx," +
				"	a.name," +
				"	CONCAT(a.name, '(', a.center_name, ')') as nick_name," +
				"	a.idx_crm_center," +
				"	a.center_name" +
				"FROM" +
				"	(" +
				"	SELECT" +
				"		idx," +
				"		name," +
				"		idx_crm_center," +
				"		(" +
				"		SELECT" +
				"			name" +
				"		FROM" +
				"			crm_center" +
				"		WHERE" +
				"			idx = idx_crm_center) as center_name" +
				"	FROM" +
				"		crm_staff ) a";

		userList = commonMapper.selectAll(sql, param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("userList", userList);

		return result;

	}
	public Map<Object, Object> insertUser(UserVO vo) throws Exception {

		log.info("#### insertUser vo : " + vo);

		Map<Object, Object> result = new HashMap<Object, Object>();
		UserVO user = new UserVO();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		int status = 0;

		if(Integer.parseInt(duplicatUser(vo.getId()).get("err").toString()) == 0) {

			if(checkIdForm(vo.getId())) {
				if(checkPasswordForm(vo.getPwd())) {
					vo.setPwd(cryptoUtilService.encryptAES256(vo.getPwd())); // 암호화 하여 비밀번호 넣기

					status = commonMapper.insertUser(vo); // staff 등록

					// 근무 시간 테이블에 insert
					if(vo.getWorkTimeList() != null) {
						for(int i=0;i<vo.getWorkTimeList().size();i++) {
							data.put("idx_crm_staff", vo.getIdx());
							data.put("day", vo.getWorkTimeList().get(i).getDay());
							data.put("time", vo.getWorkTimeList().get(i).getTime());
							commonMapper.insert("crm_staff_worktime", data);
							data.clear();
						}
					}

					result.put("err", 0);
					result.put("result", "staff 등록 완료");
					result.put("status", status);

					param.clear();
					param.put("idx", vo.getIdx());
					user = commonMapper.selectUser(param);
					result.put("err", 0);
					result.put("userVO", user);

				} else {
					result.put("err", 150);
					result.put("result", "비밀번호 형식이 맞지 않습니다.");
				}
			} else {
				result.put("err", 150);
				result.put("result", "아이디 형식이 맞지 않습니다.");
			}
		} else {
			result.put("err", 100);
			result.put("result", "중복된 ID 입니다.");
		}

        return result;
	}

	public Map<Object, Object> selectUser(int idx, String reportYn) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> certiList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> workTimeList = new ArrayList<Map<Object,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReportVO> reportList = new ArrayList<ReportVO>();
		UserVO userVO = new UserVO();
		String sql = "";

		param.put("idx", idx);

		userVO = commonMapper.selectUser(param);
		userVO.setPwd(cryptoUtilService.decryptAES256(userVO.getPwd()));
		sql = "SELECT * FROM crm_certificate WHERE idx_crm_staff = #{param.idx}";

		certiList = commonMapper.selectAll(sql, param);

		sql = "SELECT * FROM crm_staff_worktime WHERE idx_crm_staff = #{param.idx}";
		workTimeList = commonMapper.selectAll(sql, param);

		param.clear();
		param.put("idxCrmStaff", idx);
		param.put("startPage", -1);

		if("Y".equals(reportYn)) {
			reportYn = "등록";
		} else if("N".equals(reportYn)) {
			reportYn = "미등록";
		}
		param.put("reportYn", reportYn);

		reportList = commonMapper.selectReportList(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("userVO", userVO);
		result.put("certiList", certiList);
		result.put("workTimeList", workTimeList);
		result.put("reportList", reportList);

		return result;

	}

	public Map<Object, Object> updateUser(UserVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		UserVO user = new UserVO();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		int status = 0;

		if(checkIdForm(vo.getId())) {

			if(checkPasswordForm(vo.getPwd())) {
				vo.setPwd(cryptoUtilService.encryptAES256(vo.getPwd())); // 암호화 하여 비밀번호 넣기

				status = commonMapper.updateUser(vo); // staff 등록

				// 근무 시간 테이블에 insert
				if(vo.getWorkTimeList() != null) {

					param.clear();
					param.put("idx_crm_staff", vo.getIdx());
					commonMapper.delete("crm_staff_worktime", param);

					for(int i=0;i<vo.getWorkTimeList().size();i++) {
						data.put("idx_crm_staff", vo.getIdx());
						data.put("day", vo.getWorkTimeList().get(i).getDay());
						data.put("time", vo.getWorkTimeList().get(i).getTime());

						commonMapper.insert("crm_staff_worktime", data);
						data.clear();
					}
				}

				result.put("err", 0);
				result.put("result", "staff 등록 완료");
				result.put("status", status);

				param.clear();
				param.put("idx", vo.getIdx());
				user = commonMapper.selectUser(param);
				result.put("err", 0);
				result.put("user", user);
			} else {
				result.put("err", 150);
				result.put("result", "비밀번호 형식이 맞지 않습니다.");
			}
		} else {
			result.put("err", 150);
			result.put("result", "이메일 형식이 맞지 않습니다.");
		}

		return result;
	}

	public Map<Object, Object> deleteUser(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> user = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		String sql = "";

		param.put("idx", idx);
		sql = "SELECT id, authority FROM crm_staff WHERE idx = #{param.idx}";

		user = commonMapper.selectOne(sql, param);

		commonMapper.delete("crm_staff", param);
		param.put("idx_crm_staff", idx);
		commonMapper.delete("crm_certificate", param);
		/* 계정삭제로그 부분 제거
		data.put("idx_delete", idx);
		data.put("id", user.get("id"));
		data.put("authority", user.get("authority"));
		commonMapper.insert("crm_account_delete_log", data);
		*/
		result.put("err", 0);
		result.put("result", user.get("id") + " 계정이 삭제 되었습니다.");
		return result;

	}

	public Map<Object, Object> selectUserList(int idxCrmCenter, String name, String permission, String startDate, String endDate, int startPage, int pageSize) throws Exception {

		String sql ="";
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		int userListCount = 0;

		param.put("permission", permission);
        param.put("idxCrmCenter", idxCrmCenter);
        param.put("name", name);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);
        List<UserVO> userList = new ArrayList<UserVO>();

        userList = commonMapper.selectUserList(param);

        log.info("#### userList : " + userList.toString());

        if(userList.size() > 0) {
        	result.put("err", 0);
    		result.put("result", "OK");
        } else {
        	result.put("err", 105);
    		result.put("result", "검색 결과가 없습니다.");
        }

        result.put("idxCrmCenter", idxCrmCenter);
        result.put("userList", userList);
        //if(startPage == 0) {
        	userListCount = commonMapper.selectUserListCount(param);
   			result.put("userListCount", userListCount);
        //}

		return result;

	}

	public Map<Object, Object> selectUserListAll(int idxCrmCenter, String name, String permission, String startDate, String endDate) throws Exception {

		String sql ="";
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		int userListCount = 0;

		param.put("permission", permission);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("name", name);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startPage", -1);
		List<UserVO> userList = new ArrayList<UserVO>();

		userList = commonMapper.selectUserList(param);

		log.info("#### userList : " + userList.toString());

		if(userList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("idxCrmCenter", idxCrmCenter);
		result.put("userList", userList);
		//if(startPage == 0) {
		userListCount = commonMapper.selectUserListCount(param);
		result.put("userListCount", userListCount);
		//}

		return result;

	}

	public Map<Object, Object> insertExpenses(ExpensesVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		if(vo.getExpensesCompanyName() == null || "".equals(vo.getExpensesCompanyName())) {
			vo.setExpensesType("B2C");
		} else {
			vo.setExpensesType("B2B");
		}
		commonMapper.insertExpenses(vo);

		result.put("err", 0);
		result.put("result", "등록 되었습니다.");

		return result;

	}

	public Map<Object, Object> selectExpenses(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		ExpensesVO expVO = new ExpensesVO();

		param.put("idx", idx);
		expVO = commonMapper.selectExpenses(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("expVO", expVO);

		return result;

	}

	public Map<Object, Object> updateExpenses(int idx, ExpensesVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		vo.setIdx(idx);
		commonMapper.updateExpenses(vo);
		result.put("err", 0);
		result.put("result", "수정 되었습니다.");

		return result;

	}

	public Map<Object, Object> selectExpensesList(int idxCrmCenter, String value, String type, String startDate, String endDate, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

        param.put("idxCrmCenter", idxCrmCenter);
        param.put("value", value);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);
        List<ExpensesVO> expList = new ArrayList<ExpensesVO>();

        if("B2C".equals(type)) {
        	expList = commonMapper.selectExpensesB2CList(param);

        	log.info("#### expList : " + expList.toString());

            if(expList.size() > 0) {
            	result.put("err", 0);
        		result.put("result", "OK");
            } else {
            	result.put("err", 105);
        		result.put("result", "검색 결과가 없습니다.");
            }

        	//if(startPage == 0) {

       			result.put("expListCount", commonMapper.selectExpensesB2CListCount(param));
            //}
        } else {
        	expList = commonMapper.selectExpensesB2BList(param);

        	log.info("#### expList : " + expList.toString());

            if(expList.size() > 0) {
            	result.put("err", 0);
        		result.put("result", "OK");
            } else {
            	result.put("err", 105);
        		result.put("result", "검색 결과가 없습니다.");
            }

        	//if(startPage == 0) {

       			result.put("expListCount", commonMapper.selectExpensesB2BListCount(param));
            //}
        }

        result.put("expList", expList);

		return result;

	}

	public Map<Object, Object> deleteExpenses(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idx", idx);

		commonMapper.delete("crm_expenses", param);
		result.put("err", 0);
		result.put("result", "삭제 되었습니다.");

		return result;

	}

	public Map<Object, Object> selectAddressAll() throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> cityList = new ArrayList<Map<Object, Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		sql = "SELECT DISTINCT city, city2 FROM crm_address";
		cityList = commonMapper.selectAll(sql);

		result.put("cityList", cityList);
		result.put("err", 0);
		result.put("result", "OK");

		return result;

	}

	public Map<Object, Object> selectAddress(String city) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> countyList = new ArrayList<Map<Object, Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		param.put("city", city);
		sql = "SELECT county FROM crm_address WHERE city2 = #{param.city} ORDER BY county";

		countyList = commonMapper.selectAll(sql, param);

		result.put("countyList", countyList);
		result.put("err", 0);
		result.put("result", "OK");

		return result;

	}

	public Map<Object, Object> selectStaffSchedule(int idxCrmStaff, int idxCrmClient, String consDate) {

		Map<Object, Object> result = new LinkedHashMap<>();
		Map<String, Object> param = new HashMap<>();
		List<ReservationVO> schedule = new ArrayList<>();
		Map<Object, Object> workTime = new LinkedHashMap<>();
		String sql = "";

		param.put("idxCrmClient", idxCrmClient);
		param.put("idxCrmStaff", idxCrmStaff);
		param.put("consDate", consDate);

		schedule = commonMapper.selectStaffSchedule(param);

		sql = " select\r\n" +
				"	idx_crm_staff,\r\n" +
				"	day,\r\n" +
				"	GROUP_CONCAT(time) as work_time\r\n" +
				"from\r\n" +
				"	crm_staff_worktime csw\r\n" +
				"where\r\n" +
				"	idx_crm_staff = #{param.idxCrmStaff}\r\n" +
				"	and day = (\r\n" +
				"	SELECT\r\n" +
				"		CASE\r\n" +
				"			DAYOFWEEK(#{param.consDate})\r\n" +
				"			WHEN '1' THEN '일'\r\n" +
				"			WHEN '2' THEN '월'\r\n" +
				"			WHEN '3' THEN '화'\r\n" +
				"			WHEN '4' THEN '수'\r\n" +
				"			WHEN '5' THEN '목'\r\n" +
				"			WHEN '6' THEN '금'\r\n" +
				"			WHEN '7' THEN '토' END )\r\n" +
				"GROUP BY\r\n" +
				"	day";

		workTime = commonMapper.selectOne(sql, param);

		result.put("err", 0);
		result.put("schedule", schedule);
		result.put("workTime", workTime);

		return result;
	}

}
