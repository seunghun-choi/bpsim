
/** ==================================================================================================================
 * 전문인력정보 엑셀 등록확인  js
 * 
 *==================================================================================================================*/



$(document).ready(function() {
	
	// 현재 URL 가져오기
	var currentUrl = window.location.href;
	
	// 체크할 경로들
	var pathsToCheck = ['expertExcelInfoConfirm.do', 'expertMod.do'];
	
	// URL에 해당 경로가 포함되어 있는지 확인
	var isPathIncluded = pathsToCheck.some(path => currentUrl.includes(path));
	if (isPathIncluded) { // 포함됨
		clsfSelect(); // 분류코드 효과 및 css 설정
		dataSettingAll();
		checkEmptyInfo(); // 전문인력정보 값 검증
		setTimeout(() => {
			checkAllEmptyData();
		}, 500);
	}
	// 엔터키의 기본 동작 막기, 행추가 오류 발생
	$(document).on('keydown', function(event) {
	    if (event.key === "Enter") {
	        event.preventDefault();
	        return false; // 기본 동작 방지
	    }
	});
	
	// 최근활동, 학력 등 관련 +, - 버튼
	// 일반 input 행 추가
    $(document).on('click', '.addRowBtn', function(e) {
        e.preventDefault();
        var table = $(this).closest('table'); // 클릭된 버튼의 테이블을 가져옴
        var tbody = table.find('tbody');
        var rowCount = table.find('tbody tr').length + 1;
        // 테이블의 첫 번째 tr (thead 또는 tbody)의 th/td 개수 확인
        var columnCount = table.find('thead th').length - 1; // 추가 버튼 열 제외
        
        // 새로운 행 생성
        let newRow = `<tr><td>` + rowCount + `</td>`;
        for (let i = 1; i < columnCount; i++) {
			var colId = table.find('thead th').eq(i).attr('id') + rowCount
			if(table.find('tbody').attr("id") == "utlzTb" && i == 1){
	            newRow += `<td><div class="input_box type_date">`;
	            newRow += `<input type="date" id="` + colId + `" data-placeholder="활용일자 선택" aria-required="true" title="활용일자 선택" value="" max="9999-12-31" onblur="checkAllEmptyData()">`;
	            newRow += `</div></td>`;
			} else {
				if (colId.includes("err")) { // 검증 input 요소
		            newRow += `<td><input type="text" class="errDsctn" value="" id="` + colId + `" readonly ></td>`;
				} else if(colId.includes("yr")) {
		            newRow += `<td><input type="number" value="" id="` + colId + `" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()"></td>`;
				} else{
		            newRow += `<td><input type="text" value="" id="` + colId + `" onblur="checkAllEmptyData()"></td>`;
				}
			}
        }
        newRow += `<td><button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button></td></tr>`;
        tbody.append(newRow);
    });

	// 코드 행 추가 (select box / 신바이오, 정책분류)
	$(document).on('click', '.addRowSelBtn', function(e) {
        e.preventDefault();
        var table = $(this).closest('table'); // 클릭된 버튼의 테이블을 가져옴
        var tbody = table.find('tbody');
        var rowCount = table.find('tbody tr').length + 1;
        // 테이블의 첫 번째 tr (thead 또는 tbody)의 th/td 개수 확인
        var columnCount = table.find('thead th').length - 1; // 추가 버튼 열 제외
		tbody.attr("id")
		var url;
		var largeOption;
		let newRow = `<tr><td>` + rowCount + `</td>`;
		if(tbody.attr("id") == "bioClsfTb") {
			url = '/expert/getNewBioLargeCode.do'						//  대분류  url
			largeOption = '<option value="" >신바이오 대분류 선택</option>'; // 대분류 ajax로 가져왔을때 첫 설정
			
            newRow += `<td><select id="bioClsf_lclsf_cd` + rowCount + `" name="" class="select2 bioClsf_lclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><select id="bioClsf_mclsf_cd` + rowCount + `" name="" class="select2 bioClsf_mclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><select id="bioClsf_sclsf_cd` + rowCount + `" name="" class="select2 bioClsf_sclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><input type="text"  class="errDsctn" id="bioClsf_err` + rowCount + `" readonly value=""></td>`;
		} else if (tbody.attr("id") == "plcyClsfTb"){
			url = '/expert/getPolicyLargeCode.do'				//정책분류 대분류  url
			largeOption = '<option value="">정책분류 대분류</option>'; // 대분류 ajax로 가져왔을때 첫 설정
			
            newRow += `<td><select id="plcyClsf_lclsf_cd` + rowCount + `" name="" class="select2 plcyClsf_lclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><select id="plcyClsf_mclsf_cd` + rowCount + `" name="" class="select2 plcyClsf_mclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><select id="plcyClsf_sclsf_cd` + rowCount + `" name="" class="select2 plcyClsf_sclsf_cd"><option value=""></option></select></td>`;
            newRow += `<td><input type="text"  class="errDsctn" id="plcyClsf_err` + rowCount + `" readonly value=""></td>`;
		}
		// 삭제 버튼 컬럼
        newRow += `<td><button class="icon btn_bg red deleteRowSelBtn"><span class="blind">삭제</span><span class="icon_x"></span></button></td></tr>`;
        tbody.append(newRow);

		clsfSelect(); // 분류코드 효과 및 css 설정
		
		// 대분류 가져오기
        $.ajax({
            url: url , 
            method: 'GET',
            data: {},  
            dataType: 'json',
            success: function(data) {
				if(tbody.attr("id") == "bioClsfTb") {// 신바이오분류체계
					$.each(data.newBioLargeCode, function(index, item) {
                    	largeOption += '<option value="' + item.code_id + '">' + item.code_nm + '</option>';
	                });
	                $('#bioClsf_lclsf_cd'+rowCount).html(largeOption).trigger('change');
				} else if (tbody.attr("id") == "plcyClsfTb"){ // 정책분류코드
					$.each(data.policyLargeCode, function(index, item) {
                    	largeOption += '<option value="' + item.clCode + '">' + item.clCodeNm + '</option>';
	                });
	                $('#plcyClsf_lclsf_cd'+rowCount).html(largeOption).trigger('change');
				}
            },
            error: function(xhr, status, error) {
                // 에러가 발생했을 때 실행
                console.error('AJAX 에러 발생: ' + error);
            }
        });

    });

    // 일반 input 행 삭제
    $(document).on('click', '.deleteRowBtn', function(e) {
        e.preventDefault();
        var row = $(this).closest('tr');  // 클릭된 버튼의 부모 행
        var table = $(this).closest('table');  // 테이블 객체
        var rowCount = table.find('tbody tr').length;  // 현재 행의 개수

        row.remove();  // 행 삭제

        // 삭제 후 번호 및 input의 id 값 재설정
        table.find('tbody tr').each(function(rowIndex) {
            // 각 행의 번호를 다시 설정
            $(this).find('td:first-child').text(rowIndex + 1);

            // 각 input 요소의 id를 thead th의 id 기반으로 재설정
            $(this).find('input').each(function(colIndex) {
                // thead th의 id 값을 가져와서 사용
                var thId = table.find('thead th').eq(colIndex+1).attr('id'); // 첫 번째는 번호이므로 제외
                if (thId) {
                    var newId = thId + (rowIndex + 1); // th의 id + 행 번호 조합
                    $(this).attr('id', newId); // 새로운 id 값을 설정
                }
            });
        });
    });
	
	// 코드 행 삭제 (select / 신바이오, 정책분류)
    $(document).on('click', '.deleteRowSelBtn', function(e) {
		e.preventDefault();	
		var rowCount = $(this).closest('table').find('tbody tr').length;   // 현재 행의 개수
		var tbodyId = $(this).closest('tbody').attr('id');
		
		$(this).closest('tr').remove(); // 행 삭제
		
		// 남은 행의 인덱스 재정렬
        reorderRows(tbodyId);
    });
	
	// 회원 리스트의 항목을 클릭했을 때
	$(document).on('click', '.experList li', function() {
        if($("#exprt_nm").val() == ""){
			alert("성명을 입력해주세요.");
			return false
		}

		// 기존 선택된 항목의 선택 해제
        $('.experList li').removeClass('selected');
        
        // 현재 클릭된 항목 선택 표시
        $(this).addClass('selected');
		
		// 하단 데이터 셋팅
		dataSettingAll();
		 
		// 선택한 회원의 ID를 가져옴
		$("#sel_exprt_mng_no").val($(this).val());
		
		var formData =$('#confirmForm').serialize();
		
     	// AJAX 요청으로 서버에서 회원 데이터를 가져와 화면을 갱신
        $.ajax({
            url: '/async/expertExcelInfoaAsyncConfirm.do',
            type: 'GET',
            data: formData,  
            success: function(result) {
                // 서버에서 받은 데이터를 바탕으로 화면의 데이터를 갱신
                $('#tempConfirmBox').html(result); 
                $('#cnt_box').addClass("async");

				// 스크롤을 해당 항목으로 조정
                scrollToChecked();
				checkEmptyInfo(); // 전문인력정보 값 검증
				setTimeout(() => {
					checkAllEmptyData();
				}, 500);
            },
            error: function() {
                alert('회원 정보를 불러오는 중 오류가 발생했습니다.');
            }
        });
    });

	////////////////// 신바이오, 정책분류 값 설정 시작
	
    // 신바이오-대분류 선택시 중분류 가져오기
	$(document).on('change', '.bioClsf_lclsf_cd', function() {
	    var bioClsf_lclsf_cd = $(this).val();  // 선택된 값
	    var cdIndex = $(this).attr("id").substring(16); // 행의 값
	    var mediumOptions = '<option value="">신바이오 중분류</option>';
	    if (bioClsf_lclsf_cd) {  // 선택된 값이 있을 때만 요청
	        $.ajax({
	            url: '/expert/getNewBioMediumCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
	            method: 'GET',
	            data: { code : bioClsf_lclsf_cd },  // 선택된 카테고리 값을 전송
	            dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
	            success: function(data) {
	                $.each(data.newBioMediumCode, function(index, item) {
	                    mediumOptions += '<option value="' + item.code_id + '">' + item.code_nm + '</option>';
	                });
	
	                // 중분류 셀렉트 박스에 새로 추가된 옵션 적용
	                $('#bioClsf_mclsf_cd'+cdIndex).html(mediumOptions).trigger('change');   
					var smallOptions = '<option value="">신바이오 중분류</option>';                     
	                $('#bioClsf_sclsf_cd'+cdIndex).html(smallOptions).trigger('change');                        
	                
					checkEmptyBioClsfEmpty();
	            },
	            error: function(xhr, status, error) {
	                // 에러가 발생했을 때 실행
	                console.error('AJAX 에러 발생: ' + error);
	            }
	        });
	    }
	});
	
   // 신바이오-중분류 선택시 소분류 가져오기
	$(document).on('change', '.bioClsf_mclsf_cd', function() {
   	var bioClsf_mclsf_cd = $(this).val();  // 선택된 값
	var cdIndex = $(this).attr("id").substring(16); // 행의 값
   	var smallOptions = '<option value="">신바이오 소분류</option>';
   	if (bioClsf_mclsf_cd) {  // 선택된 값이 있을 때만 요청
        $.ajax({
            url: '/expert/getNewBioSmallCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
            method: 'GET',
            data: { code : bioClsf_mclsf_cd },  // 선택된 카테고리 값을 전송
            dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
            success: function(data) {
                $.each(data.newBioSmallCode, function(index, item) {
                    smallOptions += '<option value="' + item.code_id + '">' + item.code_nm + '</option>';
                });
                // 소분류 셀렉트 박스에 새로 추가된 옵션 적용
                $('#bioClsf_sclsf_cd'+cdIndex).html(smallOptions).trigger('change');	     
				checkEmptyBioClsfEmpty();                   
            },
            error: function(xhr, status, error) {
                // 에러가 발생했을 때 실행
                console.error('AJAX 에러 발생: ' + error);
            }
        });
   	}
   });                
	
   // 정책분류 > 대분류 선택시 중분류 가져오기
   $(document).on('change', '.plcyClsf_lclsf_cd', function() {
	var plcyClsf_lclsf_cd = $(this).val();  // 선택된 값
	var cdIndex = $(this).attr("id").substring(17); // 행의 값
   	var mediumOptions = '<option value="">정책 중분류</option>';
   	if (plcyClsf_lclsf_cd) {  // 선택된 값이 있을 때만 요청
        $.ajax({
            url: '/expert/getPolicyMediumCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
            method: 'GET',
            data: { code : plcyClsf_lclsf_cd },  // 선택된 카테고리 값을 전송
            dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
            success: function(data) {
                $.each(data.policyMediumCode, function(index, item) {
                    mediumOptions += '<option value="' + item.codeId + '">' + item.codeIdNm + '</option>';
                });
                // 중분류 셀렉트 박스에 새로 추가된 옵션 적용
                $('#plcyClsf_mclsf_cd'+cdIndex).html(mediumOptions).trigger('change');

				checkEmptyPlcyClsfEmpty();
            },
            error: function(xhr, status, error) {
                // 에러가 발생했을 때 실행
                console.error('AJAX 에러 발생: ' + error);
            }
        });
   	}
   });
    
   // 정책분류 중분류 선택시 소분류 가져오기
   $(document).on('change', '.plcyClsf_mclsf_cd', function() {
   	var plcyClsf_mclsf_cd = $(this).val();  // 선택된 값
	var cdIndex = $(this).attr("id").substring(17); // 행의 값
   	var smallOptions = '<option value="">정책 소분류</option>';
   	if (plcyClsf_mclsf_cd) {  // 선택된 값이 있을 때만 요청
        $.ajax({
            url: '/expert/getPolicySmallCode.do',  // 데이터를 가져올 URL (서버 컨트롤러와 연결)
            method: 'GET',
            data: { code : plcyClsf_mclsf_cd },  // 선택된 카테고리 값을 전송
            dataType: 'json',  // 반환되는 데이터 타입 (JSON 형식)
            success: function(data) {
                $.each(data.policySmallCode, function(index, item) {
                    smallOptions += '<option value="' + item.code + '">' + item.codeNm + '</option>';
                });
                // 소분류 셀렉트 박스에 새로 추가된 옵션 적용
                $('#plcyClsf_sclsf_cd'+cdIndex).html(smallOptions).trigger('change');

				checkEmptyPlcyClsfEmpty();	                        
            },
            error: function(xhr, status, error) {
                // 에러가 발생했을 때 실행
                console.error('AJAX 에러 발생: ' + error);
            }
        });
   	}
   });
   /////////////////// 신바이오, 정책분류 값 설정 끝   

});


