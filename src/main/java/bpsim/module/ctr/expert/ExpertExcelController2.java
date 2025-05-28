package bpsim.module.ctr.expert;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import bpsim.framework.util.CommonUtil;
import bpsim.framework.util.TokenMngUtil;
import bpsim.module.ctr.usr.LoginController;
import bpsim.module.dao.BpsimCommon;
import bpsim.module.dto.LoginInfoDTO;

@Controller("bpsim.module.ctr.expert.ExpertExcelController2")	 
public class ExpertExcelController2 {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name="bpsimCommonService")
	private BpsimCommon bpsimCommonService;
	
	@RequestMapping(value="/expert2/excelUpload.do")
	public ModelAndView excelUpload(@RequestParam("uploadName") MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Throwable {

	    ModelAndView mav = new ModelAndView();
	    LoginInfoDTO loginInfo = (LoginInfoDTO) CommonUtil.getSession(request, "loginInfo");
	    
	    /* 중복방지 Token 생성 */
	    TokenMngUtil.saveToken(request);
	    Map<String, String> expertMap = new HashMap<>();
	    
	    String checkNum = "";
	    try (InputStream inputStream = file.getInputStream()) {
	        // 엑셀 파일 읽기
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기
	        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); // FormulaEvaluator 생성

	        // 엑셀 데이터 처리
	        for (int i = 3; i <= sheet.getLastRowNum(); i++) { // 첫 행(0)은 헤더로 가정
	        	expertMap.put("frst_rgtr_id", loginInfo.getLoginid());
	        	expertMap.put("last_mdfr_id", loginInfo.getLoginid());
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            for (int j = 0; j < row.getLastCellNum(); j++) {
	                Cell cell = row.getCell(j);
	                String value = getCellValue(cell, evaluator);

	                // j 값에 따른 처리
	                switch (j) {
	                    case 0: // 전문가 번호 체크
	                        String num = (value != null && !value.isEmpty()) ? value : "";
	                        if (!num.equals(checkNum)) {
	                            if (!"".equals(checkNum)) {
	                                // 이전 전문가 데이터 처리 로직 (DB 저장 등)
	                                System.out.println("박현호 저장할 데이터: " + expertMap.toString());
	                                //  기본정보 등록
	                                bpsimCommonService.insert("ExpertTemp.insertTempBscInfo", expertMap);
	                                
	                                /* 금욜에 오면 테스트 해야함  */
//	                                String exprtMngNo = expertMap.get("exprt_mng_no");
//	                                System.out.println("등록된 전문가 번호: " + exprtMngNo);
//	                                expertMap.put("exprt_mng_no", String.valueOf(exprtMngNo));
	                                
	                             // 먼저 expertMap에서 가져온 값을 Object로 받고 타입을 확인 후 처리
	                                Object exprtMngNoObj = expertMap.get("exprt_mng_no");

	                                // Integer인지 String인지 확인 후 변환 처리
	                                // spotbug로 주석처리
//	                                if (exprtMngNoObj instanceof Integer) {
//	                                    String exprtMngNoStr = String.valueOf(exprtMngNoObj); // Integer를 String으로 변환
//	                                    System.out.println("등록된 전문가 번호: " + exprtMngNoStr);
//	                                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
//	                                } else if (exprtMngNoObj instanceof String) {
//	                                    String exprtMngNoStr = (String) exprtMngNoObj; // 이미 String 타입인 경우 그대로 사용
//	                                    System.out.println("등록된 전문가 번호: " + exprtMngNoStr);
//	                                    expertMap.put("exprt_mng_no", exprtMngNoStr); // 변환된 String 값을 다시 Map에 저장
//	                                } else {
//	                                    System.out.println("exprt_mng_no의 타입이 예상과 다릅니다: " + exprtMngNoObj.getClass().getName());
//	                                }
	                                
	                                // 최근활동
	                                if(expertMap.get("rcnt_actv_ogdp_nm") != null) {
	                                	int rcnt_actv_line= 1;
	                                }
	                                // 학력사항
	                                if(expertMap.get("acbg_se_nm") != null) {
	                                	int acbg_line = 1;
	                                }
	                                // 경력사항
	                                if(expertMap.get("crr_ogdp_nm") != null) {
	                                	int crr_line = 1;
	                                }
	                                // 수상내역
	                                if(expertMap.get("wnawd_dsctn") != null) {
	                                	int wnawd_line = 1;
	                                }
	                                // 신바이오분류코드
	                                if(expertMap.get("bioClsf_lclsf_cd") != null) {
	                                	int bioClsf_line = 1;
	                                }
	                                // 정책분류코드
	                                if(expertMap.get("plcyClsf_lclsf_cd") != null) {
	                                	int plcyClsf_line = 1;
	                                }
	                                // 센터 활용 실적
	                                if(expertMap.get("utlz_ymd") != null) {
	                                	int utlz_line = 1;
	                                }
//	                                tempInfoSave(request, expertMap);
	                                
	                                System.out.println("우하하하");
	                                
	                            }	                        	
	                            checkNum = num;
	                            expertMap.clear(); // 새로운 전문가 정보로 초기화
	                            continue;
	                        } else {
	                            j = 13; // 중복 시 처리 생략	                            
	                        }
	                        break;
	                    // 기본정보
	                    case 1: expertMap.put("exprt_nm", value); break; // 전문가명
	                    case 2: 
	                    	if (value.equals("남")) {
	                    		value = "1";
	                    	}else if(value.equals("여")) {
	                    		value = "2";
	                    	}else {
	                    		value = "";
	                    	}
	                    	expertMap.put("exprt_gndr", value); break; // 성별
	                    case 3: expertMap.put("brth_dt", value); break; // 출생연도
	                    case 4: expertMap.put("ogdp_nm", value); break; // 소속
	                    case 5: expertMap.put("dept_nm", value); break; // 부서명
	                    case 6: expertMap.put("jbps_nm", value); break; // 직위
	                    case 7: expertMap.put("mbl_telno", value); break; // 휴대전화번호
	                    case 8: expertMap.put("co_telno", value); break; // 회사전화번호
	                    case 9: expertMap.put("eml_addr", value); break; // 이메일주소
	                    case 10: expertMap.put("asst_eml_addr", value); break; // 보조이메일주소
	                    case 11: expertMap.put("src_nm", value); break; // 출처명
	                    case 12: expertMap.put("rmrk_cn", value); break; // 비고내용
	                    case 13: expertMap.put("kywd_nm", value); break; // 키워드
	                    
	                    // 최근활동
	                    case 14:
	                    	if (value.equals("")) {
	                    		j = 17;
	                    	}else {
	                    		expertMap.put("rcnt_actv_ogdp_nm", appendData(expertMap, "rcnt_actv_ogdp_nm", value)); // 최근활동소속명
	                    	}
	                    	break;
	                    case 15: expertMap.put("rcnt_actv_jbttl_nm", appendData(expertMap, "rcnt_actv_jbttl_nm", value)); break; // 최근활동직책명
	                    case 16: expertMap.put("rcnt_actv_bgng_yr", appendData(expertMap, "rcnt_actv_bgng_yr", value)); break; // 최근활동시작연도
	                    case 17: expertMap.put("rcnt_actv_end_yr", appendData(expertMap, "rcnt_actv_end_yr", value)); break; // 최근활동종료연도
	                    
	                    // 학력사항
	                    case 18:
	                    	if (value.equals("")) {
	                    		j = 21;
	                    	}else {
	                    		expertMap.put("acbg_se_nm", appendData(expertMap, "acbg_se_nm", value)); // 학력구분명
	                    	}
	                    	break;
	                    case 19: expertMap.put("acbg_schl_nm", appendData(expertMap, "acbg_schl_nm", value)); break; // 학력학교명
	                    case 20: expertMap.put("acbg_mjr_nm", appendData(expertMap, "acbg_mjr_nm", value)); break; // 학력전공명
	                    case 21: expertMap.put("acbg_grdtn_yr", appendData(expertMap, "acbg_grdtn_yr", value)); break; // 학력졸업년도

	                    // 경력사항
	                    case 22:
	                    	if (value.equals("")) {
	                    		j = 25;
	                    	}else {
	                    		expertMap.put("crr_ogdp_nm", appendData(expertMap, "crr_ogdp_nm", value)); // 경력소속명
	                    	}
	                    	break;
	                    case 23: expertMap.put("crr_jbttl_nm", appendData(expertMap, "crr_jbttl_nm", value)); break; // 경력직책명
	                    case 24: expertMap.put("crr_bgng_yr", appendData(expertMap, "crr_bgng_yr", value)); break; // 경력시작년도
	                    case 25: expertMap.put("crr_end_yr", appendData(expertMap, "crr_end_yr", value)); break; // 경력종료년도

	                    // 수상내역
	                    case 26:
	                    	if (value.equals("")) {
	                    		j = 27;
	                    	}else {
	                    		expertMap.put("wnawd_dsctn", appendData(expertMap, "wnawd_dsctn", value)); // 수상내역
	                    	}
	                    	break;
	                    case 27: expertMap.put("wnawd_yr", appendData(expertMap, "wnawd_yr", value)); break; // 수상연도

	                    // 신바이오분류체계
	                    case 28:
	                    	if (value.equals("")) {
	                    		j = 30;
	                    	}else {
	                    		value = getRealCode(value);
	                    		expertMap.put("bioClsf_lclsf_cd", appendData(expertMap, "bioClsf_lclsf_cd", value)); // 대분류코드	
	                    	}
	                    	break;
	                    case 29: value = getRealCode(value); expertMap.put("bioClsf_mclsf_cd", appendData(expertMap, "bioClsf_mclsf_cd", value)); break; // 중분류코드
	                    case 30: value = getRealCode(value); expertMap.put("bioClsf_sclsf_cd", appendData(expertMap, "bioClsf_sclsf_cd", value)); break; // 소분류코드

	                    // 정책분류코드
	                    case 31:
	                    	if (value.equals("")) {
	                    		j = 33;
	                    	}else {
	                    		value = getRealCode(value);
	                    		expertMap.put("plcyClsf_lclsf_cd", appendData(expertMap, "plcyClsf_lclsf_cd", value)); // 대분류코드
	                    	}
	                    	break;
	                    case 32: value = getRealCode(value); expertMap.put("plcyClsf_mclsf_cd", appendData(expertMap, "plcyClsf_mclsf_cd", value)); break; // 중분류코드
	                    case 33: value = getRealCode(value); expertMap.put("plcyClsf_sclsf_cd", appendData(expertMap, "plcyClsf_sclsf_cd", value)); break; // 소분류코드

	                    // 센터 활용실적
	                    case 34:
	                    	if (value.equals("")) {
	                    		j = 37;
	                    	}else {
	                    		expertMap.put("utlz_ymd", appendData(expertMap, "utlz_ymd", value)); // 활용일자	                    		
	                    	}
	                    	break;
	                    case 35: expertMap.put("utlz_prps", appendData(expertMap, "utlz_prps", value)); break; // 활용목적
	                    case 36: expertMap.put("info_exprtuser", appendData(expertMap, "info_exprtuser", value)); break; // 정보활용자
	                    case 37: expertMap.put("utlz_rmrk_cn", appendData(expertMap, "utlz_rmrk_cn", value)); break; // 비고

	                    default:
	                        break;
	                }
	                System.out.println("박현호 " + i + "행 " + j + "열의 값 : " + value);
	            }
	        }
	        
	        // 마지막 전문가 데이터 처리 (루프가 끝나면 마지막으로 남은 전문가 데이터 저장)
	        if (!"".equals(checkNum)) {
	            System.out.println("박현호 마지막 저장할 데이터: " + expertMap.toString());
	        }
            
	        System.out.println("엑셀 파일 업로드 및 처리 완료");

	    } catch (Exception e) {
	    	e.printStackTrace();
	        System.out.println("엑셀 파일 처리 중 오류 발생 : " + e.getMessage());
	    }

	    if (loginInfo != null) {
	        mav.setViewName("jsp/expert/expertAdd");
	    } else {
	        mav.setViewName("redirect:/login.do");
	    }

	    return mav;
	}

	// 셀 값 추출하는 공통 메서드
	private String getCellValue(Cell cell, FormulaEvaluator evaluator) {
	    if (cell == null) return "";
	    
	    switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_STRING:
	            return cell.getStringCellValue() != null ? cell.getStringCellValue() : "";
	        case Cell.CELL_TYPE_NUMERIC:
	            double numericValue = cell.getNumericCellValue();
	            return (numericValue == (int) numericValue) ? String.valueOf((int) numericValue) : String.valueOf(numericValue);
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());	            
            case Cell.CELL_TYPE_FORMULA:
                // 수식을 평가하여 결과값을 반환
                return evaluateFormulaCell(cell, evaluator);    
	        default:
	            return "";
	    }
	}
	
    // 수식을 평가하여 결과값을 반환
    private String evaluateFormulaCell(Cell cell, FormulaEvaluator evaluator) {
        CellValue cellValue = evaluator.evaluate(cell); // 수식 평가
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            case Cell.CELL_TYPE_NUMERIC:
                double numericValue = cellValue.getNumberValue();
                return (numericValue == (int) numericValue) ? String.valueOf((int) numericValue) : String.valueOf(numericValue);
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cellValue.getBooleanValue());
            default:
                return "";
        }
    }
	
	
	// 공통으로 사용할 메서드 정의
	private String appendData(Map<String, String> map, String key, String value) {
	    String existingValue = map.get(key) != null ? map.get(key) : "";
	    return existingValue.isEmpty() ? value : existingValue + "|" + value;
	}
	
	// 분류코드에서 코드만 찾기
	private String getRealCode(String value) {
	    if (value == null || value.isEmpty()) {
	        return value; // 빈 값 처리
	    }
	    int startIndex = value.indexOf("[") + 1;
	    int endIndex = value.indexOf("]");
	    return (startIndex > 0 && endIndex > startIndex) ? value.substring(startIndex, endIndex) : value;
	}	
	

}
