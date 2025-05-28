package bpsim.framework.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 접속정보(OS, 브라우저, 기기 등) 확인
 * @author KHM
 * @since 2021.12.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일        	수정자        	수정내용
 *  ----------  --------    ---------------------------
 *  2021.12.20    KHM       최초작성  
 *
 * </pre>
 */
public class AgentInfo {
	
	/**
	* @Method : getOs
	* @Description : 접속 OS 확인
	* @author : khm
	* 
	* --------------------------------------------------------
	* Modification Information
	*  수정일       수정자        	  수정내용
	* ----------  --------    ---------------------------
	*  2021.12.20     khm      최초생성
	*/	 
	public String getOs(String agent) {
		String os = "";
		
		String[] mobileos = {"iPhone", "iPod", "Android", "BlackBerry", "Windows CE", "Nokia", "Webos", "Opera Mini", "SonyEricsson", "Opera Mobi", "IEMobile"};

		if (agent != null && !agent.equals("")) {
			for(int i = 0; i < mobileos.length; i++) {
				if (agent.indexOf(mobileos[i]) > -1) {
					return os = mobileos[i];
				}
			}
		}
		
		if(agent != null && !agent.equals("")) {
			if(agent.indexOf("NT 6.0") != -1) os = "Windows Vista/Server 2008";
			else if(agent.indexOf("Windows") != -1) os = "Windows";
			else if(agent.indexOf("Linux") != -1) os = "Linux";
			else if(agent.indexOf("Macintosh") != -1) os = "Macintosh";
			else os = "ETC"; 
		}
		return os;
	}
	
	/**
	* @Method : getBrowser
	* @Description : 접속 브라우저 확인
	* @author : khm
	* 
	* --------------------------------------------------------
	* Modification Information
	*  수정일       수정자        	  수정내용
	* ----------  --------    ---------------------------
	*  2021.12.20     khm      최초생성
	*/	 
	public String getBrowser(String agent) {
		String brower = "";
		
		if (agent != null) {
		   if (agent.indexOf("Trident") > -1) {
			  return brower = "IE";
		   } else if (agent.indexOf("Opera") > -1) {
			  return brower = "Opera";
		   } else if (agent.indexOf("Edge") > -1) {
			  return brower = "Edge";
		   } else if (agent.indexOf("Whale") > -1) {
			  return brower = "Whale";
		   } else if (agent.indexOf("Opera") > -1 || agent.indexOf("OPR") > -1) {
			  return brower = "Opera";
		   } else if (agent.indexOf("Firefox") > -1) {
				  return brower = "Firefox";
		   } else if (agent.indexOf("Safari") > -1 && agent.indexOf("Chrome") == -1) {
				  return brower = "Safari";
		   } else if (agent.indexOf("Chrome") > -1) {
				  return brower = "Chrome";
		   }else {
			   return brower = "ETC";
		   }
		}
		
		return brower;
	}
	
	/**
	* @Method : getFullURL
	* @Description : 파라미터 포함 전체 URL 구하기
	* @author : khm
	* 
	* --------------------------------------------------------
	* Modification Information
	*  수정일       수정자        	  수정내용
	* ----------  --------    ---------------------------
	*  2021.12.20     khm      최초생성
	*/	 
	public String getFullURL(HttpServletRequest request) {
	     StringBuffer requestURL = request.getRequestURL();
	     String queryString = request.getQueryString();

	     if (queryString == null) {
	         return requestURL.toString();
	     } else {
	         return requestURL.append('?').append(queryString).toString();
	     }
	 }
	
	
	/**
	* @Method : getDevice
	* @Description : 접속 기기 확인
	* @author : khm
	* 
	* --------------------------------------------------------
	* Modification Information
	*  수정일       수정자        	  수정내용
	* ----------  --------    ---------------------------
	*  2021.12.20     khm      최초생성
	*/	 
	public String getDevice(String agent) {
	    if(agent.indexOf("MOBILE") > -1) {
	         return "MOBILE";
	    } else {
	        return "PC";
	    }
	}
}