// 검증이 끝난 모든 데이터 본테이블로 이관 및 임시테이블 데이터 삭제 진행
function saveExpert(){
	dataSettingAll()
	var errLen = $(".exitErr").length
	var errInfoLen = $(".errLine li").length
	var errLiLen = $(".errDsctn").length
	
	for (var i = 0; i < errLiLen; i++) {
		var errText = $(".errDsctn")[i].value;
		if(errText != null && errText != ""){
			alert("검증이 완료되지 않았습니다.\n완료 후 다시 실행해주세요.")
			return false;
		}
	}
	
	if(errLen > 0 || errInfoLen > 0){
		// 현재 수정 및 선택된 전문가에게 오류가 있는 경우
		if($("#exprt_nm").val() == $(".selected").data('name') && $(".selected").attr('class').includes('exitErr')){
			alert("임시저장 후 다시 실행해주세요.")
			return false;
		} else {
			alert("검증이 완료되지 않은 전문인력정보가 있습니다.\n완료 후 다시 실행해주세요.")
			return false;
		}
	}
	
	$("#confirmForm").attr("action", "/expert/expertOriginalTransfer.do")
	$("#confirmForm").submit();
	
}
// 회원목록에서 selected 클래스를 가진 항목으로 스크롤
function scrollToChecked() {
    var checkedItem = $('.experList li.selected');
    if (checkedItem.length) {
        var container = $('.experList');
        var scrollTop = container.scrollTop();
        var containerHeight = container.height();
        var itemTop = checkedItem.position().top;
        var itemHeight = checkedItem.outerHeight();
        
        // 스크롤 조정 (해당 항목이 보이도록)
        if (itemTop < 0 || itemTop + itemHeight > containerHeight) {
            container.scrollTop(scrollTop + itemTop - (containerHeight / 2) + (itemHeight / 2));
        }
    }
}

