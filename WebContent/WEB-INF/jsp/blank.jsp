<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="bpsim.module.dto.LoginInfoDTO"%>
<%
LoginInfoDTO loginInfo = (LoginInfoDTO)session.getAttribute("loginInfo");
if(loginInfo != null){
String sessionName = loginInfo.getLoginnm();
request.setAttribute("sessionName", sessionName);
}
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
</head>
<body>
</body>
</html>