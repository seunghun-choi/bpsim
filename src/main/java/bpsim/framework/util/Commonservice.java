package bpsim.framework.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

public class Commonservice {
	@Resource(name="bpsimCommonService")
	public BpsimCommon bpsimCommonService;
	
	@Resource(name="commonservice")
	private Commonservice commonservice;
	
	public int bbsDataWrite(HttpServletRequest request, Map params) throws SQLException {
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		String title = ReqUtils.getEmptyResult2((String) params.get("title"), "[제목없음]");
    	params.put("title", title);
    	params.put("ip_addr", request.getRemoteAddr());
    	
    	bpsimCommonService.insert("BbsData.insert", params);

    	Map newParam = new HashMap();
    	
    	int num = bpsimCommonService.getDataCnt("BbsData.getMaxSeq", params);
    	newParam.put("bno", num);
    	newParam.put("bid", ReqUtils.getEmptyResult2((String)params.get("bid"), ""));
//    	newParam.put("userseq", loginInfo.getLoginseq());
    	
    	bpsimCommonService.update("BbsFiles.updateFileUploadSeq", newParam);
    	bpsimCommonService.update("BbsLink.updateLinkSeq", newParam);

    	/* 신규저장후 리스트 화면이 아닌 view 화면으로 이동하기 위해 조회된 params에 num을  등록 */
    	return num;
    	//mav.addObject("seq", num);
	}
	
	public void bbsDataUpdate(HttpServletRequest request, String deletedSeqs, Map params) throws SQLException {
		// 파일 수정
		//Map params = ReqUtils.getParameterMap(request);
		Map updateParam = new HashMap();
		String seqs = CommonUtil.stringChk((String) params.get("delFileList"), "");
		if (!seqs.equals("")) {
			updateParam.put("seqs", seqs);
			updateParam.put("bid", ReqUtils.getEmptyResult2((String)params.get("bid"), ""));
			bpsimCommonService.delete("BbsFiles.deleteInfo", updateParam);
		}
	
		if (!"".equals(deletedSeqs) && !deletedSeqs.equals(",")) {
			deletedSeqs = deletedSeqs.substring(1);
			updateParam.put("seqs", deletedSeqs);
			updateParam.put("bid", ReqUtils.getEmptyResult2((String)params.get("bid"), ""));
			bpsimCommonService.delete("BbsFiles.deleteSeqs", updateParam);
		}
	
		params.put("ip_addr", request.getRemoteAddr());
		params.put("num", ReqUtils.getEmptyResult2((String)params.get("num"), ""));
		bpsimCommonService.update("BbsData.update", params);
	}
    
}//End Class