package bpsim.framework.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import bpsim.module.dao.BpsimCommon;

public class BoardInfo {
	private BpsimCommon bpsimCommonService;
	private Map boardInfo;
	
	public BoardInfo(BpsimCommon bpsimCommonService, String bid){
		this.bpsimCommonService = bpsimCommonService;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("bid", bid);
		try{
			this.boardInfo = this.bpsimCommonService.getObjectMap("BbsInfo.getObjectMap", params);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ModelAndView setModelAndView(ModelAndView mav){
		String bname = "";
		String pagesize = "";
		String icon = "";
		String upload = "";
		String origin = "";
		String readpermission = "";
		String kind = "";
		String top_menu_nm = "";
		if(this.boardInfo != null){
			kind						 = ReqUtils.getEmptyResult2("" + boardInfo.get("kind"), "");
			bname				 = ReqUtils.getEmptyResult2("" + boardInfo.get("bname"), "");
			pagesize				 = ReqUtils.getEmptyResult2("" + boardInfo.get("pagesize"), "");
			icon						 = ReqUtils.getEmptyResult2("" + boardInfo.get("icon"), "");
			upload				 = ReqUtils.getEmptyResult2("" + boardInfo.get("upload"), "");
			origin					 = ReqUtils.getEmptyResult2("" + boardInfo.get("origin"), "");
			readpermission	 = ReqUtils.getEmptyResult2("" + boardInfo.get("readpermission"), "");
			top_menu_nm	 = ReqUtils.getEmptyResult2("" + boardInfo.get("name"), "");
			mav.addObject("boardInfo", boardInfo);
		}
		
		mav.addObject("kind", kind);
		mav.addObject("menu", bname);
		mav.addObject("pagesize", pagesize);
		mav.addObject("icon", icon);
		mav.addObject("upload", upload);
		mav.addObject("origin", origin);
		mav.addObject("readpermission", readpermission);
		mav.addObject("navi", top_menu_nm);
		return mav;
	}
	
	public String getBoardInfoValue(String key, String default_value){
		String result = "";
		if(this.boardInfo != null){
			result	 = ReqUtils.getEmptyResult2("" + boardInfo.get(key), default_value);
		}else{
			result = default_value;
		}
		return result;
	}
}
