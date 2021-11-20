package shop.ourshopping.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shop.ourshopping.constant.MessageMethod;

/*
 *  form을 통하여 redirect로 전송할 수 없는
 *  post, put, delete의 작업을 위한 부모 클래스
 */
public class BasicController {

	protected String messageRedirect(String redirectUri, MessageMethod method, Map<String, Object> params, Model model) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("method", method);
		model.addAttribute("params", params);

		return "thymeleaf/utils/redirect";
	}

	protected String messageRedirect(String message, String redirectUri, MessageMethod method, Map<String, Object> params,
			RedirectAttributes redirectAttributes) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("redirectUri", redirectUri);
		redirectAttributes.addFlashAttribute("method", method);
		redirectAttributes.addFlashAttribute("params", params);

		return "redirect:/message";
	}
}
