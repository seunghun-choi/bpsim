<%@page import="bpsim.module.dto.LoginInfoDTO"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="bpsim.framework.util.CommonUtil" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<%
	response.setDateHeader("Expires",0);
	LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
	request.setAttribute("loginInfo", loginInfo);
%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Security-Policy" content="default-src 'self' 'unsafe-inline' data: gap: * 'unsafe-eval'; style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/variable/pretendardvariable-dynamic-subset.min.css; script-src 'self' 'unsafe-inline' 'unsafe-eval';" />
<meta name="robots" content="index,follow"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,user-scalable=yes">
<meta name="google-site-verification" content="ox2FtXFD88jc5GIVu6xUvimpMt4v8kr6UnWEJy43SLs" />
<meta name="title" content="bpsim, 바이오인, bpsim, 생명공학포털"/>
<meta name="description"  content='bpsim 미래를 선도하는 정보 인프라'  />
<meta name="keywords" content="bpsim, 바이오인, bpsim, 생명공학포털, 생명공학, 생명과학, 바이오, 바이오뉴스, 바이오 동향, 바이오 연구, 바이오분야, 바이오인컬렉션, bpsim컬렉션, bpsim Collection,bpsim책자, bpsim주제, bpsim 연구분야, 바이오 연구성과, 노화, 오가노이드, 미래유망기술, 합성생물학, 바이오 빅데이터, 디지털 치료제, 코로나19, 마이크로바이옴, 줄기세포, 유전자편집기술, 백신, 감염병"/>
<c:set var="news_keywords" value="bpsim R&D, bpsim산업보고서, bpsim 전문가, bio시장, 바이오시장, 바이오산업, bpsimwatch, bpsimdustry, bpsimpro, bpsimglobal, bpsimregulation, bpsim4P"/>
<c:forEach items="${trendKwrdList }" var="kwrd" varStatus="vs">
	<c:set var="news_keywords" value="${news_keywords}, ${kwrd.keyword}"/>
</c:forEach>
<meta name="news_keywords" content="${news_keywords}" />
<meta property="og:title" content="bpsim, 바이오인, bpsim, 생명공학포털" />
<meta property="og:description" content='bpsim 미래를 선도하는 정보 인프라' />
<meta property="og:image" content="https://www.bpsim.or.kr/images/layout/logo.png"/>
<meta property="og:url" content="https://www.bpsim.or.kr"/>
<title>bpsim, 바이오인, bpsim, 생명공학포털</title>

<!-- css -->
<link rel="stylesheet" type="text/css" href="/css/common.css?v=240612" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/layout.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/main.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/board.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/jquery.mCustomScrollbar.min.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/slick.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/css/jquery-ui.css" charset="utf-8" />

<!--스크립트 변경-->
<script src="/js/jquery-3.6.0.min.js"></script>
<!--스크립트 추가-->
<script src="/js/jquery-ui.min.js"></script>

<script type="text/javascript" src="/js/slick.min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/menu.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/main.js"></script>
<script type="text/javascript" src="/js/ScrollMagic.min.js" charset="utf-8"></script>

<link rel="stylesheet" as="style" crossorigin href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/variable/pretendardvariable-dynamic-subset.min.css" />

