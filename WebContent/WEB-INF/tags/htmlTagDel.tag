<%@tag import="bpsim.framework.util.ReqUtils"%>
<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "bytes" type="java.lang.Integer"%>
<%@ attribute name = "trim" %>
<%@ attribute name = "trail" %>

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
content = content.replaceAll("&nbsp;"," ");
content = content.replace("/^ /gi","");

if(content.indexOf("<!--[if") > -1){
	content = content.substring(0,content.indexOf("<!--[if"));
}
content = content.replaceAll("<(\\/?)[a-zA-Z0-9ㄱ-ㅎ가-힣\\s\";=:,#-]*\\>", "");
content = content.replaceAll("<(/)?([a-zA-Z0-9:]*)(\\s[a-zA-Z]*)?(\\s[a-zA-Zㄱ-ㅎ가-힣0-9#\"]*=[^>]*)?(\\s)*(/)?>", "");

 if(length != null && length.intValue() > 0 && content.length() > length.intValue()){
	 content = content.substring(0, length.intValue());
	 if(trail != null){
		 content = content + trail;
	 }
 }
 
 if(bytes != null){
	 content = ReqUtils.substringByBytes(content, -1, bytes);
 }
%>
<%= content %>