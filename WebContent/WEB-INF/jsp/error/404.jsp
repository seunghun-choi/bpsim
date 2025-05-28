<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,user-scalable=yes">
    <title>BPSIM</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap" rel="stylesheet">
</head>
<style>
  body{margin:0;overflow:hidden;height:100vh;position:relative;font-family: 'Noto Sans KR', sans-serif;}
  .wrap{position:fixed;top:50%;left:50%;-webkit-transform: translate(-50%, -50%);transform: translate(-50%, -50%);width:100%;max-width: 90%;text-align: center;}
  img{display:block;margin:auto;}
  .wrap > strong{font-weight: bold;font-size:25px;display:block;padding:26px 0 14px;letter-spacing: -1.25px;color: #111111;}
  .wrap > span{display:block;color:#767676;line-height:1.8;letter-spacing: -0.8px;}
  .btn{padding-top:44px;}
  .btn > a{width:120px;height:46px;display:inline-block;vertical-align: middle;border-radius: 4px;text-decoration: none;line-height:47px;transition:all .3s;font-weight:500;font-size:16px;letter-spacing: -0.8px;}
  .btn > a.home{background:#222;color:#fff;border:2px solid #222;}
  .btn > a.home:hover,
  .btn > a.home:focus{border-color:#222;background:none;color:#222;}
  .btn > a.prev{background:#fff;color:#222;border:2px solid #222;margin-left:4px;}
  .btn > a.prev:hover,
  .btn > a.prev:focus{background:#222;color:#fff;border-color:#222;}
</style>
<body>
  <div class="wrap">
    <img src="/images/common/404error.png" alt="404">  
    <strong>죄송합니다. 현재 찾을 수 없는 페이지를 요청 하셨습니다.</strong>
    <span>존재하지 않는 주소를 입력하셨거나, 요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다.</span>
    <span>궁금한 점이 있으시면 언제든 고객센터를 통해 문의해 주시기 바랍니다.</span>
    <div class="btn">
      <a href="/expert/expertList.do" class="home">메인으로</a>
      <a href="javascript:history.back();" class="prev">이전페이지</a>
    </div>
  </div>
</body>


</html>