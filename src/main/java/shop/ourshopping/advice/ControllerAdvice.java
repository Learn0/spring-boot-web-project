package shop.ourshopping.advice;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 컨트롤러 관련 예외 처리
// @RestControllerAdvice를 사용할 경우 Rest까지 예외처리
@RestControllerAdvice
public class ControllerAdvice {

	/*
	값을 화면으로 보내거나 리턴할 때 String 사용
	@ExceptionHandler(Exception.class)
	public String exception(Exception e) {
	
		return e.getClass() + "컨트롤러에서 에러 발생";
	}
	*/

	@ExceptionHandler(Exception.class)
	public void exceptionHandler(Exception e) {
		System.err.println("============Exception Error============");
		log(e);
	}

	@ExceptionHandler(RuntimeException.class)
	public void runtimeExceptionHandler(RuntimeException e) {
		System.err.println("============RuntimeException Error============");
		log(e);
	}

	@ExceptionHandler(SQLException.class)
	public void sqlExceptionHandler(SQLException e) {
		System.err.println("============SQLException Error============");
		log(e);
	}
	
	private void log(Exception e) {
		System.err.println("Class : " + e.getClass());
		System.err.println("Message : " + e.getMessage());
	}
}
