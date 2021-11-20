package shop.ourshopping.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import shop.ourshopping.service.MemberService;

// 로그인 실패시 발생하는 이벤트로 계정 잠금 및 실패 메세지 전달
@Component
public class MemberFailureHandler implements AuthenticationFailureHandler {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String loginFailMsg;
		if (exception instanceof LockedException) {
			loginFailMsg = exception.getMessage();
		} else if (exception instanceof SessionAuthenticationException) {
			loginFailMsg = "이미 접속 중이거나 잘못된 방법으로 로그아웃하였을 경우 약 1분간의 시간이 필요합니다.";
		} else {
			String email = request.getParameter("email");
			if (memberService.lockCount(email)) {
				int lockCount = memberService.searchLock(email);
				if (lockCount >= 3) {
					loginFailMsg = "비밀번호 횟수 초과하였습니다. 비밀번호 찾기를 진행해주세요.";
				} else {
					loginFailMsg = "계정 잠금까지 " + (3 - lockCount) + "번 남았습니다.";
				}
			} else {
				loginFailMsg = "존재하지 않는 이메일입니다.";
			}
		}
		// sendRedirect(redirect 방식 : 새로운 페이지)
		// getRequestDispatcher(forward 방식 : request값 유지)
		request.getSession().setAttribute("loginFailMsg", loginFailMsg);
		response.sendRedirect(request.getContextPath()+"/member/login");
		// request.setAttribute("loginFailMsg", exception.getMessage());
		// request.getRequestDispatcher("/member/login").forward(request, response);
	}
}