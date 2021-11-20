package shop.ourshopping.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shop.ourshopping.db.DBConnection;

// AOP를 설정하여 JDBC에서 예외 발생시 Rollback 처리
@Component
@Aspect
public class DBAspect {

	@Autowired
	private DBConnection conn;

	@Before("execution(* shop.ourshopping..dao.*DAO.*(..))")
	public void before() {
		conn.getConnection();
	}

	@AfterThrowing("execution(* shop.ourshopping..dao.*DAO.*(..))")
	public void afterThrowing() {
		conn.rollback();
	}

	@After("execution(* shop.ourshopping..dao.*DAO.*(..))")
	public void after() {
		conn.disConnection();
	}
	/*
	 * @Around("execution(* shop.ourshopping..controller.*Controller.*(..))") public
	 * Object around(ProceedingJoinPoint joinPoint) throws Throwable { Object[] ob =
	 * joinPoint.getArgs(); HttpServletRequest request = (HttpServletRequest)ob[0];
	 * request.setAttribute("jdbc", "JDBC형식은 사용하지마세요"); return joinPoint.proceed();
	 * }
	 */
}
