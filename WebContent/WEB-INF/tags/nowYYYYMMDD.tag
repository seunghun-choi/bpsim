<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag import="java.text.SimpleDateFormat"%>
<%@ tag import="java.util.Date"%>
<%@ tag import="java.util.Locale"%>
<%
Locale locale = java.util.Locale.KOREA;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", locale);
String curTime = sdf.format(new Date());

%>
<%=curTime%>