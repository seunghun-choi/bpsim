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
 
content = content.replaceAll("&lt;","<");
content = content.replaceAll("&gt;",">");
content = content.replaceAll("&amp;lt;","&lt;");
content = content.replaceAll("&amp;gt;","&gt");
content = content.replaceAll("&#40;","(");
content = content.replaceAll("&#41;",")");
content = content.replaceAll("&middot;","·");
content = content.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//content = content.replaceAll("script", ""); 
content = content.replaceAll("&#39;","'");
content = content.replaceAll("&#34;","\"");
content = content.replaceAll("&#61;","=");
content = content.replaceAll("<P","<DIV> <P"); //2004년 데이터 때문에 변환


 if(length != null && length.intValue() > 0 && content.length() > length.intValue()){
	 content = content.substring(0, length.intValue());
	 if(trail != null){
		 content = content + trail;
	 }
 }
%>
<%= content %>