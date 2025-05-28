<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>
<%@ tag import="java.io.*"%>
<jsp:doBody var="content" scope="page" />

<%
 String content = (String)jspContext.getAttribute("content");
 if(trim !=null && trim.equals("true")){
	 content = content.trim();
 }
content = content.replaceAll(".gif",".jpg");
content = content.replaceAll(".GIF",".jpg");
content = content.replaceAll(".JPG",".jpg");
content = content.replaceAll(".JPEG",".jpg");
content = content.replaceAll(".jpeg",".jpg");
content = content.replaceAll(".PNG",".jpg");
content = content.replaceAll(".png",".jpg");

String SAVE_DIR = application.getRealPath("/file/");
File uploadFile =new File(SAVE_DIR, content);
String SAVE_DIR_THUM = application.getRealPath("/file/thumbnail/");
File uploadFileThum =new File(SAVE_DIR_THUM, content);

if(!uploadFile.exists() && !uploadFileThum.exists()){ // 해당경로에 파일이 존재하지 않는경우
	content	= "no.jpg";
}
%>
<%=content%>