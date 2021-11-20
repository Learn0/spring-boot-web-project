package shop.ourshopping.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * Controller, Service, DAO, Mapper 등이 실행될 때
 * 어떤 메서드가 실행되었는지 보여주는 역할 
 */
@Component
@Aspect
public class LoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* shop.ourshopping..controller.*Controller.*(..)) or execution(* shop.ourshopping..service.*Impl.*(..)) or execution(* shop.ourshopping..dao.*DAO.*(..)) or execution(* shop.ourshopping..mapper.*Mapper.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		
		long start = System.currentTimeMillis();
		Object obj = joinPoint.proceed();
		long end = System.currentTimeMillis() - start;
		
		if (name.contains("Controller")) {
			type = "Controller ===> ";
		} else if (name.contains("Service")) {
			type = "ServiceImpl ===> ";
		} else if (name.contains("DAO")) {
			type = "DAO ===> ";
		} else if (name.contains("Mapper")) {
			type = "Mapper ===> ";
		}
		logger.info(type + name + "." + joinPoint.getSignature().getName() + "() MilliSecond:"+end);
		
		return obj;
	}
}