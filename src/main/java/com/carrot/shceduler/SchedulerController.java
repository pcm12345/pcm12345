package com.carrot.shceduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
   *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
각 별 위치에 따라 주기를 다르게 설정 할 수 있다.
순서대로 초-분-시간-일-월-요일 순이다. 그리고 괄호 안의 숫자 범위 내로 별 대신 입력 할 수도 있다.
요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
 * @author D83
 *
 */
@Component
@Slf4j
@Profile("prod")
public class SchedulerController {

	@Autowired
	private SchedulerService schedulerService;


	// 30분 마다
	//@Scheduled(fixedRate = 60000 * 30) // 1000 = 1초
	//@Scheduled(fixedRateString = "200000", initialDelay = 0)
	@Scheduled(cron="0 0 11 * * ?") // 매일 11시에 실행
	public void cronJobScheduleEmp() {

		log.info("### cronJobScheduleEmp 실행");
		log.info("### Current Thread : {} " + Thread.currentThread().getName());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		log.info("Java cron job expression :: " + strDate);
		try {
			schedulerService.sendReserveAlt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
