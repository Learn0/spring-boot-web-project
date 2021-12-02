package shop.ourshopping.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import shop.ourshopping.utils.StaticUtil;

// 로그아웃시 발생하는 이벤트로 로그인을 체크하는 비동기 스레드 중단
@Component
public class MemberLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if(StaticUtil.numberListTask != null) {
			StaticUtil.numberListTask.cancel(false);
			StaticUtil.numberListTask = null;
		}
	}
}