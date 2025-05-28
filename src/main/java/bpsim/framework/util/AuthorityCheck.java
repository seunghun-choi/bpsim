package bpsim.framework.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import bpsim.module.ctr.usr.SessionHelper;
import bpsim.module.dao.AuthCheckService;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

public class AuthorityCheck {
	
	private static final String LOGIN_URL = "redirect:/login.do";								// 로그인 URL	
	private static final String ACCESS_DENIED_URL = "/jsp/mng/authorization";					// 권한 없음 페이지 URL
	private static String[] IP_ADDR_LIST = { "183.107.178.145", "0:0:0:0:0:0:0:1", "127.0.0.1", "192.168.10.11", "192.168.125.115", "192.168.125.127", "192.168.125.107", "210.219.43.31", "210.219.43.184", "210.118.0.24", "210.118.0.43", "210.219.43.32", "210.118.0.40", "210.218.221.34", "192.168.125.124", "192.168.125.151", "192.168.125.132", "210.105.85.4", "192.168.0.180" };
	private static BpsimCommon authcheckService = null;
	  
	private LoginInfoDTO _loginInfo;
	
	public AuthorityCheck(){
	}
	
	public AuthorityCheck(HttpServletRequest request){
		_loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
	}
	/*
	 * 줄기 세포 콘서트에서 사용할 Mav
	 */
	public LoginInfoDTO getLoginInfoDTO(){
		return _loginInfo;
	}
	
	public ModelAndView getKonscrtMav(HttpServletRequest request, String return_url)	throws Exception {
		ModelAndView mav = getSessionMav(request);
		
		// 로그인 되어 있지 않을 경우
		if(_loginInfo == null){
			String fromURL = ReqUtils.filter(ReqUtils.getEmptyResult2(request.getHeader("referer"),	"/mng/main.do"));	// 로그인화면 후 돌아갈 화면을 위해 변수값 저장
			mav.addObject("fromURL", fromURL);
			return_url = LOGIN_URL;
		}	
//		}else if(_loginInfo.getLevel() < 80){
//			return_url = ACCESS_DENIED_URL;	// 로그인은 되어있으나 관리자 권한이 없을 경우.
//		}else{
//			
//		}
		mav.setViewName(return_url);
		
		return mav;
	}
	
	public ModelAndView getImproveMav(HttpServletRequest request, String return_url)	throws Exception {
		ModelAndView mav = getSessionMav(request);
		
		// 로그인 되어 있지 않을 경우
		if(_loginInfo == null){
			String fromURL = ReqUtils.filter(ReqUtils.getEmptyResult2(request.getHeader("referer"),	"/mng/main.do"));	// 로그인화면 후 돌아갈 화면을 위해 변수값 저장
			mav.addObject("fromURL", fromURL);
			return_url = LOGIN_URL;
		}else{
			
		}
		mav.setViewName(return_url);
		
		return mav;
	}
	
	//로그인 확인 및 권한 확인 후 ModelAndView를 리턴
	public static ModelAndView getMav(HttpServletRequest request, String return_url)	throws Exception {
		ModelAndView mav = getSessionMav(request);
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");

		// 로그인 되어 있지 않을 경우
//		if(loginInfo == null){
//			String fromURL = ReqUtils.filter(ReqUtils.getEmptyResult2(request.getHeader("referer"),	"/mng/main.do"));	// 로그인화면 후 돌아갈 화면을 위해 변수값 저장
//			
//			if(fromURL.contains("/index.do")){
//				fromURL = "/mng/main.do";
//			}
//
//			mav.addObject("fromURL", fromURL);
//			return_url = LOGIN_URL;
//		}
//		
//		// 20140424 Konscrt 관리자 권한 추가로 인한 접근 URL 변경
//		else if(loginInfo.getLevel() < 80){
//			return_url = ACCESS_DENIED_URL;	// 로그인은 되어있으나 관리자 권한이 없을 경우.
//		}else{
//			
//			/* 220211 주석처리
//			
//			if(loginInfo.getLevel() != 99){
//				String requestUrl = request.getServletPath();
//				if( (requestUrl.indexOf("mng/MemberList") > 0) || (requestUrl.indexOf("mng/MemberView") > 0) || (requestUrl.indexOf("mng/ForeignList") > 0) || (requestUrl.indexOf("mng/MemberMailingList") > 0) || (requestUrl.indexOf("mng/addressbook") > 0) || (requestUrl.indexOf("mng/excelinit") > 0)){
//					if((requestUrl.indexOf("mng/MemberMailingList") > 0) || (requestUrl.indexOf("mng/addressbook") > 0) && loginInfo.getLevel() == 92){
//						
//					}else{
//						if(loginInfo.getLevel() == 85){
//							return_url = "redirect:/mng/KonscrtMemberList.do";
//						}else if(loginInfo.getLevel() == 92){
//							return_url = "redirect:/mng/MemberMailingList.do?showType=Y";
//						}else if(loginInfo.getLevel() == 98){
//							return_url = "redirect:/mng/HeadlineForm.do";
//						}else{
//							return_url = "redirect:/mng/InfoList.do";
//						}
//					}
//				}
//			}
//			*/
//			
//			String ipAddr = request.getRemoteAddr();
//			Map params = new HashMap();
//			params.put("ip_addr", ipAddr);
//			
//			//Static method에서 Bean 가져오기..
//			
//			if(authcheckService == null){
//				authcheckService = new AuthCheckService(request);
//			}
//
//			if(loginInfo.getLoginseq() != 37001 && loginInfo.getLoginseq() != 48823 && loginInfo.getLoginseq() != 61415) { //연구원 외부에서 접속
//				Map result = authcheckService.getObjectMap("AccessAllow.getObjectMap", params);
//				
//				if(result == null){
//					return_url = "/jsp/usr/mypageCgi";
//					mav.addObject("cmd", "ipAddr");
//				}
//			}
//			/*
//			if(!Arrays.asList(IP_ADDR_LIST).contains(ipAddr)){
//				return_url = "/jsp/usr/mypageCgi";
//				mav.addObject("cmd", "ipAddr");
//			}
//			*/
//		}
		mav.setViewName(return_url);
		
		return mav;
	}
	
	public static ModelAndView getSessionMav(HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView();
		mav = SessionHelper.getSession(request, mav);
		return mav;
	}
	
	public static ModelAndView getLoginChk(LoginInfoDTO loginInfo)	throws Exception {
		ModelAndView mav = new ModelAndView();

		//안심본인인증되지 않은 사용자
		if( loginInfo != null
//			&& !"외국인".equals(loginInfo.getMembertype())
//			&& !"해외거주자".equals(loginInfo.getMembertype())
//			&& loginInfo.getLoginid() != null && !"".equals(loginInfo.getLoginid())
//			&& loginInfo.getCrti_yn() != null && "N".equals(loginInfo.getCrti_yn())
			){
			mav.setViewName("redirect:/rejoin.do");
			return mav;
		}				
		
		return mav;
	}
}
