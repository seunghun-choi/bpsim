package bpsim.framework.util;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextConvert {
	
	/** 로그 출력을 위해 설정. 2020.06.22. */
	private static final Logger logger = LoggerFactory.getLogger(TextConvert.class);
	
	/**************************************************
	 * 생성자초기화
	 ***************************************************/
	public TextConvert() {

	}

	/****************************************************************************************************
	 * 텍스트문자의 공백과 줄내림표시를 html형식으로 변경
	 * 
	 * @param text 텍스트문자
	 * @return String html문자
	 ****************************************************************************************************/
	public static String textToHtml(String text) {
		return text.replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
	}

	/****************************************************************************************************
	 * 텍스트문자의 공백과 줄내림표시를 html형식으로 변경
	 * 
	 * @param text 텍스트문자
	 * @return String html문자
	 ****************************************************************************************************/
	public static String codeConversion(String text, String strRep) {
		return text.replaceAll("\n", strRep);
	}

	/****************************************************************************************************
	 * 텍스트문자의 공백과 줄내림표시를 html형식으로 변경
	 * 
	 * @param text 텍스트문자
	 * @return String html문자
	 ****************************************************************************************************/
	public static String codeConversion(String text) {
		String cText = text;
		cText = cText.replaceAll("˚", "degrees");
		return cText;
	}

	/****************************************************************************************************
	 * html문자를 텍스트로 변경
	 * 
	 * @param strVal html문자
	 * @return String 텍스트문자
	 ****************************************************************************************************/
	public static String htmlToText(String strVal) {
		return strVal.replaceAll("(?:<!.*?(?:--.*?--\\s*)*.*?>)|(?:<(?:[^>'\"]*|\".*?\"|'.*?')+>)", "");
	}

	public static String HtmlRemove(String str) {
		String value = str;
		Pattern p = Pattern.compile("\\<(\\/?)(\\w+)*([^<>]*)>");
		Matcher m = p.matcher(value);
		value = m.replaceAll("");

		return value;

	}

	/******************************************************************************************************
	 * 숫자에 콤마추가(3자리씩 끈어서)
	 * 
	 * @param value 문자타입에 숫자값
	 * @return String 변형된 값
	 *******************************************************************************************************/
	public static String comaAdd(String value) {
		int i = value.length();
		String rValue = "";
		for (int j = 1; j <= i; j++) {
			if (j % 3 == 1 && j > 1) {
				rValue = value.charAt(i - j) + "," + rValue;
			} else {
				rValue = value.charAt(i - j) + rValue;
			}
		}
		return rValue;
	}

	/******************************************************************************************************
	 * 날짜형식변경
	 * 
	 * @param date yyyymmdd 형식에 날짜 데이터
	 * @param type 변경하고자 하는 형식
	 * @return String 변경된 날짜 데이터
	 *******************************************************************************************************/
	public static String cvtDateFormat(String date, String type) {
		String cvtDate = "";
		if (type != null && date != null) {
			if (type.equals("yyyy-mm-dd")) {
				cvtDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
			} else if (type.equals("yyyy.mm.dd")) {
				cvtDate = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6);
			} else if (type.equals("yyyy/mm/dd")) {
				cvtDate = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6);
			} else if (type.equals("yyyymmdd")) {
				cvtDate = date.substring(0, 4) + "년" + date.substring(4, 6) + "월" + date.substring(6) + "일";
			} else {
				cvtDate = date;
			}
		}
		return cvtDate;
	}

	/******************************************************************************************************
	 * 영어형식의 데이터를 한글화
	 * 
	 * @param english 영어형식의 데이터
	 * @return String 한글화된 데이터
	 *******************************************************************************************************/
	public static String E2K(String english) {
		
		String korean = null;

		if (english == null) {
			return null;
		}

		try {
			korean = new String(english.getBytes("ISO-8859-1"), "EUC-KR");
		} catch (UnsupportedEncodingException e) {
			korean = new String(english);
			logger.info(e.getMessage());
		}

		return korean;
	}

	/******************************************************************************************************
	 * 널데이터 체크[영어를 한글로]
	 * 
	 * @param strVal 데이터
	 * @return String 체크된 데이터
	 *******************************************************************************************************/
	public static String chkNull(String strVal) {
		// String rVal=E2K(strVal);
		String rVal = (strVal);
		if (rVal == null || rVal.equals("") || rVal.equals("null")) {
			rVal = "";
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 널데이터 체크[영어를 한글로]
	 * 
	 * @param strVal 데이터
	 * @return String 체크된 데이터
	 *******************************************************************************************************/
	public static String zeroChkNull(String strVal) {
		// String rVal=E2K(strVal);
		String rVal = (strVal);
		if (rVal.equals("0")) {
			rVal = "";
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 널데이터 체크[영어를 한글로]
	 * 
	 * @param strVal 데이터배열
	 * @return String 체크된 데이터배열
	 *******************************************************************************************************/
	public static String[] chkNullArr(String[] strVal) {
		String[] rVal = null;
		if (strVal == null || strVal.length == 0) {
			rVal = new String[1];
			rVal[0] = "";
		} else {
			rVal = new String[strVal.length];
			for (int i = 0; i < strVal.length; i++) {
				if (strVal[i] == null) {
					rVal[i] = "";
				} else {
					rVal[i] = E2K(strVal[i]);
				}
			}
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 널데이터 체크[영어를 한글로]
	 * 
	 * @param strVal 데이터
	 * @param strRepl 널일때 대체할 데이터
	 * @return String 체크된 데이터
	 *******************************************************************************************************/
	public static String chkNull(String strVal, String strRepl) {
		// String rVal=E2K(strVal);
		String rVal = (strVal);
		if (rVal == null || rVal.equals("") || rVal.equals("null")) {
			rVal = strRepl;
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 널데이터 체크 인코딩 안함
	 * 
	 * @param strVal 데이터
	 * @param strRepl 널일때 대체할 데이터
	 * @return String 체크된 데이터
	 *******************************************************************************************************/
	public static String chkNull2(String strVal, String strRepl) {
		String rVal = strVal;
		if (rVal == null || rVal.equals("") || rVal.equals("null")) {
			rVal = strRepl;
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 널데이터 체크[단지 널만 체크]
	 * 
	 * @param strVal 데이터
	 * @return String 체크된 데이터
	 *******************************************************************************************************/
	public static String chkOnlyNull(String strVal) {
		String rVal = strVal;
		if (rVal == null) {
			rVal = "";
		}
		return rVal;
	}

	/******************************************************************************************************
	 * 원하는 크기로 자르기
	 * 
	 * @param str 데이터
	 * @param num 자를 크기
	 * @return String
	 *******************************************************************************************************/
	public static String cutStr(String str, int num) {
		String tmp = "";
		if (str.length() > num) {
			tmp = str.substring(0, num) + "...";
		} else {
			tmp = str;
		}
		return tmp;
	}

	/******************************************************************************************************
	 * 해당월에 마지막 날짜 가져오기
	 * 
	 * @param month 월
	 * @param year 년
	 * @return String
	 *******************************************************************************************************/
	public static int getLastDay(String month, String year) {
		int rMonth = Integer.parseInt(month);
		int rYear = Integer.parseInt(year);
		int rInt = 0;
		switch (rMonth) {
		case 2:
			if ((rYear % 4 == 0) && ((rYear % 100 != 0) || (rYear % 400 == 0))) {
				rInt = 29;
			} else {
				rInt = 28;
			}
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			rInt = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			rInt = 30;
			break;
		}
		return rInt;
	}

	/**
	 * strToFloat
	 * 
	 * @param str
	 * @param num
	 * @return float
	 */
	public static float strToFloat(String str, float num) {
		float floatNum = 0.0f;
		try {
			if (!(chkNull(str).equals(""))) {
				floatNum = Float.parseFloat(str);
			} else {
				floatNum = 0.0f;
			}
		} catch (NumberFormatException e) {
			floatNum = num;
			logger.info(e.getMessage());
		}

		return floatNum;
	}

	/**
	 * strToDouble
	 * 
	 * @param str
	 * @param num
	 * @return double
	 */
	public static double strToDouble(String str, double num) {
		double dblNum = 0.0d;
		try {
			dblNum = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			logger.info(e.getMessage());
		}
		return dblNum;
	}

	/**
	 * strToInteger
	 * 
	 * @param str
	 * @param opt
	 * @return int
	 */
	public static int strToInteger(String str, String opt) {
		int intNum = 0;
		try {
			intNum = Integer.parseInt(TextConvert.chkNull(str, opt));
		} catch (NumberFormatException e) {
			logger.info(e.getMessage());
		}
		return intNum;
	}

	/**
	 * strToDoubleFormat
	 * 
	 * @param str
	 * @param num
	 * @return double
	 */
	public static double strToDoubleFormat(String str, double num) {

		double doubleNum = TextConvert.strToDouble(str, num);
		double returnVal = 0.0d;
		DecimalFormat df = null;

		try {
			if (num == 0) {
				df = new DecimalFormat("####");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 1) {
				df = new DecimalFormat("####.#");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 2) {
				df = new DecimalFormat("####.##");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 3) {
				df = new DecimalFormat("####.###");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 4) {
				df = new DecimalFormat("####.####");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 5) {
				df = new DecimalFormat("####.#####");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 6) {
				df = new DecimalFormat("####.######");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 7) {
				df = new DecimalFormat("####.#######");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 8) {
				df = new DecimalFormat("####.########");
				returnVal = Double.parseDouble(df.format(doubleNum));
			} else if (num == 9) {
				df = new DecimalFormat("####.#########");
				returnVal = Double.parseDouble(df.format(doubleNum));
			}
		} catch (Exception e) {
			returnVal = 0;
			logger.info(e.getMessage());
		}

		return returnVal;

	}

	/**
	 * formatNum
	 * 
	 * @param pos
	 * @param num
	 * @return String
	 */
	public static String formatNum(int pos, double num) {
		String formatString = "#";
		String endString = "";

		for (int i = 0; i < pos; i++) {
			if (i == 0){
				endString = ".";
			}
			endString = endString + "#";
		}
		formatString = formatString + endString;
		java.text.DecimalFormat format = new java.text.DecimalFormat(formatString);
		
		return format.format(num);
	}

	/**
	 * commerData
	 * 
	 * @param strValue
	 * @return String
	 */
	public static String commerData(String strValue) {
		String reVal = "";
		DecimalFormat df = new DecimalFormat("#,###.#########");
		try {
			if (!strValue.equals("")) {
				reVal = "" + df.format(strToDouble(strValue, 0));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return reVal;
	}

	/* Null 체크 */
	public static String chkNullcommerData(String strVal) {
		String rVal = strVal;
		String reVal = "";

		if (rVal == null || rVal.equals("") || rVal.equals("null")) {
			reVal = "";
		} else {
			reVal = "" + TextConvert.commerData(rVal);
		}
		return reVal;
	}

	/* 시스템 년도 */
	public String getYear() {
		String year = "";
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
			year = sdf_year.format(c.getTime());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return year;
	}

	/* 시스템 년도 */
	public String getYear2() {
		String year = "";
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf_year = new SimpleDateFormat("yy");
			year = sdf_year.format(c.getTime());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return year;
	}

	/* 시스템 월 */
	public String getMonth() {
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

	/* 시스템 일자 */
	public String getDay() {
		String day = "";
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
			day = sdf_day.format(c.getTime());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return day;
	}

	/* 시스템 년, 월, 일자 */
	public String getToDate(String opt) {
		String toDate = "";
		try {
			if (!opt.equals("")) {
				toDate = getYear() + opt + getMonth() + opt + getDay();
			} else {
				toDate = getYear() + getMonth() + getDay();
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return toDate;
	}

	public String[] strToArray(String dateStr, String delim) {
		String[] arrStr = dateStr.split(delim);
		return arrStr;
	}

	public String strTop(String str) {
		String reStr = "";
		if (str != null && !str.equals("")) {
			if (str.equals("1")){
				reStr = "ㅅ";
			}else if (str.equals("2")){
				reStr = "V";
			}else if (str.equals("3")){
				reStr = ".";
			}else{
				reStr = "";
			}
		}

		return reStr;
	}

	public String strBottom(String str) {
		String reStr = "";
		if (str != null && !str.equals("")) {
			if (str.equals("1")){
				reStr = "ㅅ";
			}else if (str.equals("2")){
				reStr = "V";
			}else if (str.equals("3")){
				reStr = ".";
			}else{
				reStr = "";
			}
		}

		return reStr;
	}

	/******************************************************************************************************
	 * 해당페이지 조회 목록 시작 번호.
	 * 
	 * @param total 총갯수
	 * @param list_per_limit 한페이당 보여주눈 줄수
	 * @param page_per_limit 페이지 묶음 단위
	 * @param page 현재페이지
	 * @return int 형으로 시작번호 반환.
	 *******************************************************************************************************/
	public static int startNumber(int total, int list_per_limit, int page_per_limit, int page) {
		int start = 0;

		try {
			start = ((page - 1) * list_per_limit) + 1;
		} catch (Exception e) {
			start = 1;
			logger.info(e.getMessage());
		}

		return start;
	}

	/******************************************************************************************************
	 * 해당페이지 조회 목록 종료 번호.
	 * 
	 * @param total 총갯수
	 * @param list_per_limit 한페이당 보여주눈 줄수
	 * @param page_per_limit 페이지 묶음 단위
	 * @param page 현재페이지
	 * @return int 형으로 시종료 번호 반환.
	 *******************************************************************************************************/
	public static int endNumber(int total, int list_per_limit, int page_per_limit, int page) {
		int end = 0;
		try {
			end = ((page) * list_per_limit);
		} catch (Exception e) {
			end = 10;
    		logger.info(e.getMessage());
		}
		return end;
	}
	
	/**
     * TEXT 형식으로 반환
     *
     * @param str
     * @return 변환된 String. str이 <code>null</code>일 경우 null을 리턴
     */
    public static String toTEXT(String str) {
        if (str == null){
        	return null;
        }

        String returnStr = str;
        
        returnStr = returnStr.replaceAll("<br>", "\n");
        returnStr = returnStr.replaceAll("&gt;", ">");
        returnStr = returnStr.replaceAll("&lt;", "<");
        returnStr = returnStr.replaceAll("&quot;", "\"");
        returnStr = returnStr.replaceAll("&nbsp;", " ");
        returnStr = returnStr.replaceAll("&amp;", "&");
        returnStr = returnStr.replaceAll("&#34;", "\"");
        returnStr = returnStr.replaceAll("&#39;", "\'");
        returnStr = returnStr.replaceAll("&#40;", "(");
        returnStr = returnStr.replaceAll("&#41;", ")");
        returnStr = returnStr.replaceAll("&#44;", ",");
        returnStr = returnStr.replaceAll("&#46;", ".");
        returnStr = returnStr.replaceAll("&#61;", "=");
        returnStr = returnStr.replaceAll("&middot;","·");
        returnStr = returnStr.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        returnStr = returnStr.replaceAll("&amp;lt;","&lt;");
        returnStr = returnStr.replaceAll("&amp;gt;","&gt");
        return returnStr;
    }

    /**
     * toTEXT_VIEW
     *
     * @param str String
     * @return String
     */
    public static String toTEXT_VIEW(String str) {
        if (str == null){
        	return null;
        }

        String returnStr = str;
        
        returnStr = returnStr.replaceAll("<br>", "\n");
        returnStr = returnStr.replaceAll("&gt;", ">");
        returnStr = returnStr.replaceAll("&lt;", "<");
        returnStr = returnStr.replaceAll("&quot;", "\"");
        returnStr = returnStr.replaceAll("&nbsp;", " ");
        returnStr = returnStr.replaceAll("&amp;", "&");
        returnStr = returnStr.replaceAll("&#34;", "\"");
        returnStr = returnStr.replaceAll("&#39;", "\'");
        returnStr = returnStr.replaceAll("&#40;", "(");
        returnStr = returnStr.replaceAll("&#41;", ")");
        returnStr = returnStr.replaceAll("&#44;", ",");
        returnStr = returnStr.replaceAll("&#46;", ".");
        returnStr = returnStr.replaceAll("&#47;", "/");
        returnStr = returnStr.replaceAll("&#61;", "=");

        return returnStr;
    }
    /**
     * convert
     * Clob -> String 변환
     */   
    public static String convert(Object targetData) throws Exception{
    	try {
	    	StringBuffer buffer = new StringBuffer();
	    	BufferedReader reader = new BufferedReader(((Clob)targetData).getCharacterStream());
	    	String dummy = "";
	    	while((dummy = reader.readLine()) != null){
	    		buffer.append(dummy);
	    	}
	    		reader.close();
	    	return buffer.toString();
    	} catch (ClassCastException e) {
    		return (String) targetData;
		}
    }
}