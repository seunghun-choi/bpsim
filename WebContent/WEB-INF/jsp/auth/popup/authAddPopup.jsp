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
		    if (confirm("정말로 등록하시겠습니까?")) {
		        // Ajax를 이용하여 폼을 비동기식으로 제출
		        $.ajax({
		            type: "POST",
		            url: $('#searchForm').attr('action'),  // 폼의 action 속성에서 URL 가져옴
		            data: $('#searchForm').serialize(),    // 폼 데이터를 직렬화하여 보냄
		            success: function(response) {
		                // 서버 응답이 성공적일 때 실행되는 부분
		                alert("회원등록이 완료되었습니다.");
		                window.close();  // 창 닫기
		                window.opener.location.reload();  // 부모 창 새로 고침
		            },
		            error: function(error) {
		                // 서버 응답이 실패했을 때 실행되는 부분
		                alert("알 수 없는 오류로 등록에 실패했습니다. 다시 시도해 주세요.");
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
	        <h1>회원 추가</h1> 
	    </div>
	    <form id="searchForm" action="/auth/insertAuth.do" method="POST">
	    <input type="hidden" id="userid" name="userid" value="${userid}" />
	    <div class="cnt_box">
	        <div class="cnt_inner">
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
                            	<th style="text-align:center;">아이디</th>
                            	<td style="text-align:center;">${userid}</td>
                                <th style="text-align:center;">부여권한</th>
                                <td colspan="2" style="align-items:center;">
                                    <select name="authCd" id="authCd" class="selectbox select2" style="width:80%;">
                                    	<c:forEach items="${authDc}" var="auth">
                                         <option value="${auth.code}">${auth.code} - ${auth.codeNm}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                            	<th style="text-align:center;">권한날짜</th>
	                            <td colspan="4">
	                                <div class="flex_start" style="display: flex; justify-content: center; align-items: center;">
	                                    <div class="input_box type_date" style="width:40%;">
	                                        <input type="date" name="authrtBgngTm" id="authrtBgngTm" data-placeholder="권한 시작일" aria-required="true" title="권한 시작일" max="9999-12-31">
	                                    </div>
	                                    <div style="margin: 0 10px;">~</div>
	                                    <div class="input_box type_date" style="width:40%;">
	                                        <input type="date" name="authrtEndYmd" id="authrtEndYmd" data-placeholder="권한 종료일" aria-required="true" title="권한 종료일" max="9999-12-31">
	                                    </div>
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