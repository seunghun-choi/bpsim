package bpsim.framework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	private static int loginTime = 60;
	/******************************************************************
	 * 널데이터 체크(Map 형식)
	 * @param map 데이터  
	 ******************************************************************/
	
	public static int numChk(String num, int value1)  {
        int value = 0;
      try {
          value = Integer.valueOf(num).intValue();
      }catch (NumberFormatException e) {    value = value1;    }
      catch(Exception e){    value = value1;    }    
      
      return value;
    }
	
	//string 널값체크
	public static String stringChk(String value, String value1) {
		try {
			if(value == null){    value = value1;    }            
		}catch(Exception e){    value = value1;    }    
		
		return value;
	}
	
	public static String getYyyymmdd() {
		SimpleDateFormat sdfFile = new SimpleDateFormat("yyyyMMdd");
		String yyyymmdd = sdfFile.format(new java.util.Date());
		return yyyymmdd;
	}
	
	public static String getYyyymmdd(String divider) {
		SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy" + divider + "MM" + divider + "dd");
		String yyyymmdd = sdfFile.format(new java.util.Date());
		return yyyymmdd;
	}
	
	// Year ===============================================
  	public static String combo_yy(String today){
  		return combo_yy(today, 1993);
	}
  	public static String combo_yy(String today, int startYear){
		StringBuffer buff = new StringBuffer(1024);
		today = today.substring(0,4);
		int startday = Integer.parseInt(today);
  	
		for(int i=startYear ; i<2016 ; ++i ){
			buff.append("<option value='"+ i +"' ");
			if( i == startday ) {
				buff.append("	selected ");  
			}
			buff.append(" > "+ i +"  </option>\n");	
		}
		return buff.toString();
	}
	
  	// To_Day Month =======================================
  	public static String combo_mm(String today){
		StringBuffer buff = new StringBuffer(1024);
	
		for(int i=1; i<13; i++){
			if(i >= 10){
				buff.append("<option value='"+ i +"' ");
				if(( today.substring(4,6)).equals(""+i)) {
					buff.append(" selected ");  
				}	
			}
			else{
				buff.append("<option value='0"+ i +"' ");
				if(( today.substring(4,6)).equals("0"+i)) {
					buff.append(" selected ");  
				}	
			}	
			buff.append(" > "+ i +"  </option>\n");
		}
		return buff.toString();
  	}

  	// To_Day Day =========================================
  	public static String combo_dd(String today){
		StringBuffer buff = new StringBuffer(1024);
	
		for(int i=1; i<32; i++){
			if(i <= 9) {  
				buff.append("<option value='0"+ i +"' ");
				if(( today.substring(6,8)).equals("0"+i)) {
					buff.append(" 	selected ");  
				}
				buff.append(" > "+ i +"  </option>\n");
			}
			else{
				buff.append("<option value='"+ i +"' ");
				if(( today.substring(6,8)).equals(""+i)) {
					buff.append(" 	selected ");  
				}
				buff.append(" > "+ i +"  </option>\n");
			}
		}
		return buff.toString();
	}
  	
  	// To_Hour/Mi/Sec =========================================
  	public static String combo_hh(int hour, String flag){
		StringBuffer buff = new StringBuffer(1024);
        int j=0;  
		if(flag.equals("hh")) j=25;
		else if(flag.equals("mm")) j=61;
		else if(flag.equals("ss")) j=61;	
	
		for(int i=1; i<j; i++){
			if(i <= 9) {  
				buff.append("<option value='0"+ i +"' ");
				if(hour==i) {
					buff.append(" 	selected ");  
				}
				buff.append(" > "+ i +"  </option>\n");
			}
			else{
				buff.append("<option value='"+ i +"' ");
				if(hour==i) {
					buff.append(" 	selected ");  
				}
				buff.append(" > "+ i +"  </option>\n");
			}
		}
		return buff.toString();
	}
  	
  	//문자열 자르기
  	public static Vector splitList( String v , String split) {
        Vector ve = new Vector();

        if ( v == null || v.equals("") ) {
       } else {
            StringTokenizer st = new StringTokenizer( v , split );
            while (st.hasMoreTokens()) {
                ve.add( st.nextToken() );
            }
        }
        return ve ;
    }
  	
  	//문자 데이타 길이만큼 잘라내기 =======================
    public static String content_div(String s, int len, String tail){
    	//rewritten 2011-12-23 by A2M
    	
        if (s == null)
            return null;
    
        int srcLen = realLength(s);
        if (srcLen < len)
            return s;

 

        String tmpTail = tail;
        if (tail == null)
            tmpTail = "";
 
        int tailLen = realLength(tmpTail);
        if (tailLen > len)
            return "";

 

        char a;
        int i = 0;
        int realLen = 0;
        for (i = 0; i < len - tailLen && realLen < len - tailLen; i++) {
           a = s.charAt(i);
           if ((a & 0xFF00) == 0)
               realLen += 1;
           else
               realLen += 2;
        }

 

        while (realLength(s.substring(0, i)) > len - tailLen) {
            i--;
        }

 

        return s.substring(0, i) + tmpTail;
    }
    
    public static int realLength(String s) {
        return s.getBytes().length; //***changed 2011-11-17 by A2M***
    	//return s.length();
    }
    
    public static String xssChk(String value, String value1) {
        try {
            value=stringChk( value, value1);
            value=value.replaceAll("<","&lt;");            
            value=value.replaceAll(">","&gt;");    
        }catch(Exception e){    System.out.print(e.toString());    }    
        return value;
      }
    
    public static String xssBack(String value) {
    	try {
    		value=value.replaceAll("&lt;","<");            
    		value=value.replaceAll("&gt;",">");
    	}catch(Exception e){    System.out.print(e.toString());    }
    
    	return value;
    }
    
    //<br> tag 생성========================================
    public static  String getBrStr(String str) { 
        if(str == null) {   return new String("");   }
        StringBuffer buff = new StringBuffer(1024);   
        for (int k = 0; k < str.length(); k++) {                  
              char one = str.charAt(k);                 
              if ('\n' == one ) {
                   buff.append("<br>");                  
              }else if(' ' == one) {
                   buff.append("&nbsp;");                  
              }else{ 
                 buff.append(one);   
              } 
        }       
        return buff.toString(); 
    }
    
    /****************************************************************************************************
	* 새글 입력시 new 아이콘 표시 하루
	****************************************************************************************************/
    public static String new_icon(String strDate){
        String ret = "";
        if(strDate == null || strDate.length() < 10){
        	return ret;
        }
        
        SimpleDateFormat frmat = new SimpleDateFormat("yyyy-MM-dd");
        String now = frmat.format(new Date());
        
        if(strDate.equals(now)) ret="<i class=\"xi-new\"><span class=\"sr_only\">새글</span></i>";
        else ret="";
        return ret;
    }
    
    /****************************************************************************************************
	* 날짜비교
	* c1: 오늘 날짜
	* c2: 입력 날짜
	* c1 > c2 =>  false
	* c1 < c2 => true
	****************************************************************************************************/
    public static boolean compareDate(String strDate){
        if(strDate != null && strDate.length() >= 10){
	        Calendar c1 = Calendar.getInstance();
	        Calendar c2 = Calendar.getInstance();
	        c2.set(Integer.parseInt(strDate.substring(0,4)),(Integer.parseInt(strDate.substring(5,7))-1),Integer.parseInt(strDate.substring(8,10)));
	
	        Date date1 = c1.getTime(); 
	        Date date2 = c2.getTime();
	        long d1 = date1.getTime();
	        long d2 = date2.getTime();
	        int days =(int)((d1-d2)/(1000*60*60*24));
	        
	        if(days < 0) return true;
        }
        return false;
    }
    
    /****************************************************************************************************
    * 파일종류 별 icon 가져오기 
    *****************************************************************************************************/
    public static String getFileIcon(String filename)
    {
    	String ext = filename.substring(filename.lastIndexOf(".")+1);
    	String iconName = null;
    	
    	//logger.debug(ext);
    	if(ext!=null && (ext.equals("hwp") || ext.equals("doc") || ext.equals("xls") || ext.equals("ppt") || ext.equals("pdf") || ext.equals("zip")))
    		iconName = "/images/common/ico_file_"+ext+".gif";
    	else
    		iconName = "/images/common/ico_file.gif";
    	return iconName;
    }
  	
    
    public static void setSession(HttpServletRequest req, String name, Object value) {
        //value = java.net.URLEncoder.encode(value);
        HttpSession session   = req.getSession(true);
        session.setAttribute(name, value);
        session.setMaxInactiveInterval(loginTime*60);	// 1시간 2019.10.07 ryugj
//        session.setMaxInactiveInterval(10*60);	// 10분 수정 2019.07.02. ryugj
//        session.setMaxInactiveInterval(15*60);	// 15분 수정 2019.07.03. ryugj
//        session.setMaxInactiveInterval(30*60);	// 30분 수정 2019.08.28. ryugj
    }
    
    //21.12.01 추가 - 로그인할 때 최초생성
    public static void setSession(HttpServletRequest req, String name, Object value, int time) {
    	HttpSession session   = req.getSession(true);
    	session.setAttribute(name, value);
    	loginTime = time;
    	session.setMaxInactiveInterval(loginTime*60);
    }
    

    
    public static Object getSession(HttpServletRequest req, String name) {
        HttpSession session   = req.getSession(true);
        return session.getAttribute(name);
    }
	/**
	 * @Method : getSessionType
	 * @Description : null처리이후 세션값 반환
	 * type Param : Boolean - B, String - S, Int - I
	 * @author : csh
	 * 
	 * --------------------------------------------------------
	 * Modification Information
	 * 수정일        	       수정자        	       수정내용
	 * ----------  --------    ---------------------------
	 * 2021.11.15  csh         최초수정
	 */	     
    public static Object getSessionType(HttpServletRequest request, String name, String type ) {
    	HttpSession session   = request.getSession(true);
    	type = type.toUpperCase();
    	if(session.getAttribute(name) == null) {
    		if("B".equals(type)) {
    			session.setAttribute(name, false);
    		}else if("S".equals(type)) {
    			session.setAttribute(name, "");
    		}else if("I".equals(type)) {
    			session.setAttribute(name, 0);
    		}
    	}
    	return session.getAttribute(name);
    }
    
    // KSC ================================================
    public static String han(String str)  {
      if(str == null) {  return new String("");    }
      try{  
            str = new String(str.getBytes("8859_1"),"KSC5601");
      }catch(Exception e){  }
      return str;
    } 

    // ISO ================================================
    public static String ksc(String str)  {
      if(str == null) { return new String("");  }
      try{
            str = new String(str.getBytes("KSC5601"),"8859_1");
      }catch(Exception e){    }
      return str;
    }
    
    // IP정보 추출
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null  || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP"); 
        }
        if (ip == null  || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null  || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null  || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        
        // IP 조회 오류로 2024.03.05 추가
        if (ip == null  || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("REMOTE_ADDR");
        }
        
        // IP 조회 오류 2024.05.27 추가
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("IPV6_ADR");
        }
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
    
    public static Map getMapParam(Map param, String key) {
    	if(param.get(key) == null) {
    		param.put(key, "");
    	}
    	return param;
    }
    
	/**
	 * @Method : getAppRes
	 * @Description : ApplicationResources.properties(properties info read)
	 * @author : csh
	 * 
	 * --------------------------------------------------------
	 * Modification Information
	 * 수정일        	       수정자        	       수정내용
	 * ----------  --------    ---------------------------
	 * 2021.11.15  csh         최초수정
	 */	       
    public static String getAppRes(String key) {
    	Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(ResourceUtils.getFile("classpath:bpsim/config/common/ApplicationResources.properties"))); //classpath 기준으로 찾을 경우
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	String returnStr = "";
    	if(props.get(key) != null) {
    		returnStr = (String) props.get(key);
    	}
    	return returnStr;  
    }
    
    public static String getDataRes(String key) {
    	Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(ResourceUtils.getFile("classpath:bpsim/config/jdbc/datasource.properties"))); //classpath 기준으로 찾을 경우
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	String returnStr = "";
    	if(props.get(key) != null) {
    		returnStr = (String) props.get(key);
    	}
    	return returnStr;  
    }
    

    
}//End Class