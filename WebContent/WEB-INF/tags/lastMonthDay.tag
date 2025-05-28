<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ tag import = "java.util.Calendar" %>

<%@ attribute name = "reqYear" type="java.lang.Integer"%>
<%@ attribute name = "reqMonth" type="java.lang.Integer"%>
<%
	Calendar cal = Calendar.getInstance();

	if(reqYear == null || reqYear == 0){
		reqYear = cal.get(Calendar.YEAR);
	}
	if(reqMonth == null || reqMonth == 0){
		reqMonth = cal.get(Calendar.MONTH)+1;
	}
	
	cal.set(reqYear,reqMonth-1,1);
	
%>
<%=cal.getActualMaximum(Calendar.DAY_OF_MONTH)%>