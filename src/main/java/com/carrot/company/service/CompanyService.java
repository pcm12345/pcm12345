package com.carrot.company.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.company.dao.CompanyVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyService extends AbstractService {

	@Autowired
	private CommonMapper commonMapper;

	// 기업 전체조회
	public Map<Object, Object> selectCompanyAll() throws Exception {

		Map<Object, Object> result = new HashMap<Object, Object>();
		List<Map<Object, Object>> companyList = new ArrayList<Map<Object,Object>>();
		String sql= "";

		sql = "SELECT * FROM crm_company ORDER BY name ASC";
		companyList = commonMapper.selectAll(sql);

		if(companyList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "등록된 회사가 없습니다.");
		}
		result.put("companyList", companyList);

		return result;
	}

	// 기업등록
	public Map<Object, Object> insertCompany(CompanyVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		param.put("name", vo.getName());
		vo.setCompanyCode(validCompanyCode(vo.getCompanyCode()));
		// 기업명 중복 체크
		sql = "SELECT COUNT(*) FROM crm_company WHERE name = #{param.name}";

		if(commonMapper.selectInt(sql, param) > 0) {
			log.info("#### code : " + vo.getName());
			result.put("err", 100);
			result.put("result", "같은 이름으로 등록된 기업이 있습니다.");
		} else {
			int status = commonMapper.insertCompany(vo);
			result.put("status", status);
			result.put("err", 0);
			result.put("result", "기업등록이 완료되었습니다.");
		}

		return result;

	}

	// 기업수정
	public Map<Object, Object> updateCompany(int idx, CompanyVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> company = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		vo.setIdx(idx);

		String sql = "";
		param.put("idx", idx);
		sql = "SELECT * FROM crm_company WHERE idx = #{param.idx}";
		company = commonMapper.selectOne(sql, param);

		vo.setCompanyCode(validCompanyCode(vo.getCompanyCode()));

		commonMapper.updateCompany(vo);
		result.put("err", 0);
		result.put("result", "수정 되었습니다.");

		return result;

	}

	// 기업조회
	public Map<Object, Object> selectCompany(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> company = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		param.put("idx", idx);
		sql = "SELECT * FROM crm_company WHERE idx = #{param.idx}";
		company = commonMapper.selectOne(sql, param);

		if(company != null) {
			result.put("err", 0);
			result.put("result", "OK");
			result.put("company", company);
		} else {
			result.put("err", 105);
			result.put("result", "기업이 없습니다.");
			result.put("company", company);
		}

		return result;

	}
	private String validCompanyCode(String companyCode){
		//TRIM
		//UpperCase
		//Eng+Num

		companyCode = companyCode.trim();
		companyCode = companyCode.toUpperCase();
		companyCode = companyCode.replaceAll("[^0-9A-Z]", "");

		return companyCode;
	}
	// 기업코드 중복검색
	public Map<Object, Object> checkCompanyCode(String company_code) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Integer codeCount = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";
		company_code = validCompanyCode(company_code);

		param.put("company_code", company_code);
		if("".equals(company_code)){
			result.put("err", 106);
			result.put("result", "잘못된 형식입니다.");
			result.put("code", company_code);
			result.put("company", codeCount);
			return result;
		}

		sql = "SELECT count(*) FROM crm_company WHERE company_code = #{param.company_code}";
		codeCount = commonMapper.selectInt(sql, param);

		if(codeCount == 0) {
			result.put("err", 0);
			result.put("result", "OK");
			result.put("code", company_code);
			result.put("codeCount", codeCount);
		} else {
			result.put("err", 105);
			result.put("result", "등록된 코드가 존재합니다.");
			result.put("code", company_code);
			result.put("codeCount", codeCount);
		}

		return result;

	}

	// 기업목록 검색
	public Map<Object, Object> selectCompanyList(String value, int startPage, int pageSize, HttpStatus ok) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<CompanyVO> companyList = new ArrayList<CompanyVO>();

		param.put("value", value);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);

		companyList = commonMapper.selectCompanyList(param);

		log.info("#### companyList : " + companyList.toString());

		//if(startPage == 0) {
			result.put("companySize", commonMapper.selectCompanyListCount(param));

		//}

		if(companyList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("companyList", companyList);


		return result;

	}

	// 삭제
	public Map<Object, Object> deleteCompany(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> company = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		sql = "SELECT * FROM crm_company WHERE idx = #{param.idx}";
		param.put("idx", idx);

		company = commonMapper.selectOne(sql, param);

		commonMapper.delete("crm_company", param);

		result.put("err", 0);
		result.put("result", company.get("name") + "가 삭제 되었습니다.");
		return result;

	}

}
