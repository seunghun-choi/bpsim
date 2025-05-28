package bpsim.framework.aop;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * '@Aspect'를 이용하여 Dao클래스가 처리되는 과정을 로그로 보여준다 
 *  (실서버 운영 시 제외 대상)
 * @author zero
 * @version 1.0
 */
@Aspect
public class LoggingServiceAspect {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	private static final Logger logger = LoggerFactory.getLogger(LoggingServiceAspect.class);
	
	@Before("execution(public * bpsim.module..*Service.*()) || execution(public * bpsim.module..*Service.*(..))")
	public void beforeLogging(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		
		//logger.debug("[S]["+sdf.format(new Date())+"]  <START > - " + className + " > " + methodName);
	}

	@AfterReturning(pointcut="execution(public * bpsim.module..*Service.*()) || execution(public * bpsim.module..*Service.*(..))", returning="ret")
	public void returningLogging(JoinPoint joinPoint, Object ret) {
		//logger.debug(" ");
		if(ret == null){
			//logger.debug("[S]["+sdf.format(new Date())+"]  <RETURN> - null");
		}else{
			//logger.debug("[S]["+sdf.format(new Date())+"]  <RETURN> - " + ret.getClass().getName());
		}
	}

	@AfterThrowing(pointcut="execution(public * bpsim.module..*Service.*()) || execution(public * bpsim.module..*Service.*(..))", throwing="ex")
	public void throwingLogging(JoinPoint joinPoint, Throwable ex) {
		logger.debug("[S]["+sdf.format(new Date())+"] Exception Occured : " + ex.getMessage());
	}

	@After("execution(public * bpsim.module..*Service.*()) || execution(public * bpsim.module..*Service.*(..))")
	public void afterLogging(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		//logger.debug("[S]["+sdf.format(new Date())+"]  <END   > - " + className + " > " + methodName);
	}
}
