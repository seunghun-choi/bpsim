package bpsim.framework.util;

import javax.servlet.http.HttpSession;

import Kisinfo.Check.IPIN2Client;

public class RestingIdentificationModule {
	private String ROOT_DOMAIN = (String)CommonUtil.getAppRes("Appcommon.ROOT_DOMAIN");
	
	private String sReturnUrl = "";
	private String sErrorUrl = "";
	private String iReturnUrl = "";
	
	private HttpSession _session;
	
	public static String sSiteCode 						= "G3980";				   			// NICE 실명인증 코드
	public static String sSitePassword 				= "3V93W6D11SJ1";			// NICE 실명인증 패스워드
	public static String ipinCode							= "B848";							// IPIN 서비스 사이트 코드		(NICE신용평가정보에서 발급한 사이트코드)
	public static String ipinPw							= "80102809";					// IPIN 서비스 사이트 패스워드	(NICE신용평가정보에서 발급한 사이트패스워드)
	private String sEncData									= "";
	private String sMessage								= "";
	private String iEncData									= "";
	private String sRtnMsg									= "";
	public RestingIdentificationModule(HttpSession session){
		this._session = session;

		this.sReturnUrl	= ROOT_DOMAIN + "join.do?cmd=resting_success";
		this.sErrorUrl = ROOT_DOMAIN + "join.do?cmd=checkplus_fail";		  // 실패시 이동될 URL
		this.iReturnUrl	= ROOT_DOMAIN + "join.do?cmd=ipin_restring_result";

		// 휴대폰 인증 시작
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
		String sRequestNumber = "REQ0000000001";
		sRequestNumber = niceCheck.getRequestNO(sSiteCode);
		
		_session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
		
		String sAuthType = "";	  	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	   	
	   	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
		String customize 	= "";			//없으면 기본 웹페이지 / Mobile : 모바일페이지
			
		// CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
		//String sReturnUrl	= "http://localhost:8888/jsp/usr/join/checkplus_success.jsp";
		

		// 입력될 plain 데이타를 만든다.
		String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
							"8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
							"9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
							"7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
							"7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
							"11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
							"9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
		
		int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
		if( iReturn == 0 )
		{
			sEncData = niceCheck.getCipherData();
		}
		else if( iReturn == -1)
		{
			sMessage = "암호화 시스템 에러입니다.";
		}	
		else if( iReturn == -2)
		{
			sMessage = "암호화 처리오류입니다.";
		}	
		else if( iReturn == -3)
		{
			sMessage = "암호화 데이터 오류입니다.";
		}	
		else if( iReturn == -9)
		{
			sMessage = "입력 데이터 오류입니다.";
		}	
		else
		{
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}
		// 휴대폰 인증 끝
		
		// 아이핀 인증 시작
		
		String sCPRequest				= "";
		// 객체 생성
		IPIN2Client pClient = new IPIN2Client();
			
		// 앞서 설명드린 바와같이, CP 요청번호는 배포된 모듈을 통해 아래와 같이 생성할 수 있습니다.
		sCPRequest = pClient.getRequestNO(ipinCode);
		
		// CP 요청번호를 세션에 저장합니다.
		// 현재 예제로 저장한 세션은 ipin_result.jsp 페이지에서 데이타 위변조 방지를 위해 확인하기 위함입니다.
		// 필수사항은 아니며, 보안을 위한 권고사항입니다.
		_session.setAttribute("CPREQUEST" , sCPRequest);
		
		iReturnUrl	= ROOT_DOMAIN + "join.do?cmd=ipin_restring_result";
		
		// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
		int iRtn = pClient.fnRequest(ipinCode, ipinPw, sCPRequest, iReturnUrl);
		
		// Method 결과값에 따른 처리사항
		if (iRtn == 0)
		{
			// fnRequest 함수 처리시 업체정보를 암호화한 데이터를 추출합니다.
			// 추출된 암호화된 데이타는 당사 팝업 요청시, 함께 보내주셔야 합니다.
			iEncData = pClient.getCipherData();
			//sRtnMsg = "정상 처리되었습니다.";
		
		}
		else if (iRtn == -1 || iRtn == -2)
		{
			sRtnMsg =	"배포해 드린 서비스 모듈 중, 귀사 서버환경에 맞는 모듈을 이용해 주시기 바랍니다.<BR>" +
						"귀사 서버환경에 맞는 모듈이 없다면 ..<BR><B>iRtn 값, 서버 환경정보를 정확히 확인하여 메일로 요청해 주시기 바랍니다.</B>";
		}
		else if (iRtn == -9)
		{
			sRtnMsg = "입력값 오류 : fnRequest 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.";
		}
		else
		{
			sRtnMsg = "iRtn 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.";
		}
	}
	
	public String getsEncData() {
		return sEncData;
	}

	public String getsMessage() {
		return sMessage;
	}

	public String getiEncData() {
		return iEncData;
	}

	public String getsRtnMsg() {
		return sRtnMsg;
	}

	public static String getsSiteCode() {
		return sSiteCode;
	}

	public static String getsSitePassword() {
		return sSitePassword;
	}

	public static String getIpinCode() {
		return ipinCode;
	}

	public static String getIpinPw() {
		return ipinPw;
	}

	public static String getAuthType(String type, String code) {
		String result = "";
		if(type.equals("M")){
			result = "휴대폰";
		}else if(type.equals("C")){
			result = "신용카드";
		}else if(type.equals("X")){
			result = "공인인증서";
		}else if(type.equals("0")){
			result = "아이핀 - 공인인증서";
		}else if(type.equals("1")){
			result = "아이핀 - 카드";
		}else if(type.equals("2")){
			result = "아이핀 - 핸드폰";
		}else if(type.equals("3")){
			result = "아이핀 - 대면확인";
		}else if(type.equals("5")){
			result = "아이핀 - 기타";
		}else if(!code.equals("")){
			result = "외국인";
		}else{
			result = "ETC (" + type + ")";
		}
		return result;
	}
}
