package bpsim.framework.aop;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import bpsim.framework.util.CommonUtil;

/**
 */

public class LoggingHandlerAspect extends HandlerInterceptorAdapter{

	private static final Logger logger = LoggerFactory.getLogger(LoggingHandlerAspect.class);
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("LoggingHandlerAspect IN");

		//String className = handler.getClass().getName();
		//String reqUri = request.getRequestURI();
		//logger.debug("[C]["+sdf.format(new Date())+"]  <START > - " + className + "("+reqUri+")");
		
		//sso 쿠키 확인
//		String pni_token = ReqUtils.getCookie(request, "pni_token");
//		logger.debug("[C]["+sdf.format(new Date())+"]  <pni_token   > - " + pni_token);	
//		LoginInfoDTO loginDto = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
//		if(pni_token != null) {
			
			//if(loginDto == null) {
				//logger.debug(CommonUtil.getSessionType(request, "loginid", "S")+"");
				//response.sendRedirect("/loginChk.do");
				//return true;
		//	}else {
		//휴면계정여부 조회
		if("Y".equals(CommonUtil.getSessionType(request, "dormancy", "S"))) {
			//휴면계정아내화면 및 성공화면은 제외함
			if("/drmncy_guid.do".equals(request.getRequestURI()) || "/drmncySuss.do".equals(request.getRequestURI())) 
			{
				return true;
			}else {
				//그외 경우는 휴면계정 안내 화면으로 강제 변경
				response.sendRedirect("/drmncy_guid.do");
				return true;
			}
		//휴면계정이 아닌 대상은 제외함	
		}else {
			//휴면계정이 아닌 대상이 휴면계정 안내 화면으로 진입하는 경우 index화면으로 리턴 처리
			if("/drmncy_guid.do".equals(request.getRequestURI())||"/drmncySuss.do".equals(request.getRequestURI())) {
				response.sendRedirect("/expert/expertList.do");
			}
			return true;
		}
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//logger.debug("[C]["+sdf.format(new Date())+"]  <VIEW > - " + modelAndView.getViewName());
		//logger.debug("[C]["+sdf.format(new Date())+"]  <MODEL> - " + modelAndView.getModel());
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String className = handler.getClass().getName();
		if(ex != null){
			logger.debug("[C]["+sdf.format(new Date())+"]  <START   > - " + className);		
			logger.debug("[C]["+sdf.format(new Date())+"] Exception Occured : " + ex.getMessage());
			logger.debug("[C]["+sdf.format(new Date())+"]  <END   > - " + className);
		}
		
	}
}