// 엑셀 등록화면 임시저장
function saveTemp(){
    	
	dataSettingAll();
	$("#confirmForm").attr("action", "/expert/expertExcelTempSave.do")
	$("#confirmForm").submit();
}

// 하단 정보 전부 셋팅해서 controller로 옮기기
// 화면의 내용을 가공해서 전부 저장처리됨
function dataSettingAll(){
	settingRcntActy();
	settingAcbg();
	settingCrr();
	settingBioClsf();
	settingPlcyClsf();
	settingWnawdTb();
	settingUtlzTb();
	validMetaKywd();
}

// 필수 값 및 데이터 형식체크
function checkEsntlData(){
	
	// 기본정보
	var exprt_nm = $("#exprt_nm").val(); //성명
	var brth_dt = $("#brth_dt").val(); //생년
	var ogdp_nm = $("#ogdp_nm").val(); //소속
	var mbl_telno = $("#mbl_telno").val(); //휴대전화번호
	var eml_addr = $("#eml_addr").val();   // 이메일
	var asst_eml_addr = $("#asst_eml_addr").val(); // 보조이메일
	
	var err_exprt_dpcn = $("#err_exprt_dpcn").attr('value'); //전문인력정보 중복검증
	var meta_kywd_err = $("#kywd_nm_err").val();
	
	
	if(err_exprt_dpcn == "Y"){
		alert("전문인력정보가 중복되었습니다.");
		$("#exprt_nm").focus();
		return false;
	}
	
	if(exprt_nm == null || exprt_nm == ""){
		alert("성명이 누락되었습니다.");
		$("#exprt_nm").focus();
		return false;
	}
	
	if(brth_dt == null || brth_dt == ""){
		alert("생년이 누락되었습니다.");
		$("#brth_dt").focus();
		return false;
	}
	
	if (brth_dt.length !== 4) {
		alert("생년은 반드시 4자리여야 합니다.");
		$("#brth_dt").focus();
        return false;
    }
	
	var currentYear = new Date().getFullYear();
	var brthChkYear = parseInt(currentYear - 20);
	if (parseInt(brth_dt) < 1900 || parseInt(brth_dt)  > brthChkYear) {
		alert("생년을 다시 확인해주시길 바랍니다.");
		$("#brth_dt").focus();
        return false;
    }
	
	if(ogdp_nm == null || ogdp_nm == ""){
		alert("소속이 누락되었습니다.");
		$("#ogdp_nm").focus();
		return false;
	}
	if(mbl_telno != null && mbl_telno != ""){
		if(!validPhoneNumber(mbl_telno)){
			alert("휴대전화번호 형식을 확인해주세요. ex)010-1234-5678");
			$("#mbl_telno").focus();
			return false;
		}
	}
	if(eml_addr != null && eml_addr != ""){
		if(!validEmail(eml_addr)){
			alert("이메일 형식을 확인해주세요. ex)expert@google.com");
			$("#eml_addr").focus();
			return false;
		}
	}
	if(asst_eml_addr != null && asst_eml_addr != ""){
		if(!validEmail(asst_eml_addr)){
			alert("이메일2 형식을 확인해주세요. ex)expert@google.com");
			$("#asst_eml_addr").focus();
			return false;
		}
	}
	
	if(meta_kywd_err == 'Y'){
		alert("meta 키워드에는 특수문자 '#'만 사용해주세요");
		$("#kywd_nm").focus();
		return false;
	}
	
	// 하단정보
	var rcnt_actv_ogdp_nm = $("#rcnt_actv_ogdp_nm").val().split("|");
	var rcnt_actv_line = $("#rcnt_actv_line").val();
	
	var acbg_se_nm = $("#acbg_se_nm").val().split("|");
	var acbg_schl_nm =$("#acbg_schl_nm").val().split("|");
	var acbg_line = $("#acbg_line").val();
	
	var crr_ogdp_nm = $("#crr_ogdp_nm").val().split("|");
	var crr_line = $("#crr_line").val();
	
	var bioClsf_lclsf_cd = $("#bioClsf_lclsf_cd").val().split("|");
	var bioClsf_mclsf_cd = $("#bioClsf_mclsf_cd").val().split("|");
	var bioClsf_sclsf_cd = $("#bioClsf_sclsf_cd").val().split("|");
	var bioClsf_line = $("#bioClsf_line").val();
	
	var plcyClsf_lclsf_cd = $("#plcyClsf_lclsf_cd").val().split("|");
	var plcyClsf_mclsf_cd = $("#plcyClsf_mclsf_cd").val().split("|");
	var plcyClsf_sclsf_cd = $("#plcyClsf_sclsf_cd").val().split("|");
	var plcyClsf_line = $("#plcyClsf_line").val();
	
	var wnawd_dsctn = $("#wnawd_dsctn").val().split("|");
	var wnawd_line = $("#wnawd_line").val();
	
	var utlz_ymd = $("#utlz_ymd").val().split("|");
	var utlz_prps = $("#utlz_prps").val().split("|");
	var utlz_line = $("#utlz_line").val();
	
	
	// 최근활동
	for (var i = 0; i < rcnt_actv_line; i++) {
		if(rcnt_actv_ogdp_nm[i] == null || rcnt_actv_ogdp_nm[i] == "" || rcnt_actv_ogdp_nm[i] == " " ){
			alert("최근 5년 내 활동 소속이 누락되었습니다.");
			$("#rcnt_actv_ogdp_nm"+(i+1)).focus();
			return false;
		}
	}
	
	// 학력사항
	for (var i = 0; i < acbg_line; i++) {
		if(acbg_se_nm[i] == null || acbg_se_nm[i] == "" || acbg_se_nm[i] == " " ){
			alert("학력사항 구분이 누락되었습니다.");
			$("#acbg_se_nm"+(i+1)).focus();
			return false;
		}
		if(acbg_schl_nm[i] == null || acbg_schl_nm[i] == "" || acbg_schl_nm[i] == " " ){
			alert("학력사항 학교명이 누락되었습니다.");
			$("#acbg_schl_nm"+(i+1)).focus();
			return false;
		}
	}
	
	// 경력사항
	for (var i = 0; i < crr_line; i++) {
		if(crr_ogdp_nm[i] == null || crr_ogdp_nm[i] == "" || crr_ogdp_nm[i] == " " ){
			alert("경력사항 소속이 누락되었습니다.");
			$("#crr_ogdp_nm"+(i+1)).focus();
			return false;
		}
	}
	
	// 신바이오분류체계
	for (var i = 0; i < bioClsf_line; i++) {
		if(bioClsf_lclsf_cd[i] == null || bioClsf_lclsf_cd[i] == "" || bioClsf_lclsf_cd[i] == " " ){
			alert("신바이오분류체계 대분류가 누락되었습니다.");
			$("#bioClsf_lclsf_cd"+(i+1)).focus();
			return false;
		}
		
		if(bioClsf_mclsf_cd[i] == null || bioClsf_mclsf_cd[i] == "" || bioClsf_mclsf_cd[i] == " " ){
			alert("신바이오분류체계 중분류가 누락되었습니다.");
			$("#bioClsf_mclsf_cd"+(i+1)).focus();
			return false;
		}
		/*
		if(bioClsf_sclsf_cd[i] == null || bioClsf_sclsf_cd[i] == "" || bioClsf_sclsf_cd[i] == " " ){
			alert("신바이오분류체계 소분류가 누락되었습니다.");
			$("#bioClsf_sclsf_cd"+(i+1)).focus();
			return false;
		}
		*/
	}
	
	// 정책분류코드
	for (var i = 0; i < plcyClsf_line; i++) {
		if(plcyClsf_lclsf_cd[i] == null || plcyClsf_lclsf_cd[i] == "" || plcyClsf_lclsf_cd[i] == " " ){
			alert("정책분류체계 대분류가 누락되었습니다.");
			$("#plcyClsf_lclsf_cd"+(i+1)).focus();
			return false;
		}
		
		if(plcyClsf_mclsf_cd[i] == null || plcyClsf_mclsf_cd[i] == "" || plcyClsf_mclsf_cd[i] == " " ){
			alert("정책분류체계 중분류가 누락되었습니다.");
			$("#plcyClsf_mclsf_cd"+(i+1)).focus();
			return false;
		}
		/*
		if(plcyClsf_sclsf_cd[i] == null || plcyClsf_sclsf_cd[i] == "" || plcyClsf_sclsf_cd[i] == " " ){
			alert("정책분류코드 소분류가 누락되었습니다.");
			$("#plcyClsf_sclsf_cd"+(i+1)).focus();
			return false;
		}
		*/
	}
	
	// 수상내역
	for (var i = 0; i < wnawd_line; i++) {
		if(wnawd_dsctn[i] == null || wnawd_dsctn[i] == "" || wnawd_dsctn[i] == " " ){
			alert("수상내역이 누락되었습니다.");
			$("#wnawd_dsctn"+(i+1)).focus();
			return false;
		}
	}
	
	// 센터 내 활용실적
	for (var i = 0; i < utlz_line; i++) {
		if(utlz_ymd[i] == null || utlz_ymd[i] == "" || utlz_ymd[i] == " " ){
			alert("활용일자가 누락되었습니다.");
			$("#utlz_ymd"+(i+1)).focus();
			return false;
		}
		if(utlz_prps[i] == null || utlz_prps[i] == "" || utlz_prps[i] == " " ){
			alert("활용목적이 누락되었습니다.");
			$("#utlz_prps"+(i+1)).focus();
			return false;
		}
	}
	
	return true;
}

