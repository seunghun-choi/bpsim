<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>    
<!DOCTYPE html>
<html>
<head>
    <style>
	    .url {
	        max-width: 100%; /* 셀 너비에 맞추기 위해 100%로 설정 */
	        white-space: nowrap;  /* 텍스트가 한 줄로만 표시되도록 설정 */
	        overflow: hidden;  /* 넘치는 부분을 숨김 */
	        text-overflow: ellipsis;  /* 넘치는 부분을 '...'로 표시 */
	        text-align: left;
	    }
        
	    table {
	        table-layout: fixed; /* 테이블 셀 너비를 고정 */
	        width: 100%; /* 테이블의 너비를 100%로 설정 */
	    }
	    
	    td {
	        word-wrap: break-word; /* 셀의 내용이 너무 길 경우 자동으로 줄바꿈 */
	        overflow: hidden; /* 넘치는 내용을 숨김 */
	        text-overflow: ellipsis; /* 넘치는 부분을 '...'로 표시 */
	    }
    </style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그목록</title>
    <link rel="stylesheet" href="/css/basic.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">
    
    // 페이지가 로드되었을 때 키보드 이벤트를 감지
    $(document).on("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 동작 방지
            fn_submit();
        }
    });
    
    $(document).ready(function(){
    	//게시글 수
    	$("#listCnt").change(function(){
    		fn_submit();
    	});
    	
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
//                 searchForm.submit();
                fn_submit();
            }
        });
    });
    
    function fn_reset(){
        // 폼의 모든 입력 요소 초기화
        $('#searchForm')[0].reset();  // 폼 리셋
        
        // select2 요소 초기화
        $('.select2').val(null).trigger('change');  // select2 초기화
        
        $('select').val('');  // 일반 select 요소 초기화
        
        // input값 초기화
        $('input[type="hidden"], input[type="search"], input[type="date"]').val('');  // 검색어 초기화

        // 폼을 초기화한 후 submit 호출
        fn_submit();
        
    }
    
    function fn_excelDown(){
    	
//         // 'page on' 클래스가 적용된 페이지 번호 가져오기
//         var cPage = $('.page.on').find('.page_link').contents().filter(function() {
//         	return this.nodeType === 3; // 텍스트 노드만 선택
//     	}).text().trim(); // 텍스트만 가져옴
    	
//         if (!cPage) {
//             cPage = 1;  // 페이지 번호가 없는 경우 기본값으로 1 설정
//         }
    	
//      	// 기존에 excelPage input이 있으면 제거
//         $('#excelPage').remove();
    	
// 		var pageInput = $("<input>")
// 		    .attr("type", "hidden")
// 		    .attr("name", "excelPage")
// 		    .attr("id", "excelPage")
// 		    .attr("value", cPage);
// 		$('#searchForm').append(pageInput);

        var startDate = $('#startDate').val(); // 시작 날짜
        var endDate = $('#endDate').val(); // 종료 날짜
        
	    // 시작 날짜와 종료 날짜 차이가 3개월을 초과하는지 확인
	    var start = new Date(startDate);
	    var end = new Date(endDate);        
        
	    // 3개월(90일)을 밀리초 단위로 계산
	    var maxDateDiff = 90 * 24 * 60 * 60 * 1000; // 90일을 밀리초로 변환
	    var dateDiff = end - start; // 종료 날짜와 시작 날짜의 차이
	    
	    if (dateDiff > maxDateDiff) {
	        alert("시작 날짜와 종료 날짜의 차이는 3개월을 초과할 수 없습니다.");
	        return false;
	    }	    
        
        // 시작 날짜 없을 때
        if (!startDate) {
            alert("로그 엑셀 다운로드는 날짜를 지정하셔야 하실 수 있습니다.");
            return false;
        }
        // 끝 날짜 없을 때
        if (!endDate) {
            alert("로그 엑셀 다운로드는 날짜를 지정하셔야 하실 수 있습니다.");
            return false;
        }
	    	
    	var data= $('#searchForm').serialize();
    	var url = '/log/logExcelDown.do?' + data;
    	
    	window.location.href = url;
//     	window.open(url, '_blank');
    }
    
    function validateDates() {
        var startDate = $('#startDate').val(); // 시작 날짜
        var endDate = $('#endDate').val(); // 종료 날짜
        
        // 시작 날짜가 끝 날짜보다 큰 경우
        if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
            alert("시작 날짜는 종료 날짜보다 클 수 없습니다.");
            return false;
        }

        return true;
    }    
    
    function fn_submit(){
    	
    	if (!validateDates()){
    		return false;
    	}
    	
    	$('#searchForm').submit();  // 폼 전송
    }
    
    </script>
</head>
<body>
	<div class="contentBox">
	    <div class="content">
	        <div class="cnt_title">
	            <h1>로그목록</h1>
	        </div>
	        <form id="searchForm" action="/log/logList.do" method="GET">
	        <div class="cnt_box">
	            <div class="cnt_inner">
	                <div class="tbl_option">
	                    <table class="tbl_style3">
	                        <caption>검색조건</caption>
	                        <colgroup>
	                            <col style="width:10%;">
	                            <col style="width:40%;">
	                            <col style="width:10%;">
	                            <col style="width:40%;">
	                        </colgroup>
	                        <tbody>
	                            <tr>
	                                <th scope="row">발생일시</th>
	                                <td colspan="1">
	                                    <div class="flex_start">
