package shop.ourshopping.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
/*
 *  빈 구성으로 등록하지 않은 클래스에서 
 *  HttpServletRequest를 사용할 수 있도록 설정
 */
public class HttpServletRequestUtils extends RequestContextListener {
   
	public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}