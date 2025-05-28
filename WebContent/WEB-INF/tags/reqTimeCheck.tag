<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ tag import = "java.util.Calendar" %>
<%@ tag import = "java.util.Date" %> 
<%@ attribute name = "sdate"  %>
<%@ attribute name = "edate"  %>
<%@ attribute name = "flag"  %>

<jsp:doBody var="content" scope="page" />
<%
	String retVal = "<em class='icon' data-label='종료'>종료</em>";
	try{
		 Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        sdate = sdate.replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "").replaceAll("\\.", "");
        edate = edate.replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "").replaceAll("\\.", "");
        c2.set(Integer.parseInt(sdate.substring(0,4)), (Integer.parseInt(sdate.substring(4,6))-1), Integer.parseInt(sdate.substring(6,8)), Integer.parseInt(sdate.substring(8, 10))-1, 59);
		c3.set(Integer.parseInt(edate.substring(0,4)), (Integer.parseInt(edate.substring(4,6))-1), Integer.parseInt(edate.substring(6,8)), Integer.parseInt(edate.substring(8, 10))-1, 59);
		//c2.set(Integer.parseInt(sdate.substring(0,4)), (Integer.parseInt(sdate.substring(4,6))-1), Integer.parseInt(sdate.substring(6,8)), Integer.parseInt(sdate.substring(8, 10)), Integer.parseInt(sdate.substring(10, 12)));
        //c3.set(Integer.parseInt(edate.substring(0,4)), (Integer.parseInt(edate.substring(4,6))-1), Integer.parseInt(edate.substring(6,8)), Integer.parseInt(edate.substring(8, 10)), Integer.parseInt(edate.substring(10, 12)));
        
        Date date1 = c1.getTime(); 
        Date date2 = c2.getTime();
        Date date3 = c3.getTime();
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        long d3 = date3.getTime();
        
        
		String content = (String)jspContext.getAttribute("content");
		// 예정
		if((d2 - d1) >= 0 && !flag.equals("form")){
			retVal = "<em class='icon' data-label='예정'>예정</em>";
		}else if((d2 - d1) >= 0 && flag.equals("form")){
			retVal = "<br/><strong style='text-align:center;display:block;'>신청기간이 아닙니다.</strong>";
		}
		else if((d3 - d1) >= 0){
			if(flag == null)
				retVal = "<em class='icon' data-label='진행중'>진행중</em>";
			else if(flag.equals("already_yn"))
				retVal = "<em class='icon' data-label='진행중'>진행중</em>";
			else if(flag.equals("form"))
				retVal = content;
			else
				retVal = "<em class='icon' data-label='진행중'>진행중</em>";
		}
		else{
			if(flag != null && flag.equals("form"))
				retVal = "<br/><strong style='text-align:center;display:block;'>마감된 신청입니다.</strong>";
			else
				retVal = "<em class='icon' data-label='종료'>종료</em>";
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
<%=retVal %>