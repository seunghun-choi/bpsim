<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="/css/basic.css">
<title>그룹 이동</title>
	<script type="text/javascript">
		function fn_submit() {
		    // 사용자에게 확인 메시지 표시
		    if (confirm("정말로 수정하시겠습니까?")) {
		        // Ajax를 이용하여 폼을 비동기식으로 제출
		        $.ajax({
		            type: "POST",
		            url: $('#searchForm').attr('action'),  // 폼의 action 속성에서 URL 가져옴
		            data: $('#searchForm').serialize(),    // 폼 데이터를 직렬화하여 보냄
		            success: function(response) {
		                // 서버 응답이 성공적일 때 실행되는 부분
		                alert("수정이 완료되었습니다.");
		                window.close();  // 창 닫기
		                window.opener.location.reload();  // 부모 창 새로 고침
		            },
		            error: function(error) {
		                // 서버 응답이 실패했을 때 실행되는 부분
		                alert("알 수 없는 오류로 수정에 실패했습니다. 다시 시도해 주세요.");
		            }
		        });
		    }
		}	

	</script>
	
</head>
<body>
<div class="contentBox" style="padding:20px;">
	<div class="content">
	    <div class="cnt_title">
	        <h1>권한만료일 변경 회원리스트</h1> 
	    </div>
	    <form id="searchForm" action="/auth/updateAuthDate.do" method="POST">
	    <div class="cnt_box">
	        <div class="cnt_inner">
	            <div class="tbl_scrollbox">
	                <table class="tbl_style1">
	                    <caption>회원리스트</caption>
	                    <colgroup>
                            <col style="width:10%;">
                            <col style="width:40%;">
                            <col style="width:40%;">	                                        
	                    </colgroup>
	                    <thead>
	                        <tr>
	                        	<th>번호</th>
	                            <th>회원ID</th>
	                            <th>이름</th>
	                        </tr>
	                    </thead>
	                    <tbody class="tbl_inner">
					        <c:forEach var="user" items="${userList}" varStatus="status">
					        	<input type="hidden" name="userIds" value="${user.userid}" />
						        <tr>
					                <td>${status.count}</td>
					                <td>${user.userid}</td>
					                <td>${user.hname}</td>
					            </tr>
					        </c:forEach>
	                    </tbody>
	                </table>
                </div>
                <div>
                    <table class="tbl_style1">
                        <caption>검색조건</caption>
                        <colgroup>
                            <col style="width:20%;">
                            <col style="width:20%;">
                            <col style="width:20%;">
                            <col style="width:20%;">
                            <col style="width:20%;">
                        </colgroup>
                        <tbody class="tbl_inner">
                            <tr>
                                <th style="text-align:center;" colspan="2">수정할 권한 만료일</th>
                                <td colspan="3" style="align-items:center;">
	                                <div class="input_box type_date">
	                                    <input type="date" name="authrtEndYmd" id="authrtEndYmd" data-placeholder="권한만료일 선택" aria-required="true" title="권한만료일 선택" max="9999-12-31">
	                                </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
	            </div>
	            <div class="tbl_option">
	                <div class="btn_wrap">
	                    <button type="button" class="btn" onclick="window.close()">취소</button>
	                    <button type="button" class="btn btn-task-type1" onclick="fn_submit()">저장</button>
	                </div>
	            </div>	            
	        </div>
	    </div>
	    </form>
	</div>
</div>

</body>
</html>