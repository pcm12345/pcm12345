package com.carrot.recept.controller;

import com.carrot.cons.dao.ReservationVO;
import com.carrot.recept.dao.ReceptionVO;
import com.carrot.recept.service.ReceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(description = "전화접수 관리", tags = "전화접수 관리")
@Slf4j
public class ReceptionController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ReceptionService receptionService;

    @PostMapping("/api/v1/recept")
    @ApiOperation(value="전화문의 등록", notes = "전화문의 등록하기")
    public ResponseEntity<Map<Object, Object>> insertReception(@RequestBody ReceptionVO vo, HttpServletRequest request) throws Exception {
        log.info("#### ReceptController.insertReception vo : " + vo.toString());
        Map<Object, Object> map = new LinkedHashMap<Object, Object>();
        String sql = "";
        int idxCrmStaff = -1;

        //accessToken 에서 작성자 키를 가져온다.
        if(request.getHeader("accessToken") != null){
            map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
            vo.setIdxCrmStaffCreate(Integer.parseInt(map.get("idx").toString()));
        }

        //입력한다.
        ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(receptionService.insertReception(vo), HttpStatus.OK);
        return rtn;
    }

    @GetMapping("/api/v1/recept/{idx}")
    @ApiOperation(value="전화문의 단일 조회", notes = "전화문의 단일 조회하기")
    public ResponseEntity<Map<Object, Object>> selectReception(@PathVariable("idx") int idx) throws Exception {
        log.info("#### ReceptController.selectReception idx : " + idx);

        ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(receptionService.selectReception(idx), HttpStatus.OK);
        return rtn;
    }

    @PutMapping("/api/v1/recept")
    @ApiOperation(value="전화문의 수정", notes = "전화문의 수정하기")
    public ResponseEntity<Map<Object, Object>> updateReception(@RequestBody ReceptionVO vo, HttpServletRequest request) throws Exception {
        log.info("#### ReceptController.updateReception " + " vo : " + vo.toString());

        Map<Object, Object> map = new LinkedHashMap<Object, Object>();
        map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

        vo.setIdxCrmStaffUpdate(Integer.parseInt(map.get("idx").toString()));
        ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(receptionService.updateReception(vo), HttpStatus.OK);
        return rtn;
    }

    @DeleteMapping("/api/v1/recept/{idx}")
    @ApiOperation(value="전화문의 삭제", notes = "전화문의 삭제하기")
    public ResponseEntity<Map<Object, Object>> deleteReception(@PathVariable("idx") int idx) throws Exception {
        log.info("#### ReceptController.deleteReception idx : " + idx);
        ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(receptionService.deleteReception(idx), HttpStatus.OK);
        return rtn;
    }

    @GetMapping("/api/v1/recept/list")
    @ApiOperation(value="전화문의 검색 목록", notes = "전화문의 검색목록 조회")
    public ResponseEntity<Map<Object, Object>> selectReceptionList (
            @RequestParam(name = "receptGender", required = false, defaultValue = "") String receptGender,
            @RequestParam(name = "receptStatus", required = false, defaultValue = "") String receptStatus,
            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
            @RequestParam(name = "receptStartDate", defaultValue = "") String receptStartDate,
            @RequestParam(name = "receptEndDate", defaultValue = "") String receptEndDate,
            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            HttpServletRequest request ) throws Exception {
        log.info("#### ReceptController.selectReceptionList receptGender : " + receptGender + " consStartDate : " + receptStatus
                + " searchText : " + searchText  + " receptStartDate : " + receptStartDate  + " receptEndDate : " + receptEndDate + " pageSize : " + pageSize + " + pageNum : " + pageNum);

        ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(receptionService.selectReceptionList(receptGender, receptStatus, searchText, receptStartDate, receptEndDate, receptionService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
        return rtn;
    }
}
