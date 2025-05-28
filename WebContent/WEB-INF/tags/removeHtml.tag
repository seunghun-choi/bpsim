<%@ tag body-content="scriptless" pageEncoding="utf-8" %>
<%@ attribute name = "length" type="java.lang.Integer"%>
<%@ attribute name = "trail" %>
<%@ attribute name = "trim" %>

<jsp:doBody var="content" scope="page" />
<%
 String content = (String)jspContext.getAttribute("content");
String contentCut = "";
 if(trim !=null && trim.equals("true")){
	 content = content.trim();
 }
 
 //content = content.replaceAll("더보기&nbsp;&raquo;","");
 ////content = content.replaceAll("&apos","'");
 //content = content.replaceAll("(?:<!.*?(?:--.*?--\\s*)*.*?>)|(?:<(?:[^>'\"]*|\".*?\"|'.*?')+>)","");
 //content = content.replaceAll("<font jquery","");
 //content = content.replaceAll("Andamp;amp;","&");
 final int NORMAL_STATE = 0;
 final int TAG_STATE = 1;
 final int START_TAG_STATE = 2;
 final int END_TAG_STATE = 3;
 final int SINGLE_QUOT_STATE = 4;
 final int DOUBLE_QUOT_STATE = 5;
 int state = NORMAL_STATE;
 int oldState = NORMAL_STATE;
 
 content = content.replaceAll("&nbsp;","");
 
 char[] chars = content.toCharArray();
 StringBuffer sb = new StringBuffer();
 char a;
 int e_cut_length = 0;
 int cut_length = 1;
  for (int i = 0; i < chars.length; i++) {
     a = chars[i];
     switch (state) {
         case NORMAL_STATE:
             if (a == '<')
                 state = TAG_STATE;
             else {
            	 
              	 if( length != null && length !=  1){
	            	 Character.UnicodeBlock block = Character.UnicodeBlock.of(a);
	            	 if( block == Character.UnicodeBlock.HANGUL_SYLLABLES
	            	    || block == Character.UnicodeBlock.HANGUL_JAMO
	            	    || block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ) {
	            		 cut_length++;
	            	 	 length--;
            	 	 } else {
            	 		 if(e_cut_length == 1) {
							cut_length++;
		            	 	length--;
		            	 	e_cut_length = 0;
	            		 } else {
							cut_length++;
	            			e_cut_length = 1;
	            		 }
            	 	 }
            	 }
                 sb.append(a);
             }
             break;
         case TAG_STATE:
             if (a == '>')
                 state = NORMAL_STATE;
             else if (a == '\"') {
                 oldState = state;
                 state = DOUBLE_QUOT_STATE;
             }
             else if (a == '\'') {
                 oldState = state;
                 state = SINGLE_QUOT_STATE;
             }
             else if (a == '/')
                 state = END_TAG_STATE;
             else if (a != ' ' && a != '\t' && a != '\n' && a != '\r' && a != '\f')
                 state = START_TAG_STATE;
             break;
         case START_TAG_STATE:
         case END_TAG_STATE:
             if (a == '>')
                 state = NORMAL_STATE;
             else if (a == '\"') {
                 oldState = state;
                 state = DOUBLE_QUOT_STATE;
             }
             else if (a == '\'') {
                 oldState = state;
                 state = SINGLE_QUOT_STATE;
             }
             else if (a == '\"')
                 state = DOUBLE_QUOT_STATE;
             else if (a == '\'')
                 state = SINGLE_QUOT_STATE;
             break;
         case DOUBLE_QUOT_STATE:
             if (a == '\"')
                 state = oldState;
             break;
         case SINGLE_QUOT_STATE:
             if (a == '\'')
                 state = oldState;
             break;
     }
 }
 
 content = sb.toString();
 //if(length != null && length.intValue() > 0 && content.length() > length.intValue()){
 if(length != null && cut_length > 0 && content.length() > cut_length){
	 
	 contentCut = content.substring(0, cut_length);
	 
	 if(trail != null){
		 content = contentCut + trail ;
		 //content = "<a style=\"{color:#000000;}\" title=\"" + content + "\">" + contentCut + trail + "</a>";
	 }else{
		 content = contentCut;
	 }
 }
 
 
%>
<%= content %>