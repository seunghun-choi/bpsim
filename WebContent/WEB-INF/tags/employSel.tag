<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>

<jsp:doBody var="sel" scope="page" />

<%
 String sel = (String)jspContext.getAttribute("sel");
 if(trim !=null && trim.equals("true")){
	 sel = sel.trim();
 }
 sel = sel.replaceAll("|","");
 sel = sel.replaceAll("S1","서류전형");
 sel = sel.replaceAll("S2","필기전형(직무적성검사)");
 sel = sel.replaceAll("S3","면접전형");
 sel = sel.replaceAll("S4","신체검사");



 
 if(length != null && length.intValue() > 0 && sel.length() > length.intValue()){
	 sel = sel.substring(0, length.intValue());
 }
%>
<%= sel %>