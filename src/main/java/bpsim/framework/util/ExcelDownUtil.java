package bpsim.framework.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelDownUtil extends AbstractExcelView {
	
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
		List bodyList = (List) map.get("bodyList");
		
		
		// Cell 스타일 설정
		CellStyle titleStyle = wb.createCellStyle();
		
		HSSFFont hssFont = wb.createFont();
		hssFont.setFontName("맑은 고딕");
		
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		hssFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hssFont.setColor(HSSFColor.BLACK.index);
		
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setFont(hssFont);
		
		CellStyle defaultStyle = wb.createCellStyle();
		
		defaultStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		defaultStyle.setWrapText(true);
		
		CellStyle headerStyle = wb.createCellStyle();
		
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(hssFont);
		
		
		
		// 시트 생성
		HSSFSheet sheet = wb.createSheet(excelSheetName);
		sheet.setDefaultColumnWidth(10);
		//sheet.setDefaultRowHeight((short)400);
		
		
		// 타이틀 설정
		HSSFRow sheetDescription = sheet.createRow(0);
		sheetDescription.createCell(0).setCellValue(excelTitle);
		sheetDescription.getCell(0).setCellStyle(titleStyle);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerList.length-1));
	    
		
		
	    HSSFCell cell = null;		
		
	    // put text in first cell
		//cell = getCell(sheet, 0, 0);
		
 
		// 헤더 생성
		for(int i=0; i<headerList.length; i++)
		{
			setText(getCell(sheet, 1, i), headerList[i]);
			getCell(sheet, 1, i).setCellStyle(headerStyle);
		}
		
		// 내용 생성
		Map excelMap;
			
		for (int i = 0; i < bodyList.size(); i++) {
				excelMap = (Map)bodyList.get(i);
 
				for(int j=0; j<columnList.length; j++)
				{
					cell = getCell(sheet, 2 + i, j);
					
					if("null".equals(String.valueOf(excelMap.get(columnList[j]))))
					{
						setText(cell, "" );
						cell.setCellStyle(defaultStyle);
					}else{
						String tmp = String.valueOf(excelMap.get(columnList[j]));
						if(tmp.indexOf("<br>") >= 0) {
							tmp = tmp.replaceAll("<br>", "\n");
						}
						//tmp = StringEscapeUtils.unescapeHtml(tmp);
 						setText(cell, tmp);
						cell.setCellStyle(defaultStyle);
					}
				}
		}
		
		//auto size 여부
		String check = (String) map.get("resize");
		if(check != null && "Y".equals(check)) {
			for(int i=0; i < headerList.length; i++) {
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, Math.min(255 * 256, (sheet.getColumnWidth(i))+300));
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
