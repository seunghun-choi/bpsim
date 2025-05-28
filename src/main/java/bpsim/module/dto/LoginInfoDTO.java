package bpsim.module.dto;

/*********************
	@author du-heon jang*/                                 

public class LoginInfoDTO {
	
	private static final long serialVersionUID = 1122179082713735621L;

	private String 	loginid;	// id
	private String 	loginnm;	// name
	private String authrtCd;   // 권한
	private String codeNm;
	
	
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getLoginnm() {
		return loginnm;
	}
	public void setLoginnm(String loginnm) {
		this.loginnm = loginnm;
	}
	public String getAuthrtCd() {
		return authrtCd;
	}
	public void setAuthrtCd(String authrtCd) {
		this.authrtCd = authrtCd;
	}
	public String getCodeNm() {
		return codeNm;
	}
	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}
	

}