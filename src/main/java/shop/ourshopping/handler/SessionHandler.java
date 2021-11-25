package shop.ourshopping.handler;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

// 세션을 체크하여 중복로그인 확인(Spring Security로 중복체크를 하기 때문에 주석처리)
@Component
public class SessionHandler implements HttpSessionListener {
	
	/*
	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

	// 중복로그인 지우기
	public synchronized static String getSessionidCheck(String type, String compareId) {
		String result = "";
		for (String key : sessions.keySet()) {
			HttpSession hs = sessions.get(key);
			if (hs != null && hs.getAttribute(type) != null && hs.getAttribute(type).toString().equals(compareId)) {
				result = key.toString();
			}
		}
		removeSessionForDoubleLogin(result);
		return result;
	}

	private static void removeSessionForDoubleLogin(String userId) {
		System.out.println("remove userId : " + userId);
		if (userId != null && userId.length() > 0) {
			sessions.get(userId).invalidate();
			sessions.remove(userId);
		}
	}
	 */

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// 세션 생성시
		/*
		sessions.put(se.getSession().getId(), se.getSession());
		*/
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// 세션 삭제시
		/*
		if (sessions.get(se.getSession().getId()) != null) {
			sessions.get(se.getSession().getId()).invalidate();
			sessions.remove(se.getSession().getId());
		}
		*/
	}
}
