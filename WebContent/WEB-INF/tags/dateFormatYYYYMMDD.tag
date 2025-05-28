<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "trim" %>
<%@ tag import = "java.util.Calendar" %>
<%@ tag import="java.util.Date"%>
<%@ tag import="java.text.SimpleDateFormat"%>
<jsp:doBody var="rssDate" scope="page" />
<%
	String dateString = "";
	String rssDate = (String)jspContext.getAttribute("rssDate");
	if(trim !=null && trim.equals("true")){
		rssDate = rssDate.trim();
		
		//*************************************************************
	     String textDate = rssDate.substring(5,16);//일, 월, 년 부분 만
	     
	     textDate = textDate.replaceAll("Jan","01");//1월
	     textDate = textDate.replaceAll("Feb","02");//2월
	     textDate = textDate.replaceAll("Mar","03");//3월
	     textDate = textDate.replaceAll("Apr","04");//4월
	     textDate = textDate.replaceAll("May","05");//5월
	     textDate = textDate.replaceAll("Jun","06");//6월
	     textDate = textDate.replaceAll("Jul","07");//7월
	     textDate = textDate.replaceAll("Aug","08");//8월
	     textDate = textDate.replaceAll("Sep","09");//9월
	     textDate = textDate.replaceAll("Oct","10");//10월
	     textDate = textDate.replaceAll("Nov","11");//11월
	     textDate = textDate.replaceAll("Dec","12");//12월
	     
	     SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
	     Date date = format.parse(textDate);
	     
	     SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
	     dateString = format1.format(date);
	}else{
		dateString = rssDate;
	}

	
%>
<%=dateString %>