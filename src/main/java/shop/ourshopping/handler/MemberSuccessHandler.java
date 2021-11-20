package shop.ourshopping.handler;

import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;
import inet.ipaddr.ipv6.IPv6Address;
import shop.ourshopping.component.AsyncCheck;
import shop.ourshopping.entity.MemberEntity;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.utils.StaticUtils;

/*
 * 로그인 성공시 발생하는 이벤트로 이동할 url 지정,
 * 락이 걸려있는지 체크, 데이터 일부를 세션에 저장 등 여러 기능을 동작
 */
@Component
public class MemberSuccessHandler implements AuthenticationSuccessHandler {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Autowired
	private AsyncCheck asyncCheck;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		MemberEntity memberEntity = (MemberEntity) request.getAttribute("memberEntity");
		if (memberEntity.getDeleteCheck().equals("Y")) {
			throw new LockedException("탈퇴한 계정입니다. 관리자에게 문의하세요.");
		} else if (memberEntity.getLockCount() >= 3) {
			throw new LockedException("비밀번호 횟수 초과하였습니다. 비밀번호 찾기를 진행해주세요.");
		}
		Integer idx = memberEntity.getIdx();
		memberService.loginSuccess(idx);

		String ip = request.getRemoteAddr();
		try {
			IPAddressString str = new IPAddressString(ip);
			IPv6Address addr = str.getAddress().toIPv6();
			if (addr.isIPv4Compatible() || addr.isIPv4Mapped()) {
				IPv4Address derivedIpv4Address = addr.getEmbeddedIPv4Address();
				ip = derivedIpv4Address.toString();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);
		session.setAttribute("memberIdx", memberEntity.getIdx());
		session.setAttribute("email", memberEntity.getEmail());
		session.setAttribute("nickname", memberEntity.getNickname());
		session.setAttribute("photo", memberEntity.getPhoto());
		session.setAttribute("clientIp", ip);

		Cookie cookie = new Cookie("memberIdx", URLEncoder.encode(memberEntity.getIdx().toString(), "UTF-8"));
		cookie.setMaxAge(60 * 60 * 24 * 7);
		cookie.setPath("/");
		// cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		String url = (String) session.getAttribute("prevPage");
		if (url != null) {
			session.removeAttribute("prevPage");
		} else {
			RequestCache requestCache = new HttpSessionRequestCache();
			SavedRequest savedRequest = requestCache.getRequest(request, response);
			if (savedRequest != null) {
				url = savedRequest.getRedirectUrl();
				if (url.contains("member") || url.contains("upload")) {
					url = request.getContextPath() + "/";
				}
			} else {
				url = request.getContextPath() + "/";
			}
		}
		StaticUtils.numberListTask = asyncCheck.process();

		response.sendRedirect(url);
	}
}