//////////////// 하단 내역 데이터 정리

// 공백라인 체크 
function noneLineCheck(list1, list2, list3, list4, index){
	
	var result1 = [];
	var result2 = [];
	var result3 = [];
	var result4 = [];
	
	// list 값이 널 체크 후 스플릿
	if(list1 != null) {
		result1 = list1.split("|");
	}
	if(list2 != null) {
		result2 = list2.split("|");
	}
	if(list3 != null) {
		result3 = list3.split("|");
	}
	if(list4 != null) {
		result4 = list4.split("|");
	}
	
	var lineCnt = 0;
	for (var i = 0; i < index; i++) {
		 // 한 행에서 공백인 열이 있는 부분은 검증하는게 에러가 안나게 공백처리
		 if(result1[i] == null){
		 	result1[i] = "";
		 }
		 if(result2[i] == null){
		 	result2[i] = "";
		 }
		 if(result3[i] == null){
		 	result3[i] = "";
		 }
		 if(result4[i] == null){
		 	result4[i] = "";
		 }
		 if ((result1[i] == null || result1[i] == 'undefined' || result1[i].trim() === "") &&
            (result2[i] == null || result2[i] == 'undefined' || result2[i].trim() === "") &&
            (result3[i] == null || result3[i] == 'undefined' || result3[i].trim() === "") &&
            (result4[i] == null || result4[i] == 'undefined' || result4[i].trim() === "")) {
            // 모두 null, undefined 또는 빈 문자열일 때 처리
        } else { // 1개라도 값이 있을때
			lineCnt += 1;
		}
	}
	return lineCnt;
}
/*
  * 유의사항
  - 모든 공백값도 추가하게끔 공백 검증은 따로하지 않음.(controller에서 ""도 가지고 있어야 List 형태로 for문 가능
  - 임시저장 버튼 클릭했을때와 우측 전문가 클릭했을때의 정보저장을 분리하기 위해서 필수값 체크는 따로 처리
*/

