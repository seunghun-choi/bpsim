package bpsim.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import bpsim.module.dao.BpsimCommon;

public class EncryptPassword {
	public static String encrpytPassowrdSHA256(String str){
		String SHA = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			SHA = null; 
		}
		return SHA;
	}
	
	public static String getTempPassword(){
    	char[] charSet = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'
    			, 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p'
    			, 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y'
    			, 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    	StringBuffer temp_password = new StringBuffer();
    	for(int i = 0; i < 10; i++){
    		int index = (int) (charSet.length * Math.random());
    		temp_password.append(charSet[index]);
    	}
		return temp_password.toString();
	}
	
	public static boolean checkPassword(Map member, String password, boolean snsYn){
    	String passwordUser = ReqUtils.getEmptyResult2((String) member.get("password"),"");
		String passwordSHA256 = encrpytPassowrdSHA256(password);
    	if(snsYn) {
    		return true;
    	}else {
    		return passwordSHA256.equals(passwordUser);
    	}
    }
}
