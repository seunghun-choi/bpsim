<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>바이오분야별 전문인력 엑셀등록화면</title>
	<script type="text/javascript">
	
    $(document).ready(function() {
    	  let errorMessage = "${errorMessage}";
    	  console.log("이건 찍히지" + errorMessage);
          if (errorMessage != '') {
        	  console.log(errorMessage);
              alert(errorMessage);
          }
      });
    
	
	function fn_excelDown(){
	    var url = "/expert/excelFormDown.do";
	    window.open(url, '_blank');
	}
	function fn_submit(){
		var fileInput = $("#uploadName").val();
		
	    if (fileInput == null || fileInput == "") { 
	        alert("첨부파일을 등록해주시기 바랍니다.");
	        return false;
	    }
	    
	    // 파일 확장자 체크 (엑셀 파일만 허용)
	    var ext = fileInput.split('.').pop().toLowerCase();
	    if($.inArray(ext, ['xlsx', 'xls']) == -1) {
	        alert('엑셀 파일만(xlsx) 업로드 가능합니다.');
	        return false;
	    }
	    
		
		$("#srchForm").submit();
	}
	</script>
</head>
<body>
	<div class="contentBox">
	    <div class="content">
	        <div class="cnt_title">
	            <h1>바이오분야별 전문인력 엑셀등록</h1>
	        </div>
	        <form name="srchForm" id="srchForm" action="/expert/excelUpload.do" method="post" enctype="multipart/form-data">
	        <div class="cnt_box">
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>양식 다운로드</h2>
	                </div>
	                <div class="tbl_option">
	                    <table class="tbl_style3">
	                        <caption>엑셀양식 다운로드</caption>
	                        <colgroup>
		                        <col style="width:10%;">
		                        <col style="width:40%;">
		                        <col style="width:10%;">
		                        <col style="width:40%;">
	                        </colgroup>
	                        <tbody>
	                            <tr>
	                                <th scope="row">양식다운로드</th>
	                                <td colspan="3">
	                                    <button type="button" onclick="fn_excelDown()" class="btn excelDown"><span class="icon icon_excel"></span>엑셀양식다운로드</button>
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
	                </div>
	                <div class="cnt_toolbar">
	                    <h2>엑셀 업로드</h2>
	                </div>
	                <p style="font-size:var(--font-lg); font-weight:450; margin-left:1rem;"><strong style="color:red;">'번호'</strong>는 필수 입력 사항이며, 미입력 시 업로드되지 않습니다.</p>
	                <div class="tbl_option">
	                    <table class="tbl_style3">
	                        <caption>엑셀업로드</caption>
	                        <colgroup>
		                        <col style="width:10%;">
		                        <col style="width:40%;">
		                        <col style="width:10%;">
		                        <col style="width:40%;">
	                        </colgroup>
	                        <tbody>
	                            <tr>
	                                <th scope="row">엑셀파일등록</th>
	                                <td colspan="3">
	                                    <input type="file" id="uploadName" name="uploadName">
	                                </td>
	                            </tr>
<!-- 	                            <tr> -->
<!-- 	                            	<td colspan="4"> -->
<!-- 	                            		엑셀 양식의 '번호' 항목은 필수 입력 사항입니다. 번호가 입력되지 않으면 해당 데이터는 업로드되지 않으니, 반드시 '번호'를 정확히 입력해주세요. -->
<!-- 	                            	</td> -->
<!-- 	                            </tr> -->
	                        </tbody>
	                    </table>	                    
	                    <div class="btn_wrap">
	                        <button type="button" onclick="fn_submit()" class="btn btn-task-type1">등록</button>
	                    </div>
	                </div>
	            </div>
	        </div>
	        </form>
	    </div>
	</div>
</body>
</html>