<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>

<jsp:doBody var="title" scope="page" />
<%
 String title = (String)jspContext.getAttribute("title");
 if(trim !=null && trim.equals("true")){
	 title = title.trim();
 }
 
 title = title.replaceAll("&lt;","<");
 title = title.replaceAll("&gt;",">");
 title = title.replaceAll("&amp;lt;","&lt;");
 title = title.replaceAll("&amp;gt;","&gt");
 title = title.replaceAll("&#40;","(");
 title = title.replaceAll("&#41;",")");
 title = title.replaceAll("&middot;","·");
 title = title.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\""); 
 title = title.replaceAll("&#39;","'");
 title = title.replaceAll("&#34;","\"");
 title = title.replaceAll("&#61;","=");

 if(length != null && length.intValue() > 0 && title.length() > length.intValue()){
	 title = title.substring(0, length.intValue());
	 if(trail != null){
		 title = title + trail;
	 }
 }
%>
<%= title %>