<style type="text/css">
	.layer-popup {display:none;position:absolute;left:50%;top: 290px;z-index:400;padding:10px;margin-left:-235px;background-color:#fff;border:1px solid #000;}
</style>
<script>
	var old_num = 9; //랜덤 이미지를 위한 old_num
	var ran_img = ""; //랜덤 이미지를 위한 변수
	
	//지식 마우스 오버 이벤트
	$(document).ready(function(){
		$(".knowCircle").hover(function(){
			var name = $(this).attr('id') + "_div"; //보여줄 div 이름
			$(".know_div").hide(); //전체 hide
			$("#"+name).show(); //선택한 div만 show
		}, function(){});
		
		
		/***** 팝업  *****/
		var w_size = 0;
		// 윈도우 크기 변할때 팝업
		$(window).resize(function() {
			if ( w_size != document.documentElement.clientWidth){
				var obj = $('#pop_point').offset();

				w_size = document.documentElement.clientWidth;

				<c:forEach items="${popupList}" var="popup" varStatus="vs">
				
					if (getCookie('bio_pop_${popup.seq}') != '1') {
						openLayer('bio_pop_${popup.seq}', '${popup.template }','${popup.linkurl}');
					}
					
				</c:forEach>
			}
		});
		setTimeout('mainPopup()', 500);
		//메인 레이어팝업 닫기
		$(".layer_close").click(function () {
			$('#layer_block').hide();
		});
	});
	
	//주간, 월간 변경
	function clickTopList(cmd){
		//주간, 월간 버튼 토글
		var name = cmd+"_btn";
		$(".weekly").children("li").removeClass();
		$("#"+name).addClass('active');
		
		var rank_type = "";
		if(cmd == "week"){
			rank_type = "0";
		}else{
			rank_type = "1";
		}
		
		var html = "";
		var rk_class = "";
		var rk_num = 0;
		$.ajax({
			url: "/getTopList.do",
			type: "post",
			data: {rank_type : rank_type},
			async:false,
			success: function(data){
				var list = data['list'];
				$.each(list,function(i, item){
					html += "<div class='slick-slide'><div><li style='width:100%;'><a href="+item['url']+"><em class=";
					if(i == 0){
						html += "'rank1'";
					}else{
						html += "'rank2'";
					}
					html += ">"+(i+1)+"</em><span class='ellipsis_2'>"+item['title']+"</span>";
					if(item['rk'] == 'new'){
						rk_class = "num1";
						rk_num = item['rk'];
					}else if(item['rk'] > 0){
						rk_class = "num1"
						rk_num = item['rk'];
					}else if(item['rk'] < 0){
						rk_class = "num2"
						rk_num = item['rk'] * -1;
					}else{
						rk_class = "num3";
						rk_num = "";
					}
					html += "<span class='"+rk_class+"'>"+rk_num+"</span></a></li></div></div>";
				});
			},
			error: function(error){
				console.log(error);
			}
		});
		
		$(".top_slide").slick('slickRemove', null, null, true); //기존 슬라이드 삭제
		$('.top_slide').slick('slickAdd',html); //슬라이드 추가
	}
	
	//최신뉴스&동향 더보기
	function moreClick(){
		moreNewsClick();
		moreBtClick();
	}
	
	//최신 뉴스 더보기
	function moreNewsClick(){
		var news_cnt = $(".news_lst").children('li').length + 1;
		var news_html = "";
		var m_level = '${m_level}';
		var head_news_sn = $("#head_news_sn").val();
		
		if(news_cnt < 35){
			$.ajax({
				url: '/getMoreNews.do',
				type: 'post',
				data: {news_cnt : news_cnt, head_news : head_news_sn},
				async:false,
				success: function(data){
					var list = data['list'];
					var link = "";
					var blank = "";
					var newicon = "";
					
					var today = new Date();
					var year = today.getFullYear();
					var month = ('0' + (today.getMonth() + 1)).slice(-2);
					var day = ('0' + today.getDate()).slice(-2);
					var dateString = year + '-' + month  + '-' + day;
					
					$.each(list,function(i, item){
						link = "";
						newicon = "";
						
						if(m_level > 80 && m_level != 92 ){
							link = '/hbrd_News.do?num=' + item['hbrd_news_sn'] + '&cmd=view&bid=' + item['hbrd_se'];
						}else{
							link = item['cn_glan'];
							blank = "target='_blank'";
						}
						if(item['rgsde'] == dateString){
							newicon = '<i class=\"xi-new\"><span class=\"sr_only\">새글</span></i>';
						}
						
						news_html += "<li><a href=\"javascript:clickUrl('"+link+"','"+item["hbrd_news_sn"]+"')\">"+newicon+item['hbrd_news_sj'];
						news_html += "<span class='days'><i class='xi-time-o' aria-hidden='true'></i>"+item['rgsde']+"</span></a>";
					});
					
				},
				error: function(error){
					console.log(error);
				}
			});
			
			$(".news_lst").append(news_html);
		}
	}
	//최신 동향 더보기
	function moreBtClick(){
		var bt_cnt = $(".bt_lst").children('li').length + 1;
		var bt_html = "";
		var origin = "";
		if(bt_cnt < 13){
		$.ajax({
			url: '/getMoreBt.do',
			type: 'post',
			data: {bt_cnt : bt_cnt},
			async:false,
			success: function(data){
				var list = data['list'];
				$.each(list,function(i,item){
					bt_html += "<li><a href='/board.do?num="+item['seq']+"&cmd=view&bid="+item['bid']+"'><div class='txt'>";
					bt_html += "<em class='"+item['bid']+"'><i><img src='../images/main/"+item['bid']+".png' alt=''></i>"+item['bname']+"</em><strong class='ellipsis_2'>"+item['title']+"</strong>";
					origin = item['origin'];
					/*
					if(item['origin'].length > 5){
						origin = origin.substring(0,5) + "...";
					}
					*/
					bt_html += "<ul class='source'><li>"+item['regdate']+"</li><li>"+origin+"</li></ul></div>";
					var randomImg = randomImg_name(item['bid']);
					bt_html += "<span class='img'><img src='/InnoDS/data/upload/"+item['bid']+"/"+item['imgfile']+"' alt='"+item['title']+"' onError=\"this.onerror=null;this.src='/images/main/"+randomImg+"';\"></span></a></li>";
				});
			},
			error: function(error){
				console.log(error);
			}
		});
		
		$(".bt_lst").append(bt_html);
		}
	}
	
	/***************************************************************************
	* Function Name : getCookie
	* Desc          : 쿠기가져오기
	***************************************************************************/	
	function getCookie(name) {

		var nameOfCookie = name + "=";
		cookiedata = document.cookie;
		var result = 1;
		if(cookiedata.indexOf(nameOfCookie) < 0){
			result = 0;
		}
		return result;
	}

	/***************************************************************************
	* Function Name : mainPopup
	* Desc          : 팝업
	***************************************************************************/	
	function mainPopup(){
		var obj = $('#pop_point').offset();

		<c:forEach items="${popupList}" var="popup" varStatus="vs">
		if (getCookie('bio_pop_${popup.seq}') != '1') {
			openLayer('bio_pop_${popup.seq}', '${popup.template }','${popup.linkurl}');
		}
		</c:forEach>
	}

	/***************************************************************************
	* Function Name : openLayer
	* Desc          : 팝업
	***************************************************************************/	
	function openLayer(targetID, template, linkurl){

		var $layer = $('#' +targetID);
		var marginLeft = 0;

		intLeft	= 0; // 2:left;
		intTop	= 0; // 2:top;
		
		$layer.hide();

		intLeft = 0;
		intTop = 0;

	 	if(!$layer.is(':visible')){
			$layer.css({'left':intLeft+'px','margin-left':marginLeft}).show();
			if(template == "in"){
				// 팝업안 링크
				$layer.find("img").attr("usemap","#Map");
			}else if(template == "all"){
				// 팝업전체링크
				$layer.find("img").attr("onclick","popupLink('"+linkurl+"')");
				$layer.find("img").css("cursor","pointer");
			}
		} 
	}
	
	/***************************************************************************
	* Function Name : randomImg
	* Desc          : 랜덤 이미지
	***************************************************************************/
	function randomImg(e){
		var num = Math.floor((Math.random() * 4));
		var filename = "r"+num+".png";
		$(e).attr('src', "/images/main/"+filename);
	}
	
	function randomImg_trend(e, bid){
		var num = Math.floor((Math.random() * 4))+1;
		var filename = bid+"_r"+num+".png";
		
		while(ran_img.indexOf(filename) > -1){
			num = Math.floor((Math.random() * 4))+1;
			filename = bid+"_r"+num+".png";
		}
		
		ran_img += filename+",";
		$(e).attr('src', "/images/main/"+filename);
	}
	
	function randomImg_name(bid){
		var num = Math.floor((Math.random() * 4))+1;
		while(num == old_num){
			num = Math.floor((Math.random() * 4))+1;
		}
		old_num = num;
		var filename = bid+"_r"+num+".png";
		return filename;
	}
	
	
	//조회수 증가 및 링크 이동
	function clickUrl(url, num, bid){
		
		window.open(url, '_blank');
		var m_level ="${m_level}";
		if(m_level < 80 || m_level == 92 || m_level  == "" || m_level  == null){
		
			$.ajax({
				url: "/mng/updateHbrdNewsRdcnt.do",
				type: "post",
				data: {num : num, bid : bid},
				dataType: "json",
				success: function(data){
					location.reload();
				},
				error: function(error){
					console.log(error);
				}
			});
		}
	}
	
	
</script>
</head>
<body class="start">
<!-- Accessibility -->
<div id="skipNav">
	<a href="#contbox">본문으로 바로가기</a>
</div>

<div id="wrap">
	<!--header -->
<!--     <header id="header"> -->
<%--     	<%@ include file = "/include/usr/header/header_top.jsp"%> --%>
<!-- 		<div class="menu_bar"> -->
<!-- 			<div class="container"> -->
<!-- 			<h1 class="logo"><a href="/index.do"><span class="sr-only">바이오인</span></a></h1> -->
<!-- 			<!-- nav --> -->
<%-- 			<%@ include file = "/include/usr/nav_bar.jsp"%> --%>
<!-- 			<!--// nav--> -->
<%-- 			<%@ include file = "/include/usr/search_bar.jsp"%> --%>
<!-- 		</div> -->
<!-- 	</header> -->
	<!-- //header -->
	
	<!--quickmenu-->
<%-- 	<%@ include file = "/include/usr/quick_menu.jsp"%> --%>
	<!--//quickmenu-->
	
	<!-- main -->
	<main id="main">
		<div class="contents_body">
			<section class="section1">
				<h2 class="sr-only">영역1</h2>
				<div class="container">
					<div class="box1">
						<div class="inner">
						<c:forEach items="${knowList}" var="know" varStatus="vs">
							<div id="${know.bid }_div" class="know_div" <c:if test="${vs.index ne 0 }">style="display:none;"</c:if>> 
				              <em class="color1" style="color:#0272F3;">${know.bname }</em>
				              <a href="/board.do?bid=${know.bid }&cmd=view&num=${know.seq}">
				              <strong class="tit ellipsis_2" title="${know.title }">${know.title }</strong>
				              <%-- <c:choose>
				              <c:when test="${m_level > 0}"><span class="depth ellipsis_2">${know.contents }</span></c:when>
				              <c:otherwise><span class="depth ellipsis_2">${know.details }</span></c:otherwise>
				              </c:choose> --%>
				              </a>
				          </div>
						</c:forEach>
			              <ul class="list">
			                <li class="knowCircle" id="issue">
			                  <a href="/board.do?bid=issue">
			                    <span class="img"></span>
			                    <span class="txt">bpsimwatch</span>
			                  </a>
			                </li>
			                <li class="knowCircle" id="watch">
			                  <a href="/board.do?bid=watch">
			                    <span class="img"></span>
			                    <span class="txt">bpsimdustry</span>
			                  </a>
			                </li>
			                <li class="knowCircle" id="report">
			                  <a href="/board.do?bid=report">
			                    <span class="img"></span>
			                    <span class="txt">bpsimpro</span>
			                  </a>
			                </li>
			                <li class="knowCircle" id="global">
			                  <a href="/board.do?bid=global">
			                    <span class="img"></span>
			                    <span class="txt">bpsimglobal</span>
			                  </a>
			                </li>
			                <li class="knowCircle" id="regulation">
			                  <a href="/board.do?bid=regulation&mid=news">
			                    <span class="img"></span>
			                    <span class="txt">bpsimregulation</span>
			                  </a>
			                </li>
			                <li class="knowCircle" id="four">
			                  <a href="/board.do?bid=four">
			                    <span class="img"></span>
			                    <span class="txt">bpsim4P</span>
			                  </a>
			                </li>
			              </ul>
			              <a href="/futureTech.do" class="quick_tech">
			                <span class="character"><img src="/images/main/character.png" alt=""></span>
			                <span class="wheel"><img src="/images/main/wheel.png" alt=""></span>
			                <p class="ballon"><span>미래유망기술 바로가기</span></p>
			              </a>
			            </div>
					</div>
					
					<div class="box2">
						<div class="inner">
							<ul class="wordtype">
								<c:forEach items="${trendKwrdList }" var="kwrd" varStatus="vs">
									<li class="style${vs.count }"><a href="/search.do?searchTerm=${kwrd.keyword}&originSearchTerm=${kwrd.keyword}&type=all&searchType=mainkeyword" >${kwrd.keyword }</a></li>
								</c:forEach>
							</ul>
						
							<div class="motion tree">
							  <img src="../images/main/treebg.png" alt="인기 검색어" usemap="#top100">
							  <map name="top100">
							      <area shape="circle" coords="144,284,34" href="javascript:void(0)" class="sch_100">
							  </map>
							</div>
				            <div class="motion treebg1">
				                <img src="../images/main/treebg1.png" alt="">
				            </div>
				            <div class="motion treebg2">
				                <img src="../images/main/treebg2.png" alt="">
				            </div>
				            <div class="motion treebg3">
				                <img src="../images/main/treebg3.png" alt="">
				            </div>
				            <div class="motion treebg4">
				                <img src="../images/main/treebg4.png" alt="">
				            </div>
				            <div class="motion treebg5">
				                <img src="../images/main/treebg5.png" alt="">
				            </div>
				            <div class="motion treebg6">
				                <img src="../images/main/treebg6.png" alt="">
				            </div>
				            <div class="motion treebg7">
				                <img src="../images/main/treebg7.png" alt="">
				            </div>
				            <div class="motion treebg8">
				                <img src="../images/main/treebg7.png" alt="">
				            </div>
				            <div class="motion treebg9">
				                <img src="../images/main/treebg7.png" alt="">
				            </div>
			            </div>
					</div>
					
					<div class="box3">
						<div class="inner">
							<ul class="banner_slide">
							<c:if test="${not empty focusList}">
							<c:forEach items="${focusList}" var="focus" varStatus="vs">
								<li <c:if test="${vs.index ne 0 }">style="display:none;"</c:if>>
									<a href="${focus.link_addr}" title="${focus.title }">
										<span class="img"><img src="/InnoDS/data/upload/mainheadline/${focus.filename }" alt="이미지"></span>
										<div class="txt">
											<em>${focus.bname }</em>
											<strong class="ellipsis_2">${focus.title}</strong>
											<span class="ellipsis_2"><tf:htmlTagDel trim="true">${focus.contents }</tf:htmlTagDel></span>
										</div>
									</a>
								</li>
							</c:forEach>
							</c:if>
							</ul>
			                <div class="arrow">
				                <button type="button" name="button" class="prev slick-arrow" style=""><i class="xi-angle-left-min" aria-hidden="true"></i><span class="sr-only">이전으로</span></button>
				                <button type="button" name="button" class="pause"><i class="xi-pause" aria-hidden="true"></i><span class="sr-only">정지</span></button>
				                <button type="button" name="button" class="start"><i class="xi-play" aria-hidden="true"></i><span class="sr-only">재생</span></button>
				                <button type="button" name="button" class="next slick-arrow" style=""><i class="xi-angle-right-min" aria-hidden="true"></i><span class="sr-only">다음으로</span></button>
			                </div>
						</div>
					</div>
				</div>
			</section>
			
			<section class="section2">
				<div class="container">
					<div class="news_wrap1">
						<p class="tit">최신뉴스</p>
						<div class="inner">
							<input type="hidden" value="${headNews.hbrd_news_sn }" id="head_news_sn"/>
							<c:choose>
							<c:when test="${m_level > 80 && m_level ne 92  }"><c:set value="/hbrd_News.do?num=${headNews.hbrd_news_sn }&cmd=view&bid=${headNews.hbrd_se }" var="news_link"/></c:when>
							<c:otherwise><c:set value="${headNews.cn_glan }" var="news_link"/></c:otherwise>
							</c:choose>
							<a href="javascript:clickUrl('${news_link }','${headNews.hbrd_news_sn }','${headNews.hbrd_se }')" class="thumb" <c:if test="${m_level == 0 || m_level == '' || m_level == null  }"></c:if>>
				                <div class="conttxt">
				                  <c:set var="regdate" value="${fn:substring(headNews.rgsde, 0, 10)}" scope="request"/>
				                  <strong class="ellipsis_2"><%= CommonUtil.new_icon((String)request.getAttribute("regdate"))%>${headNews.hbrd_news_sj }</strong> <!-- 0526new 추가 -->
				                  <span>${headNews.hbrd_news_cn }</span>
				                  <!-- 0526출처추가 -->
				                  <ul class="source">
				                  	<li>${headNews.origin }</li>
				                  	<li>${fn:substring(headNews.rgsde,0,10) }</li>
				                  </ul>
				                </div>
				                <span class="img"><img src="/InnoDS/data/upload/${headNews.hbrd_se}/${headNews.filename}" alt="${headNews.hbrd_news_sj }" class="rImg" onError="randomImg(this)" loading="lazy"></span>
							</a>
							<ul class="news_lst">
								<c:forEach items="${todaynewsList }" var="tonews" varStatus="vs">
									<c:choose>
									<c:when test="${m_level > 80 && m_level ne 92  }"><c:set value="/hbrd_News.do?num=${tonews.hbrd_news_sn }&cmd=view&bid=${tonews.hbrd_se }" var="news_list_link"/></c:when>
									<c:otherwise><c:set value="${tonews.cn_glan }" var="news_list_link"/></c:otherwise>
									</c:choose>
									<li> 
					                  <!-- 0526new 추가 --> 
					                  <c:set var="regdate" value="${fn:substring(tonews.rgsde, 0, 10)}" scope="request"/>
					                  <a href="javascript:clickUrl('${news_list_link }','${tonews.hbrd_news_sn }','${tonews.hbrd_se }')"><%= CommonUtil.new_icon((String)request.getAttribute("regdate"))%>${tonews.hbrd_news_sj }
					                  <span class="days"><i class="xi-time-o" aria-hidden="true"></i>${fn:substring(tonews.rgsde,0,10) }</span></a>
					                </li>
								</c:forEach>
							</ul>
						</div>
						<a href="/hbrd_News.do?bid=todaynews" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
					</div>
					<a href="javascript:moreNewsClick();" class="display">최신뉴스 더보기<i class="xi-angle-down-thin"></i></a>
					<div class="news_wrap2">
						<p class="tit">최신동향</p>
						<ul class="bt_lst">
							<c:forEach items="${trendListnew }" var="trend">
							<li>
				                <a href="/board.do?num=${trend.seq }&cmd=view&bid=${trend.bid}">
				                  <div class="txt">
				                  	<em class="${trend.bid}"><i><img src="../images/main/${trend.bid }.png" alt=""></i>${trend.bname }</em>
				                    <strong class="ellipsis_2">${trend.title }</strong>
				                    <ul class="source">
				                      <li>${fn:substring(trend.regdate,0,10) }</li>
				                      <li><c:if test="${trend.origin ne 'null' }">${trend.origin }</c:if></li>
				                    </ul>
				                  </div>
				                  <span class="img"><img src="/InnoDS/data/upload/${trend.bid }/${trend.imgFile}" alt="${trend.title }" class="rImg" onError="randomImg_trend(this,'${trend.bid }')" loading="lazy"></span>
				                </a>
							</li>
							</c:forEach>
						</ul>
						<a href="/board.do?bid=tot_trend" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
					</div>
					<a href="javascript:moreBtClick();" class="display">동향 더보기<i class="xi-angle-down-thin"></i></a>
					 <div class="news_banner">
					 	<div class="in">
					 		<a href="/hbrd_News.do?bid=overseanews"><strong class="tit2">해외뉴스</strong></a>
					 		<div class="news_slide">
					 			<c:forEach items="${overseanewsList }" var="ovnews">
					 				<c:choose>
									<c:when test="${(m_level > 80 && m_level ne 92) || (fn:trim(ovnews.cn_glan) eq '' || ovnews.cn_glan eq null) }"><c:set value="/hbrd_News.do?num=${ovnews.hbrd_news_sn }&cmd=view&bid=${ovnews.hbrd_se }" var="ovnews_link"/></c:when>
									<c:otherwise><c:set value="${ovnews.cn_glan }" var="ovnews_link"/></c:otherwise>
									</c:choose>
					 				<a href="${ovnews_link }" <c:if test="${m_level == 0 || m_level == '' || m_level == null  }">target="_blank"</c:if>>
							 			<div class="nslide">
						                  <span class="text">${ovnews.hbrd_news_sj }</span>
						                  <em class="days"><i class="xi-calendar-remove" aria-hidden="true"></i>${fn:substring(ovnews.rgsde,0,10) }</em>
						                </div>
					                </a>
					 			</c:forEach>
					 		</div>
					 		<div class="arrow">
				                <button type="button" name="button" class="prev slick-arrow" style=""><i class="xi-angle-left-min" aria-hidden="true"></i><span class="sr-only">이전으로</span></button>
				                <button type="button" name="button" class="pause"><i class="xi-pause" aria-hidden="true"></i><span class="sr-only">정지</span></button>
				                <button type="button" name="button" class="start"><i class="xi-play" aria-hidden="true"></i><span class="sr-only">재생</span></button>
				                <button type="button" name="button" class="next slick-arrow" style=""><i class="xi-angle-right-min" aria-hidden="true"></i><span class="sr-only">다음으로</span></button>
					 		</div>
					 	</div>
					 </div>
					 <div class="morebutton">
			            <a href="javascript:moreClick();">최신뉴스 & 동향 더보기<i class="xi-angle-down-thin"></i></a>
					 </div>
				</div>
			</section>
			
			<section class="section3">
				<div class="container">
					<strong class="tit">Collection</strong>
					<span class="depth">이슈에 대한 게시물을 각 주제별로 보실 수 있습니다.</span>
					 <div class="c_slide">
					 	<c:forEach items="${nav_collection}" var="col">
						 	<div class="child">
				              <a href="/collectView.do?bid=collection&collection_sn=${col.collection_sn }">
				                <span class="img"><img src="/InnoDS/data/upload/${col.file_nm}" alt="${col.collection_nm }" onError="randomImg(this)" loading="lazy"></span>
				                <div class="txt">
				                  <em>${col.collection_nm }</em>
				                  <strong class="ellipsis_2">${col.collection_sumry }</strong>
				                </div>
				              </a>
				            </div>
			            </c:forEach>
					 </div>
					 <div class="arrow">
			            <button type="button" name="button" class="prev slick-arrow"><i class="xi-long-arrow-left" aria-hidden="true"></i><span class="sr-only">이전으로</span></button>
			            <button type="button" name="button" class="next slick-arrow"><i class="xi-long-arrow-right" aria-hidden="true"></i><span class="sr-only">다음으로</span></button>
					 </div>
				</div>
			</section>
			
			<section class="section4">
				<div class="container">
					<article class="arti1">
						<div class="inner">
							<p class="tit"><strong>행사·교육</strong></p>
							<div class="txt">
								<a href="/EventEdc.do?num=${EventEdcList[0].event_edc_sn }&cmd=view&bid=semina">
								<div class="thumb">
				                  <p class="tt">
				                  <c:if test="${event_title2 eq null || event_title2 eq '' }">
				                    ${EventEdcList[0].event_nm }
				                  </c:if>
				                  <c:if test="${event_title2 ne null && event_title2 ne '' }">
				                  ${event_title1 }<br/>${event_title2 }
				                  </c:if>
				                  </p>
				                  <ul>
				                    <li>
				                      <em>기간</em>
				                      <span>${EventEdcList[0].event_date }</span>
				                    </li>
				                    <li>
				                      <em>장소</em>
				                      <span>${EventEdcList[0].event_place }</span>
				                    </li>
				                  </ul>
				                </div>
				                </a>
								<ul class="list">
								<c:forEach items="${EventEdcList }" var="event" varStatus="vs">
									<c:if test="${vs.index ne 0 }">
										<li>
						                    <em class="num">${event.month }</em>
						                    <a href="/EventEdc.do?num=${event.event_edc_sn }&cmd=view&bid=semina">
						                      <span class="day">${event.event_date }</span>
						                      <strong>${event.event_nm }</strong>
						                      <em class="source">${event.mngt_instt }</em>
						                    </a>
					                	</li>
									</c:if>
								</c:forEach>
								</ul>
				                <a href="/EventEdc.do?bid=semina" class="more2">행사·교육·세미나로 이동하기<i class="xi-angle-right-min"></i></a>
							</div>
							<a href="/EventEdc.do?bid=semina" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
						</div>
					</article>
					
					<article class="arti2">
						<div class="inner2">
							<p class="tit"><strong>부처연구성과</strong></p>
							<div class="txt">
								<ul>
									<c:forEach items="${rsltnewsList }" var="rslt">
										<li>
						                    <a href="/rsrch_Rslt.do?num=${rslt.rsrch_rslt_sn }&cmd=view&bid=research" class="cont">
						                      <strong class="ellipsis">${rslt.rslt_nm }
						                      <c:set var="rgsde" value="${fn:substring(rslt.rgsde, 0, 10)}" scope="request"/>
											  <%= CommonUtil.new_icon((String)request.getAttribute("rgsde"))%> 
						                      </strong>
						                      <span class="ellipsis_2">${rslt.core_cn }</span>
						                      <p class="source">
						                        <span>${rgsde}</span>
						                        <em>${rslt.sport_instt }</em>
						                      </p>
						                    </a>
										</li>
									</c:forEach>
								</ul>
							</div>
							<a href="/rsrch_Rslt.do?bid=research" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
						</div>
						<div class="inner3">
							<p class="tit"><strong>차트+</strong></p>
							<div class="txt">
								<ul>
									<c:forEach items="${chartList }" var="chart">
										<li class="style${chart.meta_info_se }">
						                    <a href="/ChartInfoList.do?bid=chart_info&s_key=title&s_str=${chart.meta_info_sj }&meta_info_se=${chart.meta_info_se}" class="cont">
						                      <strong class="ellipsis">${chart.meta_info_sj }</strong>
						                      <p class="source">
						                        <span>${fn:substring(chart.rgsde, 0, 10)}</span>
						                        <c:if test="${chart.bid eq 'metaInfo'}">
						                        	<em>${chart.orginl_origin }</em>
						                        </c:if>
						                        <c:if test="${chart.bid ne 'metaInfo'}">
						                        	<em>${chart.bname }</em>
						                        </c:if>
						                      </p>
						                    </a>
										</li>
									</c:forEach>
								</ul>
							</div>
							<a href="/ChartInfoList.do?bid=chart_info" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
						</div>
					</article>
				</div>
			</section>
			
			<section class="section5">
				<div class="container">
					<article class="arti1">
			            <p class="tit">부처 보도자료</p>
			            <ul class="list">
			              <c:forEach items="${divisionListnew }" var="division">
				              <li>
				                <a href="/board.do?num=${division.seq }&cmd=view&bid=division">
				                  <em>${division.origin }</em>
				                  <div class="txt">
				                    <strong class="ellipsis_2">${division.title }</strong>
				                    <span class="days">${fn:substring(division.regdate,0,10) }</span>
				                  </div>
				                </a>
				              </li>
			              </c:forEach>
			            </ul>
			            <a href="/board.do?bid=division" class="more"><i class="xi-plus"></i><span class="sr-only">더보기</span></a>
					</article>
					<article class="arti2">
						<div class="newsletter">
			              <b>NEWS LETTER</b>
			              <span>바이오인의 다양하고 흥미로운 최신 소식을 <br>뉴스레터를 통해 확인 하실 수 있습니다.</span>
			              <div class="btn_wrap">
			                <a href="/NewsLetterList.do?bid=letter" class="type1"><span>뉴스레터 보기</span></a>
			                <a href="/newsletterRegist.do?bid=newsletter" class="type2"><span>신청하기</span></a>
			              </div>
			            </div>
			            <ul class="box_wrap">
			             	<li>
				                <a href="/bpsim.do?cmd=summary">
				                  <strong><img src="../images/main/boxtxt1.png" alt="바이오 규제 신문고"></strong>
				                  <span>more<i class="xi-plus-min"></i></span>
				                </a>
			             	</li>
			             	<li>
				                <a href="/bkletPblancList.do?bid=req_rep">
				                  <strong><img src="../images/main/boxtxt2.png" alt="책자신청"></strong>
				                  <span>more<i class="xi-plus-min"></i></span>
				                </a>
			             	</li>
			            </ul>
			            <div class="sns">
			            	<span>bpsim Social Media</span>
			            	<ul class="sns_wrap">
				                <li><a href="https://www.facebook.com/bpsimportal" target="_blank"><img src="../images/main/ico_facebook.png" alt=""></a></li>
				                <li><a href="https://twitter.com/bpsim_BT" target="_blank"><img src="../images/main/log_x.png" alt="" style="width: 35px;"></a></li>
				                <li><a href="https://blog.naver.com/bpsimportal" target="_blank"><img src="../images/main/ico_blog.png" alt=""></a></li>
			            	</ul>
			            </div>
					</article>
				</div>
			</section>
			
			<section class="section6">
		        <div class="container">
		          <div class="tit_wrap">
		            <p>인기글<strong>TOP10</strong></p>
		            <ul class="weekly">
		              <li class="active" id="week_btn"><a href="javascript:clickTopList('week');">주간</a></li>
		              <li id="month_btn"><a href="javascript:clickTopList('month');">월간</a></li>
		            </ul>
		          </div>
		          <ul class="top_slide">
		          	<c:forEach items="${weekList}" var="week" varStatus="vs">
			            <li class="week_li">
			              <a href="${week.url }">
			                <em <c:if test="${vs.index eq 0 }">class="rank1"</c:if> <c:if test="${vs.index ne 0 }">class="rank2"</c:if> >${vs.count }</em>
			                <span class="ellipsis_2">${week.title }</span>
			                <c:if test="${week.rk ne 'new' }">
			                	<span class="<c:if test="${week.rk eq 0 }">num3</c:if><c:if test="${week.rk > 0 }">num1</c:if><c:if test="${week.rk < 0 }">num2</c:if>"><c:if test="${week.rk < 0 }">${week.rk * -1 }</c:if><c:if test="${week.rk > 0 }">${week.rk}</c:if></span>
			              	</c:if>
			              	<c:if test="${week.rk eq 'new' }">
			              		<span class="num1">${week.rk }</span>
			              	</c:if>
			              </a>
			            </li>
		          	</c:forEach>
		          </ul>
		          <div class="arrow">
		            <button type="button" name="button" class="prev slick-arrow"><i class="xi-long-arrow-left" aria-hidden="true"></i><span class="sr-only">이전으로</span></button>
		            <button type="button" name="button" class="next slick-arrow"><i class="xi-long-arrow-right" aria-hidden="true"></i><span class="sr-only">다음으로</span></button>
		          </div>
		        </div>
			</section>
		</div>
		<!-- //contents_body -->
	</main>
	<!-- //main -->
	
	<c:forEach items="${popupList}" var="popup" varStatus="vs">
	<a href="${popup.linkurl}" target="${popup.target}">
		<div class="layer-popup" id="bio_pop_${popup.seq}">
			<c:import url="/mng/PopupLayView.do?seq=${popup.seq}"></c:import>
		</div>
	</a>
	</c:forEach>

	<!-- footer -->
<!-- 	<footer id="footer"> -->
<!-- 		<div class="container"> -->
<!-- 			<ul class="f_side"> -->
<!-- 		        <li><a href="/bpsim.do?cmd=greeting">센터소개</a></li> -->
<!-- 				<li><a href="/bpsim.do?cmd=privacy">개인정보 처리방침</a></li> -->
<!-- 				<li><a href="/bpsim.do?cmd=emailReject">이메일무단수집거부</a></li> -->
<!-- 				<li><a href="/bpsim.do?cmd=map">찾아오시는길</a></li> -->
<!-- 				<li><a href="/bpsim.do?cmd=rss">RSS</a></li> -->
<!-- 			</ul> -->
<!-- 			<address class="address"> -->
<!-- 		        <span>34141 대전광역시 유성구 과학로 125(어은동) 전화 : 042-879-8388, 8377 관리자 : bpsim@kribb.re.kr </span><span>(C) 2022 BY NATIONAL BIOTECH POLICY RESEARCH CENTER. ALL RIGHTS RESERVED.  -->
<%-- 					
<%-- 					<c:if test="${loginInfo.level > 80 && not empty loginInfo.level}"> --%>
<%-- 					<a href="/mng/main.do" target="_blank"> --%>
<%-- 						<img src="/images/common/footer_admin.gif" alt="관리자모드가기" /> --%>
<%-- 					</a> --%>
<%-- 					</c:if> --%>
<%-- 					 --%> --%>
<!-- 				</span> -->
<!-- 			</address> -->
<!-- 			<a href="/index.do" class="f_logo" target="_blank" title="국가생명공학정책연구센터 새창열림"><img src="../images/layout/footer_logo.png" alt="국가생명공학정책연구센터"></a> -->
<!-- 		</div> -->
<!-- 	</footer> -->
	<!-- //footer -->
</div>
<!-- //wrap -->
</body>
</html>