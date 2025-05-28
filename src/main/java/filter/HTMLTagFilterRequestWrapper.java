/*
 * Copyright 2008-2009 MOPAS(Ministry of Public Administration and Security).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HTMLTagFilterRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletResponse response = null;

	public HTMLTagFilterRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {

		String[] values = super.getParameterValues(parameter);
		
		if(values==null){
			return null;			
		}
		
		for (int i = 0; i < values.length; i++) {			
			if (values[i] != null) {				
				StringBuffer strBuff = new StringBuffer();
				for (int j = 0; j < values[i].length(); j++) {
					char c = values[i].charAt(j);
					switch (c) {
					case '<':
						strBuff.append("&lt;");
						break;
					case '>':
						strBuff.append("&gt;");
						break;
					//case '&':
						//strBuff.append("&amp;");
						//break;
					case '"':
						strBuff.append("&quot;");
						break;
					case '\'':
						strBuff.append("&apos;");
						break;
					default:
						strBuff.append(c);
						break;
					}
				}				
				values[i] = strBuff.toString();
			} else {
				values[i] = null;
			}
		}

		return values;
	}

	public String getParameter(String parameter) {
		
		String value = super.getParameter(parameter);
		
		if(value==null){
			return null;
		}
		
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '<':
				strBuff.append("&lt;");
				break;
			case '>':
				strBuff.append("&gt;");
				break;
			case '&':
				strBuff.append("&amp;");
				break;
			case '"':
				strBuff.append("&quot;");
				break;
			case '\'':
				strBuff.append("&apos;");
				break;	
			default:
				strBuff.append(c);
				break;
			}
		}
		value = strBuff.toString();
		
		return value;
	}
	
	public void setResponse (HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpSession getSession () {
		HttpSession session = super.getSession();
		processSessionCookie(session);
		return session;
	}
	 
	public HttpSession getSession (boolean create) {
		HttpSession session = super.getSession(create);
		processSessionCookie(session);
		return session;
	}

	private void processSessionCookie (HttpSession session){
		if (null == response || null == session) {
			return;
		}
		
		Object cookieOverWritten = getAttribute("COOKIE_OVERWRITTEN_FLAG");
		
		if (null == cookieOverWritten && isSecure() && isRequestedSessionIdFromCookie() && session.isNew()) {
			Cookie cookie = new Cookie("JSESSIONID", session.getId());
			cookie.setMaxAge(-1);
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
			
			String contextPath = getContextPath();
			if ((contextPath != null) && (contextPath.length() > 0)) {
				cookie.setPath(contextPath);
			} else {
				cookie.setPath("/");
			}   
			response.addCookie(cookie);
			setAttribute("COOKIE_OVERWRITTEN_FLAG", "true");
		}
	}
}