package bpsim.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import egovframework.rte.fdl.idgnr.impl.Base64;


/**
 * Double Submit 방지 Token 생성 클래스
 * 
 * @author bcchung
 * @since 2013.04.04
 * @version 1.0
 * @see
 * 
 *      
 * << 개정이력(Modification Information) >>
 *   
 *   수정일         수정자           수정내용
 *  -------       --------    ---------------------------
 *  2013.04.04    정백철          최초 생성
 * 
 * 

 */
public class TokenMngUtil {

	private static final String TOKEN_KEY = "TOKEN_KEY";
	private static final Logger logger = Logger.getLogger(TokenMngUtil.class.getName());

	/**
	 * 로직처리를 위해 세션과 request에 Token 생성
	 * 
	 * @param request
	 */
	public static void saveToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		long systemTime = System.currentTimeMillis();
		byte[] time = new Long(systemTime).toString().getBytes();
		byte[] id = session.getId().getBytes();

		try {
			MessageDigest SHA = MessageDigest.getInstance("SHA-256");
			SHA.update(id);
			SHA.update(time);

			String token = Base64.encode(SHA.digest());
			request.setAttribute(TOKEN_KEY, token);
			session.setAttribute(TOKEN_KEY, token);

			//logger.error("#########################################################################");
			//logger.error("# Generate Token Key Value = " + token + " #");
			//logger.error("#########################################################################");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 로직처리 이후 중복방지를 위해 세션의 Token 초기화
	 * 
	 * @param request
	 */
	public static void resetToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		try {
			session.removeAttribute(TOKEN_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 세션과 request의 Token이 동일한지 비교
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isTokenValid(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String requestToken = request.getParameter(TOKEN_KEY);
		String sessionToken = (String) session.getAttribute(TOKEN_KEY);

		if (requestToken == null || sessionToken == null) {
			return false;
		} else {
			return requestToken.equals(sessionToken);
		}
	}
}
