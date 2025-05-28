<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<head>
<style>
    .remaining-7days {
        background-color: #F08080 !important;
    }
    
</style>

    <title>권한상세</title>
    <script type="text/javascript">
    
	 // 페이지가 로드되었을 때 키보드 이벤트를 감지
    $(document).on("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 동작 방지
            fn_submit();
        }
    });    
	 
    $(document).ready(function() {
    	
		// 전체선택 체크박스 클릭 이벤트
		$('#chkAll').click(function() {
		    $('tbody .button_checkbox').prop('checked', this.checked);
		});
		
    	//게시글 수
    	$("#listCnt").change(function(){
    		$("#searchForm").submit();
    	});
           
        $('.tbl_inner tr').each(function(index, element) {
        	
            var currentSortValue = '${params.sortValue}' || 'asc'; // 서버에서 받아온 정렬 상태

            // 현재 정렬 상태에 따라 아이콘 회전 상태 설정
            if (currentSortValue === 'asc') {
                $('.array button').addClass('rotated'); // 오름차순이면 180도 회전
            } else {
                $('.array button').removeClass('rotated'); // 내림차순이면 원래 상태
            }
        	
            var endDateStr = $(element).find("#endDate" + (index + 1)).text();
            
            // 문자열을 Date 객체로 변환
            var sysDate = new Date();
            var endDate = new Date(endDateStr);

            // 두 날짜 간의 차이 계산 (밀리초 단위)
            var timeDiff = endDate - sysDate;

            // 밀리초를 일 단위로 변환
            var diffInDays = Math.floor(timeDiff / (1000 * 60 * 60 * 24)) + 1;

            // 결과를 HTML에 표시
            $(element).find("#remainingDays" + (index + 1)).text(diffInDays);
            
            if(diffInDays <= 7){
            	$(element).find('td').addClass('remaining-7days');
            	console.log("CSS부여 : ", diffInDays, "일 남음");
            }
            
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
                searchForm.submit();  // 폼 전송
            }
        });
        
    }); 
   
    function fn_reset(){
        // 폼의 모든 입력 요소 초기화
        $('#searchForm')[0].reset();  // 폼 리셋
        
     	// 일반 select 요소 값 초기화
        $('select').val('');  // 일반 select 요소 초기화        
        
        // input값 초기화
        $('input[type="search"]').val('');  // 검색어 초기화

        // 폼을 초기화한 후 submit 호출
        $('#searchForm').submit();  // 폼 전송
    }
    
 	// 그룹이동 버튼 클릭 시 호출되는 함수
    function fn_changeGroup() {
        var selectedUsers = [];

        // 체크된 항목들의 userid와 authrtcd를 수집
        $('tbody .button_checkbox:checked').each(function(index, element) {
            var row = $(element).closest('tr');
            var userId = row.find('td[data-userid]').data('userid');  // data-userid 속성에서 userid 가져오기
            var authrtCd = row.find('td[data-authrtcd]').data('authrtcd');  // data-authrtcd 속성에서 authrtcd 가져오기
            var hname = row.find('td[data-hname]').data('hname');
            selectedUsers.push({ userId: userId, authrtCd: authrtCd, hname: hname });            
        });

        if (selectedUsers.length === 0) {
            alert('이동할 그룹의 사용자를 선택하세요.');
            return;
        }

        // 선택된 사용자 ID와 AUTHRT_CD를 파라미터로 전달하기 위한 문자열 생성
        var userIdsParam = selectedUsers.map(user => user.userId).join(',');
        var authrtCdsParam = selectedUsers.map(user => user.authrtCd).join(',');
        var hnamesParam = selectedUsers.map(user => user.hname).join(',');

        // 팝업 창의 크기 설정
        var popupWidth = 600;
        var defaultHeight = 350;
        var additionalHeight = 35;
        var popupHeight = defaultHeight + (selectedUsers.length * additionalHeight);
        

        // 화면 크기 가져오기
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;	        
        
        // 팝업 창의 위치를 화면 중앙에 위치시키기 위한 계산
        var popupLeft = (screenWidth - popupWidth) / 2;
        var popupTop = (screenHeight - popupHeight) / 2;	        
        
        // 팝업 창 열기
        var popupUrl = '/auth/popup/authMoveGroupPopup.do?userIds=' + encodeURIComponent(userIdsParam)
        		+'&authrtCds=' + encodeURIComponent(authrtCdsParam)
        		+'&hnames=' + encodeURIComponent(hnamesParam);
        
        var popupOptions = "width=" + popupWidth + ",height=" + popupHeight + ",left=" + popupLeft + ",top=" + popupTop + ",scrollbars=yes";

        // 팝업 창 띄우기
        window.open(popupUrl, 'groupMovePopup', popupOptions);
    }
 	
 	function fn_submit(){
 		$('#searchForm').submit();
 	}
 	
 	function fn_changeDay(){
        var selectedUsers = [];

        // 체크된 항목들의 userid와 authrtcd를 수집
        $('tbody .button_checkbox:checked').each(function(index, element) {
            var row = $(element).closest('tr');
            var userId = row.find('td[data-userid]').data('userid');  // data-userid 속성에서 userid 가져오기
            var hname = row.find('td[data-hname]').data('hname');
            
            selectedUsers.push({ userId: userId, hname: hname });            
        });

        if (selectedUsers.length === 0) {
            alert('권한 만료일을 변경할 사용자를 선택하세요.');
            return;
        }

        // 선택된 사용자 ID와 AUTHRT_CD를 파라미터로 전달하기 위한 문자열 생성
        var userIdsParam = selectedUsers.map(user => user.userId).join(',');
        var hnamesParam = selectedUsers.map(user => user.hname).join(',');
//         var authrtEndYmdParam = selectedUsers.map(user => user.authrtEndYmd).join(',');

        // 팝업 창의 크기 설정
        var popupWidth = 600;
        var defaultHeight = 350;
        var additionalHeight = 35;
        var popupHeight = defaultHeight + (selectedUsers.length * additionalHeight);

        // 화면 크기 가져오기
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;	        
        
        // 팝업 창의 위치를 화면 중앙에 위치시키기 위한 계산
        var popupLeft = (screenWidth - popupWidth) / 2;
        var popupTop = (screenHeight - popupHeight) / 2;	        
        
        // 팝업 창 열기
        var popupUrl = '/auth/popup/authDateChangePopup.do?userIds=' + encodeURIComponent(userIdsParam)
//         		+'&authrtEndYmd=' + encodeURIComponent(authrtEndYmdParam)
        		+'&hnames=' + encodeURIComponent(hnamesParam);
        
        var popupOptions = "width=" + popupWidth + ",height=" + popupHeight + ",left=" + popupLeft + ",top=" + popupTop + ",scrollbars=yes";

        // 팝업 창 띄우기
        window.open(popupUrl, 'DateChangePopup', popupOptions); 		
 	}

    // 삭제 버튼 클릭 시 호출되는 함수
    function fn_removeGroup() {
        var selectedUserIds = [];

        // 체크된 항목들의 userid를 수집
        $('tbody .button_checkbox:checked').each(function(index, element) {
            var row = $(element).closest('tr');
            var userId = row.find('td[data-userid]').data('userid');  // data-userid 속성에서 userid 가져오기
            selectedUserIds.push(userId);
        });

        if (selectedUserIds.length === 0) {
            alert('삭제할 사용자를 선택하세요.');
            return;
        }

        if (confirm('정말로 선택한 사용자를 삭제하시겠습니까?')) {
            $.ajax({
                url: '/auth/deleteAuth.do',  // 삭제 처리할 URL
                method: 'POST',
                data: { userIds: selectedUserIds }, // 데이터 전송
                dataType: 'json',
                success: function(data) {
                    alert('사용자가 삭제되었습니다.');
                    window.location.reload();
                },
                error: function(error) {
                    alert('알 수 없는 오류로 일부 사용자 삭제에 실패했습니다.');
                }
            });
        }
    }	    

    // 회원추가 버튼 클릭 시 호출되는 함수
    function fn_addUser() {
        // 회원 추가 로직 (예: 팝업 창을 띄우거나 추가 페이지로 이동)
        window.location.href = '/auth/authAdd.do?authrtCd='+$('#authrtCd').val(); // 회원추가 페이지로 이동
    }
    
    // 정렬 버튼 클릭 시 실행될 함수
    function fn_toggleSort() {
    	var currentSortValue = '${params.sortValue}' || 'asc';
        var sortField = 'authrtEndYmd'; // 정렬 기준 컬럼
        console.log("박현호 : "+ currentSortValue);
        var sortValue = (currentSortValue === 'asc' ? 'desc' : 'asc'); // 정렬 방향 토글

        // hidden input 값 설정
        $('#sort').val(sortField);
        $('#sortValue').val(sortValue);
        

        // 폼을 다시 제출하여 정렬된 결과를 서버로 전송
        
        fn_submit();
    }    
	</script>
   