//최근활동
function settingRcntActy() {

	var index = $("#actyTb").find("tr").length;
	var rcnt_actv_ogdp_nm //소속
	var rcnt_actv_jbttl_nm //직책
	var rcnt_actv_bgng_yr //시작연도
	var rcnt_actv_end_yr //종료연도
	var rcnt_actv_line;
	for (var i = 1; i <= index; i++) {
		
		var ogdp_nm = $("#rcnt_actv_ogdp_nm"+i+"").val();
		if( i == 1 ){
			rcnt_actv_ogdp_nm = ogdp_nm
		} else {
			rcnt_actv_ogdp_nm = rcnt_actv_ogdp_nm + "|" + ogdp_nm;
		}
		
		var jbttl_nm = $("#rcnt_actv_jbttl_nm"+i+"").val();
		if( i == 1 ){
			rcnt_actv_jbttl_nm = jbttl_nm
		} else{
			rcnt_actv_jbttl_nm = rcnt_actv_jbttl_nm + "|" + jbttl_nm;
		}
		
		var bgng_yr = $("#rcnt_actv_bgng_yr"+i+"").val();
		bgng_yr = bgng_yr !== undefined ? bgng_yr : "";
		if( i == 1 ){
			rcnt_actv_bgng_yr = bgng_yr
		} else{
			rcnt_actv_bgng_yr = rcnt_actv_bgng_yr + "|" + bgng_yr;
		}
		
		var end_yr = $("#rcnt_actv_end_yr"+i+"").val();
		end_yr = end_yr !== undefined ? end_yr : "";
		if( i == 1 ){
			rcnt_actv_end_yr = end_yr
		} else {
			rcnt_actv_end_yr = rcnt_actv_end_yr + "|" + end_yr;
		}
	}
	
	if(rcnt_actv_ogdp_nm == null && rcnt_actv_jbttl_nm == null && rcnt_actv_bgng_yr == null && rcnt_actv_end_yr == null){
		index = 0;
	} else {
		index = noneLineCheck(rcnt_actv_ogdp_nm, rcnt_actv_jbttl_nm, rcnt_actv_bgng_yr, rcnt_actv_end_yr, index);
	}
	$("#rcnt_actv_ogdp_nm").val(rcnt_actv_ogdp_nm);
	$("#rcnt_actv_jbttl_nm").val(rcnt_actv_jbttl_nm);
	$("#rcnt_actv_bgng_yr").val(rcnt_actv_bgng_yr);
	$("#rcnt_actv_end_yr").val(rcnt_actv_end_yr);
	$("#rcnt_actv_line").val(index);
	
}

//학력사항
function settingAcbg() {
	var index = $("#acbgTb").find("tr").length;
	
	var acbg_se_nm //구분
	var acbg_schl_nm //학교명
	var acbg_mjr_nm //전공
	var acbg_grdtn_yr //졸업년도
	var acbg_line
	for (var i = 1; i <= index; i++) {
		
		var se_nm = $("#acbg_se_nm"+i+"").val();
		if( i == 1 ){
			acbg_se_nm = se_nm
		} else{
			acbg_se_nm = acbg_se_nm + "|" + se_nm;
		}
		
		var schl_nm = $("#acbg_schl_nm"+i+"").val();
		if( i == 1  ){
			acbg_schl_nm = schl_nm
		} else{
			acbg_schl_nm = acbg_schl_nm + "|" + schl_nm;
		}
		
		var mjr_nm = $("#acbg_mjr_nm"+i+"").val();
		if( i == 1 ){
			acbg_mjr_nm = mjr_nm
		} else{
			acbg_mjr_nm = acbg_mjr_nm + "|" + mjr_nm;
		}
		
		var grdtn_yr = $("#acbg_grdtn_yr"+i+"").val();
		grdtn_yr = grdtn_yr !== undefined ? grdtn_yr : "";
		if( i == 1 ){
			acbg_grdtn_yr = grdtn_yr
		} else{
			acbg_grdtn_yr = acbg_grdtn_yr + "|" + grdtn_yr;
		}
	}
	
	if(acbg_se_nm == null && acbg_schl_nm == null && acbg_mjr_nm == null && acbg_grdtn_yr == null){
		index = 0;
	} else {
		index = noneLineCheck(acbg_se_nm, acbg_schl_nm, acbg_mjr_nm, acbg_grdtn_yr, index);
	}
	
	$("#acbg_se_nm").val(acbg_se_nm);
	$("#acbg_schl_nm").val(acbg_schl_nm);
	$("#acbg_mjr_nm").val(acbg_mjr_nm);
	$("#acbg_grdtn_yr").val(acbg_grdtn_yr);
	$("#acbg_line").val(index);
	
}
	
//경력사항
function settingCrr() {
	var index = $("#crrTb").find("tr").length;
	
	var crr_ogdp_nm //소속
	var crr_jbttl_nm //직책
	var crr_bgng_yr //시작연도
	var crr_end_yr //종료연도
	var crr_line
	for (var i = 1; i <= index; i++) {
		
		var ogdp_nm = $("#crr_ogdp_nm"+i+"").val();
		if( i == 1){
			crr_ogdp_nm = ogdp_nm
		} else {
			crr_ogdp_nm = crr_ogdp_nm + "|" + ogdp_nm;
		}
		
		var jbttl_nm = $("#crr_jbttl_nm"+i+"").val();
		if( i == 1 ){
			crr_jbttl_nm = jbttl_nm
		} else {
			crr_jbttl_nm = crr_jbttl_nm + "|" + jbttl_nm;
		}
		
		var bgng_yr = $("#crr_bgng_yr"+i+"").val();
		bgng_yr = bgng_yr !== undefined ? bgng_yr : "";
		if( i == 1 ){
			crr_bgng_yr = bgng_yr
		} else {
			crr_bgng_yr = crr_bgng_yr + "|" + bgng_yr;
		}
		
		var end_yr = $("#crr_end_yr"+i+"").val();
		end_yr = end_yr !== undefined ? end_yr : "";
		if(end_yr == 'undefined')  end_yr = "";
		if( i == 1 ){
			crr_end_yr = end_yr
		} else {
			crr_end_yr = crr_end_yr + "|" + end_yr;
		}
	}
	
	if(crr_ogdp_nm == null && crr_jbttl_nm == null && crr_bgng_yr == null && crr_end_yr == null){
		index = 0;
	} else {
		index = noneLineCheck(crr_ogdp_nm, crr_jbttl_nm, crr_bgng_yr, crr_end_yr, index);
	}
	
	$("#crr_ogdp_nm").val(crr_ogdp_nm);
	$("#crr_jbttl_nm").val(crr_jbttl_nm);
	$("#crr_bgng_yr").val(crr_bgng_yr);
	$("#crr_end_yr").val(crr_end_yr);
	$("#crr_line").val(index);
			
}

//신바이오분류체계
function settingBioClsf() {
	var index = $("#bioClsfTb").find("tr").length;
	
	var bioClsf_lclsf_cd //대분류
	var bioClsf_mclsf_cd //중분류
	var bioClsf_sclsf_cd //소분류
	var bioClsf_line
	for (var i = 1; i <= index; i++) {
		
		var lclsf_cd = $("#bioClsf_lclsf_cd"+i+"").val();
		if( i == 1 ){
			bioClsf_lclsf_cd = lclsf_cd
		} else{
			bioClsf_lclsf_cd = bioClsf_lclsf_cd + "|" + lclsf_cd;
		}
		
		var mclsf_cd = $("#bioClsf_mclsf_cd"+i+"").val();
		if( i == 1 ){
			bioClsf_mclsf_cd = mclsf_cd
		} else {
			bioClsf_mclsf_cd = bioClsf_mclsf_cd + "|" + mclsf_cd;
		}
		
		var sclsf_cd = $("#bioClsf_sclsf_cd"+i+"").val();
		if( i == 1){
			bioClsf_sclsf_cd = sclsf_cd
		} else {
			bioClsf_sclsf_cd = bioClsf_sclsf_cd + "|" + sclsf_cd;
		}
	}
	
	if(bioClsf_lclsf_cd == null && bioClsf_mclsf_cd == null && bioClsf_sclsf_cd == null){
		index = 0;
	} else {
		index = noneLineCheck(bioClsf_lclsf_cd, bioClsf_mclsf_cd, bioClsf_sclsf_cd, null, index);
	}
	
	$("#bioClsf_lclsf_cd").val(bioClsf_lclsf_cd);
	$("#bioClsf_mclsf_cd").val(bioClsf_mclsf_cd);
	$("#bioClsf_sclsf_cd").val(bioClsf_sclsf_cd);
	$("#bioClsf_line").val(index);
}

