package com.carrot.user;

import com.carrot.commons.common.Commons;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Api("사용자 정보")
@Slf4j
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 사용자 정보 조회(마이페이지)
   *
   * @param id
   * @return
   * @throws Exception
   */
  @GetMapping("/{id}")
  @ApiOperation(value = "사용자 정보 조회", notes = "사용자의 정보를 확인")
  public ResponseEntity<Map<Object, Object>> selectUser(@PathVariable("id") String id)
      throws Exception {
    log.info("#### UserController.selectUser id : " + id);
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.selectUser(id), HttpStatus.OK);
    return rtn;
  }

  /**
   * 사용자 ID 중복체크
   *
   * @param id
   * @return
   * @throws Exception
   */
  @GetMapping("")
  @ApiOperation(value = "사용자 ID 중복 체크", notes = "사용자가 존재하는지 확인")
  public ResponseEntity<Map<Object, Object>> checkUser(@RequestParam("id") String id)
      throws Exception {
    log.info("#### UserController.checkUser id : " + id);
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.duplicatUser(id), HttpStatus.OK);
    return rtn;
  }

  /**
   * 회원가입
   *
   * @param vo
   * @return
   * @throws Exception
   */
  @PostMapping("")
  @ApiOperation(value = "회원가입", notes = "사용자 정보 입력받아 회원가입")
  public ResponseEntity<Map<Object, Object>> insertUser(@RequestBody UserVO vo) throws Exception {
    log.info("#### UserController.checkUser vo : " + vo.toString());
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.insertUser(vo), HttpStatus.OK);
    return rtn;
  }

  /**
   * 사용자 목록 조회
   *
   * @param id
   * @return
   * @throws Exception
   */
  @GetMapping("/list")
  @ApiOperation(value = "사용자 목록 조회", notes = "조건에 맞는 사용자 목록을 조회한다.")
  public ResponseEntity<Map<Object, Object>> selectUserList(@RequestParam("id") String id,
      @RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) throws Exception {
    log.info("#### UserController.selectUserList id : " + id);
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.selectUserList(id, Commons
            .doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
    return rtn;
  }

  /**
   * 사용자 정보 수정(비밀번호)
   *
   * @param id
   * @param password
   * @return
   * @throws Exception
   */
  @PutMapping("/{id}")
  @ApiOperation(value = "사용자 정보 수정", notes = "선택한 사용자의 정보를 수정한다.")
  public ResponseEntity<Map<Object, Object>> updateUser(@PathVariable String id,
      @RequestParam("password") String password) throws Exception {
    log.info("#### UserController.updateUser id : " + id + " password : " + password);
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.updateUser(id, password), HttpStatus.OK);
    return rtn;
  }

  /**
   * 사용자 정보 삭제
   *
   * @param id
   * @return
   * @throws Exception
   */
  @DeleteMapping("/{id}")
  @ApiOperation(value = "사용자 정보 삭제", notes = "선택한 사용자의 정보를 삭제한다.")
  public ResponseEntity<Map<Object, Object>> deleteUser(String id) throws Exception {
    log.info("#### UserController.deleteUser id : " + id);
    ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(
        userService.deleteUser(id), HttpStatus.OK);
    return rtn;
  }


}
