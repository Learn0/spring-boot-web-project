package shop.ourshopping.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shop.ourshopping.service.EmailService;

// 이메일 인증 요청 등의 기능을 위하여 ajax로 주고 받으며 동작
@RestController
public class EmailController {

	@Resource(name = "emailServiceImpl")
	private EmailService emailService;

	@PostMapping("/member/emailSend")
	public boolean emailSend(@RequestParam(value = "email") final String email) {
		return emailService.sendSimpleMessage(email);
	}

	@PostMapping("/member/emailCheck")
	public boolean emailCheck(@RequestParam(value = "emailCode") final String emailCode, @RequestParam(value = "email") final String email) {
		return emailService.getCode(email).equals(emailCode);
	}
}