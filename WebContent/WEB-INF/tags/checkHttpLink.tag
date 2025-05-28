<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>
<jsp:doBody var="content" scope="page" />
<%
 String content = (String)jspContext.getAttribute("content");
 if(trim !=null && trim.equals("true")){
	 content = content.trim();
 }
 if((content.indexOf("http://") < 0) && (content.indexOf("https://") < 0)){
	 content = "http://"+content;
 }
%>
<%= content %>