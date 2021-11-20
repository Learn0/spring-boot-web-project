package shop.ourshopping.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import shop.ourshopping.constant.MemberRole;
import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.service.RestaurantService;
import shop.ourshopping.vo.RestaurantVO;

// 레스토랑과 관련된 기능을 동작
@Controller
@RequestMapping("/restaurant")
public class RestaurantController extends BasicController {

	@Resource(name = "restaurantServiceImpl")
	private RestaurantService restaurantService;

	@GetMapping("/list")
	public String restaurantList(@ModelAttribute final SearchDTO searchDTO, Model model) {
		List<RestaurantVO> restaurantList = restaurantService.searchRestaurant(searchDTO);
		String[] hashtagList = restaurantService.restaurantHashtag();
		model.addAttribute("restaurantList", restaurantList);
		model.addAttribute("hashtagList", hashtagList);

		return "restaurant/list";
	}

	@GetMapping("/view")
	public String viewRestaurant(@RequestParam(value = "idx") final Integer idx, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes, Authentication authentication,
			Model model) {
		int memberIdx = 0;
		if (authentication != null && authentication.isAuthenticated()) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return messageRedirect("세션 유지시간이 지났습니다.", "/member/logout", MessageMethod.post, null,
						redirectAttributes);
			}
			memberIdx = (Integer) session.getAttribute("memberIdx");
			String authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
			if (authorities.equals(MemberRole.ADMIN.getValue())) {
				model.addAttribute("admin", true);
			}
		}

		Cookie[] cookies = request.getCookies();
		List<Integer> count = new ArrayList<Integer>();
		String cookieKey = "restaurant" + memberIdx;
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().contains(cookieKey)) {
					if (cookies[i].getName().equals(cookieKey + idx)) {
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
						continue;
					}
					count.add(i);
				}
			}
			while (count.size() > 5) {
				cookies[count.get(0)].setMaxAge(0);
				response.addCookie(cookies[count.get(0)]);
				count.remove(0);
			}
		}

		RestaurantVO restaurantVO = restaurantService.selectRestaurant(idx);
		model.addAttribute("restaurantVO", restaurantVO);

		String area = restaurantVO.getAddress();
		for (int i = 0; i < 2; i++) {
			if (area.contains(" ")) {
				String save = area.substring(area.indexOf(" ") + 1);
				if (save.contains(" ")) {
					area = save;
				}
			}
		}
		area = area.substring(0, area.indexOf(" ") + 1);
		List<RestaurantVO> areaList = restaurantService.searchRestaurantArea(idx, area);
		model.addAttribute("areaList", areaList);

		List<RestaurantVO> historyList;
		if (count.size() > 0) {
			List<Integer> cookieList = new ArrayList<Integer>();
			for (int i = 0; i < count.size(); i++) {
				cookieList.add(0, Integer.parseInt(cookies[count.get(i)].getValue()));
			}
			historyList = restaurantService
					.searchRestaurantHistory((Integer[]) cookieList.toArray(new Integer[cookieList.size()]));
		} else {
			historyList = new ArrayList<RestaurantVO>();
		}
		model.addAttribute("historyList", historyList);

		Cookie cookie = new Cookie(cookieKey + idx, idx.toString());
		cookie.setMaxAge(60 * 60 * 24 * 7);
		// cookie.setPath("/");
		// cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		Gson gson = new Gson();
		model.addAttribute("evaluationCheck",
				gson.toJson(restaurantService.restaurantEvaluation(restaurantVO.getIdx(), memberIdx)));

		String mapAddress = restaurantVO.getAddress();
		while (mapAddress.matches("(.*)[0-9][Ff](.*)") || mapAddress.matches("(.*)[0-9]층(.*)")
				|| mapAddress.matches("(.*)[0-9]호(.*)")) {
			mapAddress = mapAddress.substring(0, mapAddress.lastIndexOf(" "));
		}
		model.addAttribute("mapAddress", mapAddress);

		return "restaurant/view";
	}

	@GetMapping("/viewCount")
	public String viewCount(@RequestParam(value = "idx") final Integer idx) {
		restaurantService.restaurantViewCount(idx);

		return "redirect:/restaurant/view?idx=" + idx;
	}

	@PostMapping("/evaluation")
	public ResponseEntity<String> checkEvaluation(@RequestParam(value = "restaurantIdx") final Integer restaurantIdx,
			@RequestParam(value = "evaluation") final String evaluation,
			HttpSession session) {
		int memberIdx = (Integer)session.getAttribute("memberIdx");
		restaurantService.restaurantEvaluationInsert(restaurantIdx, evaluation, memberIdx);

		Gson gson = new Gson();
		return new ResponseEntity<String>(
				gson.toJson(restaurantService.restaurantEvaluation(restaurantIdx, memberIdx)),
				HttpStatus.OK);
	}
}