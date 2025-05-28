<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bpsim.module.dto.LoginInfoDTO" %>
<%
    // 세션에서 loginDTO 객체를 가져옴
    LoginInfoDTO loginInfo = (LoginInfoDTO) session.getAttribute("loginInfo");
	
    String authrtCd = "";
    String loginnm = "";
    String codeNm = "";
    if (loginInfo != null) {
        authrtCd = loginInfo.getAuthrtCd();
        loginnm = loginInfo.getLoginnm();
        codeNm = loginInfo.getCodeNm();
        
    }
    request.setAttribute("authrtCd", authrtCd);
    request.setAttribute("loginnm", loginnm);
    request.setAttribute("codeNm", codeNm);
%>
<c:set var="path" value="${requestScope['javax.servlet.forward.servlet_path']}" /> 
<div class="menu_side">
    <div class="logo">
        <a href="/expert/expertList.do">	
<!--         	<img src="/img/logo.png" alt="바이오분야별 전문인력 관리"> -->
        	<strong style="color : white; font-size : 1.8rem;">바이오분야별 전문인력 관리</strong>
        </a>
    </div>
	<div class="myInfo">
	    <div>
	        <p>
		        <strong>${loginnm}님</strong>
		        <small>${codeNm}</small>
<!-- 		        <button class="btn btn-logout" onClick="fn_logout()">로그아웃</button> -->
	        </p>
	    </div>
	</div>
<!--     <div class="myInfo"> -->
<!--         <div class="myImgBox"><img src="/img/user_img.png" alt="내프로필 이미지"></div> -->
<!--         <div> -->
<%--             <p><strong>${loginnm}님</strong></p> --%>
<%--             <p style="text-align : right;"><small>${codeNm}</small></p> --%>
<!--         </div> -->
<!--     </div> -->
    <div>
        <ul class="menu">
            <li class="menu_1depth">
                <div onclick="location.href='/expert/expertList.do'">
                    <span class="menuName">전문인력관리</span><em class="icon icon_down"></em>
                </div>
                <ul>
                    <li class="menu_2depth" onclick="location.href='/expert/expertList.do'">
                        <div <c:if test="${fn:contains(path,'/expert/expertList.do') || fn:contains(path,'/expert/expertDetail.do')}">class="act"</c:if>><span class="menuName">전문인력정보 조회</span><em class="icon icon_down"></em></div>
                    </li>
                    <c:if test="${authrtCd == 'AUTH001' || authrtCd == 'AUTH002'}">
<!-- 	                    <li class="menu_2depth"> -->
<!-- 	                        <div><span class="menuName">전문인력정보 관리</span><em class="icon icon_down"></em></div> -->
<!-- 	                    </li> -->
	                    <li class="menu_2depth" onclick="location.href='/expert/expertAdd.do'">
	                        <div <c:if test="${fn:contains(path,'/expert/expertAdd.do')}">class="act"</c:if>><span class="menuName">전문인력정보 등록</span><em class="icon icon_down"></em></div>
	                    </li>
                    </c:if>
                </ul>
            </li>        
            <c:if test="${authrtCd == 'AUTH001'}">
            <li class="menu_1depth">
                <div onclick="location.href='/auth/authList.do'">
                    <span class="menuName">권한관리</span><em class="icon icon_down"></em>
                </div>
                <ul>
                    <li class="menu_2depth" onclick="location.href='/auth/authList.do'">
                        <div <c:if test="${fn:contains(path,'/auth')}">class="act"</c:if>><span class="menuName">권한 목록</span><em class="icon icon_down"></em></div>
                    </li>
                </ul>
            </li>
            <li class="menu_1depth">
                <div onclick="location.href='/log/logList.do'">
                    <span class="menuName">로그관리</span><em class="icon icon_down"></em>
                </div>
                <ul>
                    <li class="menu_2depth" onclick="location.href='/log/logList.do'">
                        <div <c:if test="${fn:contains(path,'/log/logList.do')}">class="act"</c:if>><span class="menuName">로그 목록</span><em class="icon icon_down"></em></div>
                    </li>
                </ul>
            </li>
            </c:if>
        </ul>
    </div>
</div>