<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ tag import = "java.util.Calendar" %>
<%@ tag import = "java.util.Date" %> 
<%@ attribute name = "itime" type="java.lang.Integer"%>
<%@ attribute name = "strDate"  %>
<jsp:doBody var="content" scope="page" />
<%
	String retVal = "";
	String content = (String)jspContext.getAttribute("content");
	if(strDate == null || strDate.length() < 10){
		
	}else{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c2.set(Integer.parseInt(strDate.substring(0,4)),(Integer.parseInt(strDate.substring(5,7))-1),Integer.parseInt(strDate.substring(8,10)));

		Date date1 = c1.getTime(); 
		Date date2 = c2.getTime();
		long d1 = date1.getTime();
		long d2 = date2.getTime();
		int days =(int)((d1-d2)/(1000*60*60*24));
		if(days < itime ) retVal= content;
		else retVal="";
	}
%>
<%=retVal %>