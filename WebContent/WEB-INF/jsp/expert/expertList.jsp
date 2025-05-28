<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<style>
    .remaining-7days {
        background-color: #F08080 !important;
    }
    
</style>

    <title>바이오분야별 전문인력 목록화면</title>
    <script type="text/javascript">
    
    
    // 페이지가 로드되었을 때 키보드 이벤트를 감지
    $(document).on("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 동작 방지
            fn_submit();
        }
    });
    
    $(document).ready(function() {
    	//게시글 수
    	$("#listCnt").change(function(){
    		fn_submit();
    	});
    	
        $(".tbl_inner tr").each(function(index, element) {
            // data-lastIdntyYmd 속성에서 날짜를 가져옴
            var lastIdntyYmd = $(element).find('td[data-lastidntyymd]').data('lastidntyymd');

            if (lastIdntyYmd) {
                // 마지막 확인 날짜를 Date 객체로 변환
                var lastDate = new Date(lastIdntyYmd);

                // 현재 날짜를 가져옴
                var sysDate = new Date();

                // 마지막 확인 날짜로부터 6개월이 지났는지 확인 (6개월 = 6 * 30일)
                var sixMonthsAgo = new Date();
                sixMonthsAgo.setMonth(sysDate.getMonth() - 6);

                // 마지막 확인 날짜가 6개월 이전인지 확인
                if (lastDate < sixMonthsAgo) {
                    $(element).find('td').addClass('remaining-7days');
                    console.log("6개월 이상 확인하지 않음: ", lastDate);
                }
            }
        });
    	
    });
        
    function fn_excelDown(){
    	
    	$('#largeCategoryNm1').val($("#largeCategory1 option:selected").text());
    	$('#mediumCategoryNm1').val($("#mediumCategory1 option:selected").text());
    	$('#smallCategoryNm1').val($("#smallCategory1 option:selected").text());
    	$('#largeCategoryNm2').val($("#largeCategory2 option:selected").text());
    	$('#mediumCategoryNm2').val($("#mediumCategory2 option:selected").text());
    	$('#smallCategoryNm2').val($("#smallCategory2 option:selected").text());
    	
    	$('#overDay').val($("#exceloverDay").val());
    	
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

    	if (!validateDates()){
    		return false;
    	}    	
    	
    	var data= $('#searchForm').serialize();
    	var url = '/expert/expertExcelDown.do?' + data;
    	
    	window.location.href = url;
    	
        // 새 창에서 엑셀 다운로드
//         window.open(url, '_blank');
//         window.open(url, '_blank', 'width=50,height=50,scrollbars=yes,resizable=yes');
    }
        
    function fn_reset(){
        // 폼의 모든 입력 요소 초기화
        $('#searchForm')[0].reset();  // 폼 리셋
        
        // select2 요소 초기화
        $('.select2').val(null).trigger('change');  // select2 초기화
        
     	// 일반 select 요소 값 초기화
        $('select').val('');  // 일반 select 요소 초기화
        
        // input값 초기화
        $('input[type="hidden"], input[type="search"], input[type="date"]').val('');  // 검색어 초기화

        // 폼을 초기화한 후 submit 호출
        fn_submit();
    }
    
    function fn_overday(){
    	$('#overDay').val("over");
    	
    	fn_submit();	
    }
    
    function fn_submit(){

    	$('#largeCategoryNm1').val($("#largeCategory1 option:selected").text());
    	$('#mediumCategoryNm1').val($("#mediumCategory1 option:selected").text());
    	$('#smallCategoryNm1').val($("#smallCategory1 option:selected").text());
    	$('#largeCategoryNm2').val($("#largeCategory2 option:selected").text());
    	$('#mediumCategoryNm2').val($("#mediumCategory2 option:selected").text());
    	$('#smallCategoryNm2').val($("#smallCategory2 option:selected").text());
    	
    	if (!validateDates()){
    		return false;
    	}
    	$('#searchForm').submit(); // 폼 전송  
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

       $(document).ready(function() {
    	   
    	   var paramslargeCategory1 = $('#largeCategory1').val();  
    	   var paramslargeCategoryNm1 = $('#largeCategoryNm1').val();  
    	   var paramsmediumCategory1 = $('#mediumCategory1').val();
    	   var paramsmediumCategoryNm1 = $('#mediumCategoryNm1').val();
    	   var paramssmallCategory1 = $('#smallCategory1').val();  
    	   var paramssmallCategoryNm1 = $('#smallCategoryNm1').val();  

    	   var paramslargeCategory2 = $('#largeCategory2').val();  
    	   var paramslargeCategoryNm2 = $('#largeCategoryNm2').val();  
    	   var paramsmediumCategory2 = $('#mediumCategory2').val();
    	   var paramsmediumCategoryNm2 = $('#mediumCategoryNm2').val();
    	   var paramssmallCategory2 = $('#smallCategory2').val();  
    	   var paramssmallCategoryNm2 = $('#smallCategoryNm2').val();

    	   
    	   // 검색조건 중복 선택 안되게 막는 거인데 주석처리함.
//         	var $searchSelects = $(".search-select");
        
//             function updateOptions() {
//                 // 현재 선택된 옵션 값 수집
//                 var selectedValues = $searchSelects.map(function() {
//                     return $(this).val();
//                 }).get();

//                 // 각 드롭다운에서 중복된 옵션 비활성화
//                 $searchSelects.each(function() {
//                     var currentSelect = $(this);
//                     var currentValue = currentSelect.val();
//                     currentSelect.find("option").each(function() {
//                         var $option = $(this);
//                         // 다른 드롭다운에서 선택된 값이라면 비활성화
//                         if (selectedValues.includes($option.val()) && $option.val() !== currentValue) {
//                             $option.prop("disabled", true);
//                         } else {
//                             $option.prop("disabled", false);
//                         }
//                     });
//                 });
//             }

            // 각 드롭다운에 change 이벤트를 등록하여 값 변경 시 updateOptions 호출
//             $searchSelects.on("change", updateOptions);

            // 처음 로딩 시에도 updateOptions 호출하여 초기 상태 처리
//             updateOptions();
        	
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
            
            $('.select2').select2({});
            // Select2 초기화: 플레이스홀더 설정
            $('#largeCategory1').select2({
                placeholder: "신바이오 대분류 선택", // 플레이스홀더 텍스트
                allowClear: true, // 선택 해제 버튼 표시
                minimumResultsForSearch: Infinity
            });
            $('#mediumCategory1').select2({
                placeholder: "신바이오 중분류 선택",
                allowClear: true,
                minimumResultsForSearch: Infinity,
                language: {
                    noResults: function() {
                        return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
                    }
                }
            });
            $('#smallCategory1').select2({
                placeholder: "신바이오 소분류 선택",
                allowClear: true,
                minimumResultsForSearch: Infinity,
                language: {
                    noResults: function() {
                        return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
                    }
                }
            });
            // Select2 초기화: 플레이스홀더 설정
            $('#largeCategory2').select2({
                placeholder: "정책 대분류 선택", // 플레이스홀더 텍스트
                allowClear: true, // 선택 해제 버튼 표시
                minimumResultsForSearch: Infinity
            });
            $('#mediumCategory2').select2({
                placeholder: "정책 중분류 선택",
                allowClear: true,
                minimumResultsForSearch: Infinity,
                language: {
                    noResults: function() {
                        return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
                    }
                }
            });
            $('#smallCategory2').select2({
                placeholder: "정책 소분류 선택",
                allowClear: true,
                minimumResultsForSearch: Infinity,
                language: {
                    noResults: function() {
                        return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
                    }
                }
            });
           // 신바이오-대분류 선택시 중분류 가져오기
           $('#largeCategory1').on('change', function() {
           	var largeCategory1 = $(this).val();  // 선택된 값
           	var mediumOptions1 = '<option value="">신바이오 대분류</option>';
           	if (largeCategory1) {  // 선택된 값이 있을 때만 요청
                $.ajax({
                    url: '/expert/getNewBioMediumCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
                    method: 'GET',
                    data: { code : largeCategory1 },  // 선택된 카테고리 값을 전송
                    dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
                    success: function(data) {
                        // 서버에서 데이터를 성공적으로 받았을 때 실행
//                         $('#synbio2').html(''); // 결과를 보여줄 div 초기화
						mediumOptions1 += '<option value="all">전체</option>';
                        $.each(data.newBioMediumCode, function(index, item) {
                            mediumOptions1 += '<option value="' + item.code_id + '">' + item.code_nm + '</option>';
                        });
                        // 중분류 셀렉트 박스에 새로 추가된 옵션 적용
                        $('#mediumCategory1').html(mediumOptions1).trigger('change');
                        $('#smallCategory1').val(null).trigger('change');
                        
                    },
                    error: function(xhr, status, error) {
                        // 에러가 발생했을 때 실행
                        console.error('AJAX 에러 발생: ' + error);
                    }
                });
           	}
           });
           // 신바이오-중분류 선택시 소분류 가져오기
           $('#mediumCategory1').on('change', function() {
           	var mediumOptions1 = $(this).val();  // 선택된 값
           	var smallOptions1 = '<option value="">신바이오 중분류</option>';
           	if (mediumOptions1) {  // 선택된 값이 있을 때만 요청
                $.ajax({
                    url: '/expert/getNewBioSmallCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
                    method: 'GET',
                    data: { code : mediumOptions1 },  // 선택된 카테고리 값을 전송
                    dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
                    success: function(data) {
                        // 서버에서 데이터를 성공적으로 받았을 때 실행
//                         $('#synbio3').html(''); // 결과를 보여줄 div 초기화
						smallOptions1 += '<option value="all">전체</option>';
                        $.each(data.newBioSmallCode, function(index, item) {
                            smallOptions1 += '<option value="' + item.code_id + '">' + item.code_nm + '</option>';
                        });
                        // 소분류 셀렉트 박스에 새로 추가된 옵션 적용
                        $('#smallCategory1').html(smallOptions1).trigger('change');
                    },
                    error: function(xhr, status, error) {
                        // 에러가 발생했을 때 실행
                        console.error('AJAX 에러 발생: ' + error);
                    }
                });
           	}
           });                

           // 대분류 선택시 중분류 가져오기
           $('#largeCategory2').on('change', function() {
           	var largeCategory2 = $(this).val();  // 선택된 값
           	var mediumOptions2 = '<option value="">정책 중분류</option>';
           	if (largeCategory2) {  // 선택된 값이 있을 때만 요청
                $.ajax({
                    url: '/expert/getPolicyMediumCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
                    method: 'GET',
                    data: { code : largeCategory2 },  // 선택된 카테고리 값을 전송
                    dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
                    success: function(data) {
                        // 서버에서 데이터를 성공적으로 받았을 때 실행
                        console.log(data.policyMediumCode);
//                         $('#policy2').html(''); // 결과를 보여줄 div 초기화
						mediumOptions2 += '<option value="all">전체</option>';
                        $.each(data.policyMediumCode, function(index, item) {
                            mediumOptions2 += '<option value="' + item.codeId + '">' + item.codeIdNm + '</option>';
                        });
                        // 중분류 셀렉트 박스에 새로 추가된 옵션 적용
                        $('#mediumCategory2').html(mediumOptions2).trigger('change');
                        $('#smallCategory2').val(null).trigger('change');
                    },
                    error: function(xhr, status, error) {
                        // 에러가 발생했을 때 실행
                        console.error('AJAX 에러 발생: ' + error);
                    }
                });
           	}
           });
            
           // 중분류 선택시 소분류 가져오기
           $('#mediumCategory2').on('change', function() {
           	var mediumOptions2 = $(this).val();  // 선택된 값
           	var smallOptions2 = '<option value="">정책 소분류</option>';
           	if (mediumOptions2) {  // 선택된 값이 있을 때만 요청
                $.ajax({
                    url: '/expert/getPolicySmallCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
                    method: 'GET',
                    data: { code : mediumOptions2 },  // 선택된 카테고리 값을 전송
                    dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
                    success: function(data) {
                        // 서버에서 데이터를 성공적으로 받았을 때 실행
//                         $('#policy3').html(''); // 결과를 보여줄 div 초기화
						smallOptions2 += '<option value="all">전체</option>';
                        $.each(data.policySmallCode, function(index, item) {
                            smallOptions2 += '<option value="' + item.code + '">' + item.codeNm + '</option>';
                        });
                        // 소분류 셀렉트 박스에 새로 추가된 옵션 적용
                        $('#smallCategory2').html(smallOptions2).trigger('change');	                        
                    },
                    error: function(xhr, status, error) {
                        // 에러가 발생했을 때 실행
                        console.error('AJAX 에러 발생: ' + error);
                    }
                });
           	}
           });                  

       });
       
       function exprtAdd(){
    	   location.href = "/expert/expertAdd.do"
       }
    </script>
</head>
<body>
<div class="contentBox">
	<div class="content">
	    <div class="cnt_title">
	        <h1>전체회원 리스트</h1> 
	    </div>
	    <form id="searchForm" action="/expert/expertList.do" method="GET">
		    <input type="hidden" id="largeCategoryNm1" name="largeCategoryNm1" value="${params.largeCategoryNm1}" />
		    <input type="hidden" id="mediumCategoryNm1" name="mediumCategoryNm1" value="${params.mediumCategoryNm1}" />
		    <input type="hidden" id="smallCategoryNm1" name="smallCategoryNm1" value="${params.smallCategoryNm1}" />
		    <input type="hidden" id="largeCategoryNm2" name="largeCategoryNm2" value="${params.largeCategoryNm2}" />
		    <input type="hidden" id="mediumCategoryNm2" name="mediumCategoryNm2" value="${params.mediumCategoryNm2}" />
		    <input type="hidden" id="smallCategoryNm2" name="smallCategoryNm2" value="${params.smallCategoryNm2}" />
		    <input type="hidden" id="overDay" name="overDay" value="${params.overDay }" />
		    <input type="hidden" id="exceloverDay" name="exceloverDay" value="${params.overDay}" />
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
	                            <th scope="row">조회기간</th>
	                            <td colspan="3">
	                                <div class="flex_start">
	                                    <select name="dateSearch" id="dateSearch" style="width:8%;">
	                                        <option value="frstRegDt" ${params.dateSearch == 'frstRegDt' ? 'selected' : ''}>입력일자</option>
	                                        <option value="lastMdfcnDt" ${params.dateSearch == 'lastMdfcnDt' ? 'selected' : ''}>수정일자</option>
	                                    </select>
	                                    <div class="input_box type_date" style="width:16%;">
	                                        <input type="date" name="startDate" id="startDate" data-placeholder="시작일 선택" aria-required="true" title="시작일 선택" value="${params.startDate}" max="9999-12-31">
	                                    </div>
	                                    <div>~</div>
	                                    <div class="input_box type_date" style="width:16%;">
	                                        <input type="date" name="endDate" id="endDate" data-placeholder="종료일 선택" aria-required="true" title="종료일 선택" value="${params.endDate}" max="9999-12-31">
	                                    </div>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">검색분류</th>
	                            <td colspan="3">
	                                <ul>
	                                    <li>
	                                        <div class="flex_start">
	                                            <select name="search1" id="search1" class="search-select" style="width:8%;">
	                                                <option value="all" ${params.search1 == 'all' ? 'selected' : ''} >전체</option>
	                                                <option value="exprtNm" ${params.search1 == 'exprtNm' ? 'selected' : ''}>성명</option>
	                                                <option value="brthDt" ${params.search1 == 'brthDt' ? 'selected' : ''}>생년</option>
	                                                <option value="ogdpNm" ${params.search1 == 'ogdpNm' ? 'selected' : ''}>소속</option>
	                                                <option value="majorNm" ${params.search1 == 'majorNm' ? 'selected' : ''}>전공</option>
	                                                <option value="jbpsNm" ${params.search1 == 'jbpsNm' ? 'selected' : ''}>직위</option>
	                                                <option value="mblTelno" ${params.search1 == 'mblTelno' ? 'selected' : ''}>번호</option>
	                                                <option value="emlAddr" ${params.search1 == 'emlAddr' ? 'selected' : ''}>이메일</option>
	                                                <option value="kywdNm" ${params.search1 == 'kywdNm' ? 'selected' : ''}>키워드</option>
	                                                <option value="srcNm" ${params.search1 == 'srcNm' ? 'selected' : ''}>출처</option>
	                                                <option value="rmrkCn" ${params.search1 == 'rmrkCn' ? 'selected' : ''}>비고</option>
	                                            </select>
	                                            <div class="input_box" style="width:300px;">
	                                                <input type="search" name="searchInput1" id="searchInput1" value="${params.searchInput1}"/>
	                                            </div>
<!-- 	                                        </div> -->
<!-- 	                                    </li> -->
<!-- 	                                    <li> -->
<!-- 	                                        <div class="flex_start"> -->
	                                            <select name="search2" id="search2" class="search-select" style="width:8%;">
	                                                <option value="all" ${params.search2 == 'all' ? 'selected' : ''} >전체</option>
	                                                <option value="exprtNm" ${params.search2 == 'exprtNm' ? 'selected' : ''}>성명</option>
	                                                <option value="brthDt" ${params.search2 == 'brthDt' ? 'selected' : ''}>생년</option>
	                                                <option value="ogdpNm" ${params.search2 == 'ogdpNm' ? 'selected' : ''}>소속</option>
	                                                <option value="majorNm" ${params.search2 == 'majorNm' ? 'selected' : ''}>전공</option>
	                                                <option value="jbpsNm" ${params.search2 == 'jbpsNm' ? 'selected' : ''}>직위</option>
	                                                <option value="mblTelno" ${params.search2 == 'mblTelno' ? 'selected' : ''}>번호</option>
	                                                <option value="emlAddr" ${params.search2 == 'emlAddr' ? 'selected' : ''}>이메일</option>
	                                                <option value="kywdNm" ${params.search2 == 'kywdNm' ? 'selected' : ''}>키워드</option>
	                                                <option value="srcNm" ${params.search2 == 'srcNm' ? 'selected' : ''}>출처</option>
	                                                <option value="rmrkCn" ${params.search2 == 'rmrkCn' ? 'selected' : ''}>비고</option>
	                                            </select>
	                                            <div class="input_box" style="width:300px;">
	                                                <input type="search" name="searchInput2" id="searchInput2" value="${params.searchInput2}" />
	                                            </div>
<!-- 	                                        </div> -->
<!-- 	                                    </li> -->
<!-- 	                                    <li> -->
<!-- 	                                        <div class="flex_start"> -->
	                                            <select name="search3" id="search3" class="search-select" style="width:8%;">
	                                                <option value="all" ${params.search3 == 'all' ? 'selected' : ''} >전체</option>
	                                                <option value="exprtNm" ${params.search3 == 'exprtNm' ? 'selected' : ''}>성명</option>
	                                                <option value="brthDt" ${params.search3 == 'brthDt' ? 'selected' : ''}>생년</option>
	                                                <option value="ogdpNm" ${params.search3 == 'ogdpNm' ? 'selected' : ''}>소속</option>
	                                                <option value="majorNm" ${params.search3 == 'majorNm' ? 'selected' : ''}>전공</option>
	                                                <option value="jbpsNm" ${params.search3 == 'jbpsNm' ? 'selected' : ''}>직위</option>
	                                                <option value="mblTelno" ${params.search3 == 'mblTelno' ? 'selected' : ''}>번호</option>
	                                                <option value="emlAddr" ${params.search3 == 'emlAddr' ? 'selected' : ''}>이메일</option>
	                                                <option value="kywdNm" ${params.search3 == 'kywdNm' ? 'selected' : ''}>키워드</option>
	                                                <option value="srcNm" ${params.search3 == 'srcNm' ? 'selected' : ''}>출처</option>
	                                                <option value="rmrkCn" ${params.search3 == 'rmrkCn' ? 'selected' : ''}>비고</option>
	                                            </select>
	                                            <div class="input_box" style="width:300px;">
	                                                <input type="search" name="searchInput3" id="searchInput3" value="${params.searchInput3}" />
	                                            </div>
	                                        </div>
	                                    </li>
	                                </ul>        
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">신바이오 분류체계</th>
	                            <td colspan="1">
	                                <ul>
	                                    <li>
	                                        <div class="flex_start">
	                                        	<div></div>
	                                            <select id="largeCategory1" name="largeCategory1" class="select2" style="width:30%;">
<%-- 	                                            	<option value="${params.largeCategory1}">${params.largeCategoryNm1}</option> --%>
	                                                <option value=""></option>
	                                                <option value="all" <c:if test="${params.largeCategory1 eq 'all'}">selected</c:if>>전체</option>
		                                            <c:forEach items="${bioLclsfCd}" var="lclsfCd" varStatus="status">
		                                            	<option value="${lclsfCd.code_id}" <c:if test="${lclsfCd.code_id eq params.largeCategory1}">selected</c:if>>${lclsfCd.code_nm}</option>
		                                            </c:forEach>
<%-- 	                                                <c:forEach items="${newBioLargeCode}" var="largeCode" varStatus="status"> --%>
<%-- 	                                                	<option value="${largeCode.code_id}">${largeCode.code_nm}</option> --%>
<%-- 	                                                </c:forEach> --%>
	                                            </select>
	                                            <select id="mediumCategory1" name="mediumCategory1" class="select2" style="width:30%;">
<%-- 	                                            	<option value="${params.mediumCategory1}">${params.mediumCategoryNm1}</option> --%>
	                                                <option value=""></option>
	                                                <option value="all" <c:if test="${params.mediumCategory1 eq 'all'}">selected</c:if>>전체</option>
			                                        <c:forEach items="${bioMclsfCd}" var="mclsfCd" varStatus="status">
			                                          	<c:if test="${params.largeCategory1 ne null && !params.largeCategory1.isEmpty() && fn:contains(mclsfCd.code_id, params.largeCategory1)}">
			                                            	<option value="${mclsfCd.code_id}" <c:if test="${mclsfCd.code_id eq params.mediumCategory1}">selected</c:if>>${mclsfCd.code_nm}</option>
			                                            </c:if>
		                                            </c:forEach>
	                                            </select>
	                                            <select id="smallCategory1" name="smallCategory1" class="select2" style="width:30%;">
<%-- 	                                            	<option value="${params.smallCategory1}">${params.smallCategoryNm1}</option> --%>
	                                                <option value=""></option>
	                                                <option value="all" <c:if test="${params.smallCategory1 eq 'all'}">selected</c:if>>전체</option>
		                                            <c:forEach items="${bioSclsfCd}" var="sclsfCd" varStatus="status">
		                                            	<c:if test="${params.mediumCategory1 ne null && !params.mediumCategory1.isEmpty() && fn:contains(sclsfCd.code_id, params.mediumCategory1)}">
			                                            	<option value="${sclsfCd.code_id}" <c:if test="${sclsfCd.code_id eq params.smallCategory1}">selected</c:if>>${sclsfCd.code_nm}</option>
			                                            </c:if>
		                                            </c:forEach>                                            
	                                            </select>
	                                        </div>
	                                    </li>
	                                </ul>
	                            </td>
	                            <th scope="row">정책 분류체계</th>
	                        	<td colspan="1">
	                        		<ul>	                                            
	                        			<li>
	                        				<div class="flex_start">
	                                            <select id="largeCategory2" name="largeCategory2" class="select2" style="width:30%;">
<%-- 	                                            	<option value="${params.largeCategory2}">${params.largeCategoryNm2}</option> --%>
	                                                <option value=""></option>
	                                                <option value="all" <c:if test="${params.largeCategory2 eq 'all'}">selected</c:if>>전체</option>
		                                   		    <c:forEach items="${plcyLclsfCd}" var="lclsfCd" varStatus="status">
		                                            	<option value="${lclsfCd.clCode}" <c:if test="${lclsfCd.clCode eq params.largeCategory2}">selected</c:if>>${lclsfCd.clCodeNm}</option>
		                                            </c:forEach>	                                                
<%-- 	                                                <c:forEach items="${policyLargeCode}" var="largeCode" varStatus="status"> --%>
<%-- 	                                                	<option value="${largeCode.clCode}">${largeCode.clCodeNm}</option> --%>
<%-- 	                                                </c:forEach> --%>
	                                            </select>
	                                            <select id="mediumCategory2" name="mediumCategory2" class="select2" style="width:30%;">
<%-- 	                                            	<option value="${params.mediumCategory2}">${params.mediumCategoryNm2}</option> --%>
	                                            	<option value=""></option>	
	                                            	<option value="all" <c:if test="${params.mediumCategory2 eq 'all'}">selected</c:if>>전체</option>
			                                        <c:forEach items="${plcyMclsfCd}" var="mclsfCd" varStatus="status">
			                                          	<c:if test="${params.largeCategory2 ne null && !params.largeCategory2.isEmpty() && fn:contains(mclsfCd.codeId, params.largeCategory2)}">
			                                            	<option value="${mclsfCd.codeId}" <c:if test="${mclsfCd.codeId eq params.mediumCategory2}">selected</c:if>>${mclsfCd.codeIdNm}</option>
			                                            </c:if>
		                                            </c:forEach>
<!-- 	                                            	<div id="policy1"></div> -->
	                                            </select>
	                                            <select id="smallCategory2" name="smallCategory2" class="select2" style="width:30%;">
<%-- 	                                                <option value="${params.smallCategory2}">${params.smallCategoryNm2}</option> --%>
	                                            	<option value=""></option>
	                                            	<option value="all" <c:if test="${params.smallCategory2 eq 'all'}">selected</c:if>>전체</option>
		                                            <c:forEach items="${plcySclsfCd}" var="sclsfCd" varStatus="status">
		                                            	<c:if test="${params.mediumCategory2 ne null && !params.mediumCategory2.isEmpty() && fn:contains(sclsfCd.codeId, params.mediumCategory2)}">
			                                            	<option value="${sclsfCd.code}" <c:if test="${sclsfCd.code eq params.smallCategory2}">selected</c:if>>${sclsfCd.codeNm}</option>
			                                            </c:if>
		                                            </c:forEach>                                            	                                             
<!-- 	                                                <div id="policy2"></div> -->
	                                            </select>
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
	                    <button type="button" class="btn btn-task-type2" onClick="fn_excelDown()">엑셀다운로드</button>
	                    <button type="button" class="btn btn-task-type2" onClick="fn_overday()">6개월 이상 미확인건 조회</button>
	                    <c:if test="${loginInfo.authrtCd == 'AUTH001' || loginInfo.authrtCd == 'AUTH002'}">
	                    	<button type="button" class="btn btn-task-type1" onclick="exprtAdd()">등록</button>
	                    </c:if>
	                </div>
	            </div>
	            <div class="tbl_scrollbox">
	                <table class="tbl_style1">
	                    <caption>회원리스트</caption>
	                    <colgroup>
	                                        
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>번호</th>
	                            <th>성명</th>
	                            <th>성별</th>
	                            <th>생년</th>
	                            <th>소속</th>
	                            <th>부서</th>
	                            <th>직위</th>
	                            <th>휴대폰번호</th>
	                            <th>직장번호</th>
	                            <th>이메일</th>
	                            <th>출처</th>
	                            <th>입력일자</th>
	                            <th>최종수정일자</th>
	                            <th>조회건수</th>
	                        </tr>
	                    </thead>
	                    <tbody class="tbl_inner">
	                       	<c:forEach items="${expertList}" var="expert" varStatus="status">
	                       		<tr>
	                       			<td data-lastidntyymd="<fmt:formatDate value='${expert.lastIdntyYmd}' pattern='yyyy-MM-dd'/>">${(status.index + 1) + ((cPage - 1) * listCnt)}</td>
<%--  	                       			<td>${status.count} </td> --%>
	                       			<td><a href="expertDetail.do?exprt_mng_no=${expert.exprtMngNo}${back_params}">${expert.exprtNm}</a></td>
	                       			<td>${expert.exprtGndr == '1' ? '남' : '여'}</td>
									<c:catch var="parseError">
									    <fmt:parseDate value="${expert.brthDt}" var="brthDt" pattern="yyyy" />
									</c:catch>
									<c:choose>
									    <c:when test="${not empty parseError}">
									        <td>-</td> <!-- 예외 발생 시 "-" 표시 -->
									    </c:when>
									    <c:otherwise>
									        <td><fmt:formatDate value="${brthDt}" pattern="yyyy"/></td>
									    </c:otherwise>
									</c:choose>
	                       			<td>${expert.ogdpNm}</td>
	                       			<c:if test="${expert.deptNm != null}">
	                       				<td>${expert.deptNm}</td>
	                       			</c:if>
	                       			<c:if test="${expert.deptNm == null}">
	                       				<td></td>
	                       			</c:if>
									<td>${expert.jbpsNm}</td>
									<td>${expert.mblTelno}</td>
									<td>${expert.coTelno}</td>
									<td>${expert.emlAddr}</td>
<%-- 									<td>${expert.asstEmlAddr}</td> --%>
									<td>${expert.srcNm}</td>
	                       			<td><fmt:formatDate value="${expert.frstRegDt}" pattern="yyyy-MM-dd"/></td>
	                       			<td><fmt:formatDate value="${expert.lastMdfcnDt}" pattern="yyyy-MM-dd"/></td>
									<td>${expert.inqCnt}</td>
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