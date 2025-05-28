<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<head>
    <title>바이오분야별 전문인력 권한목록</title>
    <script type="text/javascript">
        $(document).ready(function() {
            // 페이지네이션 클릭 시 폼 전송
            $(".page_list .page_link, .button_first, .button_previous, .button_next, .button_last").on("click", function(e) {
                e.preventDefault();
                var pageUrl = $(this).attr("onclick");  // onclick에서 URL 추출
                var pageMatch = pageUrl.match(/cPage=(\d+)/);  // 정규식으로 cPage 값 추출
                if (pageMatch && pageMatch[1]) {
                    var page = pageMatch[1];  // 추출된 페이지 번호
                    
                    // 폼에 숨겨진 필드로 페이지 번호 추가
                    var searchForm = $("#searchForm");
                    var pageInput = $("<input>")
                        .attr("type", "hidden")
                        .attr("name", "cPage")
                        .attr("value", page);
                    
                    searchForm.append(pageInput);
                    searchForm.submit();  // 폼 전송
                }
            });        	

        });
        
        function showAuthDetail(authrtCd){
        	window.location.href = '/auth/authDetail.do?authrtCd=' + authrtCd;
        }
        
    </script>   

</head>
<body>
	<div class="contentBox">
		<form id="searchForm" action="/log/logList.do" method="GET">
	    <div class="content">
	        <div class="cnt_title">
	            <h1>권한목록</h1> 
	        </div>
	        <div class="cnt_box">
	            <div class="cnt_inner">
	                <div class="tbl_scrollbox">
	                    <table class="tbl_style1">
	                        <caption>권한목록</caption>
	                        <thead>
	                            <tr>
	                                <th>권한명</th>
	                                <th>권한설명</th>
	                                <th>대상 인원수</th>
	                                <th>최종 수정일</th>
	                                <th>권한상세</th>
	                            </tr>
	                        </thead>
	                        <tbody class="tbl_inner">
	                        	<c:forEach items="${authDc}" var="auth">
	                        		<tr>
	                        			<td>${auth.codeNm}</td>
	                        			<td>${auth.codeDc}</td>
	                        			<td>${auth.codeCount}</td>
	                        			<td><fmt:formatDate value="${auth.lastUpdtPnttm}" pattern="yyyy-MM-dd"/></td>
	                        			<td><button type="button" class="btn btn-task-type2" onclick="showAuthDetail('${auth.code}')">권한상세</button></td>
	                        		</tr>
	                        	</c:forEach>
	                        </tbody>
	                    </table>
	                </div>
	            </div>
	        </div>
	    </div>
	    </form>
	</div>
</body>