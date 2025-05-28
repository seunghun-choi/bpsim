package bpsim.module.dto;
                                 

public class UserInfoDTO {
	
	private static final long serialVersionUID = 1122179082713735621L;

	private String 	userid;	// id
	private String 	hname;	// name
	private String authrtCd;   // 권한
	private String codeNm;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
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