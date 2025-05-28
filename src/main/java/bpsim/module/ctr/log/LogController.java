package bpsim.module.ctr.log;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.PageNavigator;
import bpsim.framework.util.ReqUtils;
import bpsim.framework.util.TokenMngUtil;
import bpsim.module.ctr.usr.LoginController;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller("bpsim.module.ctr.log.LogController")
public class LogController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@RequestMapping("/log/logList.do")
	public ModelAndView logList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		ModelAndView mav = new ModelAndView();
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
		System.out.println("박현호 params : " + params);
		
		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("cPage"), "1");
		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
		params.put("cPage", cPage);
		
		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
		int pageCnt = 10;									/* 가로페이징(페이지수) */
		int totalCnt = 0;
		params.put("intListCnt", intListCnt);
		
		totalCnt = bpsimCommonService.getDataCnt("Log.getLogListCnt", params);
		
		// 페이지 네비케이터
		PageNavigator pageNavigator = new PageNavigator(
			intPage		
		   ,"/log/logList.do"
		   ,pageCnt		
		   ,intListCnt	
		   ,totalCnt	
		   ,""
		);
		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
		List logList = bpsimCommonService.getList("Log.getLogList", params, 0, pageNavigator.getRecordPerPage());
		int fullCnt = bpsimCommonService.getDataCnt("Log.getLogFullCnt", params);
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);
		
		
		if(loginInfo != null){
			mav.setViewName("jsp/log/logList");
		}else{
			mav.setViewName("redirect:/login.do");
		}
		
		mav.addObject("authDc", authDc);
		mav.addObject("loginInfo", loginInfo);
		mav.addObject("logList", logList);
		mav.addObject("params", params);
		mav.addObject("pageNavigator", pageNavigator.getMakePage());
		mav.addObject("totalCnt", totalCnt);
		mav.addObject("totalPage", totalPage);
		mav.addObject("cPage", intPage);
		mav.addObject("listCnt", listCnt);
		mav.addObject("pageCnt", pageCnt);
		mav.addObject("fullCnt", fullCnt);


		return mav;
	
	}
	
	@RequestMapping("/log/logExcelDown.do")
	public void logExcelDown(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {
		
		LoginInfoDTO loginInfo = (LoginInfoDTO)CommonUtil.getSession(request, "loginInfo");
		/* 중복방지 Token 생성 */
		TokenMngUtil.saveToken(request);
		Map params = ReqUtils.getParameterMap(request);
//		String cPage 				= ReqUtils.getEmptyResult2((String)params.get("excelPage"), "1");
//		String listCnt 				= ReqUtils.getEmptyResult2((String)params.get("listCnt"), "10");	// 세로페이징
//		params.put("cPage", cPage);
//		
//		int intPage = Integer.parseInt(cPage);			/* 현재페이지*/
//		int intListCnt = Integer.parseInt(listCnt);		/* 세로페이징(게시글수)*/
//		int pageCnt = 10;									/* 가로페이징(페이지수) */
//		int totalCnt = 0;
//		params.put("intListCnt", intListCnt);
//		totalCnt = bpsimCommonService.getDataCnt("Log.getLogListCnt", params);
//		// 페이지 네비케이터
//		PageNavigator pageNavigator = new PageNavigator(
//			intPage		
//		   ,"/log/logList.do"
//		   ,pageCnt		
//		   ,intListCnt	
//		   ,totalCnt	
//		   ,""
//		);
//		int totalPage	= (totalCnt - 1 ) / intListCnt +1;
		List logList = bpsimCommonService.getList("Log.getLogExcelList", params);
		List authDc = bpsimCommonService.getList("Auth.getAuthCd", params);
		
		// 엑셀 만들기
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("로그 목록"); // 시트를 먼저 생성해야 함
		sheet.createFreezePane(0, 1);
		
		CellStyle onlyCenter = workbook.createCellStyle();
		onlyCenter.setAlignment(CellStyle.ALIGN_CENTER);  // 가로 중앙 정렬
		onlyCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  // 세로 중앙 정렬
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);  // 가로 중앙 정렬
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  // 세로 중앙 정렬		
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		Row mainHeader = sheet.createRow(0);
		String[] mains = {"이름", "ID", "권한명", "IP주소", "발생일시", "접근URL"};
		
        for (int i=0; i < mains.length; i++) {
        	Cell cell = mainHeader.createCell(i);
        	cell.setCellValue(mains[i]);
        	// 헤더 스타일 적용
        	cell.setCellStyle(headerStyle);
        }
        
        int[] maxColumnWidths = new int[6];
        
        for (int i = 0; i < maxColumnWidths.length; i++) {
            maxColumnWidths[i] = 1; // 각 인덱스에 디폴트 값 1 설정
        }
        
        int rowNum = 1;
        
        for (Object obj : logList) {
        	Map<String, Object> log = (Map<String, Object>) obj;  // 명시적 캐스팅
        	Row row = sheet.createRow(rowNum);
        	
            String acsrNm = (String) log.get("acsrNm");
            row.createCell(0).setCellValue(acsrNm);
            row.getCell(0).setCellStyle(onlyCenter);
            maxColumnWidths[0] = Math.max(maxColumnWidths[0], (acsrNm != null ? acsrNm.length() : 4));
            
            String userid = (String) log.get("userid");
            row.createCell(1).setCellValue(userid);
            row.getCell(1).setCellStyle(onlyCenter);
            maxColumnWidths[1] = Math.max(maxColumnWidths[1], (userid != null ? userid.length() : 4));
        	
            String authrtCd = (String) log.get("authrtCd");
            row.createCell(2).setCellValue(authrtCd);
            row.getCell(2).setCellStyle(onlyCenter);
            maxColumnWidths[2] = Math.max(maxColumnWidths[2], (authrtCd != null ? authrtCd.length() : 4));
            
            String acsrIpAddr = (String) log.get("acsrIpAddr");
            row.createCell(3).setCellValue(acsrIpAddr);
            maxColumnWidths[3] = Math.max(maxColumnWidths[3], (acsrIpAddr != null ? acsrIpAddr.length() : 4));
            
            Timestamp cntnDtTime = (Timestamp) log.get("cntnDt");
            String cntnDt = "";
            if (cntnDtTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cntnDt = sdf.format(cntnDtTime);
            }
            row.createCell(4).setCellValue(cntnDt);
            row.getCell(4).setCellStyle(onlyCenter);
            maxColumnWidths[4] = Math.max(maxColumnWidths[4], (cntnDt != null ? cntnDt.length() : 4));
            
            String cntnUrlAddr = (String) log.get("cntnUrlAddr");
            row.createCell(5).setCellValue(cntnUrlAddr);
            maxColumnWidths[5] = Math.max(maxColumnWidths[5], (cntnUrlAddr != null ? cntnUrlAddr.length() : 4));
        
            rowNum += 1;
    	}
        
        // 열 너비를 설정할 때, 최대 열 너비가 255로 제한되도록 설정
        for (int i = 0; i < maxColumnWidths.length; i++) {
            int calculatedWidth = (maxColumnWidths[i] + 2) * 390; // 기본 계산식
            if (calculatedWidth > 250 * 256) {
                calculatedWidth = 250 * 256; // 최대 열 너비는 255 * 256 (엑셀은 1/256 단위로 너비를 관리)
            }
            sheet.setColumnWidth(i, calculatedWidth);
        }
        
        // 파일명 설정        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = sdf.format(new Date());
        String fileName = "log_list_" + loginInfo.getLoginid() + "_" + currentDate + ".xlsx"; // 파일명에 타임스탬프 추가
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);  // 클라이언트에 파일 전송
            outputStream.flush();  // 출력 스트림을 비우고 종료
        } catch (IOException e) {
            e.printStackTrace();
        }

	}		
	
}
