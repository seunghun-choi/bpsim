<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "math" type="java.lang.Math"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>
<jsp:doBody var="content" scope="page" />
<%
// math.rount() : 반올림
// math.floor() : 내림
// math.ceil()  : 올림

 int content = Integer.parseInt((String)jspContext.getAttribute("content"));
 int total = 0;
 total = (int)math.floor(content/10);
 content = total;
%>
<%= content %>