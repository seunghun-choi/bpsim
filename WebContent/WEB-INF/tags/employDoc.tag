<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>

<jsp:doBody var="doc" scope="page" />
<%
 String doc = (String)jspContext.getAttribute("doc");
 if(trim !=null && trim.equals("true")){
	 doc = doc.trim();
 }
 doc = doc.replaceAll("|","");
 doc = doc.replaceAll("D1","이력서");
 doc = doc.replaceAll("D2","자기소개서");
 doc = doc.replaceAll("D3","추천서");
 doc = doc.replaceAll("D4","논문/초록목록");
 doc = doc.replaceAll("D5","학위증명서");
 doc = doc.replaceAll("D6","성적증명서");


 
 if(length != null && length.intValue() > 0 && doc.length() > length.intValue()){
	 doc = doc.substring(0, length.intValue());
 }
%>
<%= doc %>
