<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ tag import ="java.util.regex.*"%>
<jsp:doBody var="content" scope="page" />
<%

String content = (String)jspContext.getAttribute("content");

Pattern image_source_grabber = Pattern.compile("&lt;IMG [^<>]*&gt;"); 

Matcher captured = image_source_grabber.matcher(content);

//gallery 에디터로 입력한 이미지 파일을 찾기 위함
while(captured.find()) {
	content = captured.group(0);
	
	image_source_grabber = Pattern.compile("http://.*.jpg");
	captured = image_source_grabber.matcher(content);
	while(captured.find()) {
		content = captured.group(0);
	}
}
%>
<%= content %>