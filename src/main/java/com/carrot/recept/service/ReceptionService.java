package com.carrot.recept.service;

import com.carrot.client.dao.ClientVO;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.recept.dao.ReceptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ReceptionService extends AbstractService {

    @Autowired
    private CommonMapper commonMapper;

    public Map<Object, Object> insertReception(ReceptionVO vo) {
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        String sql = "";

        commonMapper.insertReception(vo);

        result.put("err", 0);
        result.put("result", "전화문의가 등록되었습니다.");
        result.put("idx_reception", vo.getIdx());


        return result;

    }

    public Map<Object, Object> updateReception(ReceptionVO vo) throws Exception {

        Map<Object, Object> result = new LinkedHashMap<Object, Object>();

        // 기존 상담예약 값에 따라 회기를 자동으로 추가/차감처리한다.
        Map<String, Object> param = new HashMap<String, Object>();

        if(vo.getIdx() > 0) {
            commonMapper.updateReception(vo);
            result.put("err", 0);
            result.put("result", "전화문의 수정에 성공하였습니다.");
        }else{
            result.put("err", -1);
            result.put("result", "전화문의 수정에 실패하였습니다.");
        }

        return result;
    }

    public Map<Object, Object> deleteReception(int idx) throws Exception {

        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("idx", idx);

        commonMapper.deleteReception(param);

        result.put("err", 0);
        result.put("result", "상담예약이 삭제 되었습니다");

        return result;
    }

    public Map<Object, Object> selectReception(int idx) throws Exception {

        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        ReceptionVO resVO = new ReceptionVO();
        param.put("idx", idx);
        resVO = commonMapper.selectReception(param);

        result.put("err", 0);
        result.put("result", "OK");
        result.put("resVO", resVO);

        return result;
    }

    public Map<Object, Object> selectReceptionList(String receptGender, String receptStatus, String searchText, String receptStartDate, String receptEndDate, int startPage, int pageSize) throws Exception {

        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        List<ReceptionVO> resList = new ArrayList<ReceptionVO>();
        List<ReceptionVO> firstList = new ArrayList<ReceptionVO>();
        List<Map<Object, Object>> staffList = new ArrayList<Map<Object, Object>>();

        param.put("receptGender", receptGender);
        param.put("receptStatus", receptStatus);
        param.put("searchText", searchText);
        param.put("receptStartDate", receptStartDate);
        param.put("receptEndDate", receptEndDate);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);

        resList = commonMapper.selectReceptionList(param);

        //if(startPage == 0) {
        result.put("resListCount", commonMapper.selectReceptionListCount(param));
        //}

        if(resList.size()>0) {
            result.put("err", 0);
            result.put("result", "OK");
        } else {
            result.put("err", 105);
            result.put("result", "검색 결과가 없습니다.");
        }

        result.put("resList", resList);

        return result;
    }

    public List<ReceptionVO> selectReceptionListAll(String receptGender, String receptStatus, String searchText, String receptStartDate, String receptEndDate) throws Exception {

        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        List<ReceptionVO> resList = new ArrayList<ReceptionVO>();
        List<ReceptionVO> firstList = new ArrayList<ReceptionVO>();
        List<Map<Object, Object>> staffList = new ArrayList<Map<Object, Object>>();


        param.put("receptGender", receptGender);
        param.put("receptStatus", receptStatus);
        param.put("searchText", searchText);
        param.put("receptStartDate", receptStartDate);
        param.put("receptEndDate", receptEndDate);
        param.put("startPage", -1);

        resList = commonMapper.selectReceptionList(param);


        return resList;
    }
}
