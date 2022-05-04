package com.carrot.center.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.center.dao.CenterVO;
import com.carrot.client.dao.ClientVO;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.common.service.CryptoUtilService;
import com.carrot.storage.service.ObjectStorageService;
import com.carrot.user.dao.UserVO;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CenterService extends AbstractService {

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private ObjectStorageService objectStorageService;

	@Autowired
	private CryptoUtilService cryptoUtilService;

	public Map<Object, Object> duplicateCenter(String name) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		param.put("name", name);
		sql = "SELECT COUNT(*) FROM crm_center WHERE name = #{param.name}";

		if(commonMapper.selectInt(sql, param) > 0) {
			result.put("err", 105);
			result.put("result", "같은 이름으로 등록된 센터가 있습니다.");
		} else {
			result.put("err", 0);
			result.put("result", "등록 가능한 이름입니다.");
		}

		return result;

	}

	public Map<Object, Object> insertCenter(CenterVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> centerFile1 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> centerFile2 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> centerFile3 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> regFile = new LinkedHashMap<Object, Object>();
		Map<Object, Object> bankFile = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";
		String url = "";
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		String flag1_YN = "Y";
		String flag2_YN = "Y";
		String flag3_YN = "Y";
		String flag4_YN = "Y";
		String flag5_YN = "Y";

		param.put("name", vo.getName());
		sql = "SELECT COUNT(*) FROM crm_center WHERE name = #{param.name}";

		if(commonMapper.selectInt(sql, param) > 0) {
			result.put("err", 105);
			result.put("result", "같은 이름으로 등록된 센터가 있습니다.");
		} else {

			// 센터사진
			if(!vo.getCenterFile1().isEmpty()) {
				centerFile1 = checkFile(vo.getCenterFile1(), "P");

				if(Integer.parseInt(centerFile1.get("err").toString()) == 0) {
					flag1 = true;
				} else {
					result.put("err", 210);
					result.put("result", "사진 파일 크기가 너무 큽니다.");
					flag1 = false;
				}
			} else {
				flag1_YN = "N";
			}

			if(!vo.getCenterFile2().isEmpty()) {
				centerFile2 = checkFile(vo.getCenterFile2(), "P");

				if(Integer.parseInt(centerFile2.get("err").toString()) == 0) {
					flag2 = true;
				} else {
					result.put("err", 210);
					result.put("result", "사진 파일 크기가 너무 큽니다.");
					flag2 = false;
				}
			} else {
				flag2_YN = "N";
			}

			if(!vo.getCenterFile3().isEmpty()) {
				centerFile3 = checkFile(vo.getCenterFile3(), "P");

				if(Integer.parseInt(centerFile3.get("err").toString()) == 0) {
					flag3 = true;
				} else {
					result.put("err", 210);
					result.put("result", "사진 파일 크기가 너무 큽니다.");
					flag3 = false;
				}
			} else {
				flag3_YN = "N";
			}

			// 사업자등록증
			if(!vo.getRegFile().isEmpty()) {
				regFile = checkFile(vo.getRegFile(), "B");

				if(Integer.parseInt(regFile.get("err").toString()) == 0) {
					flag4 = true;
				} else {
					result.put("err", 210);
					result.put("result", "사진 파일 크기가 너무 큽니다.");
					flag4 = false;
				}
			} else {
				flag4_YN = "N";
			}

			// 통장사본
			if(!vo.getBankFile().isEmpty()) {
				bankFile = checkFile(vo.getBankFile(), "B");

				if(Integer.parseInt(bankFile.get("err").toString()) == 0) {
					flag5 = true;
				} else {
					result.put("err", 210);
					result.put("result", "사진 파일 크기가 너무 큽니다.");
					flag5 = false;
				}
			} else {
				flag5_YN = "N";
			}

			if(flag1 == true && flag2 == true && flag3 == true && flag4 == true && flag5 == true) {

				if("Y".equals(flag1_YN)) {
					vo.setCenterPhotoName1(centerFile1.get("originalFileName").toString());
					url = objectStorageService.uploadObject(vo.getCenterFile1(), centerFile1.get("storedFileName").toString(), "crm", "center_photo");
					vo.setCenterPhotoUrl1(url);
				}

				if("Y".equals(flag2_YN)) {
					vo.setCenterPhotoName2(centerFile2.get("originalFileName").toString());
					url = objectStorageService.uploadObject(vo.getCenterFile2(), centerFile2.get("storedFileName").toString(), "crm", "center_photo");
					vo.setCenterPhotoUrl2(url);
				}

				if("Y".equals(flag3_YN)) {
					vo.setCenterPhotoName3(centerFile3.get("originalFileName").toString());
					url = objectStorageService.uploadObject(vo.getCenterFile3(), centerFile3.get("storedFileName").toString(), "crm", "center_photo");
					vo.setCenterPhotoUrl3(url);
				}

				if("Y".equals(flag4_YN)) {
					vo.setCenterRegName(regFile.get("originalFileName").toString());
					url = objectStorageService.uploadObject(vo.getRegFile(), regFile.get("storedFileName").toString(), "crm", "center_reg");
					vo.setCenterRegUrl(url);
				}

				if("Y".equals(flag5_YN)) {
					vo.setCenterBankName(bankFile.get("originalFileName").toString());
					url = objectStorageService.uploadObject(vo.getBankFile(), bankFile.get("storedFileName").toString(), "crm", "center_bank");
					vo.setCenterBankUrl(url);
				}

			}

			commonMapper.insertCenter(vo);

			// 협약센터용 계정 등록(센터장?)
			UserVO userVO = new UserVO();
			userVO.setId(vo.getId()); // 센터장 ID
			userVO.setName(vo.getCenterCap()); // 센터장 이름
			userVO.setPwd(cryptoUtilService.encryptAES256(vo.getPwd())); // 센터장 비번
			userVO.setAuthority("CENTERDIRECTOR"); // 권한명
			userVO.setIdxCrmCenter(vo.getIdx());
			commonMapper.insertUser(userVO);
			result.put("err", 0);
			result.put("result", "센터가 등록되었습니다.");
		}
		return result;
	}

	public Map<Object, Object> selectCenter(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		CenterVO center = new CenterVO();
		Map<Object, Object> director = new HashMap<Object, Object>();
		String sql = "";

		param.put("idx",  idx);
		center = commonMapper.selectCenter(param);

		param.clear();
		param.put("idxCrmCenter", idx);
		param.put("authority", "CENTERDIRECTOR");
		sql = "SELECT idx, id, pwd, name FROM crm_staff WHERE idx_crm_center = #{param.idxCrmCenter} AND authority = #{param.authority}";

		director = commonMapper.selectOne(sql, param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("center", center);
		result.put("idxCenterDirector", director.get("idx"));
		result.put("id", director.get("id"));
		result.put("pwd", cryptoUtilService.decryptAES256(director.get("pwd").toString()));

		return result;
	}

	public Map<Object, Object> updateCenter(int idx, CenterVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<Object, Object> centerFile1 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> centerFile2 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> centerFile3 = new LinkedHashMap<Object, Object>();
		Map<Object, Object> regFile = new LinkedHashMap<Object, Object>();
		Map<Object, Object> bankFile = new LinkedHashMap<Object, Object>();
		String url = "";
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		String flag1_YN = "Y";
		String flag2_YN = "Y";
		String flag3_YN = "Y";
		String flag4_YN = "Y";
		String flag5_YN = "Y";

		param.put("idx", idx);

		// 센터사진
		if(!vo.getCenterFile1().isEmpty()) {
			centerFile1 = checkFile(vo.getCenterFile1(), "P");

			if(Integer.parseInt(centerFile1.get("err").toString()) == 0) {
				flag1 = true;
			} else {
				result.put("err", 210);
				result.put("result", "사진 파일 크기가 너무 큽니다.");
				flag1 = false;
			}
		} else {
			flag1_YN = "N";
		}

		if(!vo.getCenterFile2().isEmpty()) {
			centerFile2 = checkFile(vo.getCenterFile2(), "P");

			if(Integer.parseInt(centerFile2.get("err").toString()) == 0) {
				flag2 = true;
			} else {
				result.put("err", 210);
				result.put("result", "사진 파일 크기가 너무 큽니다.");
				flag2 = false;
			}
		} else {
			flag2_YN = "N";
		}

		if(!vo.getCenterFile3().isEmpty()) {
			centerFile3 = checkFile(vo.getCenterFile3(), "P");

			if(Integer.parseInt(centerFile3.get("err").toString()) == 0) {
				flag3 = true;
			} else {
				result.put("err", 210);
				result.put("result", "사진 파일 크기가 너무 큽니다.");
				flag3 = false;
			}
		} else {
			flag3_YN = "N";
		}

		// 사업자등록증
		if(!vo.getRegFile().isEmpty()) {
			regFile = checkFile(vo.getRegFile(), "B");

			if(Integer.parseInt(regFile.get("err").toString()) == 0) {
				flag4 = true;
			} else {
				result.put("err", 210);
				result.put("result", "사진 파일 크기가 너무 큽니다.");
				flag4 = false;
			}
		} else {
			flag4_YN = "N";
		}

		// 통장사본
		if(!vo.getBankFile().isEmpty()) {
			bankFile = checkFile(vo.getBankFile(), "B");

			if(Integer.parseInt(bankFile.get("err").toString()) == 0) {
				flag5 = true;
			} else {
				result.put("err", 210);
				result.put("result", "사진 파일 크기가 너무 큽니다.");
				flag5 = false;
			}
		} else {
			flag5_YN = "N";
		}

		if(flag1 == true && flag2 == true && flag3 == true && flag4 == true && flag5 == true) {

			if("Y".equals(flag1_YN)) {
				vo.setCenterPhotoName1(centerFile1.get("originalFileName").toString());
				url = objectStorageService.uploadObject(vo.getCenterFile1(), centerFile1.get("storedFileName").toString(), "crm", "center_photo");
				vo.setCenterPhotoUrl1(url);
			}

			if("Y".equals(flag2_YN)) {
				vo.setCenterPhotoName2(centerFile2.get("originalFileName").toString());
				url = objectStorageService.uploadObject(vo.getCenterFile2(), centerFile2.get("storedFileName").toString(), "crm", "center_photo");
				vo.setCenterPhotoUrl2(url);
			}

			if("Y".equals(flag3_YN)) {
				vo.setCenterPhotoName3(centerFile3.get("originalFileName").toString());
				url = objectStorageService.uploadObject(vo.getCenterFile3(), centerFile3.get("storedFileName").toString(), "crm", "center_photo");
				vo.setCenterPhotoUrl3(url);
			}

			if("Y".equals(flag4_YN)) {
				vo.setCenterRegName(regFile.get("originalFileName").toString());
				url = objectStorageService.uploadObject(vo.getRegFile(), regFile.get("storedFileName").toString(), "crm", "center_reg");
				vo.setCenterRegUrl(url);
			}

			if("Y".equals(flag5_YN)) {
				vo.setCenterBankName(bankFile.get("originalFileName").toString());
				url = objectStorageService.uploadObject(vo.getBankFile(), bankFile.get("storedFileName").toString(), "crm", "center_bank");
				vo.setCenterBankUrl(url);
			}

		}

		vo.setIdx(idx);
		commonMapper.updateCenter(vo);

		// 협약센터용 계정 수정(센터장?)
		data.put("name", vo.getCenterCap()); // 센터장 이름
		data.put("pwd", cryptoUtilService.encryptAES256(vo.getPwd())); // 센터장 비번
		param.clear();
		param.put("authority", "CENTERDIRECTOR"); // 권한명
		param.put("idxCrmCenter", vo.getIdx());

		commonMapper.update("crm_staff", data, " idx_crm_center = #{param.idxCrmCenter} AND authority = #{param.authority}", param);

		result.put("err", 0);
		result.put("result", "OK");
		return result;
	}

	public Map<Object, Object> selectCenterList(String name, String permission, String startDate, String endDate,  int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<CenterVO> centerList = new ArrayList<CenterVO>();

		param.put("name", name);
		param.put("permission", permission);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);

		//if(startPage == 0) {
			result.put("centerListCount", commonMapper.selectCenterListCount(param));
		//}

		centerList = commonMapper.selectCenterList(param);

		log.info("##### centerList : " + centerList);

		if(centerList.size() > 0) {

			result.put("err", 0);
			result.put("result", "OK");

		} else {

			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("centerList", centerList);

		return result;
	}

	public Map<Object, Object> selectCenterAll() throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<CenterVO> centerList = new ArrayList<CenterVO>();

		centerList = commonMapper.selectCenterAll();

		result.put("err", 0);
		result.put("result", "OK");
		result.put("centerList", centerList);

		return result;
	}

	public Map<Object, Object> deleteCenter(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> center = new LinkedHashMap<Object, Object>();
		Map<Object, Object> staff = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";

		param.put("idx", idx);
		data.put("contract_end_yn", "Y");
		sql = "SELECT name FROM crm_center WHERE idx = #{param.idx}";
		center = commonMapper.selectOne(sql, param);

		commonMapper.update("crm_center", data, " idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", center.get("name") + "을 삭제 했습니다.");

		return result;
	}

	public Map<Object, Object> selectCenterStaffList(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> staffList = new ArrayList<Map<Object,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";

		if(idx != -1) {
			param.put("idx", idx);
			sql = "SELECT idx, name  FROM crm_staff WHERE idx_crm_center = #{param.idx} AND permission = 'Y' AND authority = 'STAFF'";
		} else {
			sql = "SELECT idx, name FROM crm_staff WHERE permission = 'Y' AND authority = 'STAFF'";
		}
		staffList = commonMapper.selectAll(sql, param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("staffList", staffList);

		return result;
	}

	// 신규상담신청 목록(협약센터)
	public Map<Object, Object> selectCenterEapList(int idxCrmCenter, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ClientVO> eapList = new ArrayList<ClientVO>();

		param.put("idxCrmCenter", idxCrmCenter);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);

		eapList = commonMapper.selectCenterEapList(param);

		//if(startPage == 0) {
			result.put("eapListSize", commonMapper.selectCenterEapListCount(param));
		//}

		if(eapList.size() >0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "신청 내역이 없습니다.");
		}
		result.put("eapList", eapList);

		return result;
	}

	public Map<Object, Object> insertCrmEap(int idxCrmClient, int idxCrmCenter) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("idx_crm_client", idxCrmClient);
		data.put("idx_crm_center", idxCrmCenter);
		data.put("status", "센터접수");

		commonMapper.insert("crm_eap", data);

		result.put("err", 0);
		result.put("result", "신청되었습니다.");

		return result;

	}

}
