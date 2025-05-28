package bpsim.module.ctr.interceptor;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import bpsim.framework.util.AgentInfo;
import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.ReqUtils;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

/**
 * 접속정보 인터셉터
 * @author PHH
 * @since 2024.09.13
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일        	수정자        	수정내용
 *  ----------  --------    ---------------------------
 *  2024.09.13    PHH       최초작성  
 *
 * </pre>
 */
public class ConectInterceptor extends HandlerInterceptorAdapter {
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		super.postHandle(request, response, handler, modelAndView);

		AgentInfo agentInfo = new AgentInfo();
		String uri = request.getRequestURI(); 	//접속 URI
//		String url = agentInfo.getFullURL(request); //전체 URL
//		
//		if(uri.indexOf("/index") > -1) {
//			return;
//		}
//		
//		
//		if(url.matches("(.*).jsp(.*)")) {
//			check = false;
//		}
//		
//		
		Map params	=  ReqUtils.getParameterMap(request);
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo"); //로그인 정보
//		String agent = request.getHeader("User-Agent"); //에이전트
//			
//		String userIP = CommonUtil.getClientIP(request); 						//접속 IP
//		String brower = agentInfo.getBrowser(agent); 							//브라우저
//		String os = agentInfo.getOs(agent); 									//OS
//			
		Map newParams = new HashMap();
		
//		url = URLDecoder.decode(url, "utf-8");
		uri = URLDecoder.decode(uri, "utf-8");
		
		if(uri.contains("/login.do")) {
			return true;
		}
		
		if(loginInfo != null) {
//			String userID = loginInfo.getLoginid();
//			String userName = loginInfo.getLoginnm();
			String authrtCd = loginInfo.getAuthrtCd();
			
//			newParams.put("userid", userID);
//			newParams.put("acsrNm", userName);
			newParams.put("authrtCd", authrtCd);
			
			
			int isCheck = 2;
			
			try {
			    
			    if(uri.contains("/auth")) {
			    	if(authrtCd.equals("AUTH001")) {
			    		isCheck=0;
			    	}else {
			    		isCheck=1;
			    	}
			    }else if(uri.contains("/log")) {
			    	// 잠시 테스트
			    	if(authrtCd.equals("AUTH001")) {
			    		isCheck=0;
			    	}else {
			    		isCheck=1;
			    	}
			    }else if(uri.contains("/expert")) {
			    	if(uri.contains("/expertAdd") || uri.contains("/expertExcelInfoConfirm") || uri.contains("/expertMod")) {
			    		if(authrtCd.equals("AUTH001") || authrtCd.equals("AUTH002")) {
			    			isCheck=0;
			    		}else {
			    			isCheck=1;
			    		}
			    	}else {
			    		isCheck=0;
			    	}
			    }else {
			    	isCheck = 0;
			    }
			    
			    
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
//			newParams.put("acsrIpAddr", userIP);
//			newParams.put("cntnUrlAddr", url);
//			System.out.println("박현호 LOG찍기 : " + newParams);
//			bpsimCommonService.insert("Log.InsertConectHist", newParams); //테이블에 insert
			
			if(isCheck == 0) {
				
			}else {
				response.sendRedirect("/login.do");
				System.out.println("박현호 리다이렉트");
				return false;
			}	
			
		}
	
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		
		AgentInfo agentInfo = new AgentInfo();
		String url = agentInfo.getFullURL(request); //전체 URL
		
		Map params	=  ReqUtils.getParameterMap(request);
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo"); //로그인 정보
		String agent = request.getHeader("User-Agent"); //에이전트

		String userIP = CommonUtil.getClientIP(request); 						//접속 IP
		String brower = agentInfo.getBrowser(agent); 							//브라우저
		String os = agentInfo.getOs(agent); 									//OS

		Map newParams = new HashMap();
		
		url = URLDecoder.decode(url, "utf-8");
		
		if(loginInfo != null) {
			String userID = loginInfo.getLoginid();
			String userName = loginInfo.getLoginnm();
			String authrtCd = loginInfo.getAuthrtCd();
			
			newParams.put("userid", userID);
			newParams.put("acsrNm", userName);
			newParams.put("authrtCd", authrtCd);
			
			newParams.put("acsrIpAddr", userIP);
			newParams.put("cntnUrlAddr", url);
			System.out.println("박현호 LOG찍기 : " + newParams);
			bpsimCommonService.insert("Log.InsertConectHist", newParams); //테이블에 insert			
			
		}else {
//			response.sendRedirect("/login.do");
		}
		
		
		
	}
	
	
}
