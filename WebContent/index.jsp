<%
//web.xml Index > First Output File

String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");
boolean chk = false;

if (header.indexOf("mobile") != -1) {
	chk = true;
} else {
	chk = false;
}

response.setStatus(301);

//realApplyExp
response.setHeader( "Location", request.getContextPath() + "/index.do" );
response.setHeader( "Connection", "close" );

%>