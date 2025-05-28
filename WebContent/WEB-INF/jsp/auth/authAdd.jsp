<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원 추가화면</title>
    <script type="text/javascript">
    
    
    function fn_reset(){
        // 폼의 모든 입력 요소 초기화
        $('#searchForm')[0].reset();  // 폼 리셋
        
        // select2 요소 초기화
        $('.select2').val(null).trigger('change');  // select2 초기화
        
     	// 일반 select 요소 값 초기화
        $('select').val('');  // 일반 select 요소 초기화
        
        // input값 초기화
        $('input[type="hidden"], input[type="search"]').val('');  // 검색어 초기화

        // 폼을 초기화한 후 submit 호출
        $('#searchForm').submit();  // 폼 전송
    }
    
    function fn_addUser(userid){

        // 팝업 창의 크기 설정
        var popupWidth = 600;
        var popupHeight = 400;

        // 화면 크기 가져오기
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;	        
        
        // 팝업 창의 위치를 화면 중앙에 위치시키기 위한 계산
        var popupLeft = (screenWidth - popupWidth) / 2;
        var popupTop = (screenHeight - popupHeight) / 2;	    
        
        // 팝업 창 열기
        var popupUrl = '/auth/popup/authAddPopup.do?userid=' + userid;
        
        var popupOptions = "width=" + popupWidth + ",height=" + popupHeight + ",left=" + popupLeft + ",top=" + popupTop + ",scrollbars=yes";

        // 팝업 창 띄우기
        window.open(popupUrl, 'authAddPopup', popupOptions);
        
    	
    }
	    
        $(document).ready(function() {
        	
        	//게시글 수
        	$("#listCnt").change(function(){
        		$("#searchForm").submit();
        	});        	
        	
        	var $searchSelects = $(".search-select");
        	
            function updateOptions() {
                // 현재 선택된 옵션 값 수집
                var selectedValues = $searchSelects.map(function() {
                    return $(this).val();
                }).get();

                // 각 드롭다운에서 중복된 옵션 비활성화
                $searchSelects.each(function() {
                    var $options = $(this).find("option");

                    $options.each(function() {
                        var $option = $(this);
                        // 다른 드롭다운에서 선택된 값이라면 비활성화
                        if (selectedValues.includes($option.val()) && $option.val() !== $(this).val()) {
                            $option.prop("disabled", true);
                        } else {
                            $option.prop("disabled", false);
                        }
                    });
                });
            }        
            
            // 각 드롭다운에 change 이벤트를 등록하여 값 변경 시 updateOptions 호출
            $searchSelects.on("change", updateOptions);

            // 처음 로딩 시에도 updateOptions 호출하여 초기 상태 처리
            updateOptions();            
        	
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
    </script>
</head>
<body>
<div class="contentBox">
	<div class="content">
	    <div class="cnt_title">
	        <h1>BioIN 회원 리스트</h1> 
	    </div>
	    <form id="searchForm" action="/auth/authAdd.do" method="GET">
	    <input type="hidden" name="authrtCd" id="authrtCd" value="${params.authrtCd}" />
	    <div class="cnt_box">
	        <div class="cnt_inner">
	            <div class="tbl_option">
	                <table class="tbl_style3">
	                    <caption>전체회원 리스트</caption>
	                    <colgroup>
		                    <col style="width:10%;">
		                    <col style="width:40%;">
		                    <col style="width:10%;">
		                    <col style="width:40%;">
	                    </colgroup>
	                    <tbody>
	                        <tr>
	                            <th scope="row">검색분류</th>
	                            <td colspan="3">
	                                <ul>
	                                    <li>
	                                        <div class="flex_start">
	                                            <select name="search1" id="search1" class="search-select" style="width:8%;">
	                                                <option value="hname" ${params.search1 == 'hname' ? 'selected' : ''} >성명</option>
	                                                <option value="userid" ${params.search1 == 'userid' ? 'selected' : ''} >아이디</option>
	                                            </select>
	                                            <div class="input_box" style="width:52%;">
	                                                <input type="search" name="searchInput1" id="searchInput1" value="${params.searchInput1}"/>
	                                            </div>
	                                        </div>
	                                    </li>
	                                </ul>        
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <div class="btn_wrap">
	                    <button type="button" class="btn" onclick="fn_reset()">초기화</button>
	                    <button type="submit" class="btn btn-task-type1">조회</button>
	                </div>
	            </div>
	            <div class="cnt_toolbar">
	                <div class="count_area">
	                    <p>검색 <span>${totalCnt}</span><span>명</span> / 전체 ${fullCnt}명</p>
	                </div>
	                <div>
<%-- 	                    <c:if test="${loginInfo.authrtCd == 'AUTH001' || loginInfo.authrtcd == 'AUTH002'}"> --%>
<!-- 	                    	<button type="button" class="btn btn-task-type1">등록</button> -->
<%-- 	                    </c:if> --%>
	                    <span class="form_select">
			                <select id="listCnt" name="listCnt" title="게시글 수를 선택하세요.">
							  <option value="10" <c:if test="${listCnt eq 10}">selected</c:if>>10개</option>
							  <option value="30" <c:if test="${listCnt eq 30}">selected</c:if>>30개</option>
							  <option value="50" <c:if test="${listCnt eq 50}">selected</c:if>>50개</option>
							</select>
	                    </span>	                    
	                </div>
	            </div>
	            <div class="tbl_scrollbox">
	                <table class="tbl_style1">
	                    <caption>회원리스트</caption>
	                    <colgroup>
	                        <col style="width:5%;">	                    	
	                        <col style="width:10%;">	                    	
	                        <col style="width:10%;">	                    	
	                        <col style="width:10%;">	                    	
	                        <col style="width:10%;">
	                        <col style="width:10%;">	                    	                   	
	                        <col style="width:10%;">	                    	                   	
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>번호</th>
	                            <th>성명</th>
	                            <th>아이디</th>
	                            <th>기관</th>
	                            <th>이메일</th>
	                            <th>휴대폰번호</th>
	                            <th>회원추가</th>
	                        </tr>
	                    </thead>
	                    <tbody class="tbl_inner">
	                       	<c:forEach items="${memberList}" var="member" varStatus="status">
	                       		<tr>
	                       			<td>${(status.index + 1) + ((cPage - 1) * listCnt)}</td>
	                       			<td>${member.hname}</td>
	                       			<td>${member.userid}</td>
<%-- 									<c:catch var="parseError"> --%>
<%-- 									    <fmt:parseDate value="${member.brthdy}" var="brthdy" pattern="yyyyMMdd" /> --%>
<%-- 									</c:catch> --%>
									
<%-- 									<c:choose> --%>
<%-- 									    <c:when test="${not empty parseError}"> --%>
<!-- 									        <td>-</td> 예외 발생 시 "-" 표시 -->
<%-- 									    </c:when> --%>
<%-- 									    <c:otherwise> --%>
<%-- 									        <td><fmt:formatDate value="${brthdy}" pattern="yyyy-MM-dd"/></td> --%>
<%-- 									    </c:otherwise> --%>
<%-- 									</c:choose> --%>
									<td>${member.organization}</td>
									<td>${member.email}</td>
									<td>${member.hphone}</td>
									<c:if test="${loginInfo.authrtCd == 'AUTH001' || loginInfo.authrtcd == 'AUTH002'}">
										<td><button type="button" class="btn btn-task-type1" onclick="fn_addUser('${member.userid}')">회원등록</button></td>
									</c:if>
									<c:if test="${loginInfo.authrtCd != 'AUTH001' && loginInfo.authrtcd != 'AUTH002'}">
										<td>권한없음</td>
									</c:if>
									
	                       		</tr>
	                       	</c:forEach>               
	                    </tbody>
	                </table>
	            </div>
                <div class="cnt_toolbar">
                    <div>
                        <button type="button" class="btn btn-task-type2" onclick="location.href='/auth/authDetail.do?authrtCd=${params.authrtCd}'">뒤로가기</button>
                    </div>                   
                </div>	            
	        </div>
	        <!-- 페이징 -->
			<div class="pagination">
		    	<ul class="page_list">${pageNavigator}</ul>
			</div>
			<!-- //페이징 -->	
	    </div>
	    </form>
	</div>
</div>
</body>
</html>