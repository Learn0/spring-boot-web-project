package shop.ourshopping.controller;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.manager.JsonManager;
import shop.ourshopping.service.RestaurantService;
import shop.ourshopping.utils.RestAPIUtils;

// 메인 페이지와 미완성한 url을 모아놓은 컨트롤러
@PropertySource("classpath:/application-naver.properties")
@Controller
public class MainController extends BasicController {

	@Resource(name = "restaurantServiceImpl")
	private RestaurantService restaurantService;
	@Autowired
	private JsonManager jsonManager;
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String id;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String secret;

	@GetMapping("/")
	public String main(HttpServletRequest request, RedirectAttributes redirectAttributes,
			Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			HttpSession session = request.getSession(false);
			if (session == null) {

				return messageRedirect("세션 유지시간이 지났습니다.", "/member/logout", MessageMethod.post, null,
						redirectAttributes);
			}
		}

		// 네이버API를 이용한 AI검색
		String keyword = "맛집";
		String address = "";
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!keyword.isEmpty()) {
			Map<String, String> requestHeaders = new HashMap<String, String>();
			requestHeaders.put("X-Naver-Client-Id", id);
			requestHeaders.put("X-Naver-Client-Secret", secret);

			String url = "https://openapi.naver.com/v1/search/blog?query=" + keyword;
			String responseBody = RestAPIUtils.get(url, requestHeaders);

			List<String> descriptionList = jsonManager.naverSearchJson(responseBody);
			List<String> titleList = restaurantService.searchRestaurantTitle(address);

			int[] count = new int[titleList.size()];
			Pattern[] pattern = new Pattern[titleList.size()];

			// 타이틀로 패턴 설정
			for (int i = 0; i < pattern.length; i++) {
				pattern[i] = Pattern.compile(titleList.get(i) + "*");
			}
			Matcher[] matcher = new Matcher[titleList.size()];
			for (String description : descriptionList) {
				for (int i = 0; i < matcher.length; i++) {
					matcher[i] = pattern[i].matcher(description);
					// 문자열 안에 타이틀이 포함되었는지 체크
					while (matcher[i].find()) {
						count[i] += 1;
					}
				}
			}

			int[] rank = new int[count.length];
			for(int i = 0; i < count.length; i++) {
				for(int j = 0; j < count.length; j++) {
					if(i == j || count[i] < count[j]) {
						rank[i] += 1;
					}else if(count[i] == count[j] && i > j) {
						rank[i] += 1;
					}
				}
				System.out.println(rank[i]+"위 " + count[i] + "번 체크 "+ titleList.get(i));
			}
		}

		return "thymeleaf/index";
	}

	@GetMapping("/message")
	public String message() {

		return "thymeleaf/utils/redirect";
	}

	@GetMapping("/serviceTerms")
	public String servieTerms() {

		return "thymeleaf/utils/serviceTerms";
	}

	@GetMapping("/personalInformation")
	public String personalInformation() {

		return "thymeleaf/utils/personalInformation";
	}

	@GetMapping("/calendar/view")
	public String viewCalendar(@RequestParam(value = "year", required = false) final String year,
			@RequestParam(value = "month", required = false) final String month, Model model) {
		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		int nowMonth = cal.get(Calendar.MONTH) + 1;
		int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		model.addAttribute("nowYear", nowYear);
		model.addAttribute("nowMonth", nowMonth);
		model.addAttribute("nowDay", nowDay);

		int year_;
		int month_;
		if (year != null || month != null) {
			year_ = Integer.parseInt(year);
			month_ = Integer.parseInt(month);
		} else {
			year_ = nowYear;
			month_ = nowMonth;
		}
		model.addAttribute("year", year_);
		model.addAttribute("month", month_);

		return "calendar/view";
	}
}