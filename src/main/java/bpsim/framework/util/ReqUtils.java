package bpsim.framework.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * Return 처리관련 Util
 * @author common
 * @since 2021. 11. 3
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일        	수정자        	수정내용
 *  ----------  --------    ---------------------------
 *  2021. 11. 3  	JHS     최초작성  
 *
 * </pre>
 */
public final class ReqUtils {

	/** 로그 출력을 위해 설정. 2020.06.22. */
	private static final Logger logger = LoggerFactory.getLogger(ReqUtils.class);

	private String[] strKey;//
	private String[] strValue;//
	private Hashtable htList;//
	
	final static Pattern SpecialChars = Pattern.compile("['\"\\-#()@;=*/+]");
	final static String regex = "(union|select|from|where|update|delete|insert)";
	

	/**************************************************
	 * 생성자초기화
	 ***************************************************/
	public ReqUtils() {
		strKey = new String[10];
		strValue = new String[10];
		htList = new Hashtable();
	}

	/*
	 * 사업체 구분 코드 목록
	 */
	public String[] getListKeys() {
		return strKey;
	}

	/*
	 * 사업체 구분 텍스트 목록
	 */
	public String[] getListValues() {
		return strValue;
	}

	public static Map getParameterMap(HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map paramerterMap = request.getParameterMap();
			Iterator iter = paramerterMap.keySet().iterator();
			String key = null;
			String[] value = null;

			while (iter.hasNext()) {
				key = (String) iter.next();
				value = (String[]) paramerterMap.get(key);
				
				/*
				if(value.length > 1){ for(int i=0;i<value.length;i++){
				value[i] = Jsoup.clean(value[i], Whitelist.relaxed()); }
				map.put(key, value); }else{ map.put(key,
				Jsoup.clean(value[0], Whitelist.relaxed())); }
				*/

				if (value.length > 1) {
					String[] reValue = new String[value.length];
					for (int i = 0; i < value.length; i++) {
						String rVal = value[i];
						if (StringUtils.isEmpty(value[i]) || "null".equals(value[i])) {
							rVal = "";
						} else {
							rVal = filter(patternChar(key, rVal));
						}
						reValue[i] = patternChar(key, rVal);
					}
					map.put(key, reValue);
				} else {
					String rVal = value[0];
					if (StringUtils.isEmpty(value[0]) || "null".equals(value[0])) {
						rVal = "";
					} else {
						rVal = filter(patternChar(key,rVal));
					}
					map.put(key, patternChar(key,rVal));
				}
			}
		} catch (Exception e) {
			logger.info(" <<< ReqUtils - getParameterMap(HttpServletRequest request) >>> ");
			logger.info(e.getMessage());
		}
		return map;
	}

	public static Map getParameterMap(HttpServletRequest request, String type) {
		Map map = new HashMap();
		try {
			Map paramerterMap = request.getParameterMap();
			Iterator iter = paramerterMap.keySet().iterator();
			String key = null;
			String[] value = null;

			while (iter.hasNext()) {
				key = (String) iter.next();
				value = (String[]) paramerterMap.get(key);
				if (value.length > 1 || type.equals("array")) {
					map.put(key, value);
				} else {
					map.put(key, value[0]);
				}
			}
		} catch (Exception e) {
			logger.info(" <<< ReqUtils - getParameterMap(HttpServletRequest request) >>> ");
			logger.info(e.getMessage());
		}

		return map;

	}

	public static Map getEmptyResult(String resultColumns) {
		Map map = null;
		if (!StringUtils.isEmpty(resultColumns)) {
			map = new HashMap();
			String[] columns = StringUtils.split(resultColumns, ",");

			for (int i = 0; i < columns.length; i++) {
				map.put(columns[i], "");
			}
		}
		return map;
	}

	/******************************************************************
	 * 널데이터 체크(int 형식)
	 * 
	 * @param num 데이터값
	 * @param value1 널일때 교체할 값
	 ******************************************************************/
	public static int numChk(String num, int value1) {
		int value = 0;
		try {
			value = Integer.valueOf(num).intValue();
		} catch (NumberFormatException e) {
			value = value1;
			logger.info(e.getMessage());
		} catch (Exception e) {
			value = value1;
			logger.info(e.getMessage());
		}
		return value;
	}

	/******************************************************************
	 * 널데이터 체크(String 형식)
	 * 
	 * @param getVal 데이터값
	 * @param chgdata 널일때 교체할 값
	 ******************************************************************/
	public static String getEmptyResult2(String getVal, String chgdata) {
		String rVal = getVal;
		try {
			if (StringUtils.isEmpty(getVal) || "null".equals(getVal)) {
				rVal = chgdata;
			}
		} catch (Exception e) {
			rVal = chgdata;
			logger.info(e.getMessage());
		}
		return rVal;
	}

	/******************************************************************
	 * 원하는 길이만큼 자르기.(String 형식)
	 * 
	 * @param str 데이터값
	 * @param num 자를 길이
	 ******************************************************************/
	public static String cutStr(String str, int num) {
		String tmp = "";
		if (str.length() > num) {
			tmp = str.substring(0, num) + "...";
		} else {
			tmp = str;
		}
		return tmp;
	}

	/******************************************************************
	 * 원하는 길이만큼 자르기.(String 형식)
	 * 
	 * @param str 데이터값
	 * @param num 자를 길이
	 ******************************************************************/
	public static String cutStr(String str, int num, String type) {
		String tmp = "";
		if (str.length() > num) {
			if ("N".equals(type)) {
				tmp = str.substring(0, num) + "**";
			} else if ("T".equals(type)) {
				tmp = str.substring(0, num) + "-****-****";
			} else {
				tmp = str.substring(0, num) + "...";
			}
		} else {
			tmp = str;
		}
		return tmp;
	}

	/******************************************************************
	 * 널데이터 체크(String 형식)
	 * 
	 * @param getVal 데이터값
	 * @param chgdata 널일때 교체할 값
	 ******************************************************************/
	public static String getEmptyResult2(String getVal) {
		String rVal = getVal;
		if (StringUtils.isEmpty(getVal) || "null".equals(getVal)) {
			rVal = "";
		}
		return rVal;
	}

	/******************************************************************
	 * 널데이터 체크(String 형식)
	 * 
	 * @param getVal 데이터값
	 * @param chgdata 널일때 교체할 값
	 ******************************************************************/
	public static String getEmptyResult3(Object getVal) {
		if (getVal == null) {
			return "";
		}
		return "" + getVal;
	}

	/******************************************************************
	 * 널데이터 체크(Map 형식)
	 * 
	 * @param map 데이터
	 ******************************************************************/
	public static Map getResultNullChk(Map map) {
		Map rMap = new HashMap();

		try {
			Iterator iter = map.keySet().iterator();
			String key = null;
			String value = null;

			while (iter.hasNext()) {
				key = (String) iter.next();
				value = "" + map.get(key);

				if (!StringUtils.isEmpty(value) && !"".equals(value) && !" ".equals(value)) {
					rMap.put(key, value);
				} else {
					rMap.put(key, "");
				}
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return rMap;
	}
	
	/******************************************************************
	 * 널데이터 체크(JSONObject 형식)
	 * 
	 * @param map 데이터
	 ******************************************************************/
	public static Object chkJsonObj(JSONObject json, String key) throws JSONException {
		Object obj = new Object();
		
		if(json.length() == 0) {
			obj = "";
		}else {
			obj = json.get(key);
		}
		
		return obj;
	}

	/******************************************************************
	 * 현재년도구하기
	 ******************************************************************/
	public static String getCurrYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String currYear = formatter.format(new java.util.Date());
		return currYear;
	}

	/******************************************************************
	 * 현재 월 구하기
	 ******************************************************************/
	public String getCurrMonth() {
		String month = "";
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf_month = new SimpleDateFormat("MM");
			month = sdf_month.format(c.getTime());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return month;
	}

	/******************************************************************
	 * 현재날짜
	 ******************************************************************/
	public static String getToDate() {
		String thisdate = "";
		try {
			java.util.Date todate = new java.util.Date();
			java.text.SimpleDateFormat sysdate = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);
			thisdate = sysdate.format(todate);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return thisdate;
	}

	public static String getToTime() {
		String thistime = "";
		try {
			java.util.Date todate = new java.util.Date();
			java.text.SimpleDateFormat sysdate = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.KOREA);
			thistime = sysdate.format(todate);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return thistime;
	}

	public static String getFullDate() {
		String thistime = "";
		try {
			java.util.Date todate = new java.util.Date();
			java.text.SimpleDateFormat sysdate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					java.util.Locale.KOREA);
			thistime = sysdate.format(todate);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return thistime;
	}

	// Year ===============================================
	public static String combo_yy(String today) {
		return combo_yy(today, 1993);
	}

	public static String combo_yy(String today, int startYear) {
		StringBuffer buff = new StringBuffer(1024);
		today = today.substring(0, 4);
		int startday = Integer.parseInt(today);

		for (int i = startYear; i < 2016; ++i) {
			buff.append("<option value='" + i + "' ");
			if (i == startday) {
				buff.append("	selected ");
			}
			buff.append(" > " + i + "  </option>\n");
		}
		return buff.toString();
	}

	// To_Day Month =======================================
	public static String combo_mm(String today) {
		StringBuffer buff = new StringBuffer(1024);

		for (int i = 1; i < 13; i++) {
			if (i >= 10) {
				buff.append("<option value='" + i + "' ");
				if ((today.substring(4, 6)).equals("" + i)) {
					buff.append(" selected ");
				}
			} else {
				buff.append("<option value='0" + i + "' ");
				if ((today.substring(4, 6)).equals("0" + i)) {
					buff.append(" selected ");
				}
			}
			buff.append(" > " + i + "  </option>\n");
		}
		return buff.toString();
	}

	// To_Day Day =========================================
	public static String combo_dd(String today) {
		StringBuffer buff = new StringBuffer(1024);

		for (int i = 1; i < 32; i++) {
			if (i <= 9) {
				buff.append("<option value='0" + i + "' ");
				if ((today.substring(6, 8)).equals("0" + i)) {
					buff.append(" 	selected ");
				}
				buff.append(" > " + i + "  </option>\n");
			} else {
				buff.append("<option value='" + i + "' ");
				if ((today.substring(6, 8)).equals("" + i)) {
					buff.append(" 	selected ");
				}
				buff.append(" > " + i + "  </option>\n");
			}
		}
		return buff.toString();
	}

	// To_Hour/Mi/Sec =========================================
	public static String combo_hh(int hour, String flag) {
		StringBuffer buff = new StringBuffer(1024);
		int j = 0;
		if (flag.equals("hh")) {
			j = 25;
		} else if (flag.equals("mm")) {
			j = 61;
		} else if (flag.equals("ss")) {
			j = 61;
		}

		for (int i = 1; i < j; i++) {
			if (i <= 9) {
				buff.append("<option value='0" + i + "' ");
				if (hour == i) {
					buff.append(" 	selected ");
				}
				buff.append(" > " + i + "  </option>\n");
			} else {
				buff.append("<option value='" + i + "' ");
				if (hour == i) {
					buff.append(" 	selected ");
				}
				buff.append(" > " + i + "  </option>\n");
			}
		}
		return buff.toString();
	}

	/*************************************************************
	 * 현재 시간 가져오기
	 * 
	 * @return hh
	 *************************************************************/
	public static int getHour() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/*************************************************************
	 * 현재 분 가져오기
	 * 
	 * @return mi
	 *************************************************************/
	public static int getMi() {
		Calendar now = Calendar.getInstance();
		int mi = now.get(Calendar.MINUTE);
		return mi;
	}

	/*************************************************************
	 * 현재 초 가져오기
	 * 
	 * @return se
	 *************************************************************/
	public static int getSe() {
		Calendar now = Calendar.getInstance();
		int se = now.get(Calendar.SECOND);
		return se;
	}

	/******************************************************************************************************
	 * 해당페이지 네비게이션 바를 만든다.
	 * 
	 * @param total 총 갯수
	 * @param list_per_limit 한 페이지당 보여주눈 줄수
	 * @param page_per_limit 페이지 묶음 단위
	 * @param page 현재페이지
	 * @return String 형으로 페이지 네비게이션 바를 그린다.
	 *******************************************************************************************************/
	public String paging(int total, int list_per_limit, int page_per_limit, int page) {

		int total_page_temp = total % list_per_limit == 0 ? 0 : 1;
		int total_page = (total / list_per_limit) + total_page_temp;

		if (page == 0) {
			page = 1;
		}

		int page_list_temp = page % page_per_limit == 0 ? 0 : 1;
		int page_list = (page / page_per_limit) + page_list_temp - 1;
		String navigation = "";
		int prev_page = 0;
		// 페이지 리스트의 첫번째가 아닌 경우엔 [1]...[prev] 버튼을 생성한다.
		if (page_list > 0) {
			navigation = "<a href=\"javascript:goPage('1');\" class='urLnkFunction'>[" + 1 + "]</a> ";
			prev_page = page_list * page_per_limit;
			navigation += "<a href=\"javascript:goPage('" + prev_page + "');\">[◀]</a> ";
		}

		// 페이지 목록 가운데 부분 출력
		int page_end = (page_list + 1) * page_per_limit;
		if (page_end > total_page) {
			page_end = total_page;
		}

		for (int setpage = page_list * page_per_limit + 1; setpage <= page_end; setpage++) {
			if (setpage == page_end) {
				if (setpage == page) {
					navigation += "<font color='#0066CC'><strong>" + setpage + "</strong></font> ";
				} else {
					navigation += "<a href=\"javascript:goPage('" + setpage + "')\" class='urLnkFunction'>" + setpage + "</a> ";
				}
			} else {
				if (setpage == page) {
					navigation += "<font color='#0066CC'><strong>" + setpage + "</strong></font> ";
				} else {
					navigation += "<a href=\"javascript:goPage('" + setpage + "')\" class='urLnkFunction'>" + setpage + "</a> ";
				}
			}
		}

		// 페이지 목록 맨 끝이 $total_page 보다 작을 경우에만, [next]...[total_page] 버튼을 생성한다.
		if (page_end < total_page) {
			int next_page = (page_list + 1) * page_per_limit + 1;
			navigation += "<a href=\"javascript:goPage('" + next_page + "')\">[▶]</a> ";
			navigation += "<a href=\"javascript:goPage('" + total_page + "');\" class='urLnkFunction'>[" + total_page + "]</a>";
		}

		return navigation;
	}

	/******************************************************************
	 * 문자열을 UTF-8 변환
	 * 
	 * @param str 문자열
	 ******************************************************************/
	public static String getEncode(String str) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "";
		}
	}

	public static String getEncode(String str, String fmt) {
		try {
			return java.net.URLEncoder.encode(str, fmt);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "";
		}
	}

	/******************************************************************
	 * 폴더 만들기
	 * 
	 * @param
	 ******************************************************************/
	public static void upFolder(HttpServletRequest request, String path) {
		String upFolder = request.getSession().getServletContext().getRealPath(path);
		File upDir = new File(upFolder);
		if (!upDir.exists()) {
			// 파일디렉토리없으면 만들기
			if (upDir.mkdir()) {
				logger.info(upFolder + " make ok");
			} else {
				logger.info(upFolder + " make ok");
			}
		}
	}

	/******************************************************************
	 * 'YYYYMMDD' 형태의 String형을 Date형으로 만들어 리턴
	 * 
	 * @param
	 ******************************************************************/
	public static Date stringToDate(String d) {

		int year = Integer.parseInt(d.substring(0, 4));
		int month = Integer.parseInt(d.substring(4, 6));
		int day = Integer.parseInt(d.substring(6));

		Calendar cdate = java.util.Calendar.getInstance();
		cdate.set(Calendar.YEAR, year);
		cdate.set(Calendar.MONTH, month);
		cdate.set(Calendar.DATE, day);

		Date ddate = cdate.getTime(); // java.sql.Date 가 아님..
		return ddate;
	}

	/******************************************************************
	 * System의 현재 날짜를 yyyyMMdd형식으로 반환하는 method
	 * 
	 * @param
	 ******************************************************************/
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String currDate = formatter.format(new java.util.Date());
		return currDate;
	}
	/******************************************************************
	 * System의 어제 날짜를 yyyyMMdd형식으로 반환하는 method
	 * 
	 * @param
	 ******************************************************************/
	public static String getYesterday() {
		Date dDate = new Date();
		dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
		SimpleDateFormat dSdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String yesterday = dSdf.format(dDate);
		return yesterday;
	}
	
	/******************************************************************
	 * System-month의 마지막 날짜를 yyyyMMdd형식으로 반환하는 method
	 * 
	 * @param
	 ******************************************************************/
	public static String getDateLastDay(int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
        cal.add(Calendar.MONTH, -month);
        
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(cal.getTime());
        
        date = date.substring(0, 6) + day;
        
        return date;
	}
	
	/******************************************************************
	 * date를 받아 month를 빼고 마지막 날짜를 yyyyMMdd형식으로 반환하는 method
	 * 
	 * @param
	 ******************************************************************/
	public static String getDateLastDay(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        cal.add(Calendar.MONTH, -month);
        
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date_str = formatter.format(cal.getTime());
        
        date_str = date_str.substring(0, 6) + day;
        
        return date_str;
	}
	
	/******************************************************************
	 * System-month 날짜를 yyyyMMdd형식으로 반환,  method
	 * 
	 * @param
	 ******************************************************************/
	public static String getDate(int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -month);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(cal.getTime());
		
		return date;
	}
	
	/******************************************************************
	 * date를 받아 month를 빼고 날짜를 yyyyMMdd형식으로 반환,  method
	 * 
	 * @param
	 ******************************************************************/
	public static String getDate(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -month);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date_str = formatter.format(cal.getTime());
		
		return date_str;
	}
	
	/******************************************************************
	 * System-day 날짜를 yyyyMMdd형식으로 반환,  method
	 * 
	 * @param
	 ******************************************************************/
	public static String getMinusDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -day);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(cal.getTime());
		
		return date;
	}



	/******************************************************************
	 * System의 현재 날짜를 yyyyMMdd형식으로 반환하는 method
	 * 
	 * @param
	 ******************************************************************/
	public static String getCurrentDate(String divider) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + divider + "MM" + divider + "dd");
		String currDate = formatter.format(new java.util.Date());
		return currDate;
	}

	/****************************************************************************************************
	 * 텍스트문자의 공백과 줄내림표시를 html형식으로 변경
	 * 
	 * @param text 텍스트문자
	 * @return String html문자
	 ****************************************************************************************************/
	public String textToHtml(String text) {
		String txt = getEmptyResult2(text);
		return txt.replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
	}

	/****************************************************************************************************
	 * 숫자형 데이터를 회계단위로 표현(10000=>10,000)
	 * 
	 * @param numData 텍스트형 숫자 데이타
	 * @return String 변형된 데이타
	 ****************************************************************************************************/
	public String numFormat(String numData) {
		NumberFormat nf = NumberFormat.getInstance();
		String chgDataType = "0";
		try {
			chgDataType = nf.format(Long.parseLong(numData));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return chgDataType;
	}

	/****************************************************************************************************
	 * yyyymmdd형식의 날짜 문자열을 yyyy-mm-dd형태로 변경
	 * 
	 * @param yyyymmdd 텍스트문자
	 * @return yyyy-mm-dd 텍스트문자
	 ****************************************************************************************************/
	public static String cvtDate(String yyyymmdd) {
		String rval = "";
		if (yyyymmdd != null) {
			if (!yyyymmdd.equals("") && yyyymmdd.length() >= 8) {
				rval = yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8);
			}
		}

		return rval;
	}
	
	/****************************************************************************************************
	 * yyyymmdd형식의 날짜 문자열을 yyyy/mm/dd형태로 변경
	 * 
	 * @param yyyymmdd 텍스트문자
	 * @return yyyy-mm-dd 텍스트문자
	 ****************************************************************************************************/
	public static String cvtDate2(String yyyymmdd) {
		String rval = "";
		if (yyyymmdd != null) {
			if (!yyyymmdd.equals("") && yyyymmdd.length() >= 8) {
				rval = yyyymmdd.substring(0, 4) + "/" + yyyymmdd.substring(4, 6) + "/" + yyyymmdd.substring(6, 8);
			}
		}
		
		return rval;
	}

	/****************************************************************************************************
	 * Calendar객체를 전달받은 구분값으로 yyyy mm dd형태로 반환
	 * 
	 * @param yyyymmdd
	 *            텍스트문자
	 * @return yyyy-mm-dd 텍스트문자
	 ****************************************************************************************************/
	public static String cvtDate2(Calendar cal, String d) {
		String rval = "";
		rval = cal.get(Calendar.YEAR) + d + (cal.get(Calendar.MONTH) + 1 < 10 ? ("0" + (cal.get(Calendar.MONTH) + 1)) : cal.get(Calendar.MONTH) + 1)
				+ d + (cal.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + cal.get(Calendar.DAY_OF_MONTH)) : cal.get(Calendar.DAY_OF_MONTH));

		return rval;
	}

	/****************************************************************************************************
	 * 00000000000형택의 문자열을 00-0000-0000 또는 000-0000-0000형태로 변경(전화번호 데이터에 사용)
	 * 
	 * @param 00000000000 텍스트문자
	 * @return 00-0000-0000 또는 000-0000-0000 텍스트문자 length의 return값은 int임!!
	 ****************************************************************************************************/
	public String telNum(String tel) {
		String rval = "";
		if (!"".equals(tel) && 11 == tel.length()) {
			rval = tel.substring(0, 3) + "-" + tel.substring(3, 7) + "-" + tel.substring(7, 11);
		} else if (!"".equals(tel) && 10 == tel.length()) {
			if ("2".equals(tel.substring(1, 2))) {
				rval = tel.substring(0, 2) + "-" + tel.substring(2, 6) + "-" + tel.substring(6, 10);
			} else {
				rval = tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6, 10);
			}
		} else if (!"".equals(tel) && 9 == tel.length()) {
			rval = tel.substring(0, 2) + "-" + tel.substring(2, 5) + "-" + tel.substring(5, 9);
		} else {
			rval = tel;
		}
		return rval;
	}

	/****************************************************************************************************
	 * 배열형태의 데이터를 특정 구분자 값으로 엮어 String 형태로 만들기
	 * 
	 * @param String[] data 데이터
	 * @param String needle 구분자
	 * @return String 값
	 ****************************************************************************************************/
	public String join(String[] data, String needle) {
		String rVal = "";
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				if (i == 0) {
					rVal += data[i];
				} else {
					rVal += needle + data[i];
				}
			}
		}
		return rVal;
	}

	/****************************************************************************************************
	 * 쿠키체크.
	 * 
	 * @param String name 쿠키이름
	 * @return String 값
	 ****************************************************************************************************/
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		try {
			if (cookies == null) {
				return null;
			}
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase(name)) {
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					return cookie.getValue();
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}

	/****************************************************************************************************
	 * 쿠키설정.
	 * 
	 * @param String name 쿠키이름
	 * @return String 값
	 ****************************************************************************************************/

	public static void setCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/****************************************************************************************************
	 * paramter가 Null일 경우 문자열 (공백문자)를 리턴
	 ****************************************************************************************************/
	public String nullToString(String str) {
		return (str == null) ? "" : str.trim();
	}

	/****************************************************************************************************
	 * Object 을 int으로 바꿔주기. String으로 만들고 Integer.parseInt() 돌리기. 안 되는 경우는 -1 리턴.
	 ****************************************************************************************************/
	public int objectToInt(Object obj) {
		String val = "" + obj;
		try {
			return new Integer(val);
		} catch (Throwable t) {
			logger.info(t.getMessage());
			return -1;
		}
	}

	/****************************************************************************************************
	 * Object를 String[] 으로 바꿔주기. 입력이 null 이면 길이 String[0] 리턴.
	 ****************************************************************************************************/
	public static String[] objectToStringArray(Object obj) {
		if (obj == null)
			return new String[0];
		if (obj instanceof String[]) {
			return (String[]) obj;
		}
		if (obj instanceof Object[]) {
			Object[] casted = (Object[]) obj;
			String[] ret = new String[casted.length];
			for (int i = 0; i < casted.length; i++) {
				ret[i] = "" + casted[i];
			}
			return ret;
		}
		return new String[] { "" + obj };
	}

	/****************************************************************************************************
	 * Object를 String 으로 바꿔서 첫 X 캐릭터를 돌려준다. 입력이 null 이면 "" 리턴.
	 ****************************************************************************************************/
	public static String prefix(Object o, int length) {
		if (o == null) {
			return "";
		}
		String s = "" + o;
		return s.substring(0, Math.min(s.length(), length));
	}

	/****************************************************************************************************
	 * 파라미터 맵을 String 배열로 돌려준다. 입력받은 문자는 HTML Encoding 으로 바꿔준다.
	 ****************************************************************************************************/
	public static String[] retrieveParams(Object[] keys, Map paramMap, char escape) {
		String[] ret = new String[keys.length];
		int index = 0;
		String replaceSeq = StringEscapeUtils.escapeHtml("" + escape);
		for (Object key : keys) {
			Object value = paramMap.get(key);
			String safeValue = "";
			if (value != null) {
				safeValue = "" + value;
				safeValue = safeValue.replace("" + escape, replaceSeq);
			}
			ret[index++] = safeValue;
		}
		return ret;
	}

	/******************************************
	 * 사업체 구분 코드에 따른 이름 정보
	 ******************************************/
	public String getGubnOrg(String code) {
		return (String) htList.get(code);
	}

	public String removeHtml(String content) {
		Pattern SCRIPTS = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL);
		Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>", Pattern.DOTALL);
		Pattern TAGS = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
		Pattern nTAGS = Pattern.compile("<\\w+\\s+[^<]*\\s*>");
		Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");
		Pattern WHITESPACE = Pattern.compile("\\s\\s+");

		Matcher m;

		m = SCRIPTS.matcher(content);
		content = m.replaceAll("");
		m = STYLE.matcher(content);
		content = m.replaceAll("");
		m = TAGS.matcher(content);
		content = m.replaceAll("");
		m = ENTITY_REFS.matcher(content);
		content = m.replaceAll("");
		m = WHITESPACE.matcher(content);
		content = m.replaceAll(" ");

		return content;

	}
	
	public String removeHtml2(String content) {
		return content.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}
	

	public static void printRequest(HttpServletRequest request) {
		Map params = request.getParameterMap();
		for (Object o : params.keySet()) {
			Object oo = params.get(o);
			if (oo instanceof String[]) {
				logger.info(o + ":");
				String[] ss = (String[]) oo;
				for (String s : ss) {
					logger.info(s + ";");
				}
			} else {
				logger.info(o + ":" + oo);
			}
		}
	}

	public static String[] filter(String[] values) {
		if (values == null) {
			return null;
		}
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			value = filter(value);
			values[i] = value;
		}
		return values;
	}

	public static String filter(String value) {
		if (value == null) {
			return null;
		}

		// You'll need to remove the spaces from the html entities below
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("\"", "&#34;");
		value = value.replaceAll("%20", "&#34;");
		//value = value.replaceAll("=", "&#61;"); 22.07.26 비밀번호 = 치환
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		// value = value.replaceAll("script", "");

		value = value.replaceAll("onabort", "").replaceAll("onactivate", "").replaceAll("afterprint", "").replaceAll("afterupdate", "").replaceAll("beforeactivate", "").replaceAll("beforecopy", "")
				.replaceAll("beforecut", "").replaceAll("beforedeactivate", "").replaceAll("beforeeditfocus", "").replaceAll("beforepaste", "").replaceAll("beforeprint", "").replaceAll("beforeunload", "")
				.replaceAll("beforeupdate", "").replaceAll("blur", "").replaceAll("bounce", "").replaceAll("cellchange", "").replaceAll("contextmenu", "").replaceAll("controlselect", "").replaceAll("copy", "")
				.replaceAll("dataavailable", "").replaceAll("datasetchanged", "").replaceAll("datasetcomplete", "").replaceAll("dblclick", "").replaceAll("deactivate", "").replaceAll("dragend", "")
				.replaceAll("dragenter", "").replaceAll("dragleave", "").replaceAll("dragover", "").replaceAll("dragstart", "").replaceAll("error", "").replaceAll("errorupdate", "").replaceAll("filterchange", "")
				.replaceAll("finish", "").replaceAll("focusin", "").replaceAll("focusout", "").replaceAll("keydown", "").replaceAll("keypress", "").replaceAll("keyup", "").replaceAll("layoutcomplete", "")
				.replaceAll("losecapture", "").replaceAll("mousedown", "").replaceAll("mouseenter", "").replaceAll("mouseleave", "").replaceAll("mousemove", "").replaceAll("mouseout", "")
				.replaceAll("mouseover", "").replaceAll("mouseup", "").replaceAll("mousewheel", "").replaceAll("moveend", "").replaceAll("movestart", "").replaceAll("paste", "").replaceAll("propertychange", "")
				.replaceAll("readystatechange", "").replaceAll("reset", "").replaceAll("resize", "").replaceAll("resizeend", "").replaceAll("resizestart", "").replaceAll("rowenter", "").replaceAll("rowexit", "")
				.replaceAll("rowsdelete", "").replaceAll("rowsinserted", "").replaceAll("scroll", "").replaceAll("selectionchange", "").replaceAll("selectstart", "").replaceAll("submit", "")
				.replaceAll("eval\\((.*)\\)", "");
		
				// replaceAll("focus","").
				// replaceAll("help","").
				// replaceAll("load","").
				// replaceAll("drop","").
				// replaceAll("drag","").
				// replaceAll("move","").
				// replaceAll("select","").
				// replaceAll("start","").
				// replaceAll("stop","").
				// replaceAll("change","").
				// replaceAll("click","").
				// replaceAll("cut","").
		
		return value;
	}

	public static String filterForSearch(String value) {
		if (value == null) {
			return null;
		}

		// You'll need to remove the spaces from the html entities below
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\"", "&#34;");
		value = value.replaceAll("%20", "&#34;");
		value = value.replaceAll("=", "&#61;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		value = value.replaceAll("onabort", "").replaceAll("onactivate", "").replaceAll("afterprint", "").replaceAll("afterupdate", "").replaceAll("beforeactivate", "").replaceAll("beforecopy", "")
				.replaceAll("beforecut", "").replaceAll("beforedeactivate", "").replaceAll("beforeeditfocus", "").replaceAll("beforepaste", "").replaceAll("beforeprint", "").replaceAll("beforeunload", "")
				.replaceAll("beforeupdate", "").replaceAll("blur", "").replaceAll("bounce", "").replaceAll("cellchange", "").replaceAll("click", "").replaceAll("contextmenu", "").replaceAll("controlselect", "")
				.replaceAll("dataavailable", "").replaceAll("datasetchanged", "").replaceAll("datasetcomplete", "").replaceAll("dblclick", "").replaceAll("deactivate", "").replaceAll("dragend", "")
				.replaceAll("dragenter", "").replaceAll("dragleave", "").replaceAll("dragover", "").replaceAll("dragstart", "").replaceAll("errorupdate", "").replaceAll("filterchange", "")
				.replaceAll("focusin", "").replaceAll("focusout", "").replaceAll("keydown", "").replaceAll("keypress", "").replaceAll("keyup", "").replaceAll("layoutcomplete", "").replaceAll("losecapture", "")
				.replaceAll("mousedown", "").replaceAll("mouseenter", "").replaceAll("mouseleave", "").replaceAll("mousemove", "").replaceAll("mouseout", "").replaceAll("mouseover", "").replaceAll("mouseup", "")
				.replaceAll("mousewheel", "").replaceAll("moveend", "").replaceAll("movestart", "").replaceAll("paste", "").replaceAll("propertychange", "").replaceAll("readystatechange", "")
				.replaceAll("reset", "").replaceAll("resize", "").replaceAll("resizeend", "").replaceAll("resizestart", "").replaceAll("rowenter", "").replaceAll("rowexit", "").replaceAll("rowsdelete", "")
				.replaceAll("rowsinserted", "").replaceAll("scroll", "").replaceAll("selectionchange", "").replaceAll("selectstart", "").replaceAll("submit", "").replaceAll("eval\\((.*)\\)", "");
		
				// replaceAll("change","").
				// replaceAll("copy","").
				// replaceAll("cut","").
				// replaceAll("drag","").
				// replaceAll("drop","").
				// replaceAll("error","").
				// replaceAll("finish","").
				// replaceAll("focus","").
				// replaceAll("help","").
				// replaceAll("load","").
				// replaceAll("move","").
				// replaceAll("select","").
				// replaceAll("start","").
				// replaceAll("stop","").
		
		return value;
	}


	  	public static Map setRegdate(Map params, String key) {	  
			String regdate="";
			String regyear = ReqUtils.getEmptyResult2((String)params.get("reg_year"),"");
			if(!StringUtils.isEmpty(regyear)) {
				regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_year"))+"-";
	            regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_month"))+"-";
	            regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_day"))+" ";
	            regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_hh"))+":";
	            regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_mi"))+":";
	            regdate+=ReqUtils.getEmptyResult2((String)params.get("reg_se"));
	        } else {
	        	regdate = ReqUtils.getFullDate();
	    	}
			params.put(key, regdate);
			return params;
	  	}
	  	
	  	public static Map setEditdate(Map params, String key) {	  
	  		String cateStr = "";
	  		cateStr = key.replaceAll("date", "");
	  		String targetDate = (String)params.get(key);
	  		String targetYear = ReqUtils.getEmptyResult2((String)params.get(cateStr+"_year"),"");
			if(!StringUtils.isEmpty(targetYear)){
					targetDate = ReqUtils.getEmptyResult2((String)params.get(cateStr+"_year"))+"-";
					targetDate += ReqUtils.getEmptyResult2((String)params.get(cateStr+"_month"))+"-";
					targetDate += ReqUtils.getEmptyResult2((String)params.get(cateStr+"_day"));
					
					String edit_time = ReqUtils.getEmptyResult2((String)params.get(cateStr+"_hh"));
					if(edit_time!=null && !edit_time.equals("")){
						if(edit_time.length()==1){
							edit_time = "0"+edit_time;
						}
						targetDate+=" "+edit_time + ":00.000";
					}
			}
	  		params.put(key, targetDate);
	  		return params;
	  	}
	  	
	  	
	  	
	  	public static Map makeOrigin(Map params) {
	  	// origin 생성(몇몇 게시판에서 [저자/소속] 의 형태로 데이터 생성됨.
	  		String origin = ReqUtils.getEmptyResult2((String) params.get("origin"), "");
			if ("".equals(origin)) {
				String origin1 = ReqUtils.getEmptyResult2((String) params.get("origin1"), "");
				String origin2 = ReqUtils.getEmptyResult2((String) params.get("origin2"), "");

				if ((!"".equals(origin1)) && (!"".equals(origin2))) {
					origin = origin1 + "/" + origin2;
				}
			}
			params.put("origin", origin);

			String notice = ReqUtils.getEmptyResult2((String) params.get("notice"), "0");
			params.put("notice", notice);

			// tag 넣기, 몇 몇 게시판에서는 tag를 직접 사용하기도, 합쳐서 사용하기도 함.
			String tag = ReqUtils.getEmptyResult2((String) params.get("tag"), "");

			if ("".equals(tag)) {
				String tag1 = ReqUtils.getEmptyResult2((String) params.get("tag1"), "");
				String tag2 = ReqUtils.getEmptyResult2((String) params.get("tag2"), "");
				String tag3 = ReqUtils.getEmptyResult2((String) params.get("tag3"), "");
				String tag4 = ReqUtils.getEmptyResult2((String) params.get("tag4"), "");
				String tag5 = ReqUtils.getEmptyResult2((String) params.get("tag5"), "");

				if (!"".equals(tag2)) {
					tag1 = tag1 + "|";
				}
				if (!"".equals(tag3)) {
					tag2 = tag2 + "|";
				}
				if (!"".equals(tag4)) {
					tag3 = tag3 + "|";
				}
				if (!"".equals(tag5)) {
					tag4 = tag4 + "|";
				}
				tag = tag1 + tag2 + tag3 + tag4 + tag5;
				params.put("tag", tag);
			}
			return params;
	  	}
	  	
	  	public static String substringByBytes(String str, int beginBytes, int endBytes) {
	  		
	  		if (str == null || str.length() == 0) {return "";}
  			if (beginBytes < 0) {beginBytes = 0;}
	  		if (endBytes < 1) {return "";}
	  		
	  		int len = str.length();
	  		int beginIndex = -1;
	  		int endIndex = 0;
	  		int curBytes = 0;
	  		String ch = null;
	  		
	  		for (int i = 0; i < len; i++) {
	  			ch = str.substring(i, i + 1);
	  			int tmp = ch.getBytes().length;
	  			if(tmp == 3) {tmp -= 1;}
		        curBytes += tmp;
		 
		        if (beginIndex == -1 && curBytes >= beginBytes) {
		            beginIndex = i;
		        }

		        if (curBytes > endBytes) {
		            break;
		        } else {
		            endIndex = i + 1;
		        }
		    }
		 
	  		if(str.length() > endIndex) {
	  			str = str.substring(beginIndex, endIndex)+"...";
	  		}
		    return str;
  		}
	  	
	  	public static String patternChar(String key, String str) {
	  		if(str != null) {
	  			if("s_key".equals(key) || "s_str".equals(key) || "listing".equals(key) || "cPage".equals(key) || "sort".equals(key)
	  					|| "userseq".equals(key) || "count".equals(key) || "order".equals(key) || "top".equals(key)
	  					|| "hbrd_news_sn".equals(key) || "num".equals(key) || "bno".equals(key) || "intListCnt".equals(key)|| "register_sn".equals(key)
	  					|| "cl_code".equals(key)
	  			) 
	  			{
	  				//"num".equals(key)
	  				//page_mode
	  				//_빼고 모든 특수문자 제거
	  				String match = "/[\\{\\}\\[\\]\\/?.;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"]/g";
	  				str = str.replaceAll(match, " ");
	  				//query injection 처리
	  				str = str.replaceAll("update", "");
	  				str = str.replaceAll("delete", "");
	  				str = str.replaceAll("insert", "");
	  				str = str.replaceAll("where", "");
	  				str = str.replaceAll("from", "");
	  			}
	  		}
	  		return str;
	  	}
	  	
	  	public static void chkReffer(HttpServletRequest request) throws Exception {
	  		String referer = request.getHeader("REFERER"); 
			
			if( referer == null || referer.length() == 0){
				throw new Exception("정상적인 접근이 아닙니다.");
			}
	  	}
	  	
}