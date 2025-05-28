package bpsim.framework.util;

/*******************************************************************************
EX)
// 생성자 호출
PageNavigator pageNavigator = new PageNavigator(
	cPage						- 페이지번호
	,"/xxx/xxxL.do"				- URL
	,pagePerBlock				- 블럭당 페이지 수
	,recordPerPage				- 페이지당 레코드 수
	,totalCount					- 레코드의 총 수
	,"&srchItem="+srchItem+"&srchText="+ReqUtils.getEncode(srchText));
								- 파라미터
int startIndex = pageNavigator.getRecordPerPage() * (cPage - 1);
시작위치, 페이지당 레코드 수, Map을 매개변수로 전달
List list = (List)xxxService.getList(startIndex, 
	pageNavigator.getRecordPerPage(), Map args);
xxxService 에서  원하는 레코드위치를 가져오는 메소드를 이용하여 목록을 얻음. (AbstractDao, Dao getPageList 추가)
queryForList("xxx.getPageList", hashMap, startIndex, recordPerPage);
******************************************************************************/
public class PageNavigator {
	
	// -------------------------------------------------------------------------
	private int totalRecord;										// 전체 레코드수
	private int recordPerPage;										// 페이지당 레코드수
	private int pagePerBlock;										// 블럭당 페이지수
	private int currentPage;										// 현재페이지
	private String goUrl;											// 이동할 URL
	private String parameter;										// 파라미터
	private String imgFirst = "<img src='/img/icon_btnFirst.svg' width='17' height='13' />";// 맨 앞 <strong>&lsaquo;&lsaquo;</strong>
	private String imgFirstClss = "";			// 맨 앞 클래스
	private String imgBack = "<img src='/img/icon_btnPrev.svg' width='17' height='13' />";		// 뒤 &lsaquo;
	private String imgBackClss = "";				// 뒤 클래스
	private String imgNext = "<img src='/img/icon_btnPrev.svg' width='17' height='13' /> ";			// 앞 &rsaquo;
	private String imgNextClss = "";				// 앞 클래스
	private String imgEnd = "<img src='/img/icon_btnFirst.svg' width='17' height='13' /> ";	// 맨 뒤<strong>&rsaquo;&rsaquo;</strong>
	private String imgEndClss = "";				// 맨 뒤 클래스
	private String strFirst = "처음";// 맨 앞 <strong>&lsaquo;&lsaquo;</strong>
	private String strBack = "이전";		// 뒤 &lsaquo;
	private String strNext = "다음 ";			// 앞 &rsaquo;
	private String strEnd = "끝 ";	// 맨 뒤<strong>&rsaquo;&rsaquo;</strong>
	// -------------------------------------------------------------------------	
	
	public PageNavigator(){}
	
	/**
	 * 생성자
	 * @param currentPage 	- 현재 페이지 번호
	 * @param goUrl 		- 목록 액션 URL
	 * @param pagePerBlock 	- 페이지당 블록 수
	 * @param recordPerPage - 한 페이지당 목록 수
	 * @param totalRecord 	- 총 레코드 수
	 * @param parameter 	- 파라미터
	 */
	public PageNavigator (int currentPage, String goUrl, int pagePerBlock,
			int recordPerPage, int totalRecord, String parameter){
		super();
		this.currentPage = currentPage;
		this.goUrl = goUrl;
		this.pagePerBlock = pagePerBlock;
		this.recordPerPage = recordPerPage;
		this.totalRecord = totalRecord;
		this.parameter  = parameter;
	}
	
	/**
	 * 생성자
	 * @param currentPage 	- 현재 페이지 번호
	 * @param goUrl 		- 목록 액션 URL
	 * @param pagePerBlock 	- 페이지당 블록 수
	 * @param recordPerPage - 한 페이지당 목록 수
	 * @param totalRecord 	- 총 레코드 수
	 * @param parameter 	- 파라미터
	 */
	public void setEng(){
		strFirst = "FIRST";// 맨 앞 <strong>&lsaquo;&lsaquo;</strong>
		strBack = "PREV";		// 뒤 &lsaquo;
		strNext = "NEXT";			// 앞 &rsaquo;
		strEnd = "LAST ";	// 맨 뒤<strong>&rsaquo;&rsaquo;</strong>
	}
	
