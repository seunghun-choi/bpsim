package bpsim.module.ctr.expert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.PageNavigator;
import bpsim.framework.util.ReqUtils;
import bpsim.framework.util.TokenMngUtil;
import bpsim.framework.util.ValidUtil;
import bpsim.module.ctr.usr.LoginController;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller("bpsim.module.ctr.expert.ExpertExcelController")	 
public class ExpertExcelController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	// 전문인력정보 엑셀 등록확인화면
	@RequestMapping(value="/expert/expertExcelInfoConfirm.do")
	public ModelAndView expertExcelInfoConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), "");
		
		Map expert = null; // 전문가상세정보
		List experList = null; // 전문가목록
		List rcntActyList = null; // 최근활동
		List acbgList = null;  	  // 학력사항
		List crrList = null;  	  // 경력사항
		List plcyClsfList = null;  // 정책분류
		List bioClsfList = null;  // 신바이오분류
		List wnawdList = null;    // 수상내역
		List utlzDsctnList = null;  // 센터 활용실적
		List infoErrList = null;    // 검증결과
		
		String back_params = ""; // 목록 검색 param
		
		if(loginInfo != null){
			try {
				params.put("loginid", loginInfo.getLoginid());
				// 전문가 기본정보
				experList = bpsimCommonService.getList("ExpertTemp.getTempExprtBscInfoList", params);
				// 처음에 들어올때
				
				if(experList.size() > 0 ) {
					if("".equals(exprt_mng_no) ) {
						Map tempExeprt =  (Map) experList.get(0);
						params.put("exprt_mng_no",tempExeprt.get("exprt_mng_no"));
					}
					// 전문가 상세정보
					expert = bpsimCommonService.getObjectMap("ExpertTemp.getTempExprtBscInfoDetail", params);		
					// 전문가 최근활동
					rcntActyList = bpsimCommonService.getList("ExpertTemp.getTempExprtRcntActyList", params);		
					// 전문가 학력사항
					acbgList = bpsimCommonService.getList("ExpertTemp.getTempExprtAcbgList", params);		
					// 전문가 경력사항
					crrList = bpsimCommonService.getList("ExpertTemp.getTempExprtCrrList", params);		
					// 전문가 분류(정책,신바이오)
					plcyClsfList = bpsimCommonService.getList("ExpertTemp.getTempPlcyClsfList", params);		
					bioClsfList = bpsimCommonService.getList("ExpertTemp.getTempBioClsfList", params);		
					// 전문가 수상내역
					wnawdList = bpsimCommonService.getList("ExpertTemp.getTempExprtWnawdList", params);
					// 센터 활용실적 내역
					utlzDsctnList = bpsimCommonService.getList("ExpertTemp.getTempExprtUtlzDsctnList", params);
					
					// 인적사항 검증결과
//					infoErrList = bpsimCommonService.getList("ExpertTemp.getTempExprtErrDsctnList", params);
					
					mav = getClsfCodeList(params, mav);
					mav.setViewName("jsp/expert/expertAddCheck");
				} else {
					mav.setViewName("redirect:/expert/expertList.do");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			mav.setViewName("redirect:/login.do");
		}
		System.out.println("박현호 분류체계 : " + plcyClsfList);
		
		
		// 코드값
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("expert", expert);
		mav.addObject("experList", experList);
		mav.addObject("rcntActyList", rcntActyList);
		mav.addObject("acbgList", acbgList);
		mav.addObject("crrList", crrList);
		mav.addObject("plcyClsfList", plcyClsfList);
		mav.addObject("bioClsfList", bioClsfList);
		mav.addObject("wnawdList", wnawdList);
		mav.addObject("utlzDsctnList", utlzDsctnList);
		mav.addObject("infoErrList", infoErrList);
		
		return mav;
	}
	
	// 전문인력정보 엑셀 등록확인화면 (비동기:회원누를때마다 바뀌는 형식)
	@RequestMapping(value="/async/expertExcelInfoaAsyncConfirm.do")
	public ModelAndView expertExcelInfoaAsyncConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String sel_exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("sel_exprt_mng_no"), "");
		
		Map expert = null; // 전문가상세정보
		List experList = null; // 전문가목록
		List rcntActyList = null; // 최근활동
		List acbgList = null;  	  // 학력사항
		List crrList = null;  	  // 경력사항
		List plcyClsfList = null;  // 정책분류
		List bioClsfList = null;  // 신바이오분류
		List wnawdList = null;    // 수상내역
		List utlzDsctnList = null;  // 센터 활용실적
		List infoErrList = null;    // 검증결과
		
		Map tempMap = new HashedMap(); // 조회용 임시
		String back_params = ""; // 목록 검색 param
		try {
			if(loginInfo != null){
				// 임시테이블 기본정보 수정
				tempdefaultUpdate(request,  params);
				//다른 전문가 클릭시 입력한 내용 임시저장
				tempInfoSave(request, params);
				
				tempMap.put("loginid", loginInfo.getLoginid());
				tempMap.put("exprt_mng_no", sel_exprt_mng_no);
				// 우측 전문가 목록
				experList = bpsimCommonService.getList("ExpertTemp.getTempExprtBscInfoList", tempMap);
				// 전문가 상세정보
				expert = bpsimCommonService.getObjectMap("ExpertTemp.getTempExprtBscInfoDetail", tempMap);		
				// 전문가 최근활동
				rcntActyList = bpsimCommonService.getList("ExpertTemp.getTempExprtRcntActyList", tempMap);		
				// 전문가 학력사항
				acbgList = bpsimCommonService.getList("ExpertTemp.getTempExprtAcbgList", tempMap);		
				// 전문가 경력사항
				crrList = bpsimCommonService.getList("ExpertTemp.getTempExprtCrrList", tempMap);		
				// 전문가 분류(정책,신바이오)
				plcyClsfList = bpsimCommonService.getList("ExpertTemp.getTempPlcyClsfList", tempMap);		
				bioClsfList = bpsimCommonService.getList("ExpertTemp.getTempBioClsfList", tempMap);		
				// 전문가 수상내역
				wnawdList = bpsimCommonService.getList("ExpertTemp.getTempExprtWnawdList", tempMap);
				// 센터 활용실적 내역
				utlzDsctnList = bpsimCommonService.getList("ExpertTemp.getTempExprtUtlzDsctnList", tempMap);
				
				// 인적사항 검증결과
//				infoErrList = bpsimCommonService.getList("ExpertTemp.getTempExprtErrDsctnList", tempMap);
				
				mav = getClsfCodeList(params, mav);
				
				mav.setViewName("jsp/expert/expertAddCheckForm");
			}else{
				mav.setViewName("redirect:/login.do");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("expert", expert);
		mav.addObject("experList", experList);
		mav.addObject("rcntActyList", rcntActyList);
		mav.addObject("acbgList", acbgList);
		mav.addObject("crrList", crrList);
		mav.addObject("plcyClsfList", plcyClsfList);
		mav.addObject("bioClsfList", bioClsfList);
		mav.addObject("wnawdList", wnawdList);
		mav.addObject("utlzDsctnList", utlzDsctnList);
		mav.addObject("infoErrList", infoErrList);
		
		return mav;
	}
	
	// 전문인력정보 엑셀 임시저장
	@RequestMapping(value="/expert/expertExcelTempSave.do")
	public ModelAndView expertExcelTempSave(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), ""); // 전문가관리번호
		try {
			if(loginInfo != null){
				// 임시테이블 기본정보 수정
				tempdefaultUpdate(request,  params);
				// 이외 정보 입력 및 검증내용 처리
				tempInfoSave(request,  params);
				
				mav.setViewName("redirect:/expert/expertExcelInfoConfirm.do");
			}else{
				mav.setViewName("redirect:/login.do");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("exprt_mng_no", exprt_mng_no);
		
		return mav;
	}
	
	// 임시테이블 기본정보 수정
	public void tempdefaultUpdate(HttpServletRequest request, Map params) {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), ""); // 전문가관리번호
		String exprt_nm = ReqUtils.getEmptyResult2((String) params.get("exprt_nm"), ""); // 전문가명
		String ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("ogdp_nm"), ""); // 소속
		String brth_dt = ReqUtils.getEmptyResult2((String) params.get("brth_dt"), ""); // 출생연도
		String mbl_telno = ReqUtils.getEmptyResult2((String) params.get("mbl_telno"), ""); // 휴대폰
		String eml_addr = ReqUtils.getEmptyResult2((String) params.get("eml_addr"), ""); // 이메일
		String asst_eml_addr = ReqUtils.getEmptyResult2((String) params.get("asst_eml_addr"), ""); // 보조이메일
		String kywd_nm_err = ReqUtils.getEmptyResult2((String) params.get("kywd_nm_err"), "N"); // 메타키워드 오류
		
		 // 20살 이후 체크
        int brthChkYear = LocalDate.now().getYear() - 20;
        
		try {
			if(loginInfo != null) {
				/* 기본 전문가 정보  시작 */
				Map infoErrMap = new HashedMap(); // 기본정보 검증 오류 입력
				infoErrMap.put("exprt_inpt_mng_no", exprt_mng_no);
				infoErrMap.put("brth_dt", brth_dt);
				infoErrMap.put("exprt_nm", exprt_nm);
				infoErrMap.put("exprt_mng_no", exprt_mng_no);
				infoErrMap.put("loginid", loginInfo.getLoginid());
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtErrDsctn", infoErrMap); // 에러내용 삭제
				
				//전문인력정보 중복 조회
				int dpcnCnt = bpsimCommonService.getListCount("Expert.getExprtBscInfoDpcnCnt", infoErrMap);
				if( dpcnCnt > 0) {
					infoErrMap.put("exprt_inpt_err_cd", 1);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "전문인력정보 중복");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(exprt_nm) || "null".equals(exprt_nm)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "성명 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(brth_dt)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "생년 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(ogdp_nm)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "소속 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				if(!"".equals(mbl_telno)) {
					if(!ValidUtil.validPhoneNumber(mbl_telno)) {
						infoErrMap.put("exprt_inpt_err_cd", 2);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "휴대폰번호 형식 오류(적절 형식 ex)010-1234-5678)");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				if(!"".equals(eml_addr)) {
					if(!ValidUtil.validEmail(eml_addr)) {
						infoErrMap.put("exprt_inpt_err_cd", 3);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "이메일 형식 오류");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				if(!"".equals(asst_eml_addr)) {
					if(!ValidUtil.validEmail(asst_eml_addr)) {
						infoErrMap.put("exprt_inpt_err_cd", 3);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "보조 이메일 형식 오류");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				
				if("Y".equals(kywd_nm_err)) {
					infoErrMap.put("exprt_inpt_err_cd", 4);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "meta 키워드 형식 오류");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				// 전문가 기본정보 수정
				params.put("last_mdfr_id", loginInfo.getLoginid());
				if(exprt_nm == null || "".equals(exprt_nm)) {
					params.put("exprt_nm", "null");
				}
				bpsimCommonService.update("ExpertTemp.updateTempExprtBscInfo", params);
				/* 기본 전문가 정보  종료 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 기본정보 임시저장
	public void tempBaseInfoSave(HttpServletRequest request, Map params) {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		Map tempMap = new HashedMap();
		
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), ""); // 전문가번호
		try {
			int exprt_mng = bpsimCommonService.getDataCnt("ExpertTemp.getNextExprtMngNo", tempMap); // 전문가관리번호
			exprt_mng_no = String.valueOf(exprt_mng);
			params.put("exprt_mng_no", exprt_mng_no);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		String exprt_nm = ReqUtils.getEmptyResult2((String) params.get("exprt_nm"), ""); // 전문가명
		String ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("ogdp_nm"), ""); // 소속
		String brth_dt = ReqUtils.getEmptyResult2((String) params.get("brth_dt"), ""); // 출생연도
		String mbl_telno = ReqUtils.getEmptyResult2((String) params.get("mbl_telno"), ""); // 휴대폰
		String eml_addr = ReqUtils.getEmptyResult2((String) params.get("eml_addr"), ""); // 이메일
		String asst_eml_addr = ReqUtils.getEmptyResult2((String) params.get("asst_eml_addr"), ""); // 보조이메일
		String kywd_nm_err = ReqUtils.getEmptyResult2((String) params.get("kywd_nm_err"), "N"); // 메타키워드 오류
		
		
		try {
			if(loginInfo != null) {
				/* 기본 전문가 정보  시작 */
				Map infoErrMap = new HashedMap(); // 기본정보 검증 오류 입력
				infoErrMap.put("exprt_inpt_mng_no", exprt_mng_no);
				infoErrMap.put("loginid", loginInfo.getLoginid());
				infoErrMap.put("brth_dt", brth_dt);
				infoErrMap.put("exprt_nm", exprt_nm);
				infoErrMap.put("exprt_mng_no", exprt_mng_no);
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtErrDsctn", infoErrMap); // 에러내용 삭제
				
				//전문인력정보 중복 조회
				int dpcnCnt = bpsimCommonService.getListCount("Expert.getExprtBscInfoDpcnCnt", infoErrMap);
				if( dpcnCnt > 0) {
					infoErrMap.put("exprt_inpt_err_cd", 1);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "전문인력정보 중복");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(exprt_nm)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "성명 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(brth_dt)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "생년 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				if("".equals(ogdp_nm)) {
					infoErrMap.put("exprt_inpt_err_cd", 2);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "소속 누락");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				if(!"".equals(mbl_telno)) {
					if(!ValidUtil.validPhoneNumber(mbl_telno)) {
						infoErrMap.put("exprt_inpt_err_cd", 2);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "휴대폰번호 형식 오류(적절 형식 ex)010-1234-5678)");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				if(!"".equals(eml_addr)) {
					if(!ValidUtil.validEmail(eml_addr)) {
						infoErrMap.put("exprt_inpt_err_cd", 3);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "이메일 형식 오류");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				if(!"".equals(asst_eml_addr)) {
					if(!ValidUtil.validEmail(asst_eml_addr)) {
						infoErrMap.put("exprt_inpt_err_cd", 3);
						infoErrMap.put("exprt_inpt_err_artcl_nm", "보조 이메일 형식 오류");
						bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
					}
				}
				
				if("Y".equals(kywd_nm_err)) {
					infoErrMap.put("exprt_inpt_err_cd", 4);
					infoErrMap.put("exprt_inpt_err_artcl_nm", "meta 키워드 형식 오류");
					bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", infoErrMap);
				}
				
				// 전문가 기본정보 등록
				bpsimCommonService.insert("ExpertTemp.insertTempBscInfo", params);
				/* 기본 전문가 정보  종료 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	// 임시저장 기능 분리
	public void tempInfoSave(HttpServletRequest request, Map params) {

		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		
		/* 기본정보 이외 전문가 정보 시작 */
		// 최근활동
		String rcnt_actv_ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_ogdp_nm"), ""); // 최근활동소속명
		String rcnt_actv_jbttl_nm = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_jbttl_nm"), ""); // 최근활동직책명
		String rcnt_actv_bgng_yr = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_bgng_yr"), ""); // 최근활동시작연도
		String rcnt_actv_end_yr = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_end_yr"), ""); // 최근활동종료연도
		int rcnt_actv_line = getIntValueFromParams(params, "rcnt_actv_line");
		
		// 학력사항
		String acbg_se_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_se_nm"), ""); // 학력학교명
		String acbg_schl_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_schl_nm"), ""); // 학력구분명
		String acbg_mjr_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_mjr_nm"), ""); // 학력전공명
		String acbg_grdtn_yr = ReqUtils.getEmptyResult2((String) params.get("acbg_grdtn_yr"), ""); // 학력졸업년도
		int acbg_line = getIntValueFromParams(params, "acbg_line");
		
		//경력사항
		String crr_ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("crr_ogdp_nm"), ""); // 경력소속명
		String crr_jbttl_nm = ReqUtils.getEmptyResult2((String) params.get("crr_jbttl_nm"), ""); // 경력직책명
		String crr_bgng_yr = ReqUtils.getEmptyResult2((String) params.get("crr_bgng_yr"), ""); // 경력시작년도
		String crr_end_yr = ReqUtils.getEmptyResult2((String) params.get("crr_end_yr"), ""); // 경력종료년도
		int crr_line = getIntValueFromParams(params, "crr_line");
		
		//신바이오분류체계
		String bioClsf_lclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_lclsf_cd"), ""); // 대분류코드
		String bioClsf_mclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_mclsf_cd"), ""); // 중분류코드
		String bioClsf_sclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_sclsf_cd"), ""); // 소분류코드
		int bioClsf_line = getIntValueFromParams(params, "bioClsf_line");
		
		//정책분류코드
		String plcyClsf_lclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_lclsf_cd"), ""); // 대분류코드
		String plcyClsf_mclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_mclsf_cd"), ""); // 중분류코드
		String plcyClsf_sclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_sclsf_cd"), ""); // 소분류코드
		int plcyClsf_line = getIntValueFromParams(params, "plcyClsf_line");
		
		//수상내역
		String wnawd_dsctn = ReqUtils.getEmptyResult2((String) params.get("wnawd_dsctn"), ""); // 수상내역
		String wnawd_yr = ReqUtils.getEmptyResult2((String) params.get("wnawd_yr"), ""); // 수상연도
		int wnawd_line = getIntValueFromParams(params, "wnawd_line");
		
		//센터 활용실적
		String utlz_ymd = ReqUtils.getEmptyResult2((String) params.get("utlz_ymd"), ""); // 활용일자
		String utlz_prps = ReqUtils.getEmptyResult2((String) params.get("utlz_prps"), ""); // 활용목적
		String info_exprtuser = ReqUtils.getEmptyResult2((String) params.get("info_exprtuser"), ""); // 정보활용자
		String utlz_rmrk_cn = ReqUtils.getEmptyResult2((String) params.get("utlz_rmrk_cn"), ""); // 비고
		int utlz_line = getIntValueFromParams(params, "utlz_line");
		
	
		try {
			if(loginInfo != null) {
				Map insertMap = new HashedMap();
				Map ErrMap = new HashedMap(); //검증 오류 입력
				ErrMap.put("loginid", loginInfo.getLoginid());
				ErrMap.put("exprt_inpt_mng_no", params.get("exprt_mng_no"));
				insertMap.put("exprt_mng_no",params.get("exprt_mng_no"));
				
				/* 
				 * split("\\|", -1) -> 모든 공백값도 list 형태로 만들어서 for을 돌려 값을 넣어야하기 때문에 공백값도 담음
				 * 모든 하위 입력 값 삭제 후 데이터가 있는 경우 신규로 등록
				 * */
				// 최근활동
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtRcntActv", insertMap); // 최근활동 삭제
				if(rcnt_actv_line > 0) {
		            String[] rcnt_actv_ogdp_nmList = rcnt_actv_ogdp_nm.split("\\|", -1); 
		            String[] rcnt_actv_jbttl_nmList = rcnt_actv_jbttl_nm.split("\\|", -1);
		            String[] rcnt_actv_bgng_yrList = rcnt_actv_bgng_yr.split("\\|", -1); 
		            String[] rcnt_actv_end_yrList = rcnt_actv_end_yr.split("\\|", -1);  //
		            
					for (int i = 0; i < rcnt_actv_line; i++) {
						insertMap.put("rcnt_actv_ogdp_nm",rcnt_actv_ogdp_nmList[i]);
						insertMap.put("rcnt_actv_jbttl_nm",rcnt_actv_jbttl_nmList[i]);
						insertMap.put("rcnt_actv_bgng_yr",rcnt_actv_bgng_yrList[i]);
						insertMap.put("rcnt_actv_end_yr",rcnt_actv_end_yrList[i]);
						
						if(rcnt_actv_ogdp_nmList[i] == null ||"".equals(rcnt_actv_ogdp_nmList[i])||" ".equals(rcnt_actv_ogdp_nmList[i])) {
							ErrMap.put("exprt_inpt_err_cd", 5);
							ErrMap.put("exprt_inpt_err_artcl_nm", "소속");
							// 오류 등록
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						// 최근활동 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtRcntActy", insertMap);
					}
				}
				
				// 학력사항
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtAcbg", insertMap);// 학력사항 삭제
				if(acbg_line > 0) {
		            String[] acbg_se_nmList = acbg_se_nm.split("\\|", -1); 
		            String[] acbg_schl_nmList = acbg_schl_nm.split("\\|", -1);
		            String[] acbg_mjr_nmList = acbg_mjr_nm.split("\\|", -1); 
		            String[] acbg_grdtn_yrList = acbg_grdtn_yr.split("\\|", -1); 
					
					for (int i = 0; i < acbg_line; i++) {
						insertMap.put("acbg_se_nm",acbg_se_nmList[i]);
						insertMap.put("acbg_schl_nm",acbg_schl_nmList[i]);
						insertMap.put("acbg_mjr_nm",acbg_mjr_nmList[i]);
						insertMap.put("acbg_grdtn_yr",acbg_grdtn_yrList[i]);
						
						if(acbg_se_nmList[i] == null ||"".equals(acbg_se_nmList[i])||" ".equals(acbg_se_nmList[i])) { //구분 미입력
							ErrMap.put("exprt_inpt_err_cd", 5);
							ErrMap.put("exprt_inpt_err_artcl_nm", "구분");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						
						if(acbg_schl_nmList[i] == null ||"".equals(acbg_schl_nmList[i])||" ".equals(acbg_schl_nmList[i])) { //학교명 미입력
							ErrMap.put("exprt_inpt_err_cd", 6);
							ErrMap.put("exprt_inpt_err_artcl_nm", "학교명");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						// 학력사항 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtAcbg", insertMap);
					}
				}
				
				// 경력사항
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtCrr", insertMap);// 경력사항 삭제
				if(crr_line > 0) {
		            String[] crr_ogdp_nmList = crr_ogdp_nm.split("\\|", -1); 
		            String[] crr_jbttl_nmList = crr_jbttl_nm.split("\\|", -1);
		            String[] crr_bgng_yrList = crr_bgng_yr.split("\\|", -1); 
		            String[] crr_end_yrList = crr_end_yr.split("\\|", -1);  //
		            
					for (int i = 0; i < crr_line; i++) {
						insertMap.put("crr_ogdp_nm",crr_ogdp_nmList[i]);
						insertMap.put("crr_jbttl_nm",crr_jbttl_nmList[i]);
						insertMap.put("crr_bgng_yr",crr_bgng_yrList[i]);
						insertMap.put("crr_end_yr",crr_end_yrList[i]);
						
						if(crr_ogdp_nmList[i] == null ||"".equals(crr_ogdp_nmList[i])||" ".equals(crr_ogdp_nmList[i])) { //소속 미입력
							ErrMap.put("exprt_inpt_err_cd", 7);
							ErrMap.put("exprt_inpt_err_artcl_nm", "소속");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						// 경력사항 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtCrr", insertMap);
					}
				}
				
				// 신바이오분류체계
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtBioClsf", insertMap);// 신바이오분류체계 삭제
				if(bioClsf_line > 0) {
		            String[] bioClsf_lclsf_cdList = bioClsf_lclsf_cd.split("\\|", -1); 
		            String[] bioClsf_mclsf_cdList = bioClsf_mclsf_cd.split("\\|", -1);
		            String[] bioClsf_sclsf_cdList = bioClsf_sclsf_cd.split("\\|", -1); 
		            
					for (int i = 0; i < bioClsf_line; i++) {
						insertMap.put("bioClsf_lclsf_cd",bioClsf_lclsf_cdList[i]);
						insertMap.put("bioClsf_mclsf_cd",bioClsf_mclsf_cdList[i]);
						insertMap.put("bioClsf_sclsf_cd",bioClsf_sclsf_cdList[i]);
						
						//대분류만검증
						if(bioClsf_lclsf_cdList[i] == null ||"".equals(bioClsf_lclsf_cdList[i])||" ".equals(bioClsf_lclsf_cdList[i])) { // 대분류
							ErrMap.put("exprt_inpt_err_cd", 8);
							ErrMap.put("exprt_inpt_err_artcl_nm", "대분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						} 
						
						else if(bioClsf_mclsf_cdList[i] == null ||"".equals(bioClsf_mclsf_cdList[i])||" ".equals(bioClsf_mclsf_cdList[i])) { // 중분류
							ErrMap.put("exprt_inpt_err_cd", 9);
							ErrMap.put("exprt_inpt_err_artcl_nm", "중분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						/*
						else if(bioClsf_sclsf_cdList[i] == null ||"".equals(bioClsf_sclsf_cdList[i])) { // 소분류
							ErrMap.put("exprt_inpt_err_cd", 10);
							ErrMap.put("exprt_inpt_err_artcl_nm", "소분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						*/
						// 신바이오분류코드 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtBioClsf", insertMap);
					}
				}
				
				// 정책분류코드
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtPlcyClsf", insertMap);// 정책분류코드 삭제
				if(plcyClsf_line > 0) {
		            String[] plcyClsf_lclsf_cdList = plcyClsf_lclsf_cd.split("\\|", -1); 
		            String[] plcyClsf_mclsf_cdList = plcyClsf_mclsf_cd.split("\\|", -1);
		            String[] plcyClsf_sclsf_cdList = plcyClsf_sclsf_cd.split("\\|", -1); 
		            
					for (int i = 0; i < plcyClsf_line; i++) {
						insertMap.put("plcyClsf_lclsf_cd",plcyClsf_lclsf_cdList[i]);
						insertMap.put("plcyClsf_mclsf_cd",plcyClsf_mclsf_cdList[i]);
						insertMap.put("plcyClsf_sclsf_cd",plcyClsf_sclsf_cdList[i]);
						
						//대분류만검증
						if(plcyClsf_lclsf_cdList[i] == null ||"".equals(plcyClsf_lclsf_cdList[i])||" ".equals(plcyClsf_lclsf_cdList[i])) { // 대분류
							ErrMap.put("exprt_inpt_err_cd", 11);
							ErrMap.put("exprt_inpt_err_artcl_nm", "대분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						} 
						
						else if(plcyClsf_mclsf_cdList[i] == null ||"".equals(plcyClsf_mclsf_cdList[i])||" ".equals(plcyClsf_mclsf_cdList[i])) { // 중분류
							ErrMap.put("exprt_inpt_err_cd", 12);
							ErrMap.put("exprt_inpt_err_artcl_nm", "중분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						/*
						else if(plcyClsf_sclsf_cdList[i] == null ||"".equals(plcyClsf_sclsf_cdList[i])) { // 소분류
							ErrMap.put("exprt_inpt_err_cd", 13);
							ErrMap.put("exprt_inpt_err_artcl_nm", "소분류");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						*/
						// 정책분류코드 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtPlcyClsf", insertMap);
					}
				}
				
				// 수상내역
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtWnawd", insertMap);// 수상내역 삭제
				if(wnawd_line > 0) {
					String[] wnawd_dsctnList = wnawd_dsctn.split("\\|", -1); 
					String[] wnawd_yrList = wnawd_yr.split("\\|", -1);
					
					for (int i = 0; i < wnawd_line; i++) {
						insertMap.put("wnawd_dsctn",wnawd_dsctnList[i]);
						insertMap.put("wnawd_yr",wnawd_yrList[i]);
						
						if(wnawd_dsctnList[i] == null ||"".equals(wnawd_dsctnList[i])||" ".equals(wnawd_dsctnList[i])) { // 수상내용
							ErrMap.put("exprt_inpt_err_cd", 14);
							ErrMap.put("exprt_inpt_err_artcl_nm", "수상내용");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						} 
						// 수상내역 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtWnawd", insertMap);
					}
				}
				
				// 센터 활용실적
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtUtlzDsctn", insertMap);// 센터 활용실적 삭제
				if(utlz_line > 0) {
					String[] utlz_ymdList = utlz_ymd.split("\\|", -1); 
					String[] utlz_prpsList = utlz_prps.split("\\|", -1);
					String[] info_exprtuserList = info_exprtuser.split("\\|", -1);
					String[] utlz_rmrk_cnList = utlz_rmrk_cn.split("\\|", -1);
					
					for (int i = 0; i < utlz_line; i++) {
						insertMap.put("utlz_ymd",utlz_ymdList[i]);
						insertMap.put("utlz_prps",utlz_prpsList[i]);
						insertMap.put("info_exprtuser",info_exprtuserList[i]);
						insertMap.put("utlz_rmrk_cn",utlz_rmrk_cnList[i]);
						
						if(utlz_ymdList[i] == null ||"".equals(utlz_ymdList[i])||" ".equals(utlz_ymdList[i])) { // 활용일자
							insertMap.put("utlz_ymd", null);
							ErrMap.put("exprt_inpt_err_cd", 15);
							ErrMap.put("exprt_inpt_err_artcl_nm", "활용일자");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}else if(utlz_prpsList[i] == null ||"".equals(utlz_prpsList[i])||" ".equals(utlz_prpsList[i])) { // 활용목적
							ErrMap.put("exprt_inpt_err_cd", 15);
							ErrMap.put("exprt_inpt_err_artcl_nm", "활용목적");
							bpsimCommonService.insert("ExpertTemp.insertTempExprtErrDsctn", ErrMap);
						}
						
						// 센터 활용실적 등록
						bpsimCommonService.insert("ExpertTemp.insertTempExprtUtlzDsctn", insertMap);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 기본정보 이외 전문가 정보 종료 */
	}
	
	// 전문인력정보 본테이블 이관
	@RequestMapping(value="/expert/expertOriginalTransfer.do")
	public ModelAndView expertOriginalTransfer(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		Map tempInsertMap = new HashedMap(); //임시테이블 exprt_mng_no 기준으로 데이터 이관 (전문가 기본정보)
		int newExprtMngNo = 0;
		try {
			//전문인력정보 중복 조회
			int dpcnCnt = bpsimCommonService.getListCount("Expert.getExprtBscInfoDpcnCnt", params);
			if(dpcnCnt == 0 ) {
				if(loginInfo != null){
					// 임시테이블 기본정보 수정
					tempdefaultUpdate(request,  params);
					//다른 전문가 클릭시 입력한 내용 임시저장
					tempInfoSave(request, params);
					
					tempInsertMap.put("loginid", loginInfo.getLoginid());
					// 전문인력 목록
					List expertList = bpsimCommonService.getList("ExpertTemp.getTempExprtBscInfoList", tempInsertMap);
					
					for (int i = 0; i < expertList.size(); i++) {
						Map tempExprtMap = (Map) expertList.get(i);
						// 전문가 기본정보 이관
						tempExprtMap.put("last_idfr_id", loginInfo.getLoginid());
						newExprtMngNo = bpsimCommonService.insertData("ExpertTemp.insertExprtBscInfoTransfer", tempExprtMap);
						
						// 이외의 각종 데이터 insert
						tempInsertMap.put("temp_exprt_mng_no",tempExprtMap.get("exprt_mng_no")); // 임시테이블 전문가 정보
						tempInsertMap.put("new_exprt_mng_no", newExprtMngNo);
						// 전문가 최근활동
						bpsimCommonService.insert("ExpertTemp.insertExprtRcntActyTransfer", tempInsertMap);		
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtRcntActv", tempExprtMap);		
						// 전문가 학력사항
						bpsimCommonService.insert("ExpertTemp.insertExprtAcbgTransfer", tempInsertMap);	
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtAcbg", tempExprtMap);
						// 전문가 경력사항
						bpsimCommonService.insert("ExpertTemp.insertExprtCrrTransfer", tempInsertMap);
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtCrr", tempExprtMap);
						// 전문가 분류(정책,신바이오)
						bpsimCommonService.insert("ExpertTemp.insertExprtPlcyClsfTransfer", tempInsertMap);
						bpsimCommonService.insert("ExpertTemp.insertExprtBioClsfTransfer", tempInsertMap);
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtPlcyClsf", tempExprtMap);
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtBioClsf", tempExprtMap);
						// 전문가 수상내역
						bpsimCommonService.insert("ExpertTemp.insertExprtWnawdTransfer", tempInsertMap);
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtWnawd", tempExprtMap);
						// 센터 활용실적 내역
						bpsimCommonService.insert("ExpertTemp.insertExprtUtlzDsctnTransfer", tempInsertMap);
						bpsimCommonService.delete("ExpertTemp.deleteTempExprtUtlzDsctn", tempExprtMap);
						// 각종 데이터 insert 종료
					}
					// 엑셀업로드 전문가 기본정보 삭제
					bpsimCommonService.delete("ExpertTemp.deleteTempBscInfo", tempInsertMap);
					mav.setViewName("redirect:/expert/expertList.do");
				}else{
					mav.setViewName("redirect:/login.do");
				}
			} else {
				mav.addObject("message", "중복된 전문인력정보가 있습니다.");
				mav.setViewName("redirect:/expert/expertExcelInfoConfirm.do");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		
		return mav;
	}
	
	@RequestMapping(value="/expert/excelUpload.do")
	public ModelAndView excelUpload(@RequestParam("uploadName") MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {

	    ModelAndView mav = new ModelAndView();
	    LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
	    
	    /* 중복방지 Token 생성 */
	    TokenMngUtil.saveToken(request);
	    Map expertMap = new HashMap<>();
	    
	    String checkNum = "";
	    try (InputStream inputStream = file.getInputStream()) {
	        // 엑셀 파일 읽기
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기
	        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); // FormulaEvaluator 생성
	        
	        int rcnt_actv_line = 0;
	        int acbg_line = 0;
	        int crr_line = 0;
	        int wnawd_line = 0;
	        int bioClsf_line = 0;
	        int plcyClsf_line = 0;
	        int utlz_line = 0;
	        
	        int rcnt_actv_isnull = 0;
	        int acbg_isnull = 0;
	        int crr_isnull = 0;
	        int wnawd_isnull = 0;
	        int bioClsf_isnull = 0;
	        int plcyClsf_isnull = 0;
	        int utlz_isnull = 0;
	        
	        String temp_value1 = "";
	        String temp_value2 = "";
	        String temp_value3 = "";
	        String temp_value4 = "";
	        
	        
	        if(loginInfo != null ) {
	        	expertMap.put("frst_rgtr_id", loginInfo.getLoginid());
	        	expertMap.put("last_mdfr_id", loginInfo.getLoginid());	        
	        }
	        
	        
	      //빈 행이 getLastRowNum으로 잡히는 문제가 있어 번호 기준으로 실제 마지막 행을 구함
	        int realRow = sheet.getLastRowNum();
	        for(int i = sheet.getLastRowNum(); i >= 0; i--) {
		         Row row = sheet.getRow(i);
		         if(row != null) {
			         Cell cell = row.getCell(0);
			         String value = getCellValue(cell, evaluator);
		         
			         if(value != null && !value.isEmpty()) {
				         realRow = i;
				         break;
			         }
		         }
		         realRow = i;
	        }
	        System.out.println("박현호 realRow : " + realRow);
	        
	        if(realRow < 7) {
	        	throw new Exception("바이오분야별_대표연구자_명단_입력양식 엑셀파일이 아니거나 데이터가 비어있습니다.");
	        }
	        
	        Row row = sheet.getRow(2);
	        Cell cell = row.getCell(0);
	        String checkValue = getCellValue(cell, evaluator);
	        if (!checkValue.equals("번호")) {
	        	throw new Exception("바이오분야별_대표연구자_명단_입력양식 엑셀파일이 아니거나 양식파일의 형태가 다릅니다.");
	        }
	        
	        // 엑셀 데이터 처리
	        // 시작 i가 7인 이유 : 헤더, 예시 열들 제외
	        for (int i = 7; i <= realRow; i++) {
	            row = sheet.getRow(i);
	            if (row == null) continue;

	            for (int j = 0; j <= 37; j++) {
	                cell = row.getCell(j);
	                String value = getCellValue(cell, evaluator);
	                
	                if(i == 7 && j == 0 && (value == null || value.isEmpty())) {
	                	throw new Exception("엑셀 데이터가 잘못되었습니다. 전문가 번호가 누락되었습니다.");
	                }
	                
	                // j 값에 따른 처리
	                switch (j) {
	                    case 0: // 전문가 번호 체크
	                        String num = (value != null && !value.isEmpty()) ? value : "err";
	                        
	                        if(num.equals("err")) {
	                        	System.out.println("사용자 번호에 미입력시 미진행");
	                        	continue;
	                        }
	                        
	                        if (!num.equals(checkNum)) {
	                            if (!"".equals(checkNum)) {
	                                // 이전 전문가 데이터 처리 로직 (DB 저장 등)
	                                System.out.println("박현호 저장할 데이터: " + expertMap.toString());
	                                //  기본정보 등록
	                                tempBaseInfoSave(request, expertMap);
	                             // 먼저 expertMap에서 가져온 값을 Object로 받고 타입을 확인 후 처리
	                                Object exprtMngNoObj = expertMap.get("exprt_mng_no");
	                                
	                                // Integer인지 String인지 확인 후 변환 처리
	                                if (exprtMngNoObj instanceof Integer) {
	                                    String exprtMngNoStr = String.valueOf(exprtMngNoObj); // Integer를 String으로 변환
	                                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
	                                } else if (exprtMngNoObj instanceof String) {
	                                    String exprtMngNoStr = (String) exprtMngNoObj; // 이미 String 타입인 경우 그대로 사용
	                                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
	                                } else {
	                                    System.out.println("exprt_mng_no의 타입이 예상과 다릅니다: " + exprtMngNoObj.getClass().getName());
	                                }
	                                expertMap.put("rcnt_actv_line", rcnt_actv_line);
	                                expertMap.put("acbg_line", acbg_line);
	                                expertMap.put("crr_line", crr_line);
	                                expertMap.put("wnawd_line", wnawd_line);
	                                expertMap.put("bioClsf_line", bioClsf_line);
	                                expertMap.put("plcyClsf_line", plcyClsf_line);
	                                expertMap.put("utlz_line", utlz_line);
	                                tempInfoSave(request, expertMap);
	                                
	                            }
	                            checkNum = num;
	                            expertMap.clear(); // 새로운 전문가 정보로 초기화
	                            expertMap.put("frst_rgtr_id", loginInfo.getLoginid());
	                        	expertMap.put("last_mdfr_id", loginInfo.getLoginid());
	                	        rcnt_actv_line = 0;
	                	        acbg_line = 0;
	                	        crr_line = 0;
	                	        wnawd_line = 0;
	                	        bioClsf_line = 0;
	                	        plcyClsf_line = 0;
	                	        utlz_line = 0;
	                            continue;
	                        } else {
	                            j = 13; // 중복 시 처리 생략
	                        }
	                        break;
	                    // 기본정보
	                    case 1:
	                    	if(value.equals("") || value.isEmpty()) {
	                    		expertMap.put("exprt_nm", "null"); // 전문가 이름이 빈 값일 경우
	                    	}else {
	                    		expertMap.put("exprt_nm", value); // 전문가명
	                    	}
	                    	break;
	                    case 2: 
	                    	if (value.equals("남")) {
	                    		value = "1";
	                    	}else if(value.equals("여")) {
	                    		value = "2";
	                    	}else {
	                    		value = "";
	                    	}
	                    	expertMap.put("exprt_gndr", value); break; // 성별
	                    case 3: expertMap.put("brth_dt", value); break; // 출생연도
	                    case 4: expertMap.put("ogdp_nm", value); break; // 소속
	                    case 5: expertMap.put("dept_nm", value); break; // 부서명
	                    case 6: expertMap.put("jbps_nm", value); break; // 직위
	                    case 7: expertMap.put("mbl_telno", value); break; // 휴대전화번호
	                    case 8: expertMap.put("co_telno", value); break; // 회사전화번호
	                    case 9: expertMap.put("eml_addr", value); break; // 이메일주소
	                    case 10: expertMap.put("asst_eml_addr", value); break; // 보조이메일주소
	                    case 11: expertMap.put("src_nm", value); break; // 출처명
	                    case 12: expertMap.put("rmrk_cn", value); break; // 비고내용
	                    case 13: expertMap.put("kywd_nm", value); break; // 키워드
	                    
	                    // 최근활동
	                    case 14: rcnt_actv_line += 1;
	                        rcnt_actv_isnull = value.equals("") ? rcnt_actv_isnull + 1 : 0;
	                        temp_value1 = value;
	                        break;
	                    	
	                    case 15:
	                        rcnt_actv_isnull = value.equals("") ? rcnt_actv_isnull + 1 : 0;
	                        temp_value2 = value;
	                        break;

	                    case 16:
	                        rcnt_actv_isnull = value.equals("") ? rcnt_actv_isnull + 1 : 0;
	                        temp_value3 = value;
	                        break;
	                    	
	                    case 17: 
	                        rcnt_actv_isnull = value.equals("") ? rcnt_actv_isnull + 1 : 0;
	                        temp_value4 = value;
	                    	
                    		if (rcnt_actv_isnull == 4) {
                    			rcnt_actv_isnull = 0;
                    			rcnt_actv_line -= 1;
                    		}else {
                    			rcnt_actv_isnull = 0;
                    			expertMap.put("rcnt_actv_ogdp_nm", appendData(expertMap, "rcnt_actv_ogdp_nm", temp_value1)); // 최근활동소속명
                    			expertMap.put("rcnt_actv_jbttl_nm", appendData(expertMap, "rcnt_actv_jbttl_nm", temp_value2)); // 최근활동직책명
                    			expertMap.put("rcnt_actv_bgng_yr", appendData(expertMap, "rcnt_actv_bgng_yr", temp_value3)); // 최근활동시작연도
                    			expertMap.put("rcnt_actv_end_yr", appendData(expertMap, "rcnt_actv_end_yr", temp_value4)); // 최근활동종료연도
                    		}
	                    	break;
	                    	
	                    
	                    // 학력사항
	                    case 18: acbg_line += 1;
		                    acbg_isnull = value.equals("") ? acbg_isnull + 1 : 0;
		                    temp_value1 = value;
		                    break;
		                    
	                    case 19: 
	                        acbg_isnull = value.equals("") ? acbg_isnull + 1 : 0;
	                        temp_value2 = value;
	                        break;
	                    	
	                    case 20: 
	                    	acbg_isnull = value.equals("") ? acbg_isnull + 1 : 0;
	                    	temp_value3 = value;
	                    	break;
	                    	
	                    case 21:                    	
	                    	acbg_isnull = value.equals("") ? acbg_isnull + 1 : 0;
	                    	temp_value4 = value;
	                    	
                    		if (acbg_isnull == 4) {
                    			acbg_isnull = 0;
                    			acbg_line -= 1;
                    		}else {
                    			acbg_isnull = 0;
                    			expertMap.put("acbg_se_nm", appendData(expertMap, "acbg_se_nm", temp_value1)); // 학력구분명
                    			expertMap.put("acbg_schl_nm", appendData(expertMap, "acbg_schl_nm", temp_value2)); // 학력학교명
                    			expertMap.put("acbg_mjr_nm", appendData(expertMap, "acbg_mjr_nm", temp_value3)); // 학력전공명
                    			expertMap.put("acbg_grdtn_yr", appendData(expertMap, "acbg_grdtn_yr", temp_value4)); // 학력졸업년도
                    		}
	                    	
	                    	break;                	
	                    	

	                    // 경력사항
	                    case 22: crr_line += 1;
	                    	crr_isnull = value.equals("") ? crr_isnull + 1 : 0;
	                    	temp_value1 = value;
	                    	break;
	                    case 23: 
	                    	crr_isnull = value.equals("") ? crr_isnull + 1 : 0;
	                    	temp_value2 = value;
	                    	break;
	                    	
	                    case 24: 
	                    	crr_isnull = value.equals("") ? crr_isnull + 1 : 0;
	                    	temp_value3 = value;
	                    	break;
	                    	
	                    case 25: 
	                    	crr_isnull = value.equals("") ? crr_isnull + 1 : 0;
	                    	temp_value4 = value;
	                    	
                    		if (crr_isnull == 4) {
                    			crr_isnull = 0;
                    			crr_line -= 1;
                    		}else {
                    			crr_isnull = 0;
                    			expertMap.put("crr_ogdp_nm", appendData(expertMap, "crr_ogdp_nm", temp_value1)); // 경력소속명
                    			expertMap.put("crr_jbttl_nm", appendData(expertMap, "crr_jbttl_nm", temp_value2)); // 경력직책명
                    			expertMap.put("crr_bgng_yr", appendData(expertMap, "crr_bgng_yr", temp_value3)); // 경력시작년도
                    			expertMap.put("crr_end_yr", appendData(expertMap, "crr_end_yr", temp_value4)); // 경력종료년도
                    		}
	                    	break;
	                    
	                    // 수상내역
	                    case 26: wnawd_line += 1;
	                    	wnawd_isnull = value.equals("") ? wnawd_isnull + 1 : 0;
	                    	temp_value1 = value;    
	                    	break;
	                    case 27: 
	                    	wnawd_isnull = value.equals("") ? wnawd_isnull + 1 : 0;
	                    	temp_value2 = value;
	                    	
                    		if (wnawd_isnull == 2) {
                    			wnawd_isnull = 0;
                    			wnawd_line -= 1;
                    		}else {
                    			wnawd_isnull = 0;
                    			expertMap.put("wnawd_dsctn", appendData(expertMap, "wnawd_dsctn", temp_value1)); // 수상내역
	                    		expertMap.put("wnawd_yr", appendData(expertMap, "wnawd_yr", temp_value2)); // 수상연도
                    		}
	                    	break;
	                    	
	                    // 신바이오분류체계
	                    case 28: bioClsf_line += 1;   
	                    	bioClsf_isnull = value.equals("") ? bioClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value1 = value;
	                    	break;
	                    case 29: 
	                    	bioClsf_isnull = value.equals("") ? bioClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value2 = value;
	                    	break;
	                    	
	                    case 30: 
	                    	bioClsf_isnull = value.equals("") ? bioClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value3 = value;	  
	                    	
                    		if (bioClsf_isnull == 3) {
                    			bioClsf_isnull = 0;
                    			bioClsf_line -= 1;
                    		}else {
                    			bioClsf_isnull = 0;
	                    		expertMap.put("bioClsf_lclsf_cd", appendData(expertMap, "bioClsf_lclsf_cd", temp_value1)); // 대분류코드	
	                    		expertMap.put("bioClsf_mclsf_cd", appendData(expertMap, "bioClsf_mclsf_cd", temp_value2)); // 중분류코드
	                    		expertMap.put("bioClsf_sclsf_cd", appendData(expertMap, "bioClsf_sclsf_cd", temp_value3)); // 소분류코드
	                    	}
	                    	break;
	                    	

	                    // 정책분류코드
	                    case 31: plcyClsf_line += 1;
	                    	plcyClsf_isnull = value.equals("") ? plcyClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value1 = value;

	                    	break;
	                    case 32: 
	                    	plcyClsf_isnull = value.equals("") ? plcyClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value2 = value;
	                    	break;
	                    	
	                    case 33: 
	                    	plcyClsf_isnull = value.equals("") ? plcyClsf_isnull + 1 : 0;
	                    	value = getRealCode(value);
	                    	temp_value3 = value;
	                    	
                    		if (plcyClsf_isnull == 3) {
                    			plcyClsf_isnull = 0;
                    			plcyClsf_line -= 1;
	                    	}else {
	                    		plcyClsf_isnull = 0;
	                    		expertMap.put("plcyClsf_lclsf_cd", appendData(expertMap, "plcyClsf_lclsf_cd", temp_value1)); // 대분류코드
	                    		expertMap.put("plcyClsf_mclsf_cd", appendData(expertMap, "plcyClsf_mclsf_cd", temp_value2)); // 중분류코드
	                    		expertMap.put("plcyClsf_sclsf_cd", appendData(expertMap, "plcyClsf_sclsf_cd", temp_value3)); // 소분류코드
	                    	}
	                    	break;
	                    	

	                    // 센터 활용실적
	                    case 34: utlz_line += 1;
	                    	utlz_isnull = value.equals("") ? utlz_isnull + 1 : 0;
	                    	temp_value1 = value;
	                    	break;
	                    case 35: 
	                    	utlz_isnull = value.equals("") ? utlz_isnull + 1 : 0;
	                    	temp_value2 = value;	                    	             		             		
	                    	break;
	                    	
	                    case 36: 
	                    	utlz_isnull = value.equals("") ? utlz_isnull + 1 : 0;
	                    	temp_value3 = value;
	                    	break;
	                    	
	                    case 37: 
	                    	utlz_isnull = value.equals("") ? utlz_isnull + 1 : 0;
	                    	temp_value4 = value;
	                    	
                    		if (utlz_isnull == 4) {
                    			utlz_isnull = 0;
                    			utlz_line -= 1;
	                    	}else {
	                    		utlz_isnull = 0;
	                    		expertMap.put("utlz_ymd", appendData(expertMap, "utlz_ymd", temp_value1)); // 활용일자
	                    		expertMap.put("utlz_prps", appendData(expertMap, "utlz_prps", temp_value2)); // 활용목적	      
	                    		expertMap.put("info_exprtuser", appendData(expertMap, "info_exprtuser", temp_value3)); // 정보활용자
	                    		expertMap.put("utlz_rmrk_cn", appendData(expertMap, "utlz_rmrk_cn", temp_value4)); // 비고                    		
	                    	}
	                    	break;
	                    	

	                    default:
	                        break;
	                }
	                
	                System.out.println("박현호 " + i + "행 " + j + "열의 값 : " + value);
	            }
	        }
	        
	        // 마지막 전문가 데이터 처리 (루프가 끝나면 마지막으로 남은 전문가 데이터 저장)
	        if (!"".equals(checkNum)) {
	            System.out.println("박현호 마지막 저장할 데이터: " + expertMap.toString());
	            
	            tempBaseInfoSave(request, expertMap);
                Object exprtMngNoObj = expertMap.get("exprt_mng_no");
                // Integer인지 String인지 확인 후 변환 처리
                if (exprtMngNoObj instanceof Integer) {
                    String exprtMngNoStr = String.valueOf(exprtMngNoObj); // Integer를 String으로 변환
                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
                } else if (exprtMngNoObj instanceof String) {
                    String exprtMngNoStr = (String) exprtMngNoObj; // 이미 String 타입인 경우 그대로 사용
                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
                } else {
                    System.out.println("exprt_mng_no의 타입이 예상과 다릅니다: " + exprtMngNoObj.getClass().getName());
                }
                expertMap.put("rcnt_actv_line", rcnt_actv_line);
                expertMap.put("acbg_line", acbg_line);
                expertMap.put("crr_line", crr_line);
                expertMap.put("wnawd_line", wnawd_line);
                expertMap.put("bioClsf_line", bioClsf_line);
                expertMap.put("plcyClsf_line", plcyClsf_line);
                expertMap.put("utlz_line", utlz_line);
                tempInfoSave(request, expertMap);	            
	        }
            
	        System.out.println("엑셀 파일 업로드 및 처리 완료");
	        
		    if (loginInfo != null) {
		        mav.setViewName("redirect:/expert/expertAdd.do");
		    } else {
		        mav.setViewName("redirect:/login.do");
		    }	        
	        

	    } catch (Exception e) {
	        mav.addObject("errorMessage", e.getMessage()); // 오류 메시지를 JSP로 전달
	        mav.setViewName("redirect:/expert/expertAdd.do");
	    }

	    return mav;
	}

	// 셀 값 추출하는 공통 메서드
	private String getCellValue(Cell cell, FormulaEvaluator evaluator) {
	    if (cell == null) return "";
	    
	    switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_STRING:
	            return cell.getStringCellValue() != null ? cell.getStringCellValue() : "";
	        case Cell.CELL_TYPE_NUMERIC:
	            double numericValue = cell.getNumericCellValue();
	            return (numericValue == (int) numericValue) ? String.valueOf((int) numericValue) : String.valueOf(numericValue);
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());	            
            case Cell.CELL_TYPE_FORMULA:
                // 수식을 평가하여 결과값을 반환
                return evaluateFormulaCell(cell, evaluator);    
	        default:
	            return "";
	    }
	}
	
    // 수식을 평가하여 결과값을 반환
    private String evaluateFormulaCell(Cell cell, FormulaEvaluator evaluator) {
        CellValue cellValue = evaluator.evaluate(cell); // 수식 평가
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            case Cell.CELL_TYPE_NUMERIC:
                double numericValue = cellValue.getNumberValue();
                return (numericValue == (int) numericValue) ? String.valueOf((int) numericValue) : String.valueOf(numericValue);
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cellValue.getBooleanValue());
            default:
                return "";
        }
    }
	
	
	// 엑셀업로드 다중 저장
	private String appendData(Map<String, String> map, String key, String value) {
	    String existingValue = map.get(key) != null ? map.get(key) : "";
	    
	    if(value == null || value.equals("")) {
	    	value=" ";
	    }
	    
	    return existingValue.isEmpty() ? value : existingValue + "|" + value;
	}
	
	// 엑셀업로드 분류체계 코드 찾기
	private String getRealCode(String value) {
	    if (value == null || value.isEmpty()) {
	        return value; // 빈 값 처리
	    }
	    int startIndex = value.indexOf("[") + 1;
	    int endIndex = value.indexOf("]");
	    return (startIndex > 0 && endIndex > startIndex) ? value.substring(startIndex, endIndex) : value;
	}
	
	// 공통 메서드: 임시저장 다중선택 개수 카운트
	public static int getIntValueFromParams(Map params, String key) {
	    Object value = params.get(key);

	    if (value instanceof Integer) {
	        return (Integer) value;  // 이미 Integer이면 그대로 반환
	    } else if (value instanceof String) {
	        return Integer.parseInt(ReqUtils.getEmptyResult2((String) value, "0"));  // String이면 변환
	    } else {
	        return Integer.parseInt("0");  // 기본값으로 처리
	    }
	}	
	
	
	// 전문인력정보 코드 전체 목록
	public ModelAndView getClsfCodeList(Map params, ModelAndView mav) {
		
		try {
			
			// 정책분류
			List plcyLclsfCd = bpsimCommonService.getList("Code.getPolicyLargeCode", params);				
			List plcyMclsfCd = bpsimCommonService.getList("Code.getPolicyMediumCode", params);				
			List plcySclsfCd = bpsimCommonService.getList("Code.getPolicySmallCode", params);
			
			// 신바이오분류
			List bioLclsfCd = bpsimCommonService.getList("Code.getNewBioLclsfList", params);				
			List bioMclsfCd = bpsimCommonService.getList("Code.getNewBioMclsfList", params);				
			List bioSclsfCd = bpsimCommonService.getList("Code.getNewBioSclsfList", params);				
			
			mav.addObject("plcyLclsfCd", plcyLclsfCd);
			mav.addObject("plcyMclsfCd", plcyMclsfCd);
			mav.addObject("plcySclsfCd", plcySclsfCd);
			mav.addObject("bioLclsfCd", bioLclsfCd);
			mav.addObject("bioMclsfCd", bioMclsfCd);
			mav.addObject("bioSclsfCd", bioSclsfCd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	// 전문인력정보 엑셀 등록 초기화
	@RequestMapping(value="/expert/resetExpertExcelUpload.do")
	public ModelAndView resetExpertExcelUpload(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		try {
			if(loginInfo != null){
				params.put("loginid", loginInfo.getLoginid());
				bpsimCommonService.delete("ExpertTemp.deleteTempBscInfo", params);
				mav.setViewName("redirect:/expert/expertList.do");
			}else{
				mav.setViewName("redirect:/login.do");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 코드값
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		
		return mav;
	}
	
	// 전문인력정보 엑셀등록확인 전문가정보 삭제
	@RequestMapping(value="/expert/deleteTempExpertInfo.do")
	public ModelAndView deleteTempExpertInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		try {
			if(loginInfo != null){
				params.put("loginid", loginInfo.getLoginid());
				bpsimCommonService.delete("ExpertTemp.deleteTempBscInfoMngNo", params); // 기본정보
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtErrDsctn", params); // 에러내용
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtRcntActv", params); // 최근활동
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtAcbg", params);	// 학력사항
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtCrr", params);		// 경력사항
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtBioClsf", params); // 신바이오분류코드
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtPlcyClsf", params); // 정책분류코드
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtWnawd", params); // 수상내역
				bpsimCommonService.delete("ExpertTemp.deleteTempExprtUtlzDsctn", params); // 센터 내 활용실적
				mav.setViewName("redirect:/expert/expertExcelInfoConfirm.do");
			}else{
				mav.setViewName("redirect:/login.do");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 코드값
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		
		return mav;
	}
	
	@RequestMapping(value="/expert/expertExcelDown.do")
	public void expertExcelDown(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
//		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("excelPage"), "1");
//		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
//		params.put("cPage", cPage);
//		
//		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
//		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
//		int pageCnt = 10;									/* 가로페이징(페이지수) */
//		int totalCnt = 0;										/* 총 조회수  */
//		params.put("intListCnt", intListCnt);
//		totalCnt = bpsimCommonService.getDataCnt("Expert.getExpertListCnt", params);
//		// 페이지 네비케이터
//		PageNavigator pageNavigator = new PageNavigator(
//			intPage		
//		   ,"/expert/expertList.do"
//		   ,pageCnt		
//		   ,intListCnt	
//		   ,totalCnt	
//		   ,""
//		);
//		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
//		List<Object> expertList = bpsimCommonService.getList("Expert.getExpertList", params, 0, pageNavigator.getRecordPerPage());				//리스트
		List<Object> expertList = bpsimCommonService.getList("Expert.getExpertExcelList", params);				//리스트
		
		// 엑셀 만들기
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("전문가 목록"); // 시트를 먼저 생성해야 함
		sheet.createFreezePane(0, 2);
		
		// 홀수 마지막 스타일
		CellStyle expertStyle = workbook.createCellStyle();
		expertStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		expertStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		expertStyle.setBorderLeft(CellStyle.BORDER_THIN);
		expertStyle.setBorderRight(CellStyle.BORDER_THIN);
		expertStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// 짝수 마지막 스타일
		CellStyle expertStyle2 = workbook.createCellStyle();
		expertStyle2.setBorderLeft(CellStyle.BORDER_THIN);
		expertStyle2.setBorderRight(CellStyle.BORDER_THIN);		
		expertStyle2.setBorderBottom(CellStyle.BORDER_MEDIUM);		
		// 홀수 유저 스타일
		CellStyle Style1 = workbook.createCellStyle();
		Style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		Style1.setBorderLeft(CellStyle.BORDER_THIN);
		Style1.setBorderRight(CellStyle.BORDER_THIN);
		Style1.setBorderBottom(CellStyle.BORDER_THIN);
		Style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		CellStyle Style2 = workbook.createCellStyle();
		Style2.setBorderLeft(CellStyle.BORDER_THIN);
		Style2.setBorderRight(CellStyle.BORDER_THIN);
		Style2.setBorderBottom(CellStyle.BORDER_THIN);
		
		// 기본 개인 정보 스타일
		CellStyle personalInfoStyle = createCellStyle(workbook, IndexedColors.LIGHT_GREEN);
		// meta 키워드 스타일
		CellStyle metaKeywordStyle = createCellStyle(workbook, IndexedColors.ROSE);
		// 최근 5년 내 활동 스타일
		CellStyle recentActivityStyle = createCellStyle(workbook, IndexedColors.LIGHT_YELLOW);
		// 학력사항 스타일
		CellStyle educationStyle = createCellStyle(workbook, IndexedColors.TAN);
		// 경력사항 스타일
		CellStyle careerStyle = createCellStyle(workbook, IndexedColors.CORAL);
		// 수상경력 스타일
		CellStyle awardStyle = createCellStyle(workbook, IndexedColors.BLUE_GREY);
		// 신바이오분류체계 스타일
		CellStyle bioClassificationStyle = createCellStyle(workbook, IndexedColors.LAVENDER);
		// 정책분류코드 스타일
		CellStyle policyCodeStyle = createCellStyle(workbook, IndexedColors.CORNFLOWER_BLUE);
		// 센터 내 전문가 활용 실적 스타일
		CellStyle utilizationStyle = createCellStyle(workbook, IndexedColors.YELLOW);
		
		CellStyle onlyCenter = workbook.createCellStyle();
		onlyCenter.setAlignment(CellStyle.ALIGN_CENTER);  // 가로 중앙 정렬
		onlyCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  // 세로 중앙 정렬	
		onlyCenter.setBorderLeft(CellStyle.BORDER_THIN);
		onlyCenter.setBorderRight(CellStyle.BORDER_THIN);				
		onlyCenter.setBorderBottom(CellStyle.BORDER_THIN);				
		onlyCenter.setBorderTop(CellStyle.BORDER_THIN);				
        
		Row mainHeader = sheet.createRow(0);
		String[] mains = {"기본개인정보", "meta 키워드", "최근 5년 내 활동", "학력사항", "경력사항", "수상경력", "신바이오분류체계", "정책분류코드", "센터 내 전문가 활용실적"};
        
    	// 기본 인적 사항
		Cell cell = mainHeader.createCell(0);
		cell.setCellValue(mains[0]);
		cell.setCellStyle(personalInfoStyle);
    	// 첫 번째 행의 0번째 열(1번째)부터 11번째 열(12번째)까지 병합
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); // (startRow, endRow, startCol, endCol)

    	// meta 키워드
    	cell = mainHeader.createCell(12);
    	cell.setCellValue(mains[1]);
    	cell.setCellStyle(metaKeywordStyle);
    	
    	// 최근 5년 내 활동
    	cell = mainHeader.createCell(13);
    	cell.setCellValue(mains[2]);
    	cell.setCellStyle(recentActivityStyle);    	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 16)); // (startRow, endRow, startCol, endCol)
    	// 학력사항
    	cell = mainHeader.createCell(17);
    	cell.setCellValue(mains[3]);
    	cell.setCellStyle(educationStyle);    	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 20)); // (startRow, endRow, startCol, endCol)
    	// 경력사항
    	cell = mainHeader.createCell(21);
    	cell.setCellValue(mains[4]);
    	cell.setCellStyle(careerStyle);    	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 21, 24)); // (startRow, endRow, startCol, endCol)
    	// 수상경력
    	cell = mainHeader.createCell(25);
    	cell.setCellValue(mains[5]);
    	cell.setCellStyle(awardStyle);    	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 25, 26)); // (startRow, endRow, startCol, endCol)
    	// 신바이오분류체계
    	cell = mainHeader.createCell(27);
    	cell.setCellValue(mains[6]);
    	cell.setCellStyle(bioClassificationStyle);     	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 27, 29)); // (startRow, endRow, startCol, endCol)
    	// 정책분류코드
    	cell = mainHeader.createCell(30);
    	cell.setCellValue(mains[7]);
    	cell.setCellStyle(policyCodeStyle);     	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 30, 32)); // (startRow, endRow, startCol, endCol)
    	// 센터 내 전문가 활용 실적
    	cell = mainHeader.createCell(33);
    	cell.setCellValue(mains[8]);
    	cell.setCellStyle(utilizationStyle);     	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 33, 36)); // (startRow, endRow, startCol, endCol)
		
        Row headerRow = sheet.createRow(1);
        String[] headers = {"번호","성명", "성별", "생년", "소속", "부서", "직위", "휴대폰번호", "직장번호", "이메일", "출처", "비고", "meta 키워드"
        		,"소속", "직책", "시작연도", "종료연도", "구분", "학교명", "전공", "졸업년도", "소속", "직책", "시작연도", "종료연도", "수상명", "수상년도"
        		,"대분류", "중분류", "소분류", "대분류", "중분류", "소분류", "활용일자", "활용목적", "활용자", "비고"};
        
        for (int i=0; i < headers.length; i++) {
        	cell = headerRow.createCell(i);
        	cell.setCellValue(headers[i]);
        	// 헤더 스타일 적용
        	cell.setCellStyle(onlyCenter);
        }
        
        // 각 열의 최대 길이를 저장할 배열
        int[] maxColumnWidths = new int[37];
        
        // 배열에 디폴트 값 4를 설정하는 반복문
        for (int i = 0; i < maxColumnWidths.length; i++) {
            maxColumnWidths[i] = 3; // 각 인덱스에 디폴트 값 3 설정
        }
        
		List<Object> rcntActyList = null; // 최근활동
		List<Object> acbgList = null; // 학력사항
		List<Object> crrList = null; // 경력사항
		List<Object> plcyClsfList = null; // 정책분류
		List<Object> bioClsfList = null; // 신바이오분류
		List<Object> wnawdList = null; // 수상내역
		List<Object> utlzDsctnList = null;  // 센터 활용실적
		Map findNum = new HashMap<>();

		// 다건에 대한 최대 행 계산
		int maxRow = 0;
		
		// 전문가 번호 순서 저장용
		int realNum = 1;

		// 해당 전문가의 행을 기억하기 위함
		int saveNum = 2;
        int rowNum = 2;
		
        for (Object obj : expertList) {
            Map<String, Object> expert = (Map<String, Object>) obj;  // 명시적 캐스팅
            Row row = sheet.createRow(rowNum);

            // "번호" 열 (정수)
            row.createCell(0).setCellValue(realNum);
//            row.getCell(0).setCellStyle(onlyCenter);
            maxColumnWidths[0] = Math.max(maxColumnWidths[0], String.valueOf(rowNum).length());

            // "성명" 열
            String exprtNm = (String) expert.get("exprtNm");
            row.createCell(1).setCellValue(exprtNm);
//            row.getCell(1).setCellStyle(onlyCenter);
            maxColumnWidths[1] = Math.max(maxColumnWidths[1], exprtNm.length());

            // "성별" 열 (성별 변환)
            String gender = "1".equals(expert.get("exprtGndr")) ? "남" : "여";
            row.createCell(2).setCellValue(gender);
//            row.getCell(2).setCellStyle(onlyCenter);
            maxColumnWidths[2] = Math.max(maxColumnWidths[2], gender.length());

            // "생년" 열
            String brthDtStr = (String) expert.get("brthDt");
            if (brthDtStr != null && !brthDtStr.isEmpty()) {
                try {
                    int brthDt = Integer.parseInt(brthDtStr);
                    row.createCell(3).setCellValue(brthDt);
                    maxColumnWidths[3] = Math.max(maxColumnWidths[3], (brthDtStr != null ? brthDtStr.length() : 4));
                } catch (NumberFormatException e) {
                    row.createCell(3).setCellValue("N/A");
                    maxColumnWidths[3] = Math.max(maxColumnWidths[3], 3); // "N/A"의 길이
                }
            } else {
                row.createCell(3).setCellValue("N/A");
                maxColumnWidths[3] = Math.max(maxColumnWidths[3], 3); // "N/A"의 길이
            }
//            row.getCell(3).setCellStyle(onlyCenter);

            // "소속", "부서", "직위", "휴대폰번호", "직장번호", "이메일", "출처", "비고", "meta 키워드" 열
            String[] columns = {
                (String) expert.get("ogdpNm"),
                (String) expert.get("deptNm"),
                (String) expert.get("jbpsNm"),
                (String) expert.get("mblTelno"),
                (String) expert.get("coTelno"),
                (String) expert.get("emlAddr"),
                (String) expert.get("srcNm"),
                (String) expert.get("rmrkCn"),
                (String) expert.get("kywdNm"),
            };
            
            for (int i = 4; i < columns.length + 4; i++) {
                String columnValue = columns[i - 4];
                if (columnValue != null) {
                    row.createCell(i).setCellValue(columnValue);
                    maxColumnWidths[i] = Math.max(maxColumnWidths[i], columnValue.length());
                } else {
                    row.createCell(i).setCellValue(""); // null 값에 대한 처리
                    maxColumnWidths[i] = Math.max(maxColumnWidths[i], 1); // null일 경우 길이를 1으로 설정
                }
                
                if(maxColumnWidths[12] < 10) {
                	maxColumnWidths[12] = 10;
                }
                
            }
            
            findNum.put("exprt_mng_no", expert.get("exprtMngNo"));
            maxRow = rowNum;
            
			// 전문가 최근활동
            boolean rcntFirst = true;
			rcntActyList = bpsimCommonService.getList("Expert.getRcntActyList", findNum);
			if(rcntActyList != null) {
				for (Object rn : rcntActyList) {
					Map<String, Object> rcntActy = (Map<String, Object>) rn;  // 명시적 캐스팅
					if(rcntFirst) {
						rowNum = saveNum;
						rcntFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					
					String rcnt_actv_ogdp_nm = (String) rcntActy.get("RCNT_ACTV_OGDP_NM");
					row.createCell(13).setCellValue(rcnt_actv_ogdp_nm);
					maxColumnWidths[13] = Math.max(maxColumnWidths[13], (rcnt_actv_ogdp_nm != null ? rcnt_actv_ogdp_nm.length() : 4));

					String rcnt_actv_jbttl_nm = (String) rcntActy.get("RCNT_ACTV_JBTTL_NM");
					row.createCell(14).setCellValue(rcnt_actv_jbttl_nm);
					maxColumnWidths[14] = Math.max(maxColumnWidths[14], (rcnt_actv_jbttl_nm != null ? rcnt_actv_jbttl_nm.length() : 4));
					
		            String rcnt_actv_bgng_yr_str = (String) rcntActy.get("RCNT_ACTV_BGNG_YR");
		            if (rcnt_actv_bgng_yr_str != null && !rcnt_actv_bgng_yr_str.isEmpty()) {
		                try {
		                    int rcnt_actv_bgng_yr = Integer.parseInt(rcnt_actv_bgng_yr_str);
		                    row.createCell(15).setCellValue(rcnt_actv_bgng_yr);
		                    maxColumnWidths[15] = Math.max(maxColumnWidths[15], (rcnt_actv_bgng_yr_str != null ? rcnt_actv_bgng_yr_str.length() : 4));
		                } catch (NumberFormatException e) {
		                    row.createCell(15).setCellValue("");
		                    maxColumnWidths[15] = Math.max(maxColumnWidths[15], 4); // "N/A"의 길이
		                }
		            } else {
		                row.createCell(15).setCellValue("");
		                maxColumnWidths[15] = Math.max(maxColumnWidths[15], 4); // "N/A"의 길이
		            }
//		            row.getCell(15).setCellStyle(onlyCenter);
		            
		            String rcnt_actv_end_yr_str = (String) rcntActy.get("RCNT_ACTV_END_YR");
		            if (rcnt_actv_end_yr_str != null && !rcnt_actv_end_yr_str.isEmpty()) {
		                try {
		                    int rcnt_actv_end_yr = Integer.parseInt(rcnt_actv_end_yr_str);
		                    row.createCell(16).setCellValue(rcnt_actv_end_yr);
		                    maxColumnWidths[16] = Math.max(maxColumnWidths[16], (rcnt_actv_bgng_yr_str != null ? rcnt_actv_bgng_yr_str.length() : 4));
		                } catch (NumberFormatException e) {
		                    row.createCell(16).setCellValue("");
		                    maxColumnWidths[16] = Math.max(maxColumnWidths[16], 4); // "N/A"의 길이
		                }
		            } else {
		                row.createCell(16).setCellValue("");
		                maxColumnWidths[16] = Math.max(maxColumnWidths[16], 4); // "N/A"의 길이
		            }
//		            row.getCell(16).setCellStyle(onlyCenter);
				}
			}

			// 전문가 학력사항
			boolean acbgFirst = true;
			acbgList = bpsimCommonService.getList("Expert.getAcbgList", findNum);
			if(acbgList != null) {
				for (Object ac : acbgList) {
					Map<String, Object> acbg = (Map<String, Object>) ac;  // 명시적 캐스팅
					if(acbgFirst) {
						rowNum = saveNum;
						acbgFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					String acbg_se_nm = (String) acbg.get("ACBG_SE_NM");
					row.createCell(17).setCellValue(acbg_se_nm);
//					row.getCell(17).setCellStyle(onlyCenter);
					maxColumnWidths[17] = Math.max(maxColumnWidths[17], (acbg_se_nm != null ? acbg_se_nm.length() : 4));
					
					String acbg_schl_nm = (String) acbg.get("ACBG_SCHL_NM");
					row.createCell(18).setCellValue(acbg_schl_nm);
					maxColumnWidths[18] = Math.max(maxColumnWidths[18], (acbg_schl_nm != null ? acbg_schl_nm.length() : 4));


					String acbg_mjr_nm = (String) acbg.get("ACBG_MJR_NM");
					row.createCell(19).setCellValue(acbg_mjr_nm);
					maxColumnWidths[19] = Math.max(maxColumnWidths[19], (acbg_mjr_nm != null ? acbg_mjr_nm.length() : 4));
					
					
					String acbg_grdtn_yr_str = (String) acbg.get("ACBG_GRDTN_YR");
					if (acbg_grdtn_yr_str != null && !acbg_grdtn_yr_str.isEmpty()) {
					    try {
					        int acbg_grdtn_yr = Integer.parseInt(acbg_grdtn_yr_str);
					        row.createCell(20).setCellValue(acbg_grdtn_yr);
					        maxColumnWidths[20] = Math.max(maxColumnWidths[20], (acbg_grdtn_yr_str != null ? acbg_grdtn_yr_str.length() : 4));
					    } catch (NumberFormatException e) {
					        row.createCell(20).setCellValue(""); // 예외 발생 시 빈 값
					        maxColumnWidths[20] = Math.max(maxColumnWidths[20], 4); // 기본 길이 설정
					    }
					} else {
					    row.createCell(20).setCellValue(""); // null 또는 빈 문자열일 때 빈 값
					    maxColumnWidths[20] = Math.max(maxColumnWidths[20], 4); // 기본 길이 설정
					}
//					row.getCell(20).setCellStyle(onlyCenter);
				}
			}
			
			// 전문가 경력사항
			boolean crrFirst = true;
			crrList = bpsimCommonService.getList("Expert.getCrrList", findNum);
			if(crrList != null) {
				for (Object cr : crrList) {
					Map<String, Object> crr = (Map<String, Object>) cr;  // 명시적 캐스팅
					if(crrFirst) {
						rowNum = saveNum;
						crrFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					
					String crr_ogdp_nm = (String) crr.get("CRR_OGDP_NM");
					row.createCell(21).setCellValue(crr_ogdp_nm);
					maxColumnWidths[21] = Math.max(maxColumnWidths[21], (crr_ogdp_nm != null ? crr_ogdp_nm.length() : 4));

					String crr_jbttl_nm = (String) crr.get("CRR_JBTTL_NM");
					row.createCell(22).setCellValue(crr_jbttl_nm);
					maxColumnWidths[22] = Math.max(maxColumnWidths[22], (crr_jbttl_nm != null ? crr_jbttl_nm.length() : 4));

					String crr_bgng_yr_str = (String) crr.get("CRR_BGNG_YR");
					if (crr_bgng_yr_str != null && !crr_bgng_yr_str.isEmpty()) {
					    try {
					        int crr_bgng_yr = Integer.parseInt(crr_bgng_yr_str);
					        row.createCell(23).setCellValue(crr_bgng_yr);
					        maxColumnWidths[23] = Math.max(maxColumnWidths[23], (crr_bgng_yr_str != null ? crr_bgng_yr_str.length() : 4));
					    } catch (NumberFormatException e) {
					        row.createCell(23).setCellValue(""); // 예외 발생 시 빈 값
					        maxColumnWidths[23] = Math.max(maxColumnWidths[23], 4); // 기본 길이 설정
					    }
					} else {
					    row.createCell(23).setCellValue(""); // null 또는 빈 문자열일 때 빈 값
					    maxColumnWidths[23] = Math.max(maxColumnWidths[23], 4); // 기본 길이 설정
					}
//					row.getCell(23).setCellStyle(onlyCenter);

					String crr_end_yr_str = (String) crr.get("CRR_END_YR");
					if (crr_end_yr_str != null && !crr_end_yr_str.isEmpty()) {
					    try {
					        int crr_end_yr = Integer.parseInt(crr_end_yr_str);
					        row.createCell(24).setCellValue(crr_end_yr);
					        maxColumnWidths[24] = Math.max(maxColumnWidths[24], (crr_end_yr_str != null ? crr_end_yr_str.length() : 4));
					    } catch (NumberFormatException e) {
					        row.createCell(24).setCellValue(""); // 예외 발생 시 빈 값
					        maxColumnWidths[24] = Math.max(maxColumnWidths[24], 4); // 기본 길이 설정
					    }
					} else {
					    row.createCell(24).setCellValue(""); // null 또는 빈 문자열일 때 빈 값
					    maxColumnWidths[24] = Math.max(maxColumnWidths[24], 4); // 기본 길이 설정
					}
//					row.getCell(24).setCellStyle(onlyCenter);
				}				
			}

			// 전문가 수상내역
			boolean wnawdFirst = true;
			wnawdList = bpsimCommonService.getList("Expert.getWnawdList", findNum);			
			if(wnawdList != null) {
				for (Object wn : wnawdList) {
					Map<String, Object> wnawd = (Map<String, Object>) wn;  // 명시적 캐스팅
					if( wnawdFirst) {
						rowNum = saveNum;
						wnawdFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					
					String wnawd_dsctn = (String) wnawd.get("WNAWD_DSCTN");
					row.createCell(25).setCellValue(wnawd_dsctn);
					maxColumnWidths[25] = Math.max(maxColumnWidths[25], (wnawd_dsctn != null ? wnawd_dsctn.length() : 4));

					String wnawd_yr_str = (String) wnawd.get("WNAWD_YR");
					if (wnawd_yr_str != null && !wnawd_yr_str.isEmpty()) {
					    try {
					        int wnawd_yr = Integer.parseInt(wnawd_yr_str);
					        row.createCell(26).setCellValue(wnawd_yr);
					        maxColumnWidths[26] = Math.max(maxColumnWidths[26], (wnawd_yr_str != null ? wnawd_yr_str.length() : 4));
					    } catch (NumberFormatException e) {
					        row.createCell(26).setCellValue(""); // 예외 발생 시 빈 값
					        maxColumnWidths[26] = Math.max(maxColumnWidths[26], 4); // 기본 길이 설정
					    }
					} else {
					    row.createCell(26).setCellValue(""); // null 또는 빈 문자열일 때 빈 값
					    maxColumnWidths[26] = Math.max(maxColumnWidths[26], 4); // 기본 길이 설정
					}
//					row.getCell(26).setCellStyle(onlyCenter);
				}				
			}			
			
			// 전문가 분류(정책,신바이오)
			boolean plcyFirst = true;
			plcyClsfList = bpsimCommonService.getList("Expert.getPlcyClsfList", findNum);
			if(plcyClsfList != null) {
				for (Object pl : plcyClsfList) {
					Map<String, Object> plcy = (Map<String, Object>) pl;  // 명시적 캐스팅
					if(plcyFirst) {
						rowNum = saveNum;
						plcyFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					
					String lclsf_nm = (String) plcy.get("LCLSF_NM");
					row.createCell(27).setCellValue(lclsf_nm);
					maxColumnWidths[27] = Math.max(maxColumnWidths[27], (lclsf_nm != null ? lclsf_nm.length() : 4));

					String mclsf_nm = (String) plcy.get("MCLSF_NM");
					row.createCell(28).setCellValue(mclsf_nm);
					maxColumnWidths[28] = Math.max(maxColumnWidths[28], (mclsf_nm != null ? mclsf_nm.length() : 4));

					String sclsf_nm = (String) plcy.get("SCLSF_NM");
					row.createCell(29).setCellValue(sclsf_nm);
					maxColumnWidths[29] = Math.max(maxColumnWidths[29], (sclsf_nm != null ? sclsf_nm.length() : 4));
				}				
			}			
			
			boolean bioFirst = true;
			bioClsfList = bpsimCommonService.getList("Expert.getBioClsfList", findNum);
			if(bioClsfList != null) {
				for (Object bi : bioClsfList) {
					Map<String, Object> bio = (Map<String, Object>) bi;  // 명시적 캐스팅
					if(bioFirst) {
						rowNum = saveNum;
						bioFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					
					String lclsf_nm = (String) bio.get("LCLSF_NM");
					row.createCell(30).setCellValue(lclsf_nm);
					maxColumnWidths[30] = Math.max(maxColumnWidths[30], (lclsf_nm != null ? lclsf_nm.length() : 4));

					String mclsf_nm = (String) bio.get("MCLSF_NM");
					row.createCell(31).setCellValue(mclsf_nm);
					maxColumnWidths[31] = Math.max(maxColumnWidths[31], (mclsf_nm != null ? mclsf_nm.length() : 4));

					String sclsf_nm = (String) bio.get("SCLSF_NM");
					row.createCell(32).setCellValue(sclsf_nm);
					maxColumnWidths[32] = Math.max(maxColumnWidths[32], (sclsf_nm != null ? sclsf_nm.length() : 4));
				}				
			}
			// 센터 활용실적 내역
			boolean utlzFirst = true;
			utlzDsctnList = bpsimCommonService.getList("Expert.getExprtUtlzDsctnList", findNum);
			if(utlzDsctnList != null) {
				for (Object ut : utlzDsctnList) {
					Map<String, Object> utlz = (Map<String, Object>) ut;  // 명시적 캐스팅
					if(utlzFirst) {
						rowNum = saveNum;
						utlzFirst = false;
					}else {
						rowNum += 1;
						maxRow = Math.max(maxRow, rowNum);
					}
					row = sheet.getRow(rowNum);
					if (row == null) {
					    row = sheet.createRow(rowNum); // 존재하지 않으면 새로 생성
					}
					String utlz_ymd = (String) utlz.get("UTLZ_YMD");
					row.createCell(33).setCellValue(utlz_ymd);
//					row.getCell(33).setCellStyle(onlyCenter);
					maxColumnWidths[33] = Math.max(maxColumnWidths[33], (utlz_ymd != null ? utlz_ymd.length() : 4));

					String utlz_prps = (String) utlz.get("UTLZ_PRPS");
					row.createCell(34).setCellValue(utlz_prps);
					maxColumnWidths[34] = Math.max(maxColumnWidths[34], (utlz_prps != null ? utlz_prps.length() : 4));

					String utlz_rmrk_cn = (String) utlz.get("UTLZ_RMRK_CN");
					row.createCell(35).setCellValue(utlz_rmrk_cn);
					maxColumnWidths[35] = Math.max(maxColumnWidths[35], (utlz_rmrk_cn != null ? utlz_rmrk_cn.length() : 4));
					
					String info_exprtuser = (String) utlz.get("INFO_EXPRTUSER");
					row.createCell(36).setCellValue(info_exprtuser);
					maxColumnWidths[36] = Math.max(maxColumnWidths[36], (info_exprtuser != null ? info_exprtuser.length() : 4));
				}				
			}
			for(int i = saveNum; i<= maxRow; i++) {
				row = sheet.getRow(i);		
				for (int j = 0; j <= 36; j++) {
					cell = row.getCell(j);
					if (cell == null) {
						cell = row.createCell(j);
					}
					
	                if(realNum % 2 == 1) {
	                	cell.setCellStyle(Style1);  // 홀수
	                }else {
	                	cell.setCellStyle(Style2);  // 짝수
	                }
				}
			}
			row = sheet.getRow(maxRow);
            // 한 번에 0번부터 36번까지 셀 스타일을 적용하는 방법
            for (int i = 0; i <= 36; i++) {
                cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);  // 셀이 없을 경우 생성
                }
                if(realNum % 2 == 0) {
                	cell.setCellStyle(expertStyle2);  // 짝수
                }else {
                	cell.setCellStyle(expertStyle);  // 홀수
                }
            }
			realNum += 1;
			rowNum = maxRow + 1;
			saveNum = rowNum;
        }

        // 열 너비 설정 (계산된 최대 길이 기준)
        for (int i = 0; i < maxColumnWidths.length; i++) {
            int calculatedWidth = (maxColumnWidths[i] + 2) * 390; // 기본 계산식
            if (calculatedWidth > 250 * 256) {
                calculatedWidth = 250 * 256; // 최대 열 너비는 255 * 256 (엑셀은 1/256 단위로 너비를 관리)
            }
            sheet.setColumnWidth(i, calculatedWidth);
        }        

        // 1. 서버에 파일 저장
        // 문성훈 실장님이 서버 파일 요청시 --> BPSIM WEB서버 -> excel -> expert 내부에 있는 파일 드리면 됩니다.
        String ROOT_ABS_PATH = CommonUtil.getAppRes("Appcommon.EXCEL_PATH"); 
        String UPLOAD_DIR_ABS_PATH = request.getSession().getServletContext().getRealPath("/") + ROOT_ABS_PATH;
        
        // 파일명 설정        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = sdf.format(new Date());
        String fileName = "expert_list_" + loginInfo.getLoginid() + "_" + currentDate + ".xlsx"; // 파일명에 타임스탬프 추가
        String filePath = UPLOAD_DIR_ABS_PATH + fileName;
        
        File directory = new File(UPLOAD_DIR_ABS_PATH);
        if (!directory.exists()) {
            directory.mkdirs();  // 디렉토리 생성
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
        	workbook.write(fileOut);  // 서버에 파일 저장
        	System.out.println("엑셀 파일 서버에 저장 완료: " + filePath);
        } catch (IOException e) {
        	e.printStackTrace();
        }        
        
        // 2. 사용자에게 파일 다운로드
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        try (FileInputStream fileIn = new FileInputStream(filePath);
    		ServletOutputStream outputStream = response.getOutputStream()) {
    	    byte[] buffer = new byte[4096];
    	    int bytesRead;
    	    while ((bytesRead = fileIn.read(buffer)) != -1) {
    	        outputStream.write(buffer, 0, bytesRead);
    	    }
    	    outputStream.flush();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
	}
	
	// 중앙 정렬 및 배경색을 적용한 셀 스타일을 생성하는 함수
	private CellStyle createCellStyle(XSSFWorkbook workbook, IndexedColors color) {
	    CellStyle style = workbook.createCellStyle();
	    style.setAlignment(CellStyle.ALIGN_CENTER);  // 가로 중앙 정렬
	    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  // 세로 중앙 정렬
	    style.setFillForegroundColor(color.getIndex());  // 배경색 설정
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 셀 색상 채우기 패턴 설정
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBorderTop(CellStyle.BORDER_THIN);		    
	    return style;
	}
	
	@RequestMapping(value="/expert/excelFormDown.do")
	public void expertFormDown(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
//        String filePath = "/excel/expert/form/바이오분야별_대표연구자_명단_입력양식.xlsx";
        String ROOT_ABS_PATH = CommonUtil.getAppRes("Appcommon.EXCEL_FORM_PATH"); 
        String DOWNLOAD_DIR_ABS_PATH = request.getSession().getServletContext().getRealPath("/") + ROOT_ABS_PATH;
        String fileName = "바이오분야별_대표연구자_명단_입력양식.xlsx"; // 파일명에 타임스탬프 추가
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        String filePath = DOWNLOAD_DIR_ABS_PATH + fileName;        
        File file = new File(filePath);		
        if (!file.exists()) {
            // 파일이 없을 경우 처리 (예: 404 에러 반환)
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
            return;
        }        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");                
        
        // 파일을 응답으로 전송
        try (FileInputStream fileIn = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}

	
}
