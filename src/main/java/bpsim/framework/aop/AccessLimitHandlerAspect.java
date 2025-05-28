package bpsim.framework.aop;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import bpsim.module.dao.BpsimCommon;

/**
 * 접근불가 설정
 */

public class AccessLimitHandlerAspect extends HandlerInterceptorAdapter{

	private static final Logger logger = LoggerFactory.getLogger(AccessLimitHandlerAspect.class);
	
	//logger.debug("[C]["+sdf.format(new Date())+"]  <END   > - " + className);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;	
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String className = handler.getClass().getName();
		String reqUri = request.getRequestURI();
		try
	    {
			Map parm = new HashMap();
			String ip_addr = request.getRemoteAddr();
			parm.put("ip_addr", ip_addr);
			
			int cnt = this.bpsimCommonService.getDataCnt("AccessLimit.getCheckIp", parm);

			if( cnt > 0 ){
				//response.sendRedirect("/notIp.do");

				response.setContentType("text/html");

				PrintWriter out = response.getWriter();
				Enumeration<String> e = request.getHeaderNames();
				out.println("<html>");
				out.println("<h3>'" + ip_addr + "' is a restricted access ip</h3>");
				out.println("</body>");
				out.println("</html>");
				out.close();
						
				return false;
			}
			
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("AccessLimitHandlerAspect-preHandle2");
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
