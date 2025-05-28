package bpsim.module.ctr.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.PageNavigator;
import bpsim.framework.util.ReqUtils;
import bpsim.framework.util.TokenMngUtil;
import bpsim.module.ctr.usr.LoginController;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;
import bpsim.module.dto.UserInfoDTO;

@Controller("bpsim.module.ctr.auth.AuthController")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@RequestMapping("/auth/authList.do")
	public ModelAndView authList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);
		
		// 로그인 후 해당 페이지로 이동하기 위해 ..
		if(loginInfo != null){
			mav.setViewName("jsp/auth/authList");
		}else{
			mav.setViewName("redirect:/login.do");
		}
		
		mav.addObject("authDc", authDc);
		
		
		return mav;
	
	}
	
	@RequestMapping("/auth/authDetail.do")
	public ModelAndView authDtl(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();		
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("cPage"), "1");
		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
		params.put("cPage", cPage);
		
		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
		int pageCnt = 10;									/* 가로페이징(페이지수) */
		int totalCnt = 0;										/* 총 조회수  */		
		params.put("intListCnt", intListCnt);
		totalCnt = bpsimCommonService.getDataCnt("Auth.getAuthDetailCnt", params);
		
		// 페이지 네비케이터
		PageNavigator pageNavigator = new PageNavigator(
			intPage		
		   ,"/auth/authDetail.do"
		   ,pageCnt		
		   ,intListCnt	
		   ,totalCnt
		   ,""
		);		
		
		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
		
		int fullCnt = bpsimCommonService.getDataCnt("Auth.getAuthDetailFullCnt", params);		
		
		List authDetailList = bpsimCommonService.getList("Auth.getAuthDetailList", params, 0, pageNavigator.getRecordPerPage());
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);

		if(loginInfo != null){
			mav.setViewName("jsp/auth/authDetail");
		}else{
			mav.setViewName("redirect:/login.do");
		}
		mav.addObject("authDetailList", authDetailList);
		mav.addObject("authDc", authDc);
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("pageNavigator", pageNavigator.getMakePage());
		mav.addObject("totalCnt", totalCnt);
		mav.addObject("totalPage", totalPage);
		mav.addObject("cPage", intPage);
		mav.addObject("listCnt", listCnt);
		mav.addObject("pageCnt", pageCnt);
		mav.addObject("fullCnt", fullCnt);
		
		return mav;
	
	}
	
	@RequestMapping("/auth/popup/authMoveGroupPopup.do")
	public ModelAndView authMoveGroupPopup(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
	    // 파라미터 받기
		String userIdsParam = request.getParameter("userIds");
		String authrtCdsParam = request.getParameter("authrtCds");
		String hnamesParam = request.getParameter("hnames");
	    
	    // 쉼표로 구분된 문자열을 리스트로 변환
	    List<String> userIds = Arrays.asList(userIdsParam.split(","));
	    List<String> authrtCds = Arrays.asList(authrtCdsParam.split(","));
	    List<String> hnames = Arrays.asList(hnamesParam.split(","));
	    
	    List<UserInfoDTO> userList = new ArrayList<>();
	    
	    // 각 항목을 UserInfoDTO에 담아서 리스트에 추가
	    for (int i = 0; i < userIds.size(); i++) {
	        UserInfoDTO userInfo = new UserInfoDTO();
	        userInfo.setUserid(userIds.get(i));
	        userInfo.setAuthrtCd(authrtCds.size() > i ? authrtCds.get(i) : "");  // 리스트 크기 확인 후 값 할당
	        userInfo.setHname(hnames.size() > i ? hnames.get(i) : "");
	        userList.add(userInfo);  // 리스트에 추가
	    }

	    if (loginInfo != null) {
	    	
	        mav.addObject("userList", userList);
	        mav.setViewName("jsp/auth/popup/authMoveGroupPopup");
	    } else {
	        mav.setViewName("redirect:/login.do");
	    }
	    
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);
		
		mav.addObject("authDc", authDc);
		
		return mav;
	}
	
	@RequestMapping("/auth/updateAuth.do")
	public void updateAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		Map params = ReqUtils.getParameterMap(request);
		String[] userIds = request.getParameterValues("userIds");
		String authCd = request.getParameter("authCd");
		System.out.println("박현호 params : " + params);
		
		if(userIds != null) {
			for (String userId : userIds) {
				Map newParam = new HashMap();
				newParam.put("lastMdfrId", loginInfo.getLoginid());
				newParam.put("userid", userId);
				newParam.put("authCd", authCd);
				try {
					bpsimCommonService.update("Auth.updateMoveAuth", newParam);
				}catch(Exception e) {
					 e.printStackTrace();
				}
				
			}
		}else {
			System.out.println("No userid or authCd provided");
		}
		
	}
	

	@RequestMapping("/auth/popup/authDateChangePopup.do")
	public ModelAndView authDateChangePopup(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
	    // 파라미터 받기
		String userIdsParam = request.getParameter("userIds");
		String hnamesParam = request.getParameter("hnames");
	    
	    // 쉼표로 구분된 문자열을 리스트로 변환
	    List<String> userIds = Arrays.asList(userIdsParam.split(","));
	    List<String> hnames = Arrays.asList(hnamesParam.split(","));
	    
	    List<UserInfoDTO> userList = new ArrayList<>();
	    
	    // 각 항목을 UserInfoDTO에 담아서 리스트에 추가
	    for (int i = 0; i < userIds.size(); i++) {
	        UserInfoDTO userInfo = new UserInfoDTO();
	        userInfo.setUserid(userIds.get(i));
	        userInfo.setHname(hnames.size() > i ? hnames.get(i) : "");
	        userList.add(userInfo);  // 리스트에 추가
	    }

	    if (loginInfo != null) {
	    	
	        mav.addObject("userList", userList);
	        mav.setViewName("jsp/auth/popup/authDateChangePopup");
	    } else {
	        mav.setViewName("redirect:/login.do");
	    }
		
		return mav;
	}
	
	@RequestMapping("/auth/updateAuthDate.do")
	public void updateAuthDate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		Map params = ReqUtils.getParameterMap(request);
		String[] userIds = request.getParameterValues("userIds");
		String authrtEndYmd = request.getParameter("authrtEndYmd");  // 권한 만료일 가져오기
		System.out.println("박현호 params : " + params);
		
		if(userIds != null) {
			for (String userId : userIds) {
				Map newParam = new HashMap();
				newParam.put("lastMdfrId", loginInfo.getLoginid());
				newParam.put("userid", userId);
				newParam.put("authrtEndYmd", authrtEndYmd);  // 권한 만료일 추가
				try {
					bpsimCommonService.update("Auth.updateAuthDate", newParam);
				}catch(Exception e) {
					 e.printStackTrace();
				}
				
			}
		}else {
			System.out.println("No userid or Date provided");
		}
		
	}		
	
	
	@RequestMapping("/auth/authAdd.do")
	public ModelAndView getMember(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("cPage"), "1");
		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
		params.put("cPage", cPage);
		
		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
		int pageCnt = 10;									/* 가로페이징(페이지수) */
		int totalCnt = 0;										/* 총 조회수  */
		params.put("intListCnt", intListCnt);
		totalCnt = bpsimCommonService.getDataCnt("Member.getMemberCnt", params);
		
		// 페이지 네비케이터
		PageNavigator pageNavigator = new PageNavigator(
			intPage		
		   ,"/auth/authAdd.do"
		   ,pageCnt		
		   ,intListCnt	
		   ,totalCnt
		   ,""
		);		
		
		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
		
		List memberList = bpsimCommonService.getList("Member.getMemberList", params, 0, pageNavigator.getRecordPerPage());
		int fullCnt = bpsimCommonService.getDataCnt("Member.getMemberFullCnt", params);
		
		if(loginInfo != null){
			mav.setViewName("jsp/auth/authAdd");
		}else{
			mav.setViewName("redirect:/login.do");
		}
		
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("params", params);
		mav.addObject("memberList", memberList);
		mav.addObject("pageNavigator", pageNavigator.getMakePage());
		mav.addObject("totalCnt", totalCnt);
		mav.addObject("totalPage", totalPage);
		mav.addObject("cPage", intPage);
		mav.addObject("listCnt", listCnt);
		mav.addObject("pageCnt", pageCnt);
		mav.addObject("fullCnt", fullCnt);		
		
		
		return mav;
	}

	@RequestMapping("/auth/popup/authAddPopup.do")
	public ModelAndView authAddPopup(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		
	    // 파라미터 받기
		String userid = request.getParameter("userid");

	    if (loginInfo != null) {
	        mav.setViewName("jsp/auth/popup/authAddPopup");
	    } else {
	        mav.setViewName("redirect:/login.do");
	    }
	    
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);
		mav.addObject("userid", userid);
		mav.addObject("authDc", authDc);
		
		return mav;
	}
	
	@RequestMapping("/auth/insertAuth.do")
	public void insertAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		Map params = ReqUtils.getParameterMap(request);
		System.out.println("박현호 params : " + params);

		params.put("frstRgtrId", loginInfo.getLoginid());
		params.put("lastMdfrId", loginInfo.getLoginid());
		
		try {
			bpsimCommonService.insert("Member.insertAuth", params);
		}catch(Exception e) {
			 e.printStackTrace();
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    response.setContentType("application/json");
		    response.getWriter().write("{\"message\":\"등록 실패: 오류가 발생했습니다.\"}");			 
		}
	}
	
	@RequestMapping("/auth/deleteAuth.do")
	public ModelAndView deleteAuth(HttpServletRequest request) throws Throwable {
		ModelAndView mav = new ModelAndView("bpsimjsonView");
		Map params = ReqUtils.getParameterMap(request);
//		String[] userIds = params.get("userIds");
		String[] userIds = request.getParameterValues("userIds[]");
		System.out.println("박현호 params : " + params);
		
		if(userIds != null) {
			for (String userId : userIds) {
				Map newParam = new HashMap();
				newParam.put("userid", userId);
				try {
					bpsimCommonService.delete("Auth.deleteAuth", newParam);
				}catch(Exception e) {
					 e.printStackTrace();
				}
			}
		}else {
			System.out.println("No userid provided");
		}
		
		return mav;
	}	
	
}
