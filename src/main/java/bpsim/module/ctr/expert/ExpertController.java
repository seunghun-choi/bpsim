package bpsim.module.ctr.expert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
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
import bpsim.module.ctr.usr.LoginController;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller("bpsim.module.ctr.expert.ExpertController")	 
public class ExpertController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@RequestMapping(value="/expert/expertList.do")
	public ModelAndView expertList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		mav = getClsfCodeList(params, mav);
		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("cPage"), "1");
		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
		params.put("cPage", cPage);
		
		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
		int pageCnt = 10;									/* 가로페이징(페이지수) */
		int totalCnt = 0;										/* 총 조회수  */
		params.put("intListCnt", intListCnt);
		totalCnt = bpsimCommonService.getDataCnt("Expert.getExpertListCnt", params);
		
		// 페이지 네비케이터
		PageNavigator pageNavigator = new PageNavigator(
			intPage		
		   ,"/expert/expertList.do"
		   ,pageCnt		
		   ,intListCnt	
		   ,totalCnt	
//		   ,serializedParams
		   ,""
		);
		
		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
		
		
		List expertList = bpsimCommonService.getList("Expert.getExpertList", params, 0, pageNavigator.getRecordPerPage());				//리스트
		int fullCnt = bpsimCommonService.getDataCnt("Expert.getExpertFullCnt", params);
		List policyLargeCode = bpsimCommonService.getList("Code.getPolicyLargeCode", params);				//리스트
		List newBioLargeCode = bpsimCommonService.getList("Code.getNewBioLclsfList", params);				//리스트
		
