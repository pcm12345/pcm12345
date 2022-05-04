package com.carrot.common.service;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * Transaction 처리 및 공통처리가 필요한 부분
 * @author D83
 *
 */
//@Transactional(value = "chained", rollbackFor = {Exception.class})
@Transactional(rollbackFor = {Exception.class})
@Slf4j
public abstract class AbstractService {

	/**
	 * 회원가입 시 이메일, 전화번호, 비밀번호 형식 체크
	 * @param userEmail
	 * @param userPassword
	 * @param userPhone
	 * @return
	 * @throws Exception
	 */
	public String checkFormat(String userEmail, String userPassword, String userPhone) throws Exception {
		
		String result = "OK";
		
		if(!checkEmailForm(userEmail) || "".equals(userEmail)) {
			result = "NO email format";
		} else {
			if (!checkPasswordForm(userPassword)  || "".equals(userEmail)) {
				result = "NO password format";
			} else {
				if (!checkPhoneForm(userPhone)  || "".equals(userEmail)) {
					result = "NO phone format";
				}
			}
		}
		log.info("##### checkFormat : " + result);
		return result;
	}
	
	/**
	 * 이메일형식 체크
	 * @param userEmail
	 * @return
	 * @throws Exception
	 */
	public boolean checkEmailForm(String userEmail) throws Exception {
		
//		String regex = "^[_a-zA-Z0-9-]+(._a-zA-Z0-9-]+)*@(?:\\w+\\.)+\\w+$"; // . 이런거 email에 못넣는 버전
		String regex = "^[_a-zA-Z0-9-]+[._a-zA-Z0-9-]+]*@(?:\\w+\\.)+\\w+$";
		boolean result = userEmail.matches(regex);
				
		log.info("#### check Format email : " + userEmail + " result : " + result);
		return result;
	}
	
	/**
	 * 비밀번호형식 체크
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	public boolean checkPasswordForm(String userPassword) throws Exception {
		
		boolean result = false;
		String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,20}$"; // 영어 + 숫자 + 특수문자
		//String regex1 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$"; // 영어 + 숫자
		//String regex2 = "^(?=.*[~`!@#$%\\^&*()-])(?=.*\\d)[\\d`~!@#$%^&*()-]{8,15}$"; // 숫자 + 특수문자
		//String regex3 = "^(?=.*[~`!@#$%\\^&*()-])(?=.*[A-Za-z])[A-Za-z`~!@#$%^&*()-]{8,15}$"; // 영어 + 특수문자
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(userPassword);
		
		if(m.matches()) {
			result = true;
		}
		
		log.info("#### check Format userPassword : " + userPassword + " result : " + result);
		return result;
	}
	
	public boolean checkIdForm(String id) throws Exception {
		
		boolean result = false;
		//String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,20}$"; // 영어 + 숫자 + 특수문자
		String regex1 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$"; // 영어 + 숫자
		//String regex2 = "^(?=.*[~`!@#$%\\^&*()-])(?=.*\\d)[\\d`~!@#$%^&*()-]{8,15}$"; // 숫자 + 특수문자
		//String regex3 = "^(?=.*[~`!@#$%\\^&*()-])(?=.*[A-Za-z])[A-Za-z`~!@#$%^&*()-]{8,15}$"; // 영어 + 특수문자
		Pattern p = Pattern.compile(regex1);
		Matcher m = p.matcher(id);
		
		if(m.matches()) {
			result = true;
		}
		
		log.info("#### check Format id : " + id + " result : " + result);
		return result;
	}
	
	/**
	 * 연락처형식 체크
	 * @param userPhone
	 * @return
	 * @throws Exception
	 */
	public boolean checkPhoneForm(String userPhone) throws Exception {
		
		boolean result = false;
		String regex = "^01(?:0|1|[6-9])-(?:\\d{3,4})-\\d{4}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(userPhone);
		
		if(m.matches()) {
			result = true;
		}
		
		log.info("#### check Format userPhone : " + userPhone + " result : " + result);
		return result;
	}
	
	/**
	 * 공백제거
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String trimString(String str) throws Exception {
		
		String result = str.replace(" ", "");
		result = result.trim();
		return result;
	}
	
	/**
	 * 소문자화
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String lowerCaseString(String str) throws Exception {
		
		String result = str.toLowerCase();
		return result;
		
	}
	/**
	 *  Client IP 가져오기
	 * @param request
	 * @return
	 */
	public String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    log.info("> X-FORWARDED-FOR : " + ip);

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	        log.info("> Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        log.info(">  WL-Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	        log.info("> HTTP_CLIENT_IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        log.info("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	        log.info("> getRemoteAddr : "+ip);
	    }
	    log.info("> Result : IP Address : "+ip);

	    return ip;
	}
	
	/**
	 * Date형 String 형으로 변환
	 * @param value
	 * @param formatStr
	 * @return
	 */
	public String formatDateToString(Date value, String formatStr) {
		
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(value);
	}
	
