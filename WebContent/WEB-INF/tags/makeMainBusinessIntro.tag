<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "bid" %>
<%@ attribute name = "cate1" %>
<%@ attribute name = "comments" %>
<%@ attribute name = "origin" %>
<%@ attribute name = "username" %>
<%@ attribute name = "regdate" %>
<jsp:doBody var="content" scope="page" />
<%
	String retVal = "";
	String temp = "";
	if(bid.equals("business")){
		if(cate1.equals("1")){
			retVal = "국내기업/";
			temp = "대표이사";
		}
		else if(cate1.equals("1")){
			retVal = "국외기업/";
			temp = "대표이사";
		}
		else if(cate1.equals("1")){
			retVal = "국내기관/";
			temp = "기관장";
		}
		else if(cate1.equals("1")){
			retVal = "국외기관/";
			temp = "기관장";
		}
		
		if(comments.equals("11")) retVal = retVal + "대기업/";
		else if(comments.equals("12")) retVal = retVal + "중소기업/";
		else if(comments.equals("13")) retVal = retVal + "벤처기업/";
		else if(comments.equals("14")) retVal = retVal + "기타/";
		else if(comments.equals("15")) retVal = retVal + "정부기관/";
		else if(comments.equals("16")) retVal = retVal + "연구기관/";
		else if(comments.equals("17")) retVal = retVal + "BT학회/";
		else if(comments.equals("18")) retVal = retVal + "BT협회/";
		else if(comments.equals("19")) retVal = retVal + "기타/";
		
		retVal = retVal + temp + " " + username;
	} else if(bid.equals("report")){
		retVal = origin;
	} else {
		retVal = regdate;
	}
%>
<%=retVal %>