package bpsim.framework.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelStatDownUtil extends AbstractExcelView {
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void buildExcelDocument(
			Map map,
			HSSFWorkbook wb, 
			HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		
		
		String excelFileName  = (String)map.get("excelFileName");
		String excelSheetName = (String)map.get("excelSheetName");
		String excelTitle          = (String)map.get("excelTitle");
		
		String[] headerList = (String[])map.get("headerList");
		String[] columnList = (String[])map.get("columnList");
		String[] addTopList = (String[])map.get("addTopList");
		int[][] addTopMerge = (int[][])map.get("addTopMerge");
		String hyper = (String)map.get("hyper");
		List bodyList = (List) map.get("bodyList");
		
		
		// Cell 스타일 설정
		CellStyle titleStyle = wb.createCellStyle();
		
		HSSFFont hssFont = wb.createFont();
		hssFont.setFontName("맑은 고딕");
		hssFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hssFont.setColor(HSSFColor.BLACK.index);
		
		HSSFFont linkFont = wb.createFont();
		linkFont.setFontName("맑은 고딕");
		linkFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		linkFont.setColor(HSSFColor.BLUE.index);
		linkFont.setColor(HSSFColor.BLUE.index);
		linkFont.setUnderline(Font.U_SINGLE);;
		
		// 타이틀 스타일
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setFont(hssFont);
		
		// 디폴트 스타일
		CellStyle defaultStyle = wb.createCellStyle();
		defaultStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 링크 스타일
		CellStyle linkStyle = wb.createCellStyle();
		linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		linkStyle.setAlignment(CellStyle.ALIGN_LEFT);
		linkStyle.setFont(linkFont);
		// 헤더스타일
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(hssFont);
		
		// 시트 생성
		HSSFSheet sheet = wb.createSheet(excelSheetName);
		sheet.setDefaultColumnWidth(20);
		
		// 타이틀 설정
		HSSFRow sheetDescription = sheet.createRow(0);
		sheetDescription.createCell(0).setCellValue(excelTitle);
		sheetDescription.getCell(0).setCellStyle(titleStyle);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerList.length-1));
		
	    HSSFCell cell = null;		
		
	    // 추가되는 상위행
	    int row = 0;
	    if(addTopMerge != null) {
		    row = addTopMerge[addTopList.length-1][0]; // 행
		    for(int i = 1; i < row+1; i++) {
		    	sheetDescription.createCell(row);
		    }
	    
		    for(int i=0; i<addTopList.length; i++)
			{	
		    	// 셀 병합
		    	sheet.addMergedRegion(new CellRangeAddress(addTopMerge[i][0], addTopMerge[i][1], addTopMerge[i][2], addTopMerge[i][3]));
	    		setText(getCell(sheet, addTopMerge[i][0], addTopMerge[i][2]), addTopList[i]);
	    		getCell(sheet, addTopMerge[i][0], addTopMerge[i][2]).setCellStyle(headerStyle);
			}
	    }
	   
		// 헤더 생성
		for(int i=0; i<headerList.length; i++)
		{
			setText(getCell(sheet, 1+row, i), headerList[i]);
			getCell(sheet, 1+row, i).setCellStyle(headerStyle);
		}
		
		// 내용 생성
		Map excelMap;
		
		for (int i = 0; i < bodyList.size(); i++) {
				excelMap = (Map)bodyList.get(i);
				excelMap.put("title", TextConvert.toTEXT_VIEW((String)excelMap.get("title"))) ;
				for(int j=0; j<columnList.length; j++)
				{
					cell = getCell(sheet, 2+i+row, j);
					
					if("null".equals(String.valueOf(excelMap.get(columnList[j]))))
					{
						setText(cell, "" );
						cell.setCellStyle(defaultStyle);
					}else{
						setText(cell, String.valueOf(excelMap.get(columnList[j])) );
						cell.setCellStyle(defaultStyle);
						if(hyper != null && j == Integer.parseInt(hyper)) {
							cell.setCellStyle(linkStyle);
							HSSFHyperlink url_link=new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							url_link.setAddress((String)CommonUtil.getAppRes("Appcommon.ROOT_DOMAIN").trim()+excelMap.get("view_link"));
							cell.setHyperlink(url_link);
						}
					}
				}
		}

		response.setHeader("Content-disposition","attachment;filename="+java.net.URLEncoder.encode(excelFileName, "UTF-8")+".xls");
		response.setHeader("Content-Type", "application/vnd.ms-excel; charset=MS949");
		response.setHeader("Content-Description", "JSP Generated Data");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		
	}
	
	
	
}