// 정책분류코드
function settingPlcyClsf() {
	var index = $("#plcyClsfTb").find("tr").length;
	
	var plcyClsf_lclsf_cd //대분류
	var plcyClsf_mclsf_cd //중분류
	var plcyClsf_sclsf_cd //소분류
	var plcyClsf_line
	for (var i = 1; i <= index; i++) {
		
		var lclsf_cd = $("#plcyClsf_lclsf_cd"+i+"").val();
		if( i == 1){
			plcyClsf_lclsf_cd = lclsf_cd
		} else {
			plcyClsf_lclsf_cd = plcyClsf_lclsf_cd + "|" + lclsf_cd;
		}
		
		var mclsf_cd = $("#plcyClsf_mclsf_cd"+i+"").val();
		if( i == 1 ){
			plcyClsf_mclsf_cd = mclsf_cd
		} else {
			plcyClsf_mclsf_cd = plcyClsf_mclsf_cd + "|" + mclsf_cd;
		}
		
		var sclsf_cd = $("#plcyClsf_sclsf_cd"+i+"").val();
		if( i == 1 ){
			plcyClsf_sclsf_cd = sclsf_cd
		} else {
			plcyClsf_sclsf_cd = plcyClsf_sclsf_cd + "|" + sclsf_cd;
		}
	}
	
	if(plcyClsf_lclsf_cd == null && plcyClsf_mclsf_cd == null && plcyClsf_sclsf_cd == null){
		index = 0;
	} else {
		index = noneLineCheck(plcyClsf_lclsf_cd, plcyClsf_mclsf_cd, plcyClsf_sclsf_cd, null, index);
	}
	
	$("#plcyClsf_lclsf_cd").val(plcyClsf_lclsf_cd);
	$("#plcyClsf_mclsf_cd").val(plcyClsf_mclsf_cd);
	$("#plcyClsf_sclsf_cd").val(plcyClsf_sclsf_cd);
	$("#plcyClsf_line").val(index);
	
}

// 수상내역
function settingWnawdTb() {
	var index = $("#wnawdTb").find("tr").length;
	
	var wnawd_dsctn //수상내역
	var wnawd_yr //수상연도
	var wnawd_line
	for (var i = 1; i <= index; i++) {
		
		var dsctn = $("#wnawd_dsctn"+i+"").val();
		if( i == 1 ){
			wnawd_dsctn = dsctn
		} else {
			wnawd_dsctn = wnawd_dsctn + "|" + dsctn;
		}
		
		var yr = $("#wnawd_yr"+i+"").val();
		yr = yr !== undefined ? yr : "";
		if( i == 1 ){
			wnawd_yr = yr
		} else{
			wnawd_yr = wnawd_yr + "|" + yr;
		}
	}
	if(wnawd_dsctn == null && wnawd_yr == null ){
		index = 0;
	} else {
		index = noneLineCheck(wnawd_dsctn, wnawd_yr, null, null, index);
	}
	$("#wnawd_dsctn").val(wnawd_dsctn);
	$("#wnawd_yr").val(wnawd_yr);
	$("#wnawd_line").val(index);
}

// 센터 내 활용실적
function settingUtlzTb() {
	var index = $("#utlzTb").find("tr").length;
	
	var utlz_ymd //활용일자
	var utlz_prps //활용목적
	var info_exprtuser //활용자
	var utlz_rmrk_cn //비고
	var utlz_line
	for (var i = 1; i <= index; i++) {
		
		var ymd = $("#utlz_ymd"+i+"").val();
		if( i == 1 ){
			utlz_ymd = ymd
		} else {
			utlz_ymd = utlz_ymd + "|" + ymd;
		}
		
		var prps = $("#utlz_prps"+i+"").val();
		if( i == 1 ){
			utlz_prps = prps
		} else{
			utlz_prps = utlz_prps + "|" + prps;
		}
		
		var exprtuser = $("#info_exprtuser"+i+"").val();
		if( i == 1 ){
			info_exprtuser = exprtuser
		} else{
			info_exprtuser = info_exprtuser + "|" + exprtuser;
		}
		
		var cn = $("#utlz_rmrk_cn"+i+"").val();
		if( i == 1 ){
			utlz_rmrk_cn = cn
		} else{
			utlz_rmrk_cn = utlz_rmrk_cn + "|" + cn;
		}
	}
	if(utlz_ymd == null && utlz_prps == null && info_exprtuser == null && utlz_rmrk_cn == null){
		index = 0;
	} else {
		index = noneLineCheck(utlz_ymd, utlz_prps, info_exprtuser, utlz_rmrk_cn, index);
	}
	$("#utlz_ymd").val(utlz_ymd);
	$("#utlz_prps").val(utlz_prps);
	$("#info_exprtuser").val(info_exprtuser);
	$("#utlz_rmrk_cn").val(utlz_rmrk_cn);
	$("#utlz_line").val(index);
}
///////////// 하단 정보 정리 끝

// 엑셀 등록 취소, 초기화
function resetExcelUpload(){
	if(confirm("엑셀 등록을 취소하시겠습니까?")){
		location.href = "resetExpertExcelUpload.do?"
	}
}

// 새롭게 추가된 신바이오, 정책분류체계 css 및 효과 설정
function clsfSelect(){
	 $('.select2').select2({});
    // Select2 초기화: 플레이스홀더 설정
    $('.bioClsf_lclsf_cd').select2({
        placeholder: "신바이오 대분류 선택", // 플레이스홀더 텍스트
        allowClear: false, // 선택 해제 버튼 표시
		width: '200px',
        minimumResultsForSearch: Infinity
    });
    $('.bioClsf_mclsf_cd').select2({
        placeholder: "신바이오 중분류 선택",
        allowClear: true,
		width: '200px',
        minimumResultsForSearch: Infinity,
        language: {
            noResults: function() {
                return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
            }
        }
    });
    $('.bioClsf_sclsf_cd').select2({
        placeholder: "신바이오 소분류 선택",
        allowClear: true,
		width: '200px',
        minimumResultsForSearch: Infinity,
        language: {
            noResults: function() {
                return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
            }
        }
    });
    // Select2 초기화: 플레이스홀더 설정
    $('.plcyClsf_lclsf_cd').select2({
        placeholder: "정책 대분류 선택", // 플레이스홀더 텍스트
        allowClear: true, // 선택 해제 버튼 표시
		width: '200px',
        minimumResultsForSearch: Infinity
    });
    $('.plcyClsf_mclsf_cd').select2({
        placeholder: "정책 중분류 선택",
        allowClear: true,
		width: '200px',
        minimumResultsForSearch: Infinity,
        language: {
            noResults: function() {
                return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
            }
        }
    });
    $('.plcyClsf_sclsf_cd').select2({
        placeholder: "정책 소분류 선택",
        allowClear: true,
		width: '200px',
        minimumResultsForSearch: Infinity,
        language: {
            noResults: function() {
                return "상위 분류를 선택해주세요"; // 사용자 정의 메시지
            }
        }
    });
}

