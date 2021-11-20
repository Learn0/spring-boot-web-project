package shop.ourshopping.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

// 인증 코드 생성 및 메일 전송
@PropertySource("classpath:/application-email.properties")
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	// 메모리 낭비를 줄이기 위해 DB로 사용할 예정 
	private static Map<String, String> code = new HashMap<String, String>();
	
	public String getCode(String email) {
		return EmailServiceImpl.code.get(email);
	}

	@Value("${adminMail.id}")
	private String email;
	
	private final JavaMailSender emailSender;

	private MimeMessage createMessage(String to) throws Exception {
		MimeMessage message = emailSender.createMimeMessage();
		code.put(to, createKey());
		message.setSubject("Our Shopping 확인 코드: " + code.get(to)); // 제목
		String msg = "<h1 style='font-size:30px; padding-right:30px; padding-left:30px'>이메일주소 확인</h1>"
				+ "<p style='font-size:17px; padding-right:30px; padding-left:30px'>아래 코드를 입력해주세요.</p>"
				+ "<div style='padding-right:30px; padding-left:30px; margin:32px 0 40px'>"
				+ "<h3 style='text-decoration:none; color:blue'>" + code.get(to) + "</h3>" + "</div>";
		message.addRecipients(RecipientType.TO, to); // 보내는 대상
		message.setText(msg, "utf-8", "html"); // 내용
		message.setFrom(new InternetAddress(email, "our-shoppping")); // 보내는 사람

		return message;
	}

	private String createKey() {
		StringBuffer key = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) { // 인증코드 6자리
			key.append((random.nextInt(10)));
		}

		return key.toString();
	}

	@Override
	public boolean sendSimpleMessage(String to) {
		try {
			MimeMessage message = createMessage(to);
			emailSender.send(message);

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}
}