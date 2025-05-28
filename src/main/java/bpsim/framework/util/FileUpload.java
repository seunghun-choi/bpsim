package bpsim.framework.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
/**
* 파일업로는 하는 객체로 파일 중복체크및 크기체크를 하여 Hashtable형태로 넘긴다.
* 
*/
public class FileUpload{
	
	private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);
	
	//private boolean isDebug		=true;
	private Hashtable paramTbl		=null;
	private Hashtable fileNameTbl	=null;
	private Hashtable fileNameTb2	=null;
	private Hashtable fileSizeTbl	=null;
/**************************************************
* 생성자초기화
* StringBuffer,Hashtable객체 생성 
*/	
	public FileUpload(){ 
  		paramTbl =	new Hashtable();
		fileNameTbl = new Hashtable();
		fileNameTb2 = new Hashtable();
		fileSizeTbl = new Hashtable();
	} 
/**************************************************
* 모든 파라미터를 받아서 파일과 일반파라미터를 구분하여 각각의 다른 Hashtable에 저장한다.[MultipartParser를 이용함]
* @param request 파라미터를 받아오는 객체 ,application 파일의 위치를 파악하기위한 객체,opt 파일의 이름형식을 구분[1:원래파일이름,2:시스템날짜로된 이름]
* @return  boolean 형으로 반납
*/	
	public boolean upload(HttpServletRequest request,ServletContext application,String save_path){
		File upDir = null, fileDir = null;
		String name =null, value =null, fileName = null, sizeName = null;
		//int fileSize= 0;
		Part part= null;
		ParamPart paramPart = null;
		FilePart filePart = null;
		long size = 0;
		int fileIdx=0;
		String[]strSkipExt=new String[4];//주의를 요하는 파일 확장자정보
		strSkipExt[0]=".bat";
		strSkipExt[1]=".com";
		strSkipExt[2]=".exe";
		strSkipExt[3]=".jsp";
		try{			 
			MultipartParser mp = new MultipartParser(request, 500*1024*1024);  //최대 20MB
			mp.setEncoding("euc-kr");	
			while ((part = mp.readNextPart()) != null) {
				name = part.getName();				
				if (part.isParam()) {						//파라미터부분 받기			
					paramPart = (ParamPart) part;
					value = paramPart.getStringValue();	

					if (paramTbl.containsKey(name)){
						paramTbl.put( name, addVec(paramTbl.get(name),value) );
	                }
	                else{
	                	paramTbl.put(name,value);
	                	//logger.debug(name+ " : "+ value +"==");	
	                }					
				}
				else if (part.isFile()){// 파일부분받기					
					String upFolder = application.getRealPath(save_path);
					upDir = new File(upFolder);		
					if (!upDir.exists()) { 
						//파일디렉토리없으면 만들기
						if(upDir.mkdir()) {
							logger.debug(upFolder+" make ok");
						}else {
							logger.debug(upFolder+" make fail!!");
						}
					}			
					
					filePart = (FilePart) part;
					filePart.setRenamePolicy(new DefaultFileRenamePolicy());          
					fileName = filePart.getFileName(); // 파일중복처리 후 이름
					if (fileName != null) {							
						String strName=""+Calendar.getInstance().getTimeInMillis()+"_"+fileIdx;
						String strName2=fileName.substring(fileName.lastIndexOf("."));
						boolean booVal=true;
						for(int i=0;i<strSkipExt.length;i++){//확장자 체크
							if(strSkipExt[i].equals(strName2.toLowerCase())){
								booVal=false;
							}
						}
						
						if(booVal){
							fileDir = new File(upDir, strName+strName2);
							size = filePart.writeTo(fileDir);
							fileIdx++;						
							fileNameTbl.put(name, fileName); // 한글파일이름
							fileNameTb2.put(name, strName+strName2); // 시스템날짜(실제파일) 파일이름
							fileSizeTbl.put(name, Long.toString(size)); // 파일 크기
						}						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
/**************************************************
* 파일파라미터에 모든값을 담은 Hashtable을 넘김
* @return  Hashtable 형으로 반납
*/	
	public Hashtable getParamName(){return paramTbl;}
		
/**************************************************
* 파라미터 Hashtable에서 이름에 맞는 값을 넘김
* @param name 키값(파라미터이름)
* @return  String 형으로 반납
*/	
	public String getParam(String name){
		return (String)paramTbl.get(name);
	}


	public String getParameter(String name){
        Object obj = paramTbl.get(name);
        String value = null;
        if (obj instanceof String){
            value = (String)obj;
        }
        else if (obj instanceof Vector){
            value = (String)((Vector)obj).get(0);
        }
        return value;
    }//getParameter
	
/**************************************************
* 파라미터 Hashtable에서 이름에 맞는 값을 넘김
* @param name 키값(파라미터이름)
* @return  String 형으로 반납
*/
	
	public String[] getParameterValuse(String name) {
		Object obj = paramTbl.get(name);
		String [] value = null;
        if (obj instanceof String){
            value = new String[1];
            value[0] = (String)obj;
        }
        else if (obj instanceof Vector){
            Vector valueVec = (Vector)obj;
            value = new String[valueVec.size()];
            for (int i=0; i<valueVec.size(); i++){
                value[i] = (String)valueVec.get(i);
            }//for
           
        }//else if
       
        return value;

	}
	
	
/**************************************************
* 파일파라미터에 모든값을 담은 Hashtable을 넘김
* @return  Hashtable 형으로 반납
*/	
	public Hashtable getFileName(){return fileNameTb2;}


	
/**************************************************
* 파일파라미터 Hashtable에서 순번에 맞는 값을 넘김 
* @param iname 키값(파라미터이름)
* @return  String 형으로 반납
*/	
	public String getFileName(String name){return (String)fileNameTbl.get(name);}

/**************************************************
* 파일파라미터(실제파일명)에 모든값을 담은 Hashtable을 넘김
* @return  Hashtable 형으로 반납
*/	
	public Hashtable getFileNameReal(){return fileNameTbl;}


/**************************************************
* 파일파라미터(실제파일명) Hashtable에서 순번에 맞는 값을 넘김 
* @param iname 키값(파라미터이름)
* @return  String 형으로 반납
*/	
	public String getFileNameReal(String name){return (String)fileNameTb2.get(name);}


/**************************************************
* 파일파라미터에 모든값(파일 크기)을 담은 Hashtable을 넘김
* @return  Hashtable 형으로 반납
*/	
	public Hashtable getFileSize(){return fileSizeTbl;}


/**************************************************
* 파일파라미터(파일크기) Hashtable에서 순번에 맞는 값을 넘김 
* @param isize 키값(파라미터이름)
* @return  String 형으로 반납
*/	
	public String getFileSize(String name){return (String)fileSizeTbl.get(name);}

/************************************************************************************************************
* 파일을 내용을 String 형으로 변환
* @ param path : 파일위치
* @ return : 파일내용
**************************************************************************************************************/
	public String fileToStr(String path) {
		StringBuffer result = new StringBuffer(); 
		try { 
			File newFile =new File(path);
			if(newFile.isFile()){
				BufferedReader in = new BufferedReader (new InputStreamReader(new FileInputStream(newFile))); 
				while (in.ready()){ 
					result.append(in.readLine()); 
					result.append(System.getProperty("line.separator")); 
				} 
				in.close(); 
			}else{
				result.append("");
			}
		}catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return result.toString();
	}
/************************************************************************************************************
* 파일을 내용을 String 형으로 변환
* @ param path : 파일위치
* @ return : 파일내용
**************************************************************************************************************/
	public void strToFile(String path,String dataFile,String conts){		
		try { 
			File folder=new File(path);
			if (!folder.exists()) { 
				//파일디렉토리없으면 만들기
				if(folder.mkdir()) logger.debug(folder+" make ok");
				else logger.debug(folder+" make fail!!");
			}			
			File newFile=new File(dataFile);
			FileOutputStream fops=new FileOutputStream(newFile); 
			fops.write(conts.getBytes("KSC5601"));
			fops.close();			
		}catch (Exception e) { 
			e.printStackTrace(); 
		} 		
	}

	public int[] resizeImg(String pic, int maxSize) {
        int[] size = new int[2];        
        try {
           BufferedInputStream in = new BufferedInputStream(new FileInputStream(pic));
		//	ImageInputStream in= new FileImageInputStream(new File(pic)); 
            BufferedImage img = ImageIO.read(in);   //<-- bmp일 경우 null발생
			in.close();
            //원본 이미지 사이즈
            int imgWidth  = (img == null) ? 0 : img.getWidth();
            int imgHeight = (img == null) ? 0 : img.getHeight();
			int imgRate=100;

			if(imgWidth>=imgHeight){		
				if(maxSize<imgWidth) imgRate=maxSize*100/imgWidth;				
			}else{		
				if(maxSize<imgHeight) imgRate=maxSize*100/imgHeight;			
			}          
            size[0] = (imgWidth*imgRate)/100;
            size[1] = (imgHeight*imgRate)/100;
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return size;
    } 
/************************************************************************************************************
* 파일다운로드모듈
* @ param response : 객체
* @ param request  : 객체
* @ param fileReal : 실제파일명(경로포함)
* @ param fileName : 다운로드파일명(파일명만)
* @ return : 메세지
**************************************************************************************************************/
	public String down(HttpServletResponse response,HttpServletRequest request,String fileReal,String fileName){

		InputStream in= null;
		OutputStream os= null;
		File file= null;
		boolean skip= false;
		String client= "";
		String massage="";
		try{
			try{
				file= new File(fileReal);
				in= new FileInputStream(file);
			}catch(FileNotFoundException fe){
				skip= true;
			}

			response.reset() ;

			client= request.getHeader("User-Agent");
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-Description", "JSP Generated Data");
			if(!skip){
				if(client.indexOf("MSIE 5.5") != -1){
					response.setHeader("Content-Type", "doesn/matter; charset=euc-kr");
					response.setHeader("Content-Disposition", "filename="+new String(fileName.getBytes(),"euc-kr"));
				}else{
					response.setHeader ("Content-Disposition", "attachment; filename="+fileName);
					//response.setHeader ("Content-Disposition", "attachment; filename="+new String(fileName.getBytes(),"ISO8859_1"));
				}
				response.setHeader ("Content-Length", ""+file.length() );
				os= response.getOutputStream();
				byte b[]= new byte[(int)file.length()];
				int leng= 0;
				while( (leng = in.read(b)) > 0 ){
					os.write(b,0,leng);
				}
			}else{
				response.setContentType("text/html;charset=euc-kr");
				massage="<script language='javascript'>";
				massage+="alert('선택 하신 파일을 찾을 수 없습니다');";
			//	massage+="history.back();";
				massage+="</script>";
			}
			if(in != null) in.close();
			if(os != null) os.close();
		}catch(Exception e) {
			System.out.println("error : "+e);
		}
		return massage;
	}
/************************************************************************************************************
* 문자열 여과하기(스크립트공격대비)
* @ param strVal : 문자열
* @ return : 여과된 문자열
**************************************************************************************************************/
	public String filterTag(String strVal){
		
		String[]strArr=new String[3];
		strArr[0]="<script";
		strArr[1]="<xmp";
		strArr[2]="<pre";		
		boolean booVal=false;
		if(strVal!=null){
			for(int i=0;i<strArr.length;i++){
				if(strVal.toLowerCase().indexOf(strArr[i])>-1){
					booVal=true;
				}
			}
			if(booVal){
				strVal=strVal.toLowerCase();
				for(int i=0;i<strArr.length;i++){
					strVal=strVal.replaceAll(strArr[i],"");
				}			
			}
			strVal=strVal.replaceAll("'","`");
			strVal=strVal.replaceAll("\"","``");
		}else{
			strVal="";
		}
		return strVal;
	}
	
	 private Vector addVec(Object oldValue, String newValue){
        Vector vec = null;
        if (oldValue instanceof Vector) {
            vec = (Vector)oldValue;
            vec.add(newValue);
        }
        else if (oldValue instanceof String){
            vec = new Vector();
            vec.add(oldValue);
            vec.add(newValue);
        }
        return vec;
    }// addVec
	
} 