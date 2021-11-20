package shop.ourshopping.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 인터셉터를 통해 로그 확인
public class LoggerInterceptor implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 해당 Controller 메소드 수행 전, @Before 비슷함
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("==================== BEGIN ====================");
		logger.info("Request URI ===> " + request.getRequestURI() + ":" + request.getMethod());
		return true;
	}

	// 해당 Controller 메소드 수행 후, @After-Returning 비슷함
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.info("==================== END ======================");
	}

	// Controller, View 수행 완료 후
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("================ VIEW PRINT ==================");
	}
}