	/**
	 * String형 Date 형으로 변환
	 * @param value
	 * @param formatStr
	 * @return
	 * @throws Exception
	 */
	public Date formatStringToDate(String value, String formatStr) throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(value);
	}
	
	/**
	 * 문자 암호화(비밀번호 등)
	 * @param value
	 * @return
	 */
	public String bcryptHash(String value) {
		
		String result = "";
		if("".equals(value) || value == null) {
			log.info("#### 빈값은 입력 할 수 없습니다.");
			result = "빈 값은 입력 할 수 없습니다.";
		} else {
			result = BCrypt.hashpw(value, BCrypt.gensalt());
		}
		
		return result;
	}
	
	/**
	 * 복호화 해서 암호화 한 값과 비교(비밀번호 체크 등)
	 * @param value
	 * @param hashed
	 * @return
	 */
	public boolean bcryptCheck(String value, String hashed) {
		
		boolean result = false;
		if("".equals(value) || value == null) {
			log.info("#### 빈값은 입력 할 수 없습니다.");
		} else {
			if(BCrypt.checkpw(value, hashed)) {
				log.info("#### OK");
				result = true;
			} else {
				log.info("#### 입력한 값이 맞지 않습니다.");
			}
		}
		
		return result;
	}
	
	/**
	 * 페이징 처리(시작 데이터 값을 계산)
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public int doPaging(int pageNum, int pageSize) {
		return pageNum = ((pageNum-1) * pageSize);
	}
	
	/**
	 * value의 값을 num 진수로 만들기
	 * @param value
	 * @param num
	 * @return
	 */
	public String parserNum(int value, int num) {
		String result =  Long.toString(value, num);
		
		return result;
	}
	
	/**
	 * 현재 유니코드 시간 가져오기
	 * @return
	 */
	public Long unicodeTime() {
		Date now = new Date();
        long unicodeTime = now.getTime() / 1000L; // 유니코드 타임
        log.info("##### unix time : " + unicodeTime);
        
        return unicodeTime;
	}
	
	// 업로드 파일 검사
	public Map<Object, Object> checkFile(MultipartFile file, String type) throws Exception {
		
		log.info("#### 파일 검사 file : " + file);
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>(); 
		
		// 1. 확장자 검사
		// 오리지널 파일명에 경로가 포함되어있을 경우 subString
		String originalFileName = file.getOriginalFilename();
		
		if(originalFileName.indexOf("\\") > -1 ) {
			originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
		} else if (originalFileName.indexOf("/") > -1) {
			originalFileName = originalFileName.substring(originalFileName.lastIndexOf("/") + 1);
		}
		
		String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
		String conditionExtension = "";
		log.info("#### conditionExtension : " + conditionExtension);
		log.info("#### originalFileExtension : " + originalFileExtension);
		
		int conditionSize = 1048576; // 1MB = 1048576 바이트
		
		if("P".equals("type")) {
			conditionExtension = ".jpg.gif.png.jpeg.JPEG.JPG.GIF.PNG";
			conditionSize = 1048576 * 2; // 2MB
		} else {
			conditionExtension = ".pdf.jpg.gif.png.jpeg.JPEG.PDF.JPG.GIF.PNG";
			conditionSize = 1048576 * 2; // 2MB
		}
		
		if(conditionExtension.indexOf(originalFileExtension) > -1) {
			
			// 2. 사이즈 검사
			if(file.getSize() <= conditionSize) { 
			
				log.info("##### file original Name : " + originalFileName);
				String storedFileName = UUID.randomUUID().toString().replaceAll("-", "") + unicodeTime() + originalFileExtension;
				log.info("##### file storedFileName : " + storedFileName);
				result.put("err", 0);
				result.put("storedFileName", storedFileName);
				result.put("originalFileName", originalFileName);
			} else {
				log.info("#### 파일크기를 확인해 주세요.");
				result.put("err", 210);
				result.put("result", conditionSize / 1048576 + "MB 이상은 올릴 수 없습니다.");
			}
			
		} else {
			log.info("#### 파일확장자를 확인해 주세요.");
			result.put("err", 200);
			result.put("result", "파일확장자를 확인해 주세요.");
		}
		
		return result;
	}
	
	public Map<Object, Object> nameFile(MultipartFile file) throws Exception {
		
		log.info("#### 파일 검사 file : " + file);
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>(); 
		
		// 1. 확장자 검사
		// 오리지널 파일명에 경로가 포함되어있을 경우 subString
		String originalFileName = file.getOriginalFilename();
		
		String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출

		log.info("##### file original Name : " + originalFileName);
		String storedFileName = UUID.randomUUID().toString().replaceAll("-", "") + unicodeTime() + originalFileExtension;
		log.info("##### file storedFileName : " + storedFileName);
		result.put("storedFileName", storedFileName);
		result.put("originalFileName", originalFileName + originalFileExtension);
			
		return result;
	}

}
