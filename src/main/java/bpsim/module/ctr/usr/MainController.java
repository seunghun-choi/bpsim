package bpsim.module.ctr.usr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import bpsim.framework.util.ReqUtils;
import bpsim.framework.util.TextConvert;
import bpsim.framework.util.TokenMngUtil;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller
public class MainController {
	
	/** 로그 출력 */
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Resource(name = "bpsimCommonService")
	private BpsimCommon bpsimCommonService;

	@RequestMapping("/index.do")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		ModelAndView mav = new ModelAndView();

		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		TokenMngUtil.saveToken(request);

		if(loginInfo != null){
			mav.setViewName("redirect:/expert/expertList.do");
		}else{
			mav.setViewName("redirect:/login.do");
		}		
		
		return mav;
	}

}
