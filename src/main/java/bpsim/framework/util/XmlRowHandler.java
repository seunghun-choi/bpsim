package bpsim.framework.util;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.ibatis.sqlmap.client.event.RowHandler;

public class XmlRowHandler implements RowHandler {

	/**
	 * iBatis에서 select 하면 나오는 XML 추출하기 위한것.
	 */
	public Document doc;
	public Element e;
	public SAXBuilder builder;

	// 생성자
	// 생성자로 xml의 헤더와 루트 엘리먼트 생성.
	public XmlRowHandler(String xmlRootName){
		builder = new SAXBuilder();
		
		e = new Element(xmlRootName);
		doc = new Document();
		doc.setRootElement(e);
	}
	
	/**
	 * iBatis 에서 날아온 xml의 값을 새로운 엘리먼트로 작성.
	 * ex) 
	 * 	<?xml version="1.0" encoding="euc-kr"?>
	 * <board>
	 * 		<num>1</num>
	 * 		<title>title</title>
	 * 		<content>content</content>
	 * 		<input_date>2007</input_date>
	 * <board>
	 * 
	 * 하나하나 만들어야함.
	 * boardroot
	 * <board>
	 * 		<num>1</num>
	 * 		<title>title</title>
	 * 		<content>content</content>
	 * 		<input_date>2007</input_date>
	 * </board>
	 */
	
	public void handleRow(Object xmlContent) {
		
		
		//xmlContent : xml 날라온 문자열
		StringReader stringXml = new StringReader((String)xmlContent);
		
		try{
			Document xmlDoc;															// xml 문서
			xmlDoc = builder.build(stringXml);											// 문자열로된 xml 내용을 XML로 변환
			Element boardRoot = new Element(xmlDoc.getRootElement().getName());			// 'board' 이름으로 Element 하나만들기
			List childList = xmlDoc.getRootElement().getChildren();						// num, title, content, input_date Element List에 넣기
			
			for( int i = 0 ; i < childList.size(); i++ ){
				Element child = new Element(((Element)childList.get(i)).getName());
				child.addContent(xmlDoc.getRootElement().getChildText(child.getName()));
				boardRoot.addContent(child);
			}
			
			e.addContent(boardRoot);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Document getDocument(){
		return doc;
	}

	// 콘솔로 화면에 출력
	public void consolePrintOutput(){
		try{
			XMLOutputter op = new XMLOutputter();
			
			Format format = Format.getCompactFormat();
			op.setFormat(format.setIndent("    "));
			op.setFormat(format.setEncoding("UTF-8"));
			op.output(doc, System.out);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 파일로 화면에 출력
	/*
	public void filePrintOutput(){
		try{
			XMLOutputter op = new XMLOutputter();
			
			Format format = Format.getCompactFormat();
			op.setFormat(format.setIndent("    "));
			op.setFormat(format.setEncoding("UTF-8"));
			
			FileWriter fout = new FileWriter(new File("C:/temp"));
			op.output(doc, fout);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String toString(){
		String xmlStr = null;
		try{
			XMLOutputter out = new XMLOutputter();
			Format format = Format.getCompactFormat();
			out.setFormat(format.setIndent("    "));
			out.setFormat(format.setEncoding("UTF-8"));
			xmlStr = out.outputString(doc);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xmlStr;
	}
	
	// 파일로  출력
	public void filePrintOutput(String fileName){
		try{
			XMLOutputter op = new XMLOutputter();
			
			Format format = Format.getCompactFormat();
			op.setFormat(format.setIndent("    "));
			op.setFormat(format.setEncoding("UTF-8"));
			FileWriter fout = new FileWriter(new File("chartXml/"+fileName));
			op.output(doc, fout);
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	*/
}
