package bpsim.framework.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * '@Controller'어노테이션을 사용한 컨트롤러 구현 시 사용할 Interface
 *  (Spring이 컨트롤러를 Bean으로 등록할 수 있도록 Proxy용 Interface) 
 * @author zero
 * @version 1.0
 */

public interface SimpleController {     

	/**
	 * 최초 페이지 요청(그리드, 폼등 초기화면 구성)<br>
	 * 1) 어노테이션 임포트 : import org.springframework.stereotype.Controller;<br>
	 * 2) 어노테이션 지정 : '@Controller'("컨트롤러명")<br>
	 * 3) 핸들맵핑 임포트 : import org.springframework.web.bind.annotation.RequestMapping;<br>
	 * 4) 메소드에 핸들맵핑 정의 : @RequestMapping("요청URI")<br>
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @param reference - Map @ModelAttribute("참조이름") Map reference
	 * @return ModelAndView
	 * @throws Exception
	 */
	ModelAndView Insert(HttpServletRequest request, HttpServletResponse response, Map reference) throws Exception;
	
	/**
	 * 상세 페이지 요청(사용자 선택에 의한 화면 구성)<br>
	 * 1) 어노테이션 임포트 : import org.springframework.stereotype.Controller;<br>
	 * 2) 어노테이션 지정 : '@Controller'("컨트롤러명")<br>
	 * 3) 핸들맵핑 임포트 : import org.springframework.web.bind.annotation.RequestMapping;<br>
	 * 4) 메소드에 핸들맵핑 정의 : @RequestMapping("요청URI")<br>
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @param reference - Map @ModelAttribute("참조이름") Map reference
	 * @return ModelAndView
	 * @throws Exception
	 */
	ModelAndView Update(HttpServletRequest request, HttpServletResponse response, Map reference) throws Exception;
	
	/**
	 * 데이터 조작 요청(등록|수정|삭제등 요청 처리)<br>
	 * 1) 어노테이션 임포트 : import org.springframework.stereotype.Controller;<br>
	 * 2) 어노테이션 지정 : '@Controller'("컨트롤러명")<br>
	 * 3) 핸들맵핑 임포트 : import org.springframework.web.bind.annotation.RequestMapping;<br>
	 * 4) 메소드에 핸들맵핑 정의 : @RequestMapping("요청URI")<br>
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @param mode - String(VarConsts의 MODE 참조) @RequestParam(required=true|false,value="파라미터명")
	 * @return ModelAndView
	 * @throws Exception
	 */
	ModelAndView Delete(HttpServletRequest request, HttpServletResponse response, String mode) throws Exception;
	

	/**
	 * 뷰에 필요한 데이터 전송<br>
	 * 1) @ModelAttribute("참조이름") 어노테이션 적용<br>
	 * @param request - HttpServletRequest<br>  
	 * Map params = ReqUtils.getParameterMap(request)
	 * @return Map<br>
	 * Map reference = new HashMap();<br>
	 * reference.put("이름",값);<br>
	 * @throws Exception
	 */
	ModelAndView View(HttpServletRequest request, HttpServletResponse response, Map reference) throws Exception;
	
	/**
	 * 뷰에 필요한 데이터 전송<br>
	 * 1) @ModelAttribute("참조이름") 어노테이션 적용<br>
	 * @param request - HttpServletRequest<br>  
	 * Map params = ReqUtils.getParameterMap(request)
	 * @return Map<br>
	 * Map reference = new HashMap();<br>
	 * reference.put("이름",값);<br>
	 * @throws Exception
	 */
	ModelAndView List(HttpServletRequest request, HttpServletResponse response, Map reference) throws Exception;
	
	Map getReference(HttpServletRequest request) throws Exception;
	
}
