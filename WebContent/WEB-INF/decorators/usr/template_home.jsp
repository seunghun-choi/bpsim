<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<%@ include file="/WEB-INF/decorators/usr/header.jsp" %>
		<sitemesh:write property="head"/>
	</head>
	<body>
		<div class="container sub">
			<%@ include file="/WEB-INF/decorators/usr/top.jsp" %>
			<div class="main_layout">
				<%@ include file="/WEB-INF/decorators/usr/left.jsp" %>
				<sitemesh:write property="body"/>
			</div>
		</div>
	</body>
</html>