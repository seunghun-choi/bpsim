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
 
 content = content.replaceAll("더보기&nbsp;&raquo;","");
 //content = content.replaceAll("&apos","'");
 content = content.replaceAll("(?:<!.*?(?:--.*?--\\s*)*.*?>)|(?:<(?:[^>'\"]*|\".*?\"|'.*?')+>)","");
 content = content.replaceAll("Andamp;amp;","&");
 content = content.replaceAll("나노", "<mark>나노</mark>");
 content = content.replaceAll("nano", "<mark>nano</mark>");
 content = content.replaceAll("NANO", "<mark>NANO</mark>");
 content = content.replaceAll("Nano", "<mark>Nano</mark>");
 
 if(length != null && length.intValue() > 0 && content.length() > length.intValue()){
	 content = content.substring(0, length.intValue());
	 if(trail != null){
		 content = content + trail;
	 }
 }
%>
<%= content %>