package com.carrot.shceduler;

//import com.carrot.common.mapper.itm.ItmMapper;
//import com.carrot.common.mapper.sugang.SugangMapper;
import com.carrot.alt.service.AltService;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.cons.controller.ConsController;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.cons.service.ConsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class SchedulerService extends AbstractService {

//	@Autowired
//	private ItmMapper itmMapper;
//
	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private AltService AltService;

	//알림톡 발신 대상자 조회 함수
	public List<ReservationVO> getReserveList(String reserveDate){
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReservationVO> resList = new ArrayList<ReservationVO>();
		param.put("idxCrmStaff", -1);
		param.put("value", "");
		param.put("type", "예약");
		param.put("consStartDate", reserveDate);
		param.put("consEndDate", reserveDate);
		param.put("startPage", -1);

		return commonMapper.selectReservationList(param);
	}
	//알림톡 발신 함수

	//알림톡 발신 서비스
	public Map<Object, Object> sendReserveAlt() throws Exception {
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		Calendar cal = Calendar.getInstance();
		String format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		cal.add(cal.DATE, +1); //날짜를 하루 더한다.
		String reserveDate = sdf.format(cal.getTime());

		//reserveDate = "2021-12-21";

		List<ReservationVO> resList = getReserveList(reserveDate);

		if(resList.size()>0) {
			for(int i=0;i<resList.size();i++) {
				log.debug("##### sendReserveAlt.insertReserveTalk reserveDate : " + reserveDate);
				log.debug("##### sendReserveAlt.insertReserveTalk getPhone : " + resList.get(i).getPhone());

				//방문시간 값을 가져온다.
				String getConsTime = "";
				String tmp = resList.get(i).getConsTime();
				List<String> tmpList = Arrays.asList(tmp.split("/"));
				if(tmpList.size() > 0) getConsTime = tmpList.get(0).toString();

				AltService.insertReserveTalk(resList.get(i).getConsDate()+" "+getConsTime, resList.get(i).getPhone().toString(), 17648);
			}
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("resList", resList);

		return result;

	}


}
