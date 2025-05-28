package bpsim.framework.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageAlert {
	
	/**
     * 자바스트립트 (메세지)
     * @param res HttpServletResponse
     * @param message String
     * @param action String
     * @throws Exception
     */
    public static void scriptMessage(HttpServletResponse res,
                                         String message) throws Exception {
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        message = str2alert(message);
        out.println("<script language='javascript'>");
        out.println("alert('" + message + "');");
        out.println("</script>");       
        out.flush();
    }
    
    public static void MessageAndClose(HttpServletResponse res, 
    										String message) throws IOException {
    	res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        message = str2alert(message);
        out.println("<script language='javascript'>");
        out.println("alert('" + message + "');");
        out.println("self.close();");
        out.println("</script>");       
        out.flush();
	}    
    
    
    public static void closeAndRedirect(HttpServletRequest req, 
    		HttpServletResponse res, String message) throws IOException {
	    res.setContentType("text/html; charset=utf-8");
	    PrintWriter out = res.getWriter();
	    out.println("<html>");
	    out.println("<script>");
	    out.println("function goNext(){");
	    if(message != null && !message.trim().equals("")){
	        out.println("alert(\""+message+"\");");
	    }
	    out.println("self.close();");
	    //out.println("opener.location.href=\"" + serverRoot + "\";");
	    out.println("}</script>");
	    out.println("<body onload=\"goNext();\">");
	    out.println("</body></html>");
	}    
    
	/**
     * 자바스트립트 (메세지 + 액션)
     * @param res HttpServletResponse
     * @param message String
     * @param action String
     * @throws Exception
     */
    public static void scriptMessage(HttpServletResponse res,
                                         String message, String action) throws Exception {
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        message = str2alert(message);
        out.println("<script language='javascript'>");
        out.println("alert('" + message + "');");
        out.println(action);
        out.println("</script>");
        out.flush();
    }
     
    /**
     * 메세지내용 확인
     * @param s String
     */
    public static String str2alert(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        char[] c = s.toCharArray();
        int len = c.length;
        for (int i = 0; i < len; i++) {
            if (c[i] == '\n') {
                buf.append("\\n");
            } else if (c[i] == '\t') {
                buf.append("\\t");
            } else if (c[i] == '"') {
                buf.append("'");
            } else {
                buf.append(c[i]);
            }
        }
        return buf.toString();
    }
    /**
     * Redirect용 - 지정URL로
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param message String
     * @param actionUrl String
     * @throws IOException
     */
    public static void redirectForm(HttpServletRequest req, HttpServletResponse res, String message,
                                String actionUrl) throws
        IOException {
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("function goNext(){");
        if(message != null && !message.trim().equals("")){
            out.println("alert(\""+message+"\");");
        }
        out.println("parent.location.href=\"" + actionUrl + "\";");
        out.println("}</script>");
        out.println("<body onload=\"goNext();\">");
        out.println("</body></html>");

    }
    /**
     * Redirect용 - History.back();
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param message String
     * @param actionUrl String
     * @throws IOException
     */
    public static void redirectForm(HttpServletRequest req, HttpServletResponse res, String message) throws
        IOException {
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("function goNext(){");
        if(message != null && !message.trim().equals("")){
            out.println("alert(\""+message+"\");");
        }
        out.println("history.back(-1);");
        out.println("}</script>");
        out.println("<body onload=\"goNext();\">");
        out.println("</body></html>");
    }  
    /**
     * Redirect용 - Root페이지
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param message String
     * @param actionUrl String
     * @throws IOException
     */
    public static void redirectFormToRoot(HttpServletRequest req, HttpServletResponse res, String message) throws
        IOException {
    	String serverRoot = req.getScheme() + "://" + req.getServerName()+ ":" + req.getServerPort()+ req.getContextPath() + "/";
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("function goNext(){");
        if(message != null && !message.trim().equals("")){
            out.println("alert(\""+message+"\");");
        }
        out.println("parent.location.href=\"" + serverRoot + "\";");
        out.println("}</script>");
        out.println("<body onload=\"goNext();\">");
        out.println("</body></html>");
    }    
    /**
     * PopUp닫기 & Redirect용 - Root페이지
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param message String
     * @param actionUrl String
     * @throws IOException
     */
    public static void closeAndRedirectToRoot(HttpServletRequest req, HttpServletResponse res, String message) throws
        IOException {
    	String serverRoot = req.getScheme() + "://" + req.getServerName()+ ":" + req.getServerPort()+ req.getContextPath() + "/";
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("function goNext(){");
        if(message != null && !message.trim().equals("")){
            out.println("alert(\""+message+"\");");
        }
        out.println("self.close();");
        out.println("opener.location.href=\"" + serverRoot + "\";");
        out.println("}</script>");
        out.println("<body onload=\"goNext();\">");
        out.println("</body></html>");
    }    
}
