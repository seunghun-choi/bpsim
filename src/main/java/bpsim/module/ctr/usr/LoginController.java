package bpsim.module.ctr.usr;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.EncryptPassword;
import bpsim.framework.util.ReqUtils;
import bpsim.framework.util.TokenMngUtil;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller("bpsim.module.ctr.usr.LoginController")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@RequestMapping("/login.do")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
//		String fromURL = ReqUtils.getEmptyResult2((String)params.get("fromURL"), "");
		
//		if(fromURL.contains("/logout.do")){
//			fromURL = "/login.do";
//		}
		
		// 로그인 후 해당 페이지로 이동하기 위해 ..
//		if(fromURL.equals("")){
//			fromURL = ReqUtils.filter(ReqUtils.getEmptyResult2(request.getHeader("referer"), fromURL));
//		}		
//		mav.addObject("fromURL", fromURL);
//		System.out.println("박현호 돌아갈 페이지 fromUTL : " + fromURL);
		
		if(loginInfo != null){
			mav.setViewName("redirect:/expert/expertList.do");
		}else{
			mav.setViewName("jsp/usr/login");
			mav.addObject("msg", params.get("msg"));
		}

		return mav;
	
	}
	
	@RequestMapping(value="/loginChk.do", method = RequestMethod.POST)
	public ModelAndView loginChk(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		HttpSession session   = request.getSession(true);
		ModelAndView mav = new ModelAndView();
		String returnURL = "redirect:/expert/expertList.do";
		
		Map params = ReqUtils.getParameterMap(request);
        String userid 	= ReqUtils.getEmptyResult2((String)params.get("userid"),"");
        
        String passwd 	= ReqUtils.getEmptyResult2((String)params.get("passwd"),"");
        String rcmd 	= ReqUtils.getEmptyResult2((String)params.get("cmd"),"");
		
        String cmd 		= "";
        
        //로그인의 경우에만 Token 확인
        if(rcmd.equals("login")){
        	/* 중복방지 Token 체크 */
        	if(TokenMngUtil.isTokenValid(request)) {
        		/* 중복방지 Token 초기화 */
        		TokenMngUtil.resetToken(request); // 중복체크 true 면 진행
        	}else{
        		throw new Exception("정상적인 로그인이 아닙니다.");
        	}      	
        }
        else{
        	TokenMngUtil.resetToken(request); // 로그인이 아니여도 Token 은 생성되므로 삭제해줌
        }        
        
        LoginInfoDTO loginDto = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
        
//		if(fromURL.equals("")){
//			fromURL = ReqUtils.filter(ReqUtils.getEmptyResult2(request.getHeader("referer"), fromURL));
//		}        
		
        // 이미 로그인되어 있는 상태라면 index 페이지로 다시 이동
        if(loginDto != null && !rcmd.equals("join")) {
        	String serverName = request.getServerName();
        	//운영배포 realApplyExp
            //returnURL = "redirect:https://" + serverName + "/index.do";
            
        	//개발
        	returnURL = "redirect:/expert/expertList.do";
        }else {
        	Map newParams = new HashMap();
        	newParams.put("userid", userid);
        	List<Map> userMapList = bpsimCommonService.getObjectMapRow("Member.getLoginValue", newParams);
    		Map userMap = null;
    		int easyLoginIndex = 0;
    		
     		if(userMapList.size() != 0) {
    			userMap = userMapList.get(easyLoginIndex);
    		}
    		
    		// 값이 존재하지 않으면 없는 아이디
    		if (userMap == null){
    			logger.debug("값이 존재하지 않으면 없는 아이디");
				cmd = "login_fail_userid";
    		} // 값이 존재하면 비밀번호 체크
    		else {
    			logger.debug("값이 존재하면 비밀번호 체크");
    			// 알고리즘 SHA-256 으로 변경 후 로그인 처리
    			if(EncryptPassword.checkPassword(userMap, passwd.trim(), (boolean)false)){
    				logger.debug("cmd login");
    				cmd = "login";
    				loginDto = new LoginInfoDTO();
    				
    				userMap = ReqUtils.getResultNullChk(userMap);
    				
    	        	String loginid		= (String)userMap.get("userid"); 
    	        	String loginnm		= (String)userMap.get("hname");
    	        	String authrtCd		= (String)userMap.get("authrtCd");    		
    	        	String codeNm 		= (String)userMap.get("codeNm");
    	        	
    	        	loginDto.setLoginid(loginid);
    	        	loginDto.setLoginnm(loginnm);
    	        	loginDto.setAuthrtCd(authrtCd);    	   
    	        	loginDto.setCodeNm(codeNm);
    				
//    	        		setLoginSessionAdm(request, loginDto);
	        		setLoginSession(request, loginDto);
    	        	
    			}else {
    				cmd = "login_fail_password";
    				returnURL = "redirect:/login.do";
    				String msg = "로그인에 실패하였습니다. 아이디 또는 비밀번호가 틀립니다.";
    				mav.addObject("msg", msg);
    			}
    		}
        }
        
        logger.debug("cmd : "+cmd);
        mav.setViewName(returnURL);
		System.out.println("박현호 returnURL : " + returnURL);
		return mav;
	}
	
	public void setLoginSession(HttpServletRequest request, LoginInfoDTO loginDto) {
		//logger.debug("SessionTime.getSessionTime");
		int time = 60;
//		try {
//			Map args = new HashMap();
//			time = bpsimCommonService.getDataCnt("SessionTime.getSessionTime", args);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		CommonUtil.setSession(request, "loginInfo", loginDto, time);
	}
	
	public void setLoginSessionAdm(HttpServletRequest request, LoginInfoDTO loginDto) {
		//logger.debug("SessionTime.getSessionTime");
		int time = 60;
//		try {
//			Map args = new HashMap();
//			time = bpsimCommonService.getDataCnt("SessionTime.getSessionTime", args);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		CommonUtil.setSession(request, "loginInfo", loginDto, time);
	}	
	
}
