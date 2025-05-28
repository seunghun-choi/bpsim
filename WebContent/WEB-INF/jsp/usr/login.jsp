<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>바이오분야별 전문인력정보조사 및 관리프로그램</title>
	<link rel="stylesheet" href="/css/basic.css">
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var msg = "${msg}";
			if(msg != null && msg != ''){
				alert(msg);
			}
		});

		
		var login = "${login}";
		if (login.length > 1) {
			location.href = "/expert/expertList.do";
		}
		
		
		function check() {
			f = document.loginForm;

			if (Js_isNull(f.userid.value)) {
				alert('아이디를 입력해주세요');
				f.userid.focus();
				return false;
			} else if (Js_isNull(f.passwd.value)) {
				alert('비밀번호를 입력해주세요');
				f.passwd.focus();
				return false;
			}
		}
			
		function Js_isNull( Jv_Value )
		{
			if(Jv_Value == 'undefined' || Jv_Value == null)
				{
					return false;
				}
			var Jv_chkstr = Jv_Value + "";
			var Jv_Result = true;

			if ( (Jv_chkstr == "") || (Jv_chkstr == null) )
			{
				return Jv_Result;
			}
			for ( jdx = 0; Jv_Result && (jdx < Jv_Value.length); jdx++ )
			{
				if ( Jv_Value.substring(jdx, jdx+1) != " " )
				{
					Jv_Result = false;
				}
			}
			return Jv_Result;
		}
	</script>
</head>
<body>
   
    <!-- Accessibility -->
	<div id="skipNav">
		<a href="#contbox">본문으로 바로가기</a>
	</div>

    <div class="wrap">
		<!--header-->
		<header class="login_header">
			<h1 class="login_logo">
				<a href="https://www.bioin.or.kr"><img src="/img/bioin_logo.png" alt="bioin바이오인"></a>
				<a href="https://www.bics.re.kr"><img src="/img/bics.png" alt="bics바이오혁신연계서비스"></a>
			</h1>
		</header>
		<!-- //header-->

		<main class="login_main">
			<div class="contents_body">
				<div class="container">
					<!--로그인 시작-->
					<h2 class="login_title">바이오분야별 전문인력 관리</h2>
					<span class="ex">바이오분야별 전문인력 관리에서는 BioIN(바이오정보포털), BICS(바이오혁신연계서비스)의 통합 로그인 정책을 진행합니다.</span>
					<form name="loginForm" action="/loginChk.do" method="post" onsubmit="return check();">
						<input type="hidden" id="msg" name="msg" value="" />
						<input type="hidden" id="cmd" name="cmd" value="login"/>
						<input type="hidden" id="TOKEN_KEY" name="TOKEN_KEY" id="TOKEN_KEY" value="${TOKEN_KEY}" />
					<div class="member">
						<div class="login">
							<div class="log1">
									<h2 class="tit">통합 로그인</h2>
									<div class="form">
										<input type="text" class="form_textbox" maxlength="15" name="userid" title="아이디" placeholder="아이디" />
										<input type="password" autocomplete="off" class="form_textbox" maxlength="20" name="passwd" title="비밀번호" placeholder="비밀번호" />
										<button type="submit" class="btn">로그인</button>
									</div>
							</div>
						</div>
					</div>
					</form>

					<!-- //컨텐츠 끝-->
				</div>

			</div>
		</main>

		<!--footer-->
		<footer class="login_footer">	
<!-- 			<address> -->
<!-- 				34141 대전광역시 유성구 과학로 125(어은동) 전화 : <a href="tel:042-879-8377">042-879-8377</a> -->
<!-- 				관리자 : <a href="mailto:bioin@kribb.re.kr">bioin@kribb.re.kr</a><br>COPYRIGHT(C) -->
<!-- 				2022 BY NATIONAL BIOTECH POLICY RESEARCH CENTER. ALL RIGHTS RESERVED. -->
<!-- 			</address> -->
			<p class="foot-logo">
				<img src="/img/footer_logo.png" alt="국가생명공학정책연구센터">
			</p>
		</footer>
		<!--//footer-->

	</div>

</body>
</html>