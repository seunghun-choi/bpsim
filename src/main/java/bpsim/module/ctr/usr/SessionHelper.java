package bpsim.module.ctr.usr;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.module.dto.LoginInfoDTO;




public class SessionHelper {
	public static ModelAndView getSession(HttpServletRequest request, ModelAndView mav) throws Exception{
		LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
		
		if(loginInfo != null){
			mav.addObject("sessionName", loginInfo.getLoginnm());
			mav.addObject("sessionid", loginInfo.getLoginid());
			mav.addObject("sessionAuthrtCd", loginInfo.getAuthrtCd());
		}
		return mav;
	}
}