//		List expertList = bpsimCommonService.getList("Expert.getExpertList", params);
		
		if(loginInfo != null){
			mav.setViewName("jsp/expert/expertList");
		}else{
			mav.setViewName("redirect:/login.do");
		}
		
		String back_params = "";
		// 파라미터(목록에서 뷰로 갔다가 다시 목록으로 와도 해당 상태를 유지하기 위해)
		String[] keyset = { "dateSearch", "startDate", "search1", "searchInput1", "search2", "searchInput2", "search3",
				"searchInput3", "largeCategory1", "mediumCategory1", "smallCategory1", "largeCategory2",
				"mediumCategory2", "smallCategory2", "largeCategoryNm1", "mediumCategoryNm1", 
				"smallCategoryNm1", "largeCategoryNm2", "mediumCategoryNm2", "smallCategoryNm2", "overDay"};
		for (String s : keyset) {
			if (params.get(s) != null) {
				back_params += "&" + s + "=" + params.get(s);
			}
		}
		
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("expertList", expertList);
		mav.addObject("policyLargeCode", policyLargeCode);
		mav.addObject("newBioLargeCode", newBioLargeCode);
		mav.addObject("pageNavigator", pageNavigator.getMakePage());
		mav.addObject("totalCnt", totalCnt);
		mav.addObject("totalPage", totalPage);
		mav.addObject("cPage", intPage);
		mav.addObject("listCnt", listCnt);
		mav.addObject("pageCnt", pageCnt);
		mav.addObject("fullCnt", fullCnt);
		
		return mav;
	}
	
	// 정책코드 대분류
	@RequestMapping(value="/expert/getPolicyLargeCode.do")
	public ModelAndView policyLargeCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List policyLargeCode = bpsimCommonService.getList("Code.getPolicyLargeCode", params);
		
		mav.addObject("policyLargeCode", policyLargeCode);
		return mav;
	}
	// 정책코드 중분류	
	@RequestMapping(value="/expert/getPolicyMediumCode.do")
	public ModelAndView getPolicyMediumCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List policyMediumCode = bpsimCommonService.getList("Code.getPolicyMediumCode", params);
		
		mav.addObject("policyMediumCode", policyMediumCode);
		return mav;
	}
	// 정책코드 소분류	
	@RequestMapping(value="/expert/getPolicySmallCode.do")
	public ModelAndView getPolicySmallCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List policySmallCode = bpsimCommonService.getList("Code.getPolicySmallCode", params);
		
		mav.addObject("policySmallCode", policySmallCode);
		return mav;
	}
	
	// 신바이오 대분류	
	@RequestMapping(value="/expert/getNewBioLargeCode.do")
	public ModelAndView getNewBioLargeCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List newBioLargeCode = bpsimCommonService.getList("Code.getNewBioLclsfList", params);
		
		mav.addObject("newBioLargeCode", newBioLargeCode);
		return mav;
	}
	// 신바이오 중분류	
	@RequestMapping(value="/expert/getNewBioMediumCode.do")
	public ModelAndView getNewBioMediumCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List newBioMediumCode = bpsimCommonService.getList("Code.getNewBioMclsfList", params);
		
		mav.addObject("newBioMediumCode", newBioMediumCode);
		return mav;
	}
	// 신바이오 소분류
	@RequestMapping(value="/expert/getNewBioSmallCode.do")
	public ModelAndView getNewBioSmallCode(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
		List newBioSmallCode = bpsimCommonService.getList("Code.getNewBioSclsfList", params);
		
		mav.addObject("newBioSmallCode", newBioSmallCode);
		return mav;
	}
	
	@RequestMapping(value="/expert/expertAdd.do")
	public ModelAndView expertAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		int tempCnt = 0;
		if(loginInfo != null){
			try {
				// 전문가 엑셀등록 임시저장 여부 (엑셀로 등록 중이던 데이터가 있는지 확인)
				params.put("loginid", loginInfo.getLoginid());
				tempCnt = bpsimCommonService.getListCount("ExpertTemp.getTempExprtBscInfoCount", params);
				
				if(tempCnt > 0){ // 임시저장 중이던 데이터 있으면 엑셀등록확인 확면
					mav.setViewName("redirect:/expert/expertExcelInfoConfirm.do");
				} else {// 임시저장 중이던 데이터 없으면 엑셀등록화면
					mav.setViewName("jsp/expert/expertAdd");
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			mav.setViewName("redirect:/login.do");
		}
		
		if(params.get("errorMessage") != null && !params.get("errorMessage").equals("")) {
			mav.addObject("errorMessage", params.get("errorMessage"));
		}
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		
		return mav;
	}	

	// 전문인력정보 상세화면
	@RequestMapping(value = "/expert/expertDetail.do")
	public ModelAndView expertDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Throwable {

		ModelAndView mav = new ModelAndView();

		LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), "");
		String back_params = ReqUtils.getEmptyResult2((String) params.get("back_params"), "");

		Map expert = null;
		List rcntActyList = null; // 최근활동
		List acbgList = null; // 학력사항
		List crrList = null; // 경력사항
		List plcyClsfList = null; // 정책분류
		List bioClsfList = null; // 신바이오분류
		List wnawdList = null; // 수상내역
		List utlzDsctnList = null;  // 센터 활용실적
		
		if (loginInfo != null) {
			mav.setViewName("jsp/expert/expertDetail");
			try {
				// 전문가 상세정보
				expert = bpsimCommonService.getObjectMap("Expert.getExpertDetail", params);
	
				// 전문가 최근활동
				rcntActyList = bpsimCommonService.getList("Expert.getRcntActyList", params);
				// 전문가 학력사항
				acbgList = bpsimCommonService.getList("Expert.getAcbgList", params);
				// 전문가 경력사항
				crrList = bpsimCommonService.getList("Expert.getCrrList", params);
				// 전문가 분류(정책,신바이오)
				plcyClsfList = bpsimCommonService.getList("Expert.getPlcyClsfList", params);
				bioClsfList = bpsimCommonService.getList("Expert.getBioClsfList", params);
				// 전문가 수상내역
				wnawdList = bpsimCommonService.getList("Expert.getWnawdList", params);
				// 센터 활용실적 내역
				utlzDsctnList = bpsimCommonService.getList("Expert.getExprtUtlzDsctnList", params);
				
				// 조회수 증가
				bpsimCommonService.update("Expert.updateExpertInqCnt", params);
	
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else {
			mav.setViewName("redirect:/login.do");
		}

		if ("".equals(exprt_mng_no)) {
			mav.setViewName("redirect:/expert/expertList.do");
		}

		// 파라미터(목록에서 뷰로 갔다가 다시 목록으로 와도 해당 상태를 유지하기 위해)
		String[] keyset = { "dateSearch", "startDate", "search1", "searchInput1", "search2", "searchInput2", "search3",
				"searchInput3", "largeCategory1", "mediumCategory1", "smallCategory1", "largeCategory2",
				"mediumCategory2", "smallCategory2", "largeCategoryNm1", "mediumCategoryNm1", 
				"smallCategoryNm1", "largeCategoryNm2", "mediumCategoryNm2", "smallCategoryNm2" };
		for (String s : keyset) {
			if (params.get(s) != null) {
				back_params += "&" + s + "=" + params.get(s);
			}
		}

		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("expert", expert);
		mav.addObject("rcntActyList", rcntActyList);
		mav.addObject("acbgList", acbgList);
		mav.addObject("crrList", crrList);
		mav.addObject("plcyClsfList", plcyClsfList);
		mav.addObject("bioClsfList", bioClsfList);
		mav.addObject("wnawdList", wnawdList);
		mav.addObject("utlzDsctnList", utlzDsctnList);

		return mav;
	}

	// 전문인력정보 수정화면
	@RequestMapping(value = "/expert/expertMod.do")
	public ModelAndView expertMod(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Throwable {

		ModelAndView mav = new ModelAndView();

		LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), "");
		String back_params = ReqUtils.getEmptyResult2((String) params.get("back_params"), "");


		Map expert = null;
		List rcntActyList = null; // 최근활동
		List acbgList = null; // 학력사항
		List crrList = null; // 경력사항
		List plcyClsfList = null; // 정책분류
		List bioClsfList = null; // 신바이오분류
		List wnawdList = null; // 수상내역
		List utlzDsctnList = null;  // 센터 활용실적
		Map file = null;

		try {
			// 전문가 상세정보
			expert = bpsimCommonService.getObjectMap("Expert.getExpertDetail", params);

			// 전문가 최근활동
			rcntActyList = bpsimCommonService.getList("Expert.getRcntActyList", params);
			// 전문가 학력사항
			acbgList = bpsimCommonService.getList("Expert.getAcbgList", params);
			// 전문가 경력사항
			crrList = bpsimCommonService.getList("Expert.getCrrList", params);
			// 전문가 분류(정책,신바이오)
			plcyClsfList = bpsimCommonService.getList("Expert.getPlcyClsfList", params);
			bioClsfList = bpsimCommonService.getList("Expert.getBioClsfList", params);
			// 전문가 수상내역
			wnawdList = bpsimCommonService.getList("Expert.getWnawdList", params);
			// 센터 활용실적 내역
			utlzDsctnList = bpsimCommonService.getList("Expert.getExprtUtlzDsctnList", params);
			
			// 코드
			mav = getClsfCodeList(params, mav);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (loginInfo != null) {
			mav.setViewName("jsp/expert/expertMod");
		} else {
			mav.setViewName("redirect:/login.do");
		}

		if ("".equals(exprt_mng_no)) {
			mav.setViewName("redirect:/expert/expertList.do");
		}
		
		// 파라미터(목록에서 뷰로 갔다가 다시 목록으로 와도 해당 상태를 유지하기 위해)
		String[] keyset = { "dateSearch", "startDate", "search1", "searchInput1", "search2", "searchInput2", "search3",
				"searchInput3", "largeCategory1", "mediumCategory1", "smallCategory1", "largeCategory2",
				"mediumCategory2", "smallCategory2" };
		for (String s : keyset) {
			if (params.get(s) != null) {
				back_params += "&" + s + "=" + params.get(s);
			}
		}

		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("expert", expert);
		mav.addObject("rcntActyList", rcntActyList);
		mav.addObject("acbgList", acbgList);
		mav.addObject("crrList", crrList);
		mav.addObject("plcyClsfList", plcyClsfList);
		mav.addObject("bioClsfList", bioClsfList);
		mav.addObject("wnawdList", wnawdList);
		mav.addObject("utlzDsctnList", utlzDsctnList);
		mav.addObject("file", file);

		return mav;
	}

	// 전문인력정보 담당자확인
	@RequestMapping(value = "/expert/expertInfoConfirm.do")
	public ModelAndView expertInfoConfirm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Throwable {

		ModelAndView mav = new ModelAndView();

		LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), "");
		String back_params = ReqUtils.getEmptyResult2((String) params.get("back_params"), "");
		
		if (loginInfo != null) {
			try {
				// 담당자 확인 처리
				bpsimCommonService.update("Expert.updateLastIndty", params);

			} catch (Exception e) {
				e.printStackTrace();
			}
			mav.setViewName("redirect:/expert/expertDetail.do");
		} else {
			mav.setViewName("redirect:/login.do");
		}

		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("exprt_mng_no", exprt_mng_no);

		return mav;
	}
	
	// 전문인력정보 수정
	@RequestMapping(value="/expert/updateExpertInfo.do")
	public ModelAndView updateExpertInfo(@RequestParam("profilePhoto") MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		String exprt_mng_no = ReqUtils.getEmptyResult2((String) params.get("exprt_mng_no"), ""); // 전문가관리번호
		String exprt_nm = ReqUtils.getEmptyResult2((String) params.get("exprt_nm"), ""); // 전문가명
		
		String back_params = ReqUtils.getEmptyResult2((String) params.get("back_params"), "");
		
		String img_file_path_nm = ReqUtils.getEmptyResult2((String) params.get("img_file_path_nm"), ""); //이미지 저장경로명
		String img_file_del_yn = ReqUtils.getEmptyResult2((String) params.get("img_file_del_yn"), "");	// 파일 삭제여부
		
		if(loginInfo != null){
			// 프로필 이미지 삭제
			if("Y".equals(img_file_del_yn)) {
				String filePath = request.getSession().getServletContext().getRealPath("/") + img_file_path_nm;
				
				File f = new File(filePath);
	    		if (f.isFile()){
	    			f.delete();
	    		}
	    		 params.put("img_file_path_nm", null);
	    		 bpsimCommonService.update("Expert.updateExpertProfile", params);
			}
			
		    if (!file.isEmpty()) {
	            try {
	            	// 실제저장될 파일 위치 가져옴 (로컬, 서버 공통사용가능) / 서버 반영시 ApplicationResources 변경되어야함 
	                String ROOT_ABS_PATH = CommonUtil.getAppRes("Appcommon.PROFILE_PATH"); 
	                String UPLOAD_DIR_ABS_PATH = request.getSession().getServletContext().getRealPath("/") + ROOT_ABS_PATH;
	                
	                String fileOrgName = file.getOriginalFilename(); //원본명
	                String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
	                String fileName = exprt_mng_no + fileType;
	                
	                // 실제파일 저장 경로 및 명칭
	                String filePath = UPLOAD_DIR_ABS_PATH + fileName;
	                
	                // 저장할 디렉토리가 없으면 생성
	                File directory = new File(UPLOAD_DIR_ABS_PATH);
	                if (!directory.exists()) {
	                    directory.mkdirs();  // 디렉토리 생성
	                }
	                // 이전 프로필 사진이 존재하는 경우 삭제
	                File f = new File(filePath);
	        		if (f.isFile()){
	        			f.delete();
	        		}
	                // 신규 프로필 파일 저장
	                File dest = new File(filePath);
	                file.transferTo(dest);
	                
	                // DB 저장 파일명
	                String fileSaveNm = ROOT_ABS_PATH + fileName;
	                // 모델에 파일 경로 저장 후 전달
	                params.put("img_file_path_nm", fileSaveNm);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } 
		    
			try {
					// 전문가 기본정보 수정
					params.put("last_mdfr_id", loginInfo.getLoginid());
					bpsimCommonService.update("Expert.updateExprtBscInfo", params);
					
					// 이외 정보 입력 및 검증내용 처리
					updateEtcInfo(request,  params);
					
					params.put("loginid", loginInfo.getLoginid());
					bpsimCommonService.update("Expert.updateLastIndty", params);
					
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mav.setViewName("redirect:/expert/expertDetail.do");
		}else{
			mav.setViewName("redirect:/login.do");
		}
	    
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("back_params", back_params);
		mav.addObject("exprt_mng_no", exprt_mng_no);
		
		return mav;
	}

	// 전문가정보 수정
	public void updateEtcInfo(HttpServletRequest request, Map params) {

		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		
		/* 기본정보 이외 전문가 정보 시작 */
		// 최근활동
		String rcnt_actv_ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_ogdp_nm"), ""); // 최근활동소속명
		String rcnt_actv_jbttl_nm = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_jbttl_nm"), ""); // 최근활동직책명
		String rcnt_actv_bgng_yr = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_bgng_yr"), ""); // 최근활동시작연도
		String rcnt_actv_end_yr = ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_end_yr"), ""); // 최근활동종료연도
		int rcnt_actv_line = Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("rcnt_actv_line"), "0")); // 
		
		// 학력사항
		String acbg_se_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_se_nm"), ""); // 학력학교명
		String acbg_schl_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_schl_nm"), ""); // 학력구분명
		String acbg_mjr_nm = ReqUtils.getEmptyResult2((String) params.get("acbg_mjr_nm"), ""); // 학력전공명
		String acbg_grdtn_yr = ReqUtils.getEmptyResult2((String) params.get("acbg_grdtn_yr"), ""); // 학력졸업년도
		int acbg_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("acbg_line"), "0")); // 
		
		//경력사항
		String crr_ogdp_nm = ReqUtils.getEmptyResult2((String) params.get("crr_ogdp_nm"), ""); // 경력소속명
		String crr_jbttl_nm = ReqUtils.getEmptyResult2((String) params.get("crr_jbttl_nm"), ""); // 경력직책명
		String crr_bgng_yr = ReqUtils.getEmptyResult2((String) params.get("crr_bgng_yr"), ""); // 경력시작년도
		String crr_end_yr = ReqUtils.getEmptyResult2((String) params.get("crr_end_yr"), ""); // 경력종료년도
		int crr_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("crr_line"), "0")); // 
		
		//신바이오분류체계
		String bioClsf_lclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_lclsf_cd"), ""); // 대분류코드
		String bioClsf_mclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_mclsf_cd"), ""); // 중분류코드
		String bioClsf_sclsf_cd = ReqUtils.getEmptyResult2((String) params.get("bioClsf_sclsf_cd"), ""); // 소분류코드
		int bioClsf_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("bioClsf_line"), "0")); // 
		
		//정책분류코드
		String plcyClsf_lclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_lclsf_cd"), ""); // 대분류코드
		String plcyClsf_mclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_mclsf_cd"), ""); // 중분류코드
		String plcyClsf_sclsf_cd = ReqUtils.getEmptyResult2((String) params.get("plcyClsf_sclsf_cd"), ""); // 소분류코드
		int plcyClsf_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("plcyClsf_line"), "0")); //
		
		//수상내역
		String wnawd_dsctn = ReqUtils.getEmptyResult2((String) params.get("wnawd_dsctn"), ""); // 수상내역
		String wnawd_yr = ReqUtils.getEmptyResult2((String) params.get("wnawd_yr"), ""); // 수상연도
		int wnawd_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("wnawd_line"), "0")); //
		
		//센터 활용실적
		String utlz_ymd = ReqUtils.getEmptyResult2((String) params.get("utlz_ymd"), ""); // 활용일자
		String utlz_prps = ReqUtils.getEmptyResult2((String) params.get("utlz_prps"), ""); // 활용목적
		String info_exprtuser = ReqUtils.getEmptyResult2((String) params.get("info_exprtuser"), ""); // 정보활용자
		String utlz_rmrk_cn = ReqUtils.getEmptyResult2((String) params.get("utlz_rmrk_cn"), ""); // 비고
		int utlz_line =  Integer.parseInt(ReqUtils.getEmptyResult2((String) params.get("utlz_line"), "0")); //
	
		try {
			if(loginInfo != null) {
				Map insertMap = new HashedMap();
				insertMap.put("exprt_mng_no",params.get("exprt_mng_no"));
				
				/* 
				 * split("\\|", -1) -> 모든 공백값도 list 형태로 만들어서 for을 돌려 값을 넣어야하기 때문에 공백값도 담음
				 * 모든 하위 입력 값 삭제 후 데이터가 있는 경우 신규로 등록
				 * */
				// 최근활동
				bpsimCommonService.delete("Expert.deleteExprtRcntActv", insertMap);// 최근활동 삭제
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
						
						// 최근활동 등록
						bpsimCommonService.insert("Expert.insertExprtRcntActy", insertMap);
					}
				}
				
				// 학력사항
				bpsimCommonService.delete("Expert.deleteExprtAcbg", insertMap);// 학력사항 삭제
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
						
						// 학력사항 등록
						bpsimCommonService.insert("Expert.insertExprtAcbg", insertMap);
					}
				}
				
				// 경력사항
				bpsimCommonService.delete("Expert.deleteExprtCrr", insertMap);// 경력사항 삭제
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
						
						// 경력사항 등록
						bpsimCommonService.insert("Expert.insertExprtCrr", insertMap);
					}
				}
				
				// 신바이오분류체계
				bpsimCommonService.delete("Expert.deleteExprtBioClsf", insertMap);// 신바이오분류체계 삭제
				if(bioClsf_line > 0) {
		            String[] bioClsf_lclsf_cdList = bioClsf_lclsf_cd.split("\\|", -1); 
		            String[] bioClsf_mclsf_cdList = bioClsf_mclsf_cd.split("\\|", -1);
		            String[] bioClsf_sclsf_cdList = bioClsf_sclsf_cd.split("\\|", -1); 
		            
					for (int i = 0; i < bioClsf_line; i++) {
						insertMap.put("bioClsf_lclsf_cd",bioClsf_lclsf_cdList[i]);
						insertMap.put("bioClsf_mclsf_cd",bioClsf_mclsf_cdList[i]);
						insertMap.put("bioClsf_sclsf_cd",bioClsf_sclsf_cdList[i]);
						
						bpsimCommonService.insert("Expert.insertExprtBioClsf", insertMap);
					}
				}
				
				// 정책분류코드
				bpsimCommonService.delete("Expert.deleteExprtPlcyClsf", insertMap);// 정책분류코드 삭제
				if(plcyClsf_line > 0) {
		            String[] plcyClsf_lclsf_cdList = plcyClsf_lclsf_cd.split("\\|", -1); 
		            String[] plcyClsf_mclsf_cdList = plcyClsf_mclsf_cd.split("\\|", -1);
		            String[] plcyClsf_sclsf_cdList = plcyClsf_sclsf_cd.split("\\|", -1); 
		            
					for (int i = 0; i < plcyClsf_line; i++) {
						insertMap.put("plcyClsf_lclsf_cd",plcyClsf_lclsf_cdList[i]);
						insertMap.put("plcyClsf_mclsf_cd",plcyClsf_mclsf_cdList[i]);
						insertMap.put("plcyClsf_sclsf_cd",plcyClsf_sclsf_cdList[i]);
						
						// 정책분류코드 등록
						bpsimCommonService.insert("Expert.insertExprtPlcyClsf", insertMap);
					}
				}
				
				// 수상내역
				bpsimCommonService.delete("Expert.deleteExprtWnawd", insertMap);// 수상내역 삭제
				if(wnawd_line > 0) {
					String[] wnawd_dsctnList = wnawd_dsctn.split("\\|", -1); 
					String[] wnawd_yrList = wnawd_yr.split("\\|", -1);
					
					for (int i = 0; i < wnawd_line; i++) {
						insertMap.put("wnawd_dsctn",wnawd_dsctnList[i]);
						insertMap.put("wnawd_yr",wnawd_yrList[i]);
						
						// 수상내역 등록
						bpsimCommonService.insert("Expert.insertExprtWnawd", insertMap);
					}
				}
				
				// 센터 활용실적
				bpsimCommonService.delete("Expert.deleteExprtUtlzDsctn", insertMap);// 센터 활용실적 삭제
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
						
						// 센터 활용실적 등록
						bpsimCommonService.insert("Expert.insertExprtUtlzDsctn", insertMap);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 기본정보 이외 전문가 정보 종료 */
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
	
	// 전문인력정보 엑셀등록확인 전문가정보 삭제
	@RequestMapping(value="/expert/deleteExpertInfo.do")
	public ModelAndView deleteExpertInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		try {
			if(loginInfo != null){
				params.put("loginid", loginInfo.getLoginid());
				bpsimCommonService.delete("Expert.deleteExprtRcntActv", params); // 최근활동
				bpsimCommonService.delete("Expert.deleteExprtAcbg", params);	// 학력사항
				bpsimCommonService.delete("Expert.deleteExprtCrr", params);		// 경력사항
				bpsimCommonService.delete("Expert.deleteExprtBioClsf", params); // 신바이오분류코드
				bpsimCommonService.delete("Expert.deleteExprtPlcyClsf", params); // 정책분류코드
				bpsimCommonService.delete("Expert.deleteExprtWnawd", params); // 수상내역
				bpsimCommonService.delete("Expert.deleteExprtUtlzDsctn", params); // 센터 내 활용실적
				bpsimCommonService.delete("Expert.deleteBscInfo", params); // 기본정보
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
	
	// 전문인력정보 엑셀등록확인 중복체크
	@RequestMapping(value="/expert/expertDpcnChk.do")
	public ModelAndView expertDpcnChk(HttpServletRequest request) throws Throwable {
		
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		Map params = ReqUtils.getParameterMap(request);
		String excel_yn = ReqUtils.getEmptyResult2((String) params.get("excel_yn"), "N"); // 엑셀업로드화면여부
		params.put("loginid", loginInfo.getLoginid());
		
		int dpcnCnt = 0;
		int dpcnExcelCnt = 0;
		try {
			//전문인력정보 중복 조회
			dpcnCnt = bpsimCommonService.getListCount("Expert.getExprtBscInfoDpcnCnt", params);
			mav.addObject("dpcnCnt", dpcnCnt);
			
			if("Y".equals(excel_yn)) {
				dpcnExcelCnt = bpsimCommonService.getListCount("ExpertTemp.getExprtBscInfoDpcnExcelCnt", params);
				mav.addObject("dpcnExcelCnt", dpcnExcelCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
}
