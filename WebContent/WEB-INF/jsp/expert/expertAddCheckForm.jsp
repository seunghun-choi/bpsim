<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
	 <title>바이오분야별 전문인력 엑셀등록 화면</title>
    <style>
    td .errDsctn {
		color: red;
		font-weight: 600;
		font-size: medium;
	}
    </style>
    <script>
    $(document).ready(function() {
    	let message = "${message}";
        if (message) {
            alert(message);
        }
        
    	clsfSelect();
    });  
    	
    </script>
</head>
<body>
	    <div class="content">
	        <div class="cnt_title">
	            <h1>전문인력 엑셀등록 확인</h1> 
	        </div>
	        <form name="confirmForm" id="confirmForm" method="post" action="" enctype="multipart/form-data">
	        <input type="hidden" name="exprt_mng_no" id="exprt_mng_no" value="${expert.exprt_mng_no }">
	        <input type="hidden" name="sel_exprt_mng_no" id="sel_exprt_mng_no">
	        <input type="hidden" name="kywd_nm_err" id="kywd_nm_err">
	        <input type="hidden" name="excel_yn" id="excel_yn" value="Y">
	        <!-- 최근활동 -->
	        <input type="hidden" name="rcnt_actv_ogdp_nm" id= "rcnt_actv_ogdp_nm">
	        <input type="hidden" name="rcnt_actv_jbttl_nm" id= rcnt_actv_jbttl_nm>
	        <input type="hidden" name="rcnt_actv_bgng_yr" id= "rcnt_actv_bgng_yr">
	        <input type="hidden" name="rcnt_actv_end_yr" id= "rcnt_actv_end_yr">
	        <input type="hidden" name="rcnt_actv_line" 	id= "rcnt_actv_line">
	        <!-- 학력사항 -->
	        <input type="hidden" name="acbg_se_nm" id= "acbg_se_nm">
	        <input type="hidden" name="acbg_schl_nm" id= "acbg_schl_nm">
	        <input type="hidden" name="acbg_mjr_nm" id= "acbg_mjr_nm">
	        <input type="hidden" name="acbg_grdtn_yr" id= "acbg_grdtn_yr">
	        <input type="hidden" name="acbg_line" 	id= "acbg_line">
	        <!-- 경력사항 -->
	        <input type="hidden" name="crr_ogdp_nm" id= "crr_ogdp_nm">
	        <input type="hidden" name="crr_jbttl_nm" id= "crr_jbttl_nm">
	        <input type="hidden" name="crr_bgng_yr" id= "crr_bgng_yr">
	        <input type="hidden" name="crr_end_yr" id= "crr_end_yr">
	        <input type="hidden" name="crr_line" 	id= "crr_line">
	        <!-- 신바이오분류체계 -->
	        <input type="hidden" name="bioClsf_lclsf_cd" id= "bioClsf_lclsf_cd">
	        <input type="hidden" name="bioClsf_mclsf_cd" id= "bioClsf_mclsf_cd">
	        <input type="hidden" name="bioClsf_sclsf_cd" id= "bioClsf_sclsf_cd">
	        <input type="hidden" name="bioClsf_line" 		id= "bioClsf_line">
	        <!-- 정책분류코드 -->
	        <input type="hidden" name="plcyClsf_lclsf_cd" id= "plcyClsf_lclsf_cd">
	        <input type="hidden" name="plcyClsf_mclsf_cd" id= "plcyClsf_mclsf_cd">
	        <input type="hidden" name="plcyClsf_sclsf_cd" id= "plcyClsf_sclsf_cd">
	        <input type="hidden" name="plcyClsf_line" id= "plcyClsf_line">
	        <!-- 수상내역 -->
	        <input type="hidden" name="wnawd_dsctn" id= "wnawd_dsctn">
	        <input type="hidden" name="wnawd_yr" id= "wnawd_yr">
	        <input type="hidden" name="wnawd_line" id= "wnawd_line">
	         <!-- 센터 내 활용실적 -->
	        <input type="hidden" name="utlz_ymd" id= "utlz_ymd">
	        <input type="hidden" name="utlz_prps" id= "utlz_prps">
	        <input type="hidden" name="info_exprtuser" id= "info_exprtuser">
	        <input type="hidden" name="utlz_rmrk_cn" id= "utlz_rmrk_cn">
	        <input type="hidden" name="utlz_line" id= "utlz_line">
	        <div style="display: flex;justify-content: end;">
                <button type="button" class="btn btn-task-type1" onclick="resetExcelUpload()">엑셀 등록 취소</button>
            </div>
	        <div class="cnt_box" id="cnt_box">
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>인적사항</h2>
	                    <div style="display: flex;justify-content: end;">
			                <button type="button" class="btn btn-reject" onclick="deleteTempExpertInfo()">전문가정보 삭제</button>
			            </div>
	                </div>
	                <table class="tbl_style3">
	                    <caption>인적사항</caption>
	                    <colgroup>
	                        <col style="width:10%;">
                            <col style="width:auto;">
                            <col style="width:auto;">
                            <col style="width:auto;">
	                        <col style="width:10%;">
                            <col style="width:auto;">
                            <col style="width:auto;">
                            <col style="width:auto;">
	                        <col style="width:15%;">
	                    </colgroup>
	                    <tbody>
	                        <tr>
	                            <th scope="row"><span class="required">성명</span></th>
	                            <td colspan="3" >
	                                <input type="text" class="infoInputBox" id="exprt_nm" name="exprt_nm" value="${expert.exprt_nm }"  onblur="checkEmptyInfo()">
	                            </td>
	                            <th scope="row"><span class="required">성별</span></th>
	                            <td colspan="3">
	                                <div class="radiobox">
	                                    <label for="male">
	                                    <input type="radio" name="exprt_gndr" class="button_radio" id="male" value="1" <c:if test="${expert.exprt_gndr eq '1' || expert.exprt_gndr eq null}">checked="checked"</c:if>>남</label>
	                                    <label for="female">
	                                    <input type="radio" name="exprt_gndr" class="button_radio" id="female" value="2" <c:if test="${expert.exprt_gndr eq '2'}">checked="checked"</c:if>>녀</label>
	                                </div>
	                            </td>
	                            <td rowspan="6">
	                                <div class="experList">
	                                	 <ul>
	                                	 	<c:forEach items="${experList}" var="expertSel" varStatus="status">
							               	  <li value="${expertSel.exprt_mng_no }" data-name="${expertSel.exprt_nm }" class="<c:if test='${expertSel.exprt_mng_no eq expert.exprt_mng_no }'>selected</c:if> <c:if test='${expertSel.err_cnt > 0 }'>exitErr</c:if>">${expertSel.exprt_nm }</li>
	                                	 	</c:forEach>
							            </ul>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr>
	                        	<th scope="row"><span class="required">생년</span></th>
	                            <td colspan="3">
	                                <input type="number" id="brth_dt" name="brth_dt" min="1900" max="2100" step="1" placeholder="YYYY" value="${expert.brth_dt}" style="width:13rem;" oninput="this.value=this.value.slice(0,4)" onblur="checkEmptyInfo()">
	                            </td>
	                            <th scope="row"><span class="required">소속</span></th>
	                            <td colspan="3">
	                                <input type="text" class="infoInputBox" id="ogdp_nm" name="ogdp_nm" class="infoInput" value="${expert.ogdp_nm}" onblur="checkEmptyInfo()">
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">부서</th>
	                            <td colspan="3">
	                                <input type="text" id="" class="infoInputBox" name="dept_nm" class="infoInput" value="${expert.dept_nm}">
	                            </td>
	                            <th scope="row">직위</th>
	                            <td colspan="3">
	                                <input type="text" class="infoInputBox" id="" name="jbps_nm" class="infoInput" value="${expert.jbps_nm}">
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">휴대폰번호</th>
	                            <td colspan="3">
	                                <input type="tel" class="infoInputBox" id="mbl_telno" name="mbl_telno" class="infoInput" value="${expert.mbl_telno}" onblur="checkEmptyInfo()">
	                            </td>
	                            <th scope="row">회사번호</th>
	                            <td colspan="3">
	                                <input type="tel" class="infoInputBox"  id="" name="co_telno" class="infoInput" value="${expert.co_telno}">
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">이메일</th>
	                            <td colspan="3">
	                                <input type="email" id="eml_addr" class="infoInputBox" name="eml_addr" class="infoInput" value="${expert.eml_addr}" onblur="checkEmptyInfo()">
	                            </td>
	                            <th scope="row">이메일2</th>
	                            <td colspan="3">
	                                <input type="email" id="asst_eml_addr" class="infoInputBox" name="asst_eml_addr" class="infoInput" value="${expert.asst_eml_addr}" onblur="checkEmptyInfo()">
	                            </td>
	                        </tr>
	                        <tr>
	                           <th scope="row" style="position: relative;">meta 키워드
	                               <span class="tooltipBtn">?</span>
				                   <div class="tooltipBox">
				                      <ul class="custom-list">
				                      	<li>
					                        <div class="sign_meaning">
					                            <p><span class="example">표준 입력 : #키워드</span> </p>
					                            <p>
					                                <span class="example_meaning"> > ex) #양자전문 #정책연구센터</span>
					                            </p>
					                            <p>
					                                <span class="example_meaning"> > '#'이외의 특수문자는 입력하실 수 없습니다.</span>
					                            </p>
					                        </div>
				                      	</li>
				                      </ul>
				                    </div>
	                            </th>
	                            <td colspan="3">
	                                <input type="text" id="kywd_nm" name="kywd_nm" class="infoInputBox" value="${expert.kywd_nm}" onblur="checkEmptyInfo()">
	                            </td>
	                            <th scope="row">출처</th>
	                            <td colspan="3">
	                                <input type="text" id="" class="infoInputBox" name="src_nm" class="infoInput" value="${expert.src_nm}">
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row">비고</th>
	                            <td colspan="8">
	                                <input type="text" id="" class="infoInputBox" name="rmrk_cn" class="" style="width: 68.5%;" value="${expert.rmrk_cn}">
	                            </td>
	                        </tr>
	                        <tr>
	                            <th scope="row" rowspan="2">인적사항 검증결과</th>
	                            <td colspan="8">
	                                <ul class="errLine" id="errorList1">
                                	<c:forEach items="${infoErrList}" var="err" varStatus="status">
                               	 		<c:if test="${(status.index ) % 2 == 0}">
					               	  		<li value="${err.exprt_inpt_err_cd }" >&nbsp&nbsp&nbsp·${err.exprt_inpt_err_artcl_nm }</li>
                               	 		</c:if>
                                	</c:forEach>
						            </ul>
	                            </td>
	                        </tr><tr>
	                            <td colspan="8">
	                            	<ul class="errLine" id="errorList2">
	                            	<c:forEach items="${infoErrList}" var="err" varStatus="status">
                               	 		<c:if test="${(status.index ) % 2 != 0}">
					               	  		<li value="${err.exprt_inpt_err_cd }" >&nbsp&nbsp&nbsp·${err.exprt_inpt_err_artcl_nm }</li>
                               	 		</c:if>
                               	 	</c:forEach>
	                            	</ul>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>                      
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>최근 5년 내 활동</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>최근 5년 내 활동</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:20%;">
	                        <col style="width:20%;">
	                        <col style="width:15%;">
	                        <col style="width:15%;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="rcnt_actv_ogdp_nm"><span class="required">소속</span></th>
	                            <th id="rcnt_actv_jbttl_nm">직책</th>
	                            <th id="rcnt_actv_bgng_yr">시작연도</th>
	                            <th id="rcnt_actv_end_yr">종료연도</th>
	                            <th id="rcnt_actv_err">검증</th>
	                            <th><button class="icon btn_bg blue addRowBtn"><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="actyTb">
	                    	<c:forEach items="${rcntActyList}" var="acty" varStatus="status">
	                    	<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
	                                	<input type="text" id="rcnt_actv_ogdp_nm${index}" value="${acty.rcnt_actv_ogdp_nm}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="text" id="rcnt_actv_jbttl_nm${index}" value="${acty.rcnt_actv_jbttl_nm}" onblur="checkAllEmptyData()">    
		                            </td>
		                            <td>
		                                <input type="number" id="rcnt_actv_bgng_yr${index}" value="${acty.rcnt_actv_bgng_yr}" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="number" id="rcnt_actv_end_yr${index}" value="${acty.rcnt_actv_end_yr}" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()">
		                            </td>
		                            <td><input type="text"  class="errDsctn" id="rcnt_actv_err${index}" readonly value=""></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(rcntActyList) eq 0 }">
		                        <tr>
		                            <td>1</td>
		                            <td><input type="text" id="rcnt_actv_ogdp_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="rcnt_actv_jbttl_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="number" id="rcnt_actv_bgng_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="number" id="rcnt_actv_end_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="text" class="errDsctn" id="rcnt_actv_err1"readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>학력사항</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>학력사항</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:10%;">
	                        <col style="width:25%;">
	                        <col style="width:20%;">
	                        <col style="width:15%;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">	 
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="acbg_se_nm"><span class="required">구분</span></th>
	                            <th id="acbg_schl_nm"><span class="required">학교명</span></th>
	                            <th id="acbg_mjr_nm">전공</th>
	                            <th id="acbg_grdtn_yr">졸업년도</th>
	                            <th id="acbg_err">검증</th>
	                            <th><button class="icon btn_bg blue addRowBtn" ><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="acbgTb">
	                    	<c:forEach items="${acbgList}" var="acbg" varStatus="status">
	                    		<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
	                                	<input type="text" id="acbg_se_nm${index}" value="${acbg.acbg_se_nm}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="text" id="acbg_schl_nm${index}" value="${acbg.acbg_schl_nm}" onblur="checkAllEmptyData()">    
		                            </td>
		                            <td>
		                                <input type="text" id="acbg_mjr_nm${index}" value="${acbg.acbg_mjr_nm}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="number" id="acbg_grdtn_yr${index}" value="${acbg.acbg_grdtn_yr}" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                            	<input type="text"  class="errDsctn" id="acbg_err${index}" readonly value="">
		                            </td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(acbgList) eq 0 }">
		                        <tr>
		                            <td>1</td>
		                            <td><input type="text" id="acbg_se_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="acbg_schl_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="acbg_mjr_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="number" id="acbg_grdtn_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="text" class="errDsctn" id="acbg_err1" readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>경력사항</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>경력사항</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:25%;">
	                        <col style="width:25%;">
	                        <col style="width:10%;">
	                        <col style="width:10%;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="crr_ogdp_nm"><span class="required">소속</span></th>
	                            <th id="crr_jbttl_nm">직책</th>
	                            <th id="crr_bgng_yr">시작연도</th>
	                            <th id="crr_bgng_yr">종료연도</th>
	                            <th id="crr_err">검증</th>
	                            <th><button class="icon btn_bg blue addRowBtn"><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="crrTb">
	                    	<c:forEach items="${crrList}" var="crr" varStatus="status">
	                    		<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
	                                	<input type="text" id="crr_ogdp_nm${index}" value="${crr.crr_ogdp_nm}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="text" id="crr_jbttl_nm${index}" value="${crr.crr_jbttl_nm}" onblur="checkAllEmptyData()">    
		                            </td>
		                            <td>
		                                <input type="number" id="crr_bgng_yr${index}" value="${crr.crr_bgng_yr}" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()">
		                            </td>
		                            <td><input type="number" id="crr_end_yr${index}" value="${crr.crr_end_yr }" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text"  class="errDsctn" readonly id="crr_err${index}" value=""></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(crrList) eq 0 }">
		                        <tr>
		                            <td>1</td>
		                            <td><input type="text" id="crr_ogdp_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="crr_jbttl_nm1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="number" id="crr_bgng_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="number" id="crr_end_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="text" class="errDsctn" id="crr_err1" readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>신바이오분류체계</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>신바이오분류체계</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:auto;">
	                        <col style="width:auto;">
	                        <col style="width:auto;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="bioClsf_lclsf_cd"><span class="required">대분류</span></th>
	                            <th id="bioClsf_mclsf_cd"><span class="required">중분류</span></th>
	                            <th id="bioClsf_sclsf_cd">소분류</th>
	                            <th>검증</th>
	                            <th><button class="icon btn_bg blue addRowSelBtn" ><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="bioClsfTb">
	                    	<c:forEach items="${bioClsfList}" var="bioClsf" varStatus="status">
	                    		<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
		                                <select id="bioClsf_lclsf_cd${index}" name="" class="select2 bioClsf_lclsf_cd">
                                            <option value=""></option>
                                            <c:forEach items="${bioLclsfCd}" var="lclsfCd" varStatus="status">
                                            	<option value="${lclsfCd.code_id}" <c:if test="${lclsfCd.code_id eq bioClsf.exprt_lclsf_cd}">selected</c:if>>
                                            		${lclsfCd.code_nm}
                                            	</option>
                                            </c:forEach>
                                        </select>
		                            </td>
		                            <td>
		                                <select id="bioClsf_mclsf_cd${index}" name="" class="select2 bioClsf_mclsf_cd">
	                                          <option value=""></option>
	                                          <c:forEach items="${bioMclsfCd}" var="mclsfCd" varStatus="status">
	                                          	<c:if test="${fn:contains(mclsfCd.code_id, bioClsf.exprt_lclsf_cd)}">
	                                            	<option value="${mclsfCd.code_id}" <c:if test="${mclsfCd.code_id eq bioClsf.exprt_mclsf_cd}">selected</c:if>>
	                                            		${mclsfCd.code_nm}
	                                            	</option>
	                                            </c:if>
                                            </c:forEach>
	                                    </select>
		                            </td>
		                            <td>
		                            	<select id="bioClsf_sclsf_cd${index}" name="" class="select2 bioClsf_sclsf_cd">
                                            <option value=""></option>
                                            <c:forEach items="${bioSclsfCd}" var="sclsfCd" varStatus="status">
                                            	<c:if test="${bioClsf.exprt_mclsf_cd ne null && fn:contains(sclsfCd.code_id, bioClsf.exprt_mclsf_cd)}">
	                                            	<option value="${sclsfCd.code_id}" <c:if test="${sclsfCd.code_id eq bioClsf.exprt_sclsf_cd}">selected</c:if>>
	                                            		${sclsfCd.code_nm}
	                                            	</option>
	                                            </c:if>
                                            </c:forEach>
                                        </select>
		                            </td>
		                            <td>
		                            	<input type="text"  class="errDsctn"  id="bioClsf_err${index}" readonly value="">
		                            </td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowSelBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(bioClsfList) eq 0}">
		                        <tr>
		                            <td>1</td>
		                            <td>
		                            	<select id="bioClsf_lclsf_cd1" name="" class="select2 bioClsf_lclsf_cd">
			                            	<option value=""></option>
			                            	<c:forEach items="${bioLclsfCd}" var="lclsfCd" varStatus="status">
	                                        	<option value="${lclsfCd.code_id}">
	                                        		${lclsfCd.code_nm}
	                                        	</option>
	                                        </c:forEach>
		                            	</select>
		                            </td>
		                            <td>
		                            	<select id="bioClsf_mclsf_cd1" name="" class="select2 bioClsf_mclsf_cd">
                                            <option value=""></option>
                                        </select>
		                            </td>
		                            <td>
		                            	<select id="bioClsf_sclsf_cd1" name="" class="select2 bioClsf_sclsf_cd">
                                            <option value=""></option>
                                        </select>
		                            </td>
		                            <td><input type="text" class="errDsctn" id="bioClsf_err1" readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowSelBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>정책분류체계</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>정책분류체계</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:auto;">
	                        <col style="width:auto;">
	                        <col style="width:auto;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="plcyClsf_lclsf_cd"><span class="required">대분류</span></th>
	                            <th id="plcyClsf_mclsf_cd"><span class="required">중분류</span></th>
	                            <th id="plcyClsf_sclsf_cd">소분류</th>
	                            <th>검증</th>
	                            <th><button class="icon btn_bg blue addRowSelBtn"><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="plcyClsfTb">
	                    	<c:forEach items="${plcyClsfList}" var="plcyClsf" varStatus="status">
	                    		<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
		                                <select id="plcyClsf_lclsf_cd${index}" name="" class="select2 plcyClsf_lclsf_cd">
                                            <option value=""></option>
                                            <c:forEach items="${plcyLclsfCd}" var="lclsfCd" varStatus="status">
                                            	<option value="${lclsfCd.clCode}" <c:if test="${lclsfCd.clCode eq plcyClsf.exprt_lclsf_cd}">selected</c:if>>
                                            		${lclsfCd.clCodeNm}
                                            	</option>
                                            </c:forEach>
                                        </select>
		                            </td>
		                            <td>
		                                <select id="plcyClsf_mclsf_cd${index}" name="" class="select2 plcyClsf_mclsf_cd">
	                                          <option value=""></option>
	                                          <c:forEach items="${plcyMclsfCd}" var="mclsfCd" varStatus="status">
	                                          	<c:if test="${fn:contains(mclsfCd.codeId, plcyClsf.exprt_lclsf_cd)}">
	                                            	<option value="${mclsfCd.codeId}" <c:if test="${mclsfCd.codeId eq plcyClsf.exprt_mclsf_cd}">selected</c:if>>
	                                            		${mclsfCd.codeIdNm}
	                                            	</option>
	                                            </c:if>
                                            </c:forEach>
	                                    </select>
		                            </td>
		                            <td>
		                            	<select id="plcyClsf_sclsf_cd${index}" name="" class="select2 plcyClsf_sclsf_cd">
                                            <option value=""></option>
                                            <c:forEach items="${plcySclsfCd}" var="sclsfCd" varStatus="status">
                                            	<c:if test="${plcyClsf.exprt_mclsf_cd ne null && fn:contains(sclsfCd.codeId, plcyClsf.exprt_mclsf_cd)}">
	                                            	<option value="${sclsfCd.code}" <c:if test="${sclsfCd.code eq plcyClsf.exprt_sclsf_cd}">selected</c:if>>
	                                            		${sclsfCd.codeNm}
	                                            	</option>
	                                            </c:if>
                                            </c:forEach>
                                        </select>
		                            </td>
		                            <td><input type="text" class="errDsctn" id="plcyClsf_err${index}" readonly value=""></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowSelBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(plcyClsfList) eq 0}">
		                        <tr>
		                            <td>1</td>
		                            <td>
		                            	<select id="plcyClsf_lclsf_cd1" name="" class="select2 plcyClsf_lclsf_cd">
                                            <option value=""></option>
                                            <c:forEach items="${plcyLclsfCd}" var="lclsfCd" varStatus="status">
                                            	<option value="${lclsfCd.clCode}">
                                            		${lclsfCd.clCodeNm}
                                            	</option>
                                            </c:forEach>
                                        </select>
		                            </td>
		                            <td>
		                            	<select id="plcyClsf_mclsf_cd1" name="" class="select2 plcyClsf_mclsf_cd">
                                            <option value=""></option>
                                        </select>
		                            </td>
		                            <td>
		                            	<select id="plcyClsf_sclsf_cd1" name="" class="select2 plcyClsf_sclsf_cd">
                                            <option value=""></option>
                                        </select>
		                            </td>
		                            <td><input type="text" class="errDsctn"  id="plcyClsf_err1" readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowSelBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>수상내역</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>수상내역</caption> 
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:50%;">
	                        <col style="width:20%;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="wnawd_dsctn">수상내역</span></th>
	                            <th id="wnawd_yr">수상연도</th>
	                            <th id="wnawd_err">검증</th>
	                            <th><button class="icon btn_bg blue addRowBtn"><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="wnawdTb">
	                    	<c:forEach items="${wnawdList}" var="wnawd" varStatus="status">
	                    	<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
	                                	<input type="text" id="wnawd_dsctn${index}" value="${wnawd.wnawd_dsctn}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td>
		                                <input type="number" id="wnawd_yr${index}" value="${wnawd.wnawd_yr}" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)" onblur="checkAllEmptyData()">    
		                            </td>
		                            <td><input type="text"  class="errDsctn" id="wnawd_err${index}" readonly value=""></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(wnawdList) eq 0 }">
		                        <tr>
		                            <td>1</td>
		                            <td><input type="text" id="wnawd_dsctn1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="number" id="wnawd_yr1" value="" onblur="checkAllEmptyData()" min="1000" max="9999" oninput="this.value=this.value.slice(0,4)"></td>
		                            <td><input type="text" class="errDsctn" id="wnawd_err1" readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
							</c:if>	                    	
	                    </tbody>
	                </table>
	            </div>
	            <div class="cnt_inner">
	                <div class="cnt_toolbar">
	                    <h2>센터 내 활용실적</h2>
	                </div>
	                <table class="tbl_style1 modify">
	                    <caption>센터 내 활용실적</caption>
	                    <colgroup>
	                        <col style="width:5%;">
	                        <col style="width:15%;">
	                        <col style="width:25%;">
	                        <col style="width:15%;">
	                        <col style="width:15%;">
	                        <col style="width:20%;">
	                        <col style="width:5%;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th>No</th>
	                            <th id="utlz_ymd"><span class="required">활용일자</span></th>
	                            <th id="utlz_prps"><span class="required">활용목적</span></th>
	                            <th id="info_exprtuser">활용자</th>
	                            <th id="utlz_rmrk_cn">비고</th>
	                            <th id="utlz_err">검증</th>
	                            <th><button class="icon btn_bg blue addRowBtn"><span class="blind">추가</span><span class="icon_plus"></span></button></th>
	                        </tr>
	                    </thead>
	                    <tbody id="utlzTb">
	                    	<c:forEach items="${utlzDsctnList}" var="utlz" varStatus="status">
	                    		<c:set var="index" value="${(status.index + 1)}"/>
		                        <tr>
		                            <td><span>${index}</span></td>
		                            <td>
		                            	<div class="input_box type_date">
	                                    	<input type="date" id="utlz_ymd${index}" data-placeholder="활용일자 선택" aria-required="true" title="활용일자 선택" value="${utlz.utlz_ymd}" max="9999-12-31" onblur="checkAllEmptyData()">
		                            	</div>
		                            </td>
		                            <td>
		                                <input type="text" id="utlz_prps${index}" value="${utlz.utlz_prps}" onblur="checkAllEmptyData()">    
		                            </td>
		                            <td>
		                                <input type="text" id="info_exprtuser${index}" value="${utlz.info_exprtuser}" onblur="checkAllEmptyData()">
		                            </td>
		                            <td><input type="text" id="utlz_rmrk_cn${index}" value="${utlz.utlz_rmrk_cn }" onblur="checkAllEmptyData()"></td>
		                            <td>
		                            	<input type="text"  class="errDsctn" id="utlz_err${index}" readonly value="">
		                            </td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:forEach>
	                    	<c:if test="${fn:length(utlzDsctnList) eq 0 }">
		                        <tr>
		                            <td>1</td>
		                            <td>
		                            	<div class="input_box type_date">
		                            		<input type="date" id="utlz_ymd1" data-placeholder="활용일자 선택" aria-required="true" title="활용일자 선택" value="" max="9999-12-31">
		                            	</div>
		                            </td>
		                            <td><input type="text" id="utlz_prps1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="info_exprtuser1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" id="utlz_rmrk_cn1" value="" onblur="checkAllEmptyData()"></td>
		                            <td><input type="text" class="errDsctn" id="utlz_err1"readonly></td>
		                            <td>
		                                <button class="icon btn_bg red deleteRowBtn"><span class="blind">삭제</span><span class="icon_x"></span></button>
		                            </td>
		                        </tr>
	                    	</c:if>
	                    </tbody>
	                </table>
	            </div>
	            <div class="txt_right mgt20">
	                <button type="button" class="btn btn-task-type2" onclick="saveTemp()">임시저장</button>
	                <button type="button" class="btn btn-task-type1" onclick="saveExpert()">저장</button>
	            </div>
	        </div>
	        </form>
	    </div> 
</body>
</html>