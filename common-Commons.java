package com.carrot.commons.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class Commons {

  /**
   * 회원가입 시 이메일, 전화번호, 비밀번호 형식 체크
   * @param userEmail
   * @param userPassword
   * @param userPhone
   * @return
   * @throws Exception
   */
  public static String checkFormat(String userEmail, String userPassword, String userPhone) throws Exception {

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
  public static boolean checkEmailForm(String userEmail) throws Exception {

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
  public static boolean checkPasswordForm(String userPassword) throws Exception {

    boolean result = false;
    String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,15}$"; // 영어 + 숫자 + 특수문자
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

  /**
   * 연락처형식 체크
   * @param userPhone
   * @return
   * @throws Exception
   */
  public static boolean checkPhoneForm(String userPhone) throws Exception {

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
  public static String trimString(String str) throws Exception {

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
  public static String lowerCaseString(String str) throws Exception {

    String result = str.toLowerCase();
    return result;

  }
  /**
   *  Client IP 가져오기
   * @param request
   * @return
   */
  public static String getClientIP(HttpServletRequest request) {
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
  public static String formatDateToString(Date value, String formatStr) {

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
  public static Date formatStringToDate(String value, String formatStr) throws Exception {

    SimpleDateFormat format = new SimpleDateFormat(formatStr);
    return format.parse(value);
  }

  /**
   * 문자 암호화(비밀번호 등)
   * @param value
   * @return
   */
  public static String bcryptHash(String value) {

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
  public static boolean bcryptCheck(String value, String hashed) {

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
  public static int doPaging(int pageNum, int pageSize) {
    return pageNum = ((pageNum-1) * pageSize);
  }

  /**
   * value의 값을 num 진수로 만들기
   * @param value
   * @param num
   * @return
   */
  public static String parserNum(int value, int num) {
    String result =  Long.toString(value, num);

    return result;
  }

  /**
   * 현재 유니코드 시간 가져오기
   * @return
   */
  public static Long unicodeTime() {
    Date now = new Date();
    long unicodeTime = now.getTime() / 1000L; // 유니코드 타임
    log.info("##### unix time : " + unicodeTime);

    return unicodeTime;
  }

  /**
   * request 접근
   */
  public static HttpServletRequest recallRequest() {
    return ((ServletRequestAttributes) Objects
        .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
  }

  /**
   * response 접근
   */
  public static HttpServletResponse recallResponse() {
    return ((ServletRequestAttributes) Objects
        .requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
  }

}
