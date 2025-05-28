package bpsim.framework.aop;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * DefaultAnnotationHandlerMapping => HandlerInterceptorAdapter를 이용한 
 * @author zero
 * @version 1.0
 */

public class SessionHandlerAspect extends HandlerInterceptorAdapter{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		return true;
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
		//if(ex != null){
			//logger.debug("[C]["+sdf.format(new Date())+"] Exception Occured : " + ex.getMessage());
		//}
		
		//logger.debug("[C]["+sdf.format(new Date())+"]  <END   > - " + className);		
	}
}