// 행 재정렬 함수
function reorderRows(tbodyId) {
	if(tbodyId == "bioClsfTb"){
	    $('#bioClsfTb tr').each(function(index) {
	        var newIndex = index + 1;
	 		$(this).find('td:first-child').text(newIndex );
	
	        // th에서 id 값을 가져와서 td의 id 값에 적용
	        $(this).find('select[id^="bioClsf_lclsf_cd"]').attr('id', 'bioClsf_lclsf_cd' + newIndex);
	        $(this).find('select[id^="bioClsf_mclsf_cd"]').attr('id', 'bioClsf_mclsf_cd' + newIndex);
	        $(this).find('select[id^="bioClsf_sclsf_cd"]').attr('id', 'bioClsf_sclsf_cd' + newIndex);
	    });
	} else {
		$('#plcyClsfTb tr').each(function(index) {
	        var newIndex = index + 1;
	 		$(this).find('td:first-child').text(newIndex );
	
	        // th에서 id 값을 가져와서 td의 id 값에 적용
	        $(this).find('select[id^="plcyClsf_lclsf_cd"]').attr('id', 'plcyClsf_lclsf_cd' + newIndex);
	        $(this).find('select[id^="plcyClsf_mclsf_cd"]').attr('id', 'plcyClsf_mclsf_cd' + newIndex);
	        $(this).find('select[id^="plcyClsf_sclsf_cd"]').attr('id', 'plcyClsf_sclsf_cd' + newIndex);
	    });
	}
}

// 이메일 검증
function validEmail(email) {
    var re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
}

// 휴대폰번호 검증
function validPhoneNumber(phone) {
    var re = /^010-\d{4}-\d{4}$/;
    return re.test(phone);
}

