package com.carrot.user;

import com.carrot.commons.common.Commons;
import com.carrot.mapper.main.CommonMapper;
import com.carrot.service.CarrotService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService extends CarrotService {

  @Autowired
  private CommonMapper commonMapper;

  public Map<Object, Object> selectUser(String id) {

    String sql = "";
    Map<String, Object> param = new HashMap<String, Object>();
    Map<Object, Object> result = new LinkedHashMap<Object, Object>();

    sql = "SELECT idx, id FROM user WHERE id = #{param.id}";
    param.put("id", id);
    Map<Object, Object> user = commonMapper.selectOne(sql, param);

    if (user == null) {
      result.put("err", 104);
      result.put("result", "없는 사용자 입니다.");
    } else {
      result.put("err", 0);
      result.put("result", "OK");
      result.put("userId", user.get("id"));
    }

    return result;

  }

  public Map<Object, Object> duplicatUser(String id) {

    log.info("#### checkUser id : " + id);
    String sql = "";
    Map<String, Object> param = new HashMap<String, Object>();
    Map<Object, Object> result = new LinkedHashMap<Object, Object>();

    sql = "SELECT count(*) FROM user WHERE id = #{param.id}";
    param.put("id", id);

    int count = commonMapper.selectInt(sql, param);

    if (count < 1) {
      result.put("err", 0);
      result.put("result", "사용할 수 있는 ID입니다.");
    } else {
      result.put("err", 100);
      result.put("result", "이미 사용중인 ID 입니다");
    }

    return result;
  }

  public Map<Object, Object> insertUser(UserVO vo) {

    log.info("#### insertUser vo : " + vo);

    HashMap<String, Object> data = new HashMap<String, Object>();
    Map<Object, Object> result = new HashMap<Object, Object>();

    if (Integer.parseInt(duplicatUser(vo.getId()).get("err").toString()) == 0) {

      data.put("id", vo.getId());
      data.put("password", Commons.bcryptHash(vo.getPassword())); // 암호화 하여 비밀번호 넣기
      int status = commonMapper.insert("user", data); // 첫번재 param : 테이블 명, 두번쨰 param : 정보
      result.put("err", 0);
      result.put("result", "회원가입 완료");
      result.put("status", status);

    } else {
      result.put("err", 100);
      result.put("result", "중복된 ID 입니다.");
    }

    return result;
  }

  public Map<Object, Object> selectUserList(String id, int startPage, int pageSize) {

    log.info("#### selecUserList id : " + id);
    String sql = "";
    Map<String, Object> param = new HashMap<String, Object>();
    Map<Object, Object> result = new LinkedHashMap<Object, Object>();

    sql = "SELECT idx, id FROM user WHERE id LIKE CONCAT ('%', #{param.id}, '%') LIMIT #{param.startPage}, #{param.pageSize}";
    param = new HashMap<String, Object>();
    param.put("id", id);
    param.put("startPage", startPage);
    param.put("pageSize", pageSize);
    List<Map<Object, Object>> userList = commonMapper.selectAll(sql, param);

    log.info("#### userList : " + userList.toString());

    result.put("userList", userList);
    result.put("userListSize", userList.size());
    result.put("err", 0);
    result.put("result", "OK");

    return result;

  }

  public Map<Object, Object> updateUser(String id, String password) {

    log.info("#### updateUser id : " + id);
    Map<String, Object> data = new HashMap<String, Object>(); // 수정할 컬럼 SET 에 들어갈 값
    data.put("password", Commons.bcryptHash(password));
    Map<String, Object> param = new HashMap<String, Object>(); // 조건 컬럼 WHERE 에 들어갈 값
    param.put("id", id);

    Map<Object, Object> result = new LinkedHashMap<Object, Object>(); // 결과

    // param : 테이블명, SET데이터, 조건절, 조건데이터
    int status = commonMapper.update("user", data, "id = #{param.id}", param);

    log.info("#### 사용자 정보가 수정 되었습니다.");
    result.put("err", 0);
    result.put("result", "사용자 정보가 수정 되었습니다.");
    result.put("status", status);

    return result;

  }

  public Map<Object, Object> deleteUser(String id) {

    log.info("#### updateUser id : " + id);
    Map<String, Object> param = new HashMap<String, Object>(); // 조건 컬럼 WHERE 에 들어갈 값
    param.put("id", id);

    Map<Object, Object> result = new LinkedHashMap<Object, Object>(); // 결과

    // param : 테이블명, 조건Map
    if (commonMapper.delete("user", param) > 0) {

      log.info("#### 사용자 정보가 삭제 되었습니다.");
      result.put("err", 0);
      result.put("result", "사용자 정보가 삭제 되었습니다.");

    } else {

      log.info("#### 삭제된 정보가 없습니다.");
      result.put("err", 0);
      result.put("result", "삭제된 정보가 없습니다.");

    }

    return result;

  }

}
