<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="bpsim.framework.util.CateUtil,bpsim.framework.util.CommonUtil" %>
<%@ page import="bpsim.framework.util.ReqUtils" %>
<%@ page import="bpsim.module.dto.LoginInfoDTO" %>
<script type="text/javascript">

	function fn_openPopup(url){
		var opts = "width=800,height=600,left=500,top=300,scrollbars=yes";
		window.open(url, 'pop', opts);
		return false;
	}
</script>
<div id="wrap">
	<!--header -->
    <header id="header">
<%-- 	<%@ include file = "/include/usr/header/all_menu.jsp"%> --%>
 
<!-- 	<p id="skip"><a href="#content">본문 바로가기</a></p> -->
	
		<%@ include file = "/include/usr/header/header_top.jsp"%>
		<div class="menu_bar">
			<div class="container">
			<h1 class="logo"><a href="/index.do"><span class="sr-only">바이오인</span></a></h1>
			
			<!-- nav -->
			<%@ include file = "/include/usr/nav_bar.jsp"%>
			<!--// nav-->
			<%@ include file = "/include/usr/search_bar.jsp"%>
			<script type="text/javascript"> 
			// 메인 페이지 롤링
			var top_report_timer;
			var top_idx = 0;
			$(function(){
				setTopReportTimer();
				$(".top_report").mouseover(function(){
			        clearTimeout(top_report_timer);
			    });
			    $(".top_report").mouseout(function(){
			    	setTopReportTimer();
			    });     
			});

			function setTopReportTimer(){
				top_report_timer = setInterval(function(){
					if(top_idx >= ($(".top_report").length - 1)) top_idx = 0;
					else top_idx++;
					$(".top_report").css("display", "none");
					$("#top_report_"+top_idx).css("display", "block");
				}, 5000);
			}
			
			/***************************************************************************
			* Function Name : goClltSearch
			* Desc          : 컬렉션 더보기 셀렉트박스 체인지 이벤트
			***************************************************************************/	
			function goClltSearch(obj){
				var sn = obj.value;
				if(sn != ""){
					location.href = "/collectView.do?bid=collection&collection_sn="+sn;
				}
			}
			</script>
			
			<c:forEach items="${centerList}" var="center" varStatus="state"> 
				<div id="top_report_${state.index }" class="top_report" style="<c:if test="${!state.first }">display:none;</c:if>">
					<div class="top_report_text">
						<img src="/images/common/top_report_tit.gif" alt="센터발간자료" />
						<a href="/board.do?num=${center.seq}&cmd=view&bid=${center.bid}">${center.title}</a>
					</div>
					<div class="top_report_img">
						<span>
							<img src="/InnoDS/data/upload/${center.bid}/${center.imgFile}" alt="${center.title}"  style="width:50px; height:62px; "/>
						</span>
					</div>
				</div>
			</c:forEach>
		</div>
		
		</div>
		</header>
	<!-- //header -->
	
	<!--quickmenu-->
	<%@ include file = "/include/usr/quick_menu.jsp"%>
	<!--//quickmenu-->
	
<main id="main">
<div class="main_wrap">

	<!-- 메뉴 네비 -->
	<c:if test="${bid ne 'future'}">
	<div class="location">
	  <div class="container">
	    <ul class="path">
	      <li class="icon"><a href="/index.do"><i class="xi-home-o"></i><span class="sr-only">홈</span></a></li>
	      <!-- 상단 네비 -->
	      <%@ include file = "/include/usr/location.jsp"%>
	    </ul>
	    <c:if test="${bid eq 'collection'}">
	    <c:set var="cmd" value="view" scope="request" />
	    <%@ include file = "/include/usr/contents_top.jsp"%>
	    <c:set var="cmd" value="" scope="request" />
	    </c:if>
	  </div>
	</div>
	</c:if>
	<!-- collection -->
<c:if test="${bid eq 'collection'}">
      <div class="snb_visual" style="background-image: url(/InnoDS/data/upload/${collection.file_nm});">
        <div class="snb_tit_wrap">
          <h2>${collection.collection_nm }</h2>
          <span class="depth">${collection.collection_sumry }</span>
          <a href="javascript:openClltDc();" class="more"><span>개념 및 동향 자세히 보기</span><i class="xi-external-link"></i>
          </a>
        </div>
        <div class="c_tab_wrap container">
          <ul>
          <c:if test="${fn:length(collection_list) gt 21}">
	          <c:forEach var="list" items="${collection_list}" end="20">
	          	<li <c:if test="${list.collection_sn eq cllt_sn}">class="active"</c:if>><a href="/collectView.do?bid=collection&collection_sn=${list.collection_sn}"><span title="${list.collection_nm}">${list.collection_nm}</span></a></li>
	          </c:forEach>
	          <li>
	          	<span class="form_select" style="display:block;">
	          		<select title="더보기" onchange="goClltSearch(this);">
	          			<option value="">주제 더보기</option>
	          			<c:forEach var="list" items="${collection_list}" begin="21">
	          				<option value="${list.collection_sn}" title="${list.collection_nm}" <c:if test="${list.collection_sn eq cllt_sn}">selected</c:if>>${fn:substring(list.collection_nm,0,10)}<c:if test="${fn:length(list.collection_nm)>10}">...</c:if></option>
	          			</c:forEach>
	          		</select>
	          	</span>
	          </li>
          </c:if>
          <c:if test="${fn:length(collection_list) le 20}">
           <c:forEach var="list" items="${collection_list}">
	          	<li <c:if test="${list.collection_sn eq cllt_sn}">class="active"</c:if>><a href="/collectView.do?bid=collection&collection_sn=${list.collection_sn}"><span title="${list.collection_nm}">${list.collection_nm}</span></a></li>
	          </c:forEach>
          </c:if>
          </ul>
        </div>
      </div>
      
</c:if>
	
<c:if test="${bid ne 'future' }">
	<!-- contents_body -->
	<div class="contents_body">
	<div class="container">
	
	<!-- 왼쪽 메뉴 collection 제외 -->
	<c:if test="${fn:indexOf(bid,'collection') lt 0 && cmd ne 'sitemap' && bid ne 'future'}">
	<nav id="snb">
	<%@ include file = "/include/usr/left_menu.jsp"%>
	</nav>
	</c:if>
	<!-- //왼쪽 메뉴 -->
	
	<!-- 컨텐츠 시작 -->
	<div id="contbox" <c:if test="${bid eq 'chart_info' || fn:indexOf(bid,'collection') ge 0 || cmd eq 'sitemap'}">class="conttype"</c:if>>
		<!-- 게시판명, 설명 -->
		<%@ include file = "/include/usr/contents_top.jsp"%>
</c:if>