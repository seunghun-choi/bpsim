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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HTMLTagFilter implements Filter{
	
	private FilterConfig config;	
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		/**
		 * HTTPS => HTTP로 redirect할때 세션이 공유되지 않는 문제가 발생한다.
		 * 이 문제를 처리를 하기위해 HTMLTagFilterRequestWrapper 를 사용한다.
		 */
		HTMLTagFilterRequestWrapper httpsRequest = new HTMLTagFilterRequestWrapper((HttpServletRequest)request);
		httpsRequest.setResponse((HttpServletResponse)response);
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
//        httpServletResponse.setHeader("Content-Security-Policy", "default-src 'script-src *' 'style-src 'self' img-src *");
//        httpServletResponse.setHeader("Content-Security-Policy", "default-src 'unsafe-inline' 'unsafe-eval' *");
        httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
//        httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
		
		chain.doFilter(httpsRequest, response);		
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;		
	}
	
	public void destroy() {
		
	}
}