//Meta 키워드 검증
function validMetaKywd(){
	var kywd_nm = $("#kywd_nm").val();
	// 정규식: #을 제외한 특수문자 체크 (예: !, @, $, %, &, *, 등등)
    var specialCharsRegex = /[^a-zA-Z0-9#\s가-힣]/g;
	
	if(kywd_nm != null && kywd_nm != ''){
	    // 특수문자가 있는지 확인
	    var specialChars = kywd_nm.match(specialCharsRegex);
		if(specialChars){
			$("#kywd_nm_err").val("Y");
		} else {
			$("#kywd_nm_err").val("N");
		}
	}
}

// 엑셀등록확인화면 전문가 정보 삭제
function deleteTempExpertInfo(){
	var exprt_nm = $("#exprt_nm").val();
	if(confirm(exprt_nm+"님의 전문가 정보를 삭제하시겠습니까?")){
		$("#confirmForm").attr("action", "/expert/deleteTempExpertInfo.do")
		$("#confirmForm").submit();
	}
 	e.preventDefault();
}


//////////// 값 입력시 검증 시작
function checkAllEmptyData(){
	checkEmptyRcntActvEmpty();
	checkEmptyAcbgEmpty();
	checkEmptyCrrEmpty();
	checkEmptyBioClsfEmpty();
	checkEmptyPlcyClsfEmpty();
	checkEmptyWnawdEmpty();
	checkEmptyUtlzEmpty();
}
// 전문가 기본정보
function checkEmptyInfo() {
    var exprt_nm = $("#exprt_nm").val().trim();  // 입력 필드 값 가져오기 (공백 제거)
    var brth_dt = $("#brth_dt").val().trim(); 
    var ogdp_nm = $("#ogdp_nm").val().trim(); 
	var mbl_telno = $("#mbl_telno").val().trim();
    var eml_addr = $("#eml_addr").val().trim(); 
    var asst_eml_addr = $("#asst_eml_addr").val().trim();
	var kywd_nm = $("#kywd_nm").val(); 
    var errorList = $('#errorList');  // ul 요소

	var cnt = 0;
	var errDsctn // 오류입력
	$('#err_exprt_nm').remove();
	$('#err_brth_dt').remove();
	$('#err_ogdp_nm').remove();
	$('#err_mbl_telno').remove();
	$('#err_eml_addr').remove();
	$('#err_asst_eml_addr').remove();
	$('#err_kywd_nm').remove();
	// 전문인력정보 중복체크
	checkDpcnExpert(exprt_nm, brth_dt);
	
    if (exprt_nm == null || exprt_nm === "" || exprt_nm === "null") {
        errDsctn = $('<li id="err_exprt_nm">&nbsp;&nbsp;&nbsp;· 성명 누락</li>');
		$('#errorList1').append(errDsctn);
		cnt += 1;
    } 
	
	if(brth_dt == null || brth_dt == ""){
		errDsctn = $('<li id="err_brth_dt">&nbsp;&nbsp;&nbsp;· 생년 누락</li>');
		if(cnt % 2 == 0) {
			$('#errorList1').append(errDsctn); 
		} else {
			$('#errorList2').append(errDsctn); 
		}
		cnt += 1;
	} 
	if(brth_dt != "" && brth_dt.length !== 4) {
		errDsctn = $('<li id="err_brth_dt">&nbsp;&nbsp;&nbsp;· 생년 형식 오류</li>');
		if(cnt % 2 == 0) {
			$('#errorList1').append(errDsctn); 
		} else {
			$('#errorList2').append(errDsctn); 
		}
		cnt += 1;
	} 
	if(ogdp_nm == null || ogdp_nm == "") {
		errDsctn = $('<li id="err_ogdp_nm">&nbsp;&nbsp;&nbsp;· 소속 누락</li>');
		if(cnt % 2 == 0) {
			$('#errorList1').append(errDsctn); 
		} else {
			$('#errorList2').append(errDsctn); 
		}
		cnt += 1;
    }
	if(mbl_telno != null && mbl_telno != ""){
		if(!validPhoneNumber(mbl_telno)){
			errDsctn = $('<li id="err_mbl_telno">&nbsp;&nbsp;&nbsp;· 휴대폰번호 형식 오류(적절 형식 ex)010-1234-5678)</li>');
			if(cnt % 2 == 0) {
				$('#errorList1').append(errDsctn); 
			} else {
				$('#errorList2').append(errDsctn); 
			}
			cnt += 1;
		}
	}
	if(eml_addr != null && eml_addr != ""){
		if(!validEmail(eml_addr)){
			errDsctn = $('<li id="err_eml_addr">&nbsp;&nbsp;&nbsp;· 이메일 형식 오류</li>');
			if(cnt % 2 == 0) {
				$('#errorList1').append(errDsctn); 
			} else {
				$('#errorList2').append(errDsctn); 
			}
			cnt += 1;
		}
	}
	if(asst_eml_addr != null && asst_eml_addr != ""){
		if(!validEmail(asst_eml_addr)){
			errDsctn = $('<li id="err_asst_eml_addr">&nbsp;&nbsp;&nbsp;· 이메일2 형식 오류</li>');
			if(cnt%2 == 0) {
				$('#errorList1').append(errDsctn); 
			} else {
				$('#errorList2').append(errDsctn); 
			}
			cnt += 1;
		}
	} 
	
	var specialCharsRegex = /[^a-zA-Z0-9#\s가-힣]/g;
	if(kywd_nm != null){
	    // 특수문자가 있는지 확인
	    var specialChars = kywd_nm.match(specialCharsRegex);
		if(specialChars && kywd_nm != ''){
			errDsctn = $('<li id="err_kywd_nm">&nbsp;&nbsp;&nbsp;· meta 키워드 형식 오류</li>');
			if(cnt % 2 == 0) {
				$('#errorList1').append(errDsctn); 
			} else {
				$('#errorList2').append(errDsctn); 
			}
			cnt += 1;
		}
	}
}

// 최근활동
function checkEmptyRcntActvEmpty(){
	settingRcntActy();
	var rcnt_actv_ogdp_nm = $("#rcnt_actv_ogdp_nm").val().split("|");
	var rcnt_actv_line = $("#rcnt_actv_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < rcnt_actv_line; i++) {
		if(rcnt_actv_ogdp_nm[i] == null || rcnt_actv_ogdp_nm[i] == "" || rcnt_actv_ogdp_nm[i] == " " ){
			var errInput = $('#rcnt_actv_err'+(i+1));  // input 요소
			errInput.val("소속 누락")
		} else {
			$('#rcnt_actv_err'+(i+1)).val('');
		}
	}
}

// 학력사항
function checkEmptyAcbgEmpty(){
	settingAcbg();
	var acbg_se_nm = $("#acbg_se_nm").val().split("|");
	var acbg_schl_nm =$("#acbg_schl_nm").val().split("|");
	var acbg_line = $("#acbg_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < acbg_line; i++) {
		var errInput = $('#acbg_err'+(i+1));  // input 요소
		if(acbg_se_nm[i] == null || acbg_se_nm[i] == "" ){
			errInput.val("구분 누락")
		} else if(acbg_schl_nm[i] == null || acbg_schl_nm[i] == "" || acbg_schl_nm[i] == " " ){
			errInput.val("학교명 누락")
		}else {
			$('#acbg_err'+(i+1)).val('')
		}
	}
}

// 경력사항
function checkEmptyCrrEmpty(){
	settingCrr();
	var crr_ogdp_nm = $("#crr_ogdp_nm").val().split("|");
	var crr_line = $("#crr_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < crr_line; i++) {
		var errInput = $('#crr_err'+(i+1));  // input 요소
		if(crr_ogdp_nm[i] == null || crr_ogdp_nm[i] == "" || crr_ogdp_nm[i] == " " ){
			errInput.val("소속 누락")
		}else {
			$('#crr_err'+(i+1)).val('')
		}
	}
}

// 신바이오분류체계
function checkEmptyBioClsfEmpty(){
	settingBioClsf();
	var bioClsf_lclsf_cd = $("#bioClsf_lclsf_cd").val().split("|");
	var bioClsf_mclsf_cd = $("#bioClsf_mclsf_cd").val().split("|");
	var bioClsf_line = $("#bioClsf_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < bioClsf_line; i++) {
		var errInput = $('#bioClsf_err'+(i+1));  // input 요소
		
		if(bioClsf_lclsf_cd[i] == null || bioClsf_lclsf_cd[i] == "" || bioClsf_lclsf_cd[i] == " " ){
			errInput.val("대분류 누락")
		}else if(bioClsf_mclsf_cd[i] == null || bioClsf_mclsf_cd[i] == "" || bioClsf_mclsf_cd[i] == " " ){
			errInput.val("중분류 누락")
		}else {
			$('#bioClsf_err'+(i+1)).val('')
		}
	}
}

// 정책분류체계
function checkEmptyPlcyClsfEmpty(){
	settingPlcyClsf();
	var plcyClsf_lclsf_cd = $("#plcyClsf_lclsf_cd").val().split("|");
	var plcyClsf_mclsf_cd = $("#plcyClsf_mclsf_cd").val().split("|");
	var plcyClsf_line = $("#plcyClsf_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < plcyClsf_line; i++) {
		var errInput = $('#plcyClsf_err'+(i+1));  // input 요소
		
		if(plcyClsf_lclsf_cd[i] == null || plcyClsf_lclsf_cd[i] == "" || plcyClsf_lclsf_cd[i] == " " ){
			errInput.val("대분류 누락")
		}else if(plcyClsf_mclsf_cd[i] == null || plcyClsf_mclsf_cd[i] == "" || plcyClsf_mclsf_cd[i] == " " ){
			errInput.val("중분류 누락")
		}else {
			$('#plcyClsf_err'+(i+1)).val('')
		}
	}
}

// 수상내역
function checkEmptyWnawdEmpty(){
	settingWnawdTb();
	var wnawd_dsctn = $("#wnawd_dsctn").val().split("|");
	var wnawd_line = $("#wnawd_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < wnawd_line; i++) {
		var errInput = $('#wnawd_err'+(i+1));  // input 요소
		
		if(wnawd_dsctn[i] == null || wnawd_dsctn[i] == "" || wnawd_dsctn[i] == " " ){
			errInput.val("수상내역 누락")
		}else {
			$('#wnawd_err'+(i+1)).val('')
		}
	}
}

// 센터 내 활용실적
function checkEmptyUtlzEmpty(){
	settingUtlzTb();
	var utlz_ymd = $("#utlz_ymd").val().split("|");
	var utlz_prps = $("#utlz_prps").val().split("|");
	var utlz_line = $("#utlz_line").val();
	
	var errDsctn // 오류입력
	for (var i = 0; i < utlz_line; i++) {
		var errInput = $('#utlz_err'+(i+1));  // input 요소
		if(utlz_ymd[i] == null || utlz_ymd[i] == "" || utlz_ymd[i] == " " ){
			errInput.val("활용일자 누락");
		} else if(utlz_prps[i] == null || utlz_prps[i] == "" || utlz_prps[i] == " " ){
			errInput.val("활용목적 누락");
		}else {
			$('#utlz_err'+(i+1)).val('')
		}
	}
}

// 전문인력정보 이름, 생년, 전문가번호 중복체크
function checkDpcnExpert(exprt_nm, brth_dt){
	var currentUrl = window.location.href;
	var chkUrl = currentUrl.split('/').pop().split('?')[0];
	var exprt_mng_no = $("#exprt_mng_no").val();
	var excel_yn = $("#excel_yn").val();
	// 중복체크
    $.ajax({
        url: "/expert/expertDpcnChk.do" , 
        method: 'POST',
        data: {exprt_nm: exprt_nm
			  , brth_dt: brth_dt 
			  , exprt_mng_no : exprt_mng_no
			  , excel_yn : excel_yn},  
        dataType: 'json',
        success: function(data) {
			$('#err_exprt_dpcn').remove();
			if(data.dpcnCnt > 0){
				var errDsctn = $('<li id="err_exprt_dpcn" value="Y">&nbsp;&nbsp;&nbsp;· 전문인력정보 중복</li>');
				$('#errorList1').append(errDsctn); 
			} else if(data.dpcnExcelCnt > 0){
				var errDsctn = $('<li id="err_exprt_dpcn" value="Y">&nbsp;&nbsp;&nbsp;· 엑셀 전문인력정보 중복</li>');
				$('#errorList1').append(errDsctn); 
			}
			
        },
        error: function(xhr, status, error) {
            // 에러가 발생했을 때 실행
            console.error('AJAX 에러 발생: ' + error);
        }
    });

}
