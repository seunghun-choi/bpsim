package bpsim.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpsim.module.dao.BpsimCommon;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * 통합메일명단 엑셀업로드
 *
 * << 개정이력(Modification Information) >>
 *
 *  수정일        	수정자        	수정내용
 *  ----------  --------    ---------------------------
 *  2021. 12. 21  	JHS     
 *
 */
public class ExcelUtil {
	private BpsimCommon bpsimCommonService;
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	public ExcelUtil(BpsimCommon bpsimCommonService){
		this.bpsimCommonService = bpsimCommonService;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> excelRead(File f, Map<String, String> params){
		String[] keys = {"hname", "email"};
		
		WorkbookSettings setting = new WorkbookSettings();
		setting.setEncoding("euc-kr");
		
		Workbook w = null;
		List<Map<String, String>> emailList = new ArrayList<Map<String, String>>();
		try{
			w = Workbook.getWorkbook(f, setting);
			
			Sheet sheet = w.getSheet(0);
			for(int i =1; i < sheet.getRows(); i++){
				Map<String, String> excelData = new HashMap<String, String>();
				for(int j = 0; j < keys.length; j++){
					Cell cell = sheet.getCell(j, i);
					excelData.put(keys[j], cell.getContents() + "");
				}
				
				// biozine_mailing테이블 이메일로만 중복체크
				List<Map<String, String>> emailMemList = bpsimCommonService.getList("BiozineMailing.getEmailAddressCheck", excelData);

				/* 그룹(part)는 화면에서 선택된 그룹으로 등록 */
				String part = ReqUtils.getEmptyResult2("" + params.get("part"), "no_group");
				excelData.put("part", part);
				
				if(emailMemList == null || emailMemList.size() == 0){
					// 기본값
					String email_se = "1|2|3|4";
					String mailonoff = "Y";
					
					// 회원 테이블 조회(회원의 메일수신여부, 구분 조회) -> 데이터 겹치는 오류방지를 위해
					Map<String, String> umap = bpsimCommonService.getObjectMap("Member.getObjectMap", excelData);
					if(umap != null) {
						email_se = umap.get("email_se");
						mailonoff = umap.get("mailonoff");
					}
					excelData.put("mailonoff", mailonoff);	// 메일수신여부
					excelData.put("email_se", email_se);	// 구분
					bpsimCommonService.insert("BiozineMailing.insert", excelData);
				} else {
					
					for(int j=0; j<emailMemList.size(); j++){
						Map<String, String> map = (Map<String, String>)emailMemList.get(j);
						if(!StringUtils.isEmpty((String) map.get("part"))) {
							emailList.add(map);
						}else {
							// part만 변경
							excelData.put("seq", String.valueOf(map.get("seq")));
							bpsimCommonService.update("BiozineMailing.update", excelData);
						}
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return emailList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> excelReadExpert(File f, Map<String, String> params){
		String[] keys = {"hname", "email"};
		
		WorkbookSettings setting = new WorkbookSettings();
		setting.setEncoding("euc-kr");
		
		Workbook w = null;
		List<Map<String, String>> emailList = new ArrayList<Map<String, String>>();
		try{
			w = Workbook.getWorkbook(f, setting);
			
			Sheet sheet = w.getSheet(0);
			for(int i =1; i < sheet.getRows(); i++){
				Map<String, String> excelData = new HashMap<String, String>();
				for(int j = 0; j < keys.length; j++){
					Cell cell = sheet.getCell(j, i);
					excelData.put(keys[j], cell.getContents() + "");
				}
				
				// biozine_mailing테이블 이메일로만 중복체크
				List<Map<String, String>> emailMemList = bpsimCommonService.getList("BiozineMailing.getEmailAddressCheck", excelData);

				/* 그룹(part)는 화면에서 선택된 그룹으로 등록 */
				String part = ReqUtils.getEmptyResult2("" + params.get("part"), "no_group");
				excelData.put("part", part);
				
				if(emailMemList == null || emailMemList.size() == 0){
					// 기본값
					String email_se = "1|2|3|4";
					String mailonoff = "Y";
					
					// 회원 테이블 조회(회원의 메일수신여부, 구분 조회) -> 데이터 겹치는 오류방지를 위해
					Map<String, String> umap = bpsimCommonService.getObjectMap("Member.getObjectMap", excelData);
					if(umap != null) {
						email_se = umap.get("email_se");
						mailonoff = umap.get("mailonoff");
					}
					excelData.put("mailonoff", mailonoff);	// 메일수신여부
					excelData.put("email_se", email_se);	// 구분
					bpsimCommonService.insert("BiozineMailing.insert", excelData);
				} else {
					
					for(int j=0; j<emailMemList.size(); j++){
						Map<String, String> map = (Map<String, String>)emailMemList.get(j);
						if(!StringUtils.isEmpty((String) map.get("part"))) {
							emailList.add(map);
						}else {
							// part만 변경
							excelData.put("seq", String.valueOf(map.get("seq")));
							bpsimCommonService.update("BiozineMailing.update", excelData);
						}
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return emailList;
	}	
	
}
