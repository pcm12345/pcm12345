package com.carrot.common.mapper.crm;

import java.util.List;
import java.util.Map;

import com.carrot.cons.dao.*;
import com.carrot.recept.dao.ReceptionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.carrot.admin.dao.ExpensesVO;
import com.carrot.center.dao.CenterVO;
import com.carrot.client.dao.ClientVO;
import com.carrot.client.dao.MemoVO;
import com.carrot.client.dao.PersonVO;
import com.carrot.client.dao.PurchaseVO;
import com.carrot.company.dao.CompanyVO;
import com.carrot.pay.dao.PayVO;
import com.carrot.status.dao.StatusVO;
import com.carrot.user.dao.UserFileVO;
import com.carrot.user.dao.UserVO;

@Mapper
public interface CommonMapper {

    public Map<Object, Object> selectOne(@Param("sql") String sql);
    public Map<Object, Object> selectOne(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public List<Map<Object, Object>> selectAll(@Param("sql") String sql);
    public List<Map<Object, Object>> selectAll(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public int selectInt(@Param("sql") String sql);
    public int selectInt(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public void delete(@Param("sql") String sql);
    public int delete(@Param("table") String table, @Param("param") Map<String, Object> param);

    public void insert(@Param("sql") String sql);
    public int insert(@Param("table") String table, @Param("data") Map<String, Object> data);

    public int update(@Param("table") String table, @Param("data") Map<String, Object> data, @Param("where") String where);
    public int update(@Param("table") String table, @Param("data") Map<String, Object> data, @Param("where") String where, @Param("param") Map<String, Object> param);
    public int updateQuery(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    // 사용자 관련
    public int insertUser(UserVO vo);
    public int insertCert(UserFileVO vo);
    public UserVO selectUser(@Param("param") Map<String, Object> param);
    public List<UserVO> selectUserList(@Param("param") Map<String, Object> param);
    public int selectUserListCount(@Param("param") Map<String, Object> param);
    public int updateUser(UserVO vo);
    public int updateCert(UserFileVO vo);

    // 기업관련
    public int insertCompany(CompanyVO vo);
    public int updateCompany(CompanyVO vo);
    public List<CompanyVO> selectCompanyList(@Param("param") Map<String, Object> param);
    public List<CompanyVO> selectExcCompanyList(@Param("param") Map<String, Object> param);

    public int selectCompanyListCount(@Param("param") Map<String, Object> param);

    // 고객관련
    public int insertClient(ClientVO vo);
    public ClientVO selectClient(@Param("param") Map<String, Object> param);
    public int updateClient(ClientVO vo);
    public List<ClientVO> selectClientList(@Param("param") Map<String, Object> param);
    public List<ClientVO> selectCounselorClientList(@Param("param") Map<String, Object> param);
    public List<ClientVO> selectClientAll(@Param("param") Map<String, Object> param);
    public int selectClientListCount(@Param("param") Map<String, Object> param);

    // 수납관리
    public int insertPurchase(PurchaseVO vo);
    public int selectSumPriceClient(@Param("param") Map<String, Object> param);
    public int selectSumPriceCompany(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseB2C(@Param("param") Map<String, Object> param);
    public int selectPurchaseB2CCount(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseB2B(@Param("param") Map<String, Object> param);
    public int selectPurchaseB2BCount(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseDetail(@Param("param") Map<String, Object> param);
    public int updatePurchase(PurchaseVO vo);
    public List<PurchaseVO> selectPurchaseB2CList(@Param("param") Map<String, Object> param);
    public int selectPurchaseB2CListCount(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseB2BList(@Param("param") Map<String, Object> param);
    public int selectPurchaseB2BListCount(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseB2BUserList(@Param("param") Map<String, Object> param);
    public int selectPurchaseB2BUserListCount(@Param("param") Map<String, Object> param);

    // 센터관리
    public int insertCenter(CenterVO vo);
    public CenterVO selectCenter(@Param("param") Map<String, Object> param);
    public List<CenterVO> selectCenterList(@Param("param") Map<String, Object> param);
    public List<CenterVO> selectCenterAll();
    public int selectCenterListCount(@Param("param") Map<String, Object> param);
    public int updateCenter(CenterVO vo);

    // 상담예약관리
    public int insertReservation(ReservationVO vo);
    public int updateReservation(ReservationVO vo);
    public int reservationDupCheck(@Param("param") Map<String, Object> param);
    public ReservationVO selectReservation(@Param("param") Map<String, Object> param);
    public List<ReservationVO> selectReservationList(@Param("param") Map<String, Object> param);
    public int selectReservationListCount(@Param("param") Map<String, Object> param);

    // 상담일지관리
    public ReportVO selectReport(@Param("param") Map<String, Object> param);
    public List<ReportVO> selectReportList(@Param("param") Map<String, Object> param);
    public int selectReportListCount(@Param("param") Map<String, Object> param);

    // 지출관리
    public int insertExpenses(ExpensesVO vo);
    public int updateExpenses(ExpensesVO vo);
    public ExpensesVO selectExpenses(@Param("param") Map<String, Object> param);
    public List<ExpensesVO> selectExpensesB2CList(@Param("param") Map<String, Object> param);
    public int selectExpensesB2CListCount(@Param("param") Map<String, Object> param);
    public List<ExpensesVO> selectExpensesB2BList(@Param("param") Map<String, Object> param);
    public int selectExpensesB2BListCount(@Param("param") Map<String, Object> param);

    // EAP 배정현황
    public List<ClientVO> selectEapList(@Param("param") Map<String, Object> param);
    public int selectEapListCount(@Param("param") Map<String, Object> param);

    // 각 센터별 신규 상담 목록
    public List<ClientVO> selectCenterEapList(@Param("param") Map<String, Object> param);
    public int selectCenterEapListCount(@Param("param") Map<String, Object> param);

    // 센터현황
    public ClientVO selectCenterStatusClientNum(@Param("param") Map<String, Object> param);
    public List<ReservationVO> selectCenterStatusStaffConsNum(@Param("param") Map<String, Object> param);

    // 통계
    public List<StatusVO> selectMainStatusStaff(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusProduct(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusType(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusSubject(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusGender(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusAge(@Param("param") Map<String, Object> param);
    public List<StatusVO> selectMainStatusRoute(@Param("param") Map<String, Object> param);

    // B2C매출내역
    public PurchaseVO selectPurchaseHistoryB2CStatus(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseHistoryB2CList(@Param("param") Map<String, Object> param);
    public int selectPurchaseHistoryB2CListCount(@Param("param") Map<String, Object> param);
    // B2B매출내역
    public PurchaseVO selectPurchaseHistoryB2BStatus(@Param("param") Map<String, Object> param);
    public List<PurchaseVO> selectPurchaseHistoryB2BList(@Param("param") Map<String, Object> param);
    public int selectPurchaseHistoryB2BListCount(@Param("param") Map<String, Object> param);

    // 상담사급여내역
    public List<PayVO> selectPayStaffList(@Param("param") Map<String, Object> param);
    public int selectPayStaffListCount(@Param("param") Map<String, Object> param);
    public List<PayVO> selectPayStaff(@Param("param") Map<String, Object> param);
    public PayVO selectPayStaffTotal(@Param("param") Map<String, Object> param);
    public List<PayVO> selectPayCenterList(@Param("param") Map<String, Object> param);
    public int selectPayCenterListCount(@Param("param") Map<String, Object> param);



    public List<ReservationVO> selectStaffSchedule(@Param("param") Map<String, Object> param); // 상담사 예약 스케쥴

    public List<ReservationVO> selectReservationListFirst(@Param("param") Map<String, Object> param); // 첫상담 표기

    // 내담자 정보
    public int insertPerson(PersonVO vo);
    public int updatePerson(PersonVO vo);
    public PersonVO selectPerson(@Param("param") Map<String, Object> param);
    public List<PersonVO> selectPersonList(@Param("param") Map<String, Object> param);
    public int selectPersonListCount(@Param("param") Map<String, Object> param);

    // 내담자차트 목록
    public List<ClientVO> selectChartList(@Param("param") Map<String, Object> param);
    // 내담자차트 예약현황 목록
    public List<ReservationVO> selectChartReservationList(@Param("param") Map<String, Object> param);
    public int selectChartReservationListCount(@Param("param") Map<String, Object> param);
    // 내담자차트 메모 목록
    public List<MemoVO> selectMemoList(@Param("param") Map<String, Object> param);
    // 내담자차트 수납내역
    public List<PurchaseVO> selectChartPurchaseB2C(@Param("param") Map<String, Object> param);

    // 상담사 급여 사아앙세내역
    public List<PayVO> selectPayStaffDetail(@Param("param") Map<String, Object> param);

    public CompanyStaticPersonVO selectCompanyStaticPerson(@Param("param") Map<String, Object> param);
    public CompanyStaticReportVO selectCompanyStaticReport(@Param("param") Map<String, Object> param);
    public CompanyStaticReport2VO selectCompanyStaticReport2(@Param("param") Map<String, Object> param);

    public List<excelCompanyStaticVO> selectExcelCompanyStatic(@Param("param") Map<String, Object> param);


    void updateConsCountUseMinus(Integer idxCrmClient);

    void updateConsCountUsePlus(Integer idxCrmClient);

    int insertReception(ReceptionVO vo);

    ReceptionVO selectReception(Map<String, Object> param);

    void deleteReception(Map<String, Object> param);

    void updateReception(ReceptionVO vo);

    List<ReceptionVO> selectReceptionList(Map<String, Object> param);

    Object selectReceptionListCount(Map<String, Object> param);

    List<CenterVO> selectExcCenterList(@Param("param") Map<String, Object> param); // 센터관리 - excel관련

}
