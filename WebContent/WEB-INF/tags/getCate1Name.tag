<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "bid"  %>
<%@ attribute name = "cate1"  %>
<jsp:doBody var="content" scope="page" />
<%
	String retVal = "";
	if(bid.equals("stat")){
		if(cate1.equals("01")){
			retVal = "인력";
		}
		else if(cate1.equals("02")){
			retVal = "예산";
		}
		else if(cate1.equals("03")){
			retVal = "산업";
		}
		else if(cate1.equals("04")){
			retVal = "논문";
		}
		else if(cate1.equals("05")){
			retVal = "특허";
		}
		else if(cate1.equals("06")){
			retVal = "기타";
		}
	}
%>
<%=retVal %>