</head>
<body>
	<div class="contentBox">
	    <div class="content">
	        <div class="cnt_title">
	            <h1>권한 상세</h1> 
	        </div>
	        <form id="searchForm" action="/auth/authDetail.do" method="GET">
		        <input type="hidden" name="authrtCd" id="authrtCd" value="${params.authrtCd}" />
				<input type="hidden" name="sort" id="sort" value="${params.sort}" />
				<input type="hidden" name="sortValue" id="sortValue" value="${params.sortValue}" />
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
				                            <th scope="row">검색분류</th>
				                            <td colspan="3">
				                                <ul>
				                                    <li>
				                                        <div class="flex_start">
				                                            <select name="search1" id="search1" class="search-select" style="width:8%;">
				                                                <option value="all" ${params.search1 == 'all' ? 'selected' : ''}>전체</option>
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
			                        <button type="button" class="btn btn-task-type1" onclick="fn_submit()">조회</button>
			                    </div>
			                </div>	                            
			                <div class="cnt_toolbar">              
				                <div class="count_area">
				                    <p>검색 <span>${totalCnt}</span><span>명</span> / 전체 ${fullCnt}명</p>
				                </div>
			                    <div>
				                    <span class="form_select">
						                <select id="listCnt" name="listCnt" title="게시글 수를 선택하세요.">
										  <option value="10" <c:if test="${listCnt eq 10}">selected</c:if>>10개</option>
										  <option value="30" <c:if test="${listCnt eq 30}">selected</c:if>>30개</option>
										  <option value="50" <c:if test="${listCnt eq 50}">selected</c:if>>50개</option>
										</select>
				                    </span>
			                        <button type="button" class="btn btn-task-type1" onclick="fn_addUser()">회원추가</button>
			                    </div>	             	                           
			                </div>
		                
			                <div class="tbl_scrollbox">
			                    <table class="tbl_style1">
			                        <caption>조회권한 상세목록</caption>
			                        <colgroup>
			                            <col style="width: 10%">
			                            <col style="width: 10%">
			                            <col style="width: 15%">
			                            <col style="width: 15%">
			                            <col style="width: 20%">
			                            <col style="width: 30%">
			                        </colgroup>
			                        <thead>
			                            <tr>
			                                <th>
			                                    <div class="button_checkbox_wrap"><input type="checkbox" id="chkAll" class="button_checkbox blind"><label for="chkAll"></label></div>
			                                </th>
			                                <th>번호</th>
			                                <th>회원ID</th>
			                                <th>사용자명</th>
			                                <th>권한 부여일</th>
			                                <th>
												<strong class="array">
												    <span>권한 만료일자</span>
												    <button type="button" onclick="fn_toggleSort()">정렬선택</button>
												</strong>
											</th>
			                            </tr>
			                        </thead>
			                        <tbody class="tbl_inner">
			                        	<c:forEach items="${authDetailList}" var="authList" varStatus="status">
			                        		<tr>
											<td>
			                                     <div class="button_checkbox_wrap"><input type="checkbox" id="chk${status.count}" class="button_checkbox blind"><label for="chk${status.count}"></label></div>
			                                </td>
			                        			<td data-authrtcd="${authList.authrtCd}">${(status.index + 1) + ((cPage - 1) * listCnt)}</td>
			                        			<td data-userid="${authList.userid}">${authList.userid}</td>
			                        			<td data-hname="${authList.hname}">${authList.hname}</td>
			                        			<td><span id="startDate${status.count}"><fmt:formatDate value="${authList.authrtBgngTm}" pattern="yyyy-MM-dd"/></span></td>
			                        			<td data-authrtendymd="<fmt:formatDate value='${authList.authrtEndYmd}' pattern='yyyy-MM-dd'/>"><span id="endDate${status.count}"><fmt:formatDate value="${authList.authrtEndYmd}" pattern="yyyy-MM-dd"/></span>(<span id="remainingDays${status.count}"></span>일)</td>
			                        		</tr>
			                        	</c:forEach>
				                    	<c:if test="${fn:length(authDetailList) eq 0 }">
				                    		<tr>
												<td colspan="6">
													등록된 사용자가 없습니다.
												</td>
								        	</tr>
										</c:if>			                        	
			                        </tbody>
			                    </table>
			                </div>
			                <div class="cnt_toolbar">
			                    <div>
			                        <button type="button" class="btn btn-task-type2" onclick="location.href='/auth/authList.do'">뒤로가기</button>
			                    </div>
			                    <div>
			                        <button type="button" class="btn btn-task-type2" onclick="fn_changeDay()">권한만료일 변경</button>
			                        <button type="button" class="btn btn-task-type2" onclick="fn_changeGroup()">그룹이동</button>
			                        <button type="button" class="btn btn-remove" onclick="fn_removeGroup()">삭제</button>
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