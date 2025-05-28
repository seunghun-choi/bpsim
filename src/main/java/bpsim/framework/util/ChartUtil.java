package bpsim.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartUtil {
	public static String label(Object o, String intervalFormat) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(intervalFormat);
		Date d = sdf.parse(""+o);
		String formatString = "";
		switch(intervalFormat.length()){
		case 4:
			formatString = "yyyy년";
			break;
		case 6:
			formatString = "yyyy년MM월";
			break;
		case 8:
			formatString = "yyyy년MM월dd일";
			break;
		default:
			throw new Exception("Invalid intervalFormat.");	
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	}
	
	@SuppressWarnings("serial")
	public static final Map<Integer, String> intervalFormatMap = new HashMap<Integer, String>(){
		{
			put(GregorianCalendar.DATE, "yyyyMMdd");
			put(GregorianCalendar.WEEK_OF_YEAR, "yyyyMMdd");
			put(GregorianCalendar.MONTH, "yyyyMM");
			put(GregorianCalendar.YEAR, "yyyy");
		}
	};
	
	public static List getIntervalSet(int interval, int intervalCount, String intervalFormat){
		List ret = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat(intervalFormat);
		GregorianCalendar gc = new GregorianCalendar();
		
		for(int i=0;i<intervalCount;i++){
			ret.add(sdf.format(gc.getTime()));
			gc.add(interval, -1);
		}
		
		return ret;
	}
	public static int getIntervalCount(Calendar start, Calendar end, int interval){
		int count = 1;
		int increment = 1;
		if(interval==GregorianCalendar.WEEK_OF_YEAR){
			interval = GregorianCalendar.DATE;
			increment = 7;
		}
		while(start.before(end)){
			count++;
			start.add(interval, increment);
		}
		return count;
	}
	
	public static Map getIntervalSetting(Map params){
		String intervalFormat = "yyyyMM";
		int interval = GregorianCalendar.MONTH;
		int intervalCount = 6;
		
		String sInterval = (String)params.get("interval");
		String sdate_lowerbound = (String)params.get("sdate_lowerbound");
		String sdate_upperbound = (String)params.get("sdate_upperbound");
		try{
			Calendar lower = new GregorianCalendar();
			Calendar upper = new GregorianCalendar();
			lower.setTime(ReqUtils.stringToDate(sdate_lowerbound.replace("-","")));
			upper.setTime(ReqUtils.stringToDate(sdate_upperbound.replace("-","")));
			interval = new Integer(sInterval);
			intervalFormat = ChartUtil.intervalFormatMap.get(interval);
			intervalCount = ChartUtil.getIntervalCount(lower, upper, interval);
		}catch(Exception e){}
		
		List interval_set = ChartUtil.getIntervalSet(interval, intervalCount, intervalFormat);
		
		Map ret = new HashMap();
		ret.put("intervalFormat", intervalFormat);
		ret.put("interval", interval);
		ret.put("interval_set", interval_set);
		return ret;
	}
}