<!-- 	                                        <select name="" id="" style="width:120px;"> -->
<!-- 	                                            <option value="">전체</option> -->
<!-- 	                                            <option value="">입력일자</option> -->
<!-- 	                                            <option value="">수정일자</option> -->
<!-- 	                                        </select> -->
		                                    <div class="input_box type_date" style="width:32%;">
		                                        <input type="date" name="startDate" id="startDate" data-placeholder="시작일 선택" aria-required="true" title="시작일 선택" value="${params.startDate}" max="9999-12-31">
		                                    </div>
		                                    <div>~</div>
		                                    <div class="input_box type_date" style="width:32%;">
		                                        <input type="date" name="endDate" id="endDate" data-placeholder="종료일 선택" aria-required="true" title="종료일 선택" value="${params.endDate}" max="9999-12-31">
		                                    </div>
	                                    </div>
	                                </td>
	                                <th scope="row">권한명</th>
	                                <td colspan="1">
	                                    <select name="authCd" id="authCd" class="selectbox select2" style="width:32%;">
	                                    	<option value="" ${params.authCd == '' ? 'selected' : ''}>전체</option>
	                                    	<c:forEach items="${authDc}" var="auth">
	                                         <option value="${auth.code}" ${params.authCd == auth.code ? 'selected' : ''} >${auth.code} - ${auth.codeNm}</option>
	                                        </c:forEach>
	                                    </select>
	                                </td>
	                            </tr>
	                            <tr>
	                                <th scope="row">이름</th>
	                                <td colspan="1">
	                                    <div class="input_box" style="width:50%;">
	                                        <input type="search" id="acsrNm" name="acsrNm" value="${params.acsrNm}">
	                                    </div>
	                                </span>
	                                </td>
	                                <th scope="row">접근URL</th>
	                                <td colspan="1">
	                                    <div class="input_box" style="width:50%;">
	                                        <input type="search" name="cntnUrlAddr" id="cntnUrlAddr" value="${params.cntnUrlAddr}">
	                                    </div>
	                                </span>
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
	                    <div class="btn_wrap">
	                    	<button type="button" class="btn" onclick="fn_reset()">초기화</button>
	                        <button type="button" class="btn btn-task-type1" onClick="fn_submit()">조회</button>
	                    </div>
	                </div>
	                <div class="cnt_toolbar">
	                    <div class="count_area">
	                        <p>검색 <span>${totalCnt}</span><span>건</span> / 전체 ${fullCnt}건</p>
	                    </div>         
	                    <div>
		                    <span class="form_select">
				                <select id="listCnt" name="listCnt" title="게시글 수를 선택하세요.">
								  <option value="10" <c:if test="${listCnt eq 10}">selected</c:if>>10개</option>
								  <option value="50" <c:if test="${listCnt eq 50}">selected</c:if>>50개</option>
								  <option value="100" <c:if test="${listCnt eq 100}">selected</c:if>>100개</option>
								</select>
		                    </span>	                        
	                        <button type="button" class="btn btn-task-type2" onClick="fn_excelDown()">엑셀다운로드</button>
	                    </div>
	                </div>
	                <div class="tbl_scrollbox">
	                    <table class="tbl_style1">
	                        <caption>로그목록</caption>
	                        <colgroup>
	                            <col style="width: 5%;">
	                            <col style="width: 5%;">
	                            <col style="width: 10%;">
	                            <col style="width: 10%;">
	                            <col style="width: 55%;">
	                            <col style="width: 15%;">
	                        </colgroup>
	                        <thead>
	                            <tr>
	                                <th>이름</th>
	                                <th>ID</th>
	                                <th>권한명</th>
	                                <th>IP주소</th>
	                                <th>접근URL</th>
	                                <th>발생일시</th>
	                            </tr>
	                        </thead>
	                        <tbody class="tbl_inner">
	                        	<c:forEach items="${logList}" var="log">
	                        		<tr>
	                        			<td>${log.acsrNm != null ? log.acsrNm : ''}</td>
	                        			<td>${log.userid}</td>
										<c:if test="${log.codeNm != null}">
										    <td>${log.authrtCd} - ${log.codeNm}</td>
										</c:if>
										<c:if test="${log.codeNm == null}">
											<td>정보 없음</td>
										</c:if> 			
<%-- 	                        			<td>${log.codeNm != null ? log.authrtCd + " - " + log.codeNm : ''}</td> --%>
	                        			<td>${log.acsrIpAddr}</td>
	                        			<td class="url">${log.cntnUrlAddr}</td>
	                        			<td><fmt:formatDate value="${log.cntnDt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                        		</tr>
	                        	</c:forEach>
	                        </tbody>
	                    </table>
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