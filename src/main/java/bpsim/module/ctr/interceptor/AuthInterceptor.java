package bpsim.module.ctr.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import bpsim.framework.util.AgentInfo;
import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.ReqUtils;
import bpsim.module.dto.LoginInfoDTO;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
			List<String> checkCmd = new ArrayList<String>();
			checkCmd.add("(.*)write(.*)");	//cmd=write
			checkCmd.add("(.*)update(.*)");	//cmd=update
			checkCmd.add("(.*)delete(.*)");	//cmd=delete
			checkCmd.add("(.*)booklist(.*)");	//cmd=booklist
			
			AgentInfo agentInfo = new AgentInfo();
			String url = agentInfo.getFullURL(request); //전체 URL
			boolean check = true;
			LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo"); //로그인 정보
			int m_level = 0;
			if(loginInfo != null) {
//				m_level = loginInfo.getLevel();
			}
			
			Map params	=  ReqUtils.getParameterMap(request);
			String bid = ReqUtils.getEmptyResult2((String)params.get("bid"), "");
			
			if(!"suggest".equals(bid)) { //규제 건의신청 게시판은 예외
				if(!"notice".equals(bid)) { //사업공고 일반인 등록 가능
				//CRUD 접근권한 확인
				if(!url.contains("board.do") && !url.contains("hbrd_News.do") && !url.contains("EntrprsIntrcn.do")) {
					if(m_level < 80 || m_level == 92) {
						check = false;
					}
				}else{ //cmd를 사용하는 게시판
					for (int i = 0; i < checkCmd.size(); i++) {
						if(url.matches(checkCmd.get(i))) {
							if(m_level < 80 || m_level == 92) {
								check = false;
								break;
							}
						}
					}
				}
				
				if(!check) {
					request.setAttribute("cmd", "no_authority");
					request.setAttribute("bid", bid);
					RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/jsp/usr/boardCgi.jsp");
					rq.forward(request, response);
					return false;
				}
			  }
			}
		
		return super.preHandle(request, response, handler);
		
	}
}
