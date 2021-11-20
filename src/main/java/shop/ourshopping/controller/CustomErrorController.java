package shop.ourshopping.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// 에러 코드에 알맞은 동작을 실행
@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String error(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute("javax.servlet.error.status_code");
	
		model.addAttribute("status", "상태:" + status);
		model.addAttribute("path", request.getAttribute("javax.servlet.error.request_uri"));
		model.addAttribute("timestamp", new Date());
		
		Object exceptionObj = request.getAttribute("javax.servlet.error.exception");
		if (exceptionObj != null) {
			Throwable e = ((Exception) exceptionObj).getCause();
			model.addAttribute("exception", e.getClass().getName());
			model.addAttribute("message", e.getMessage());
		}

		if (status.equals(HttpStatus.NOT_FOUND.value())) {
			return "thymeleaf/error/404";
		} else if (status.equals(405)) {
			return "redirect:/";
		} else {
			return "thymeleaf/error/500";
		}
	}
}