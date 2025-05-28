<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ tag import = "java.util.Calendar" %>
<%@ tag import = "java.util.Date" %> 
<%@ attribute name = "cl_code" %>
<jsp:doBody var="content" scope="page" />
<%
	String retVal = "";
	if(cl_code.equals("01")) retVal = "R&amp;D성과";
	else if(cl_code.equals("02")) retVal = "기술";
	else if(cl_code.equals("03")) retVal = "정책";
	else if(cl_code.equals("04")) retVal = "산업/시장";
	else if(cl_code.equals("05")) retVal = "윤리";
	else if(cl_code.equals("06")) retVal = "제도";
	else if(cl_code.equals("07")) retVal = "보건";
	else if(cl_code.equals("08")) retVal = "의학";
	else if(cl_code.equals("09")) retVal = "제약/의약";
	else if(cl_code.equals("10")) retVal = "과학";
	else if(cl_code.equals("11")) retVal = "기업";
	else if(cl_code.equals("12")) retVal = "행사/세미나";
	else if(cl_code.equals("13")) retVal = "BT종합";
	else if(cl_code.equals("14")) retVal = "인물";
	else if(cl_code.equals("15")) retVal = "안전성";
	else if(cl_code.equals("16")) retVal = "기술이전";
	else if(cl_code.equals("17")) retVal = "사설/칼럼";
	else if(cl_code.equals("18")) retVal = "기타";
%>
<%=retVal %>