	/**
	 * 페이지 네비게이터 생성
	 * @return 네비게이터 HTML 
	 */
	public String getMakePage() {
		
		if(totalRecord == 0) return "&nbsp;";
		
		int totalNumOfPage = (totalRecord % recordPerPage == 0) ? 
				totalRecord / recordPerPage :
				totalRecord / recordPerPage + 1;
		
		int totalNumOfBlock = (totalNumOfPage % pagePerBlock == 0) ?
				totalNumOfPage / pagePerBlock :
				totalNumOfPage / pagePerBlock + 1;
		
		int currentBlock = (currentPage % pagePerBlock == 0) ? 
				currentPage / pagePerBlock :
				currentPage / pagePerBlock + 1;
		
		int startPage = (currentBlock - 1) * pagePerBlock + 1;
		int endPage = startPage + pagePerBlock - 1;
		
		if(endPage > totalNumOfPage)
			endPage = totalNumOfPage;
		
		boolean isNext = false;
		boolean isPrev = false;
		boolean isFirst = false;
		boolean isLast = false;
		
		if(currentBlock < totalNumOfBlock) {
			isNext = true;
			isLast = true;
		}
		if(currentBlock > 1) {
			isPrev = true;
			isFirst = true;
		}
		if(totalNumOfBlock == 1){
			isNext = false;
			isPrev = false;
			isFirst = false;
			isLast = false;
		}
		
		StringBuffer sb = new StringBuffer();
		
		if(isFirst){
		    sb.append("<li class='page'>");
		    sb.append("<button type='button' class='button_first' onclick=\"location.href='").append(goUrl);
		    sb.append("?cPage=1").append(parameter).append("'\">");
		    sb.append("<span class='blind'>" + strFirst + "</span>");
		    sb.append("</button></li>");			
					
		}else {
		    sb.append("<li class='page'>");
		    sb.append("<button type='button' class='button_first' disabled>");
		    sb.append("<span class='blind'>" + strFirst + "</span>");
		    sb.append("</button></li>");
		}
		
		if (isPrev) {
		    if (currentPage > 1) {
		        // 이전 페이지로 이동 가능할 때
//		        int goPrevPage = currentPage - 1;
		        int goPrevPage = startPage - pagePerBlock;
		        sb.append("<li class='page'>");
		        sb.append("<button type='button' class='button_previous' onclick=\"location.href='").append(goUrl);
		        sb.append("?cPage=").append(goPrevPage).append(parameter).append("'\">");
		        sb.append("<span class='blind'>" + strBack + "</span>");
		        sb.append("</button></li>");
		    }
		} else {
		    sb.append("<li class='page'>");
		    sb.append("<button type='button' class='button_previous' disabled>");
		    sb.append("<span class='blind'>" + strBack + "</span>");
		    sb.append("</button></li>");			
		}

		// 페이지 번호 처리
		for (int i = startPage; i <= endPage; i++) {
		    if (i == currentPage) {
		        sb.append("<li class='page on'><button type='button' class='page_link'>").append(i);
		        sb.append("<span class='blind'>").append(i).append("번째 페이지 선택됨</span></button></li>");
		    } else {
		        sb.append("<li class='page'><button type='button' class='page_link' onclick=\"location.href='").append(goUrl);
		        sb.append("?cPage=").append(i).append(parameter).append("'\">").append(i).append("</button></li>");
		    }
		}

		// 다음 페이지 버튼 처리
		if (isNext) {
		    int goNextPage = startPage + pagePerBlock;
		    sb.append("<li class='page'><button type='button' class='button_next' onclick=\"location.href='").append(goUrl);
		    sb.append("?cPage=").append(goNextPage).append(parameter).append("'\">");
		    sb.append("<span class='blind'>다음 페이지</span>");
		    sb.append("</button></li>");
		} else {
		    sb.append("<li class='page'><button type='button' class='button_next' disabled>");
		    sb.append("<span class='blind'>다음 페이지</span>");
		    sb.append("</button></li>");
		}
		
		// 마지막 페이지 버튼 처리
		if(isLast) {
		    if(totalNumOfPage > currentPage){
		        sb.append("<li class='page'><button type='button' class='button_last' onclick=\"location.href='").append(goUrl);
		        sb.append("?cPage=").append(totalNumOfPage).append(parameter).append("'\">");
		        sb.append("<span class='blind'>마지막 페이지</span>");
		        sb.append("</button></li>");
		    } else {
		        sb.append("<li class='page'><button type='button' class='button_last' disabled>");
		        sb.append("<span class='blind'>마지막 페이지</span>");
		        sb.append("</button></li>");
		    }
		}else{
	        sb.append("<li class='page'><button type='button' class='button_last' disabled>");
	        sb.append("<span class='blind'>마지막 페이지</span>");
	        sb.append("</button></li>");
		}

		return sb.toString();
		
	}
	
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public int getPagePerBlock() {
		return pagePerBlock;
	}
	public void setPagePerBlock(int pagePerBlock) {
		this.pagePerBlock = pagePerBlock;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getGoUrl() {
		return goUrl;
	}
	public void setGoUrl(String goUrl) {
		this.goUrl = goUrl;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
