package bpsim.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpsim.module.dao.BpsimCommon;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 */
public class ValidUtil {
	private static final Logger logger = LoggerFactory.getLogger(ValidUtil.class);
	
	// 휴대폰 번호 검증
    public static boolean validPhoneNumber(String phone) {
        // 정규식 패턴
        String regex = "^010-\\d{4}-\\d{4}$";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        
        return matcher.matches();
    }
	
    // 이메일 검증 
    public static boolean validEmail(String email) {
        // 정규식 패턴
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
}
