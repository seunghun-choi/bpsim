<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>

<jsp:doBody var="wel" scope="page" />

<%
 String wel = (String)jspContext.getAttribute("wel");
 if(trim !=null && trim.equals("true")){
	 wel = wel.trim();
 }
 wel = wel.replaceAll("|","");
 wel = wel.replaceAll("W1","국민4대보험");
 wel = wel.replaceAll("W2","소톡옵션(인센티브)");
 wel = wel.replaceAll("W3","주5일근무");
 wel = wel.replaceAll("W4","격주휴무");
 wel = wel.replaceAll("W5","숙소지원");
 wel = wel.replaceAll("W6","보건휴가");



 
 if(length != null && length.intValue() > 0 && wel.length() > length.intValue()){
	 wel = wel.substring(0, length.intValue());
 }
%>
<%= wel %>