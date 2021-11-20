package shop.ourshopping.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.manager.JsonManager;
import shop.ourshopping.manager.JsoupManager;
import shop.ourshopping.manager.XmlDOMManager;
import shop.ourshopping.parsingVO.AreaVO;

// 관리자 계정의 전용 컨트롤러
@Controller
@AllArgsConstructor
public class AdminController extends BasicController {

	private JsonManager jsonManager;
	private JsoupManager jsoupManager;
	private XmlDOMManager xmlDomManager;

	@PostMapping("/admin/music/download")
	public String downloadMusic(RedirectAttributes redirectAttributes) {
		if (jsoupManager.downloadMusic()) {
			return messageRedirect("다운로드에 성공하셨숩니다.", "/music/list", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("다운로드에 실패하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}

	@GetMapping("/admin/restaurant/search")
	public String searchRestaurant(Model model) {
		model.addAttribute("subwayList", jsonManager.subwayJson());

		return "restaurant/search";
	}

	@PostMapping("/admin/restaurant/download")
	public String downloadRestaurant(@RequestParam(value = "searchKeyword") final String searchKeyword,
			RedirectAttributes redirectAttributes) {
		if (searchKeyword.equals("ALL")) {
			String[] array = jsonManager.subwayJson();
			for (String subway : array) {
				jsoupManager.downloadRestaurant(subway);
			}

			List<AreaVO> list = xmlDomManager.areaXML();
			for (AreaVO vo : list) {
				for (String borough : vo.getBorough()) {
					jsoupManager.downloadRestaurant(borough);
				}
			}
			return messageRedirect("다운로드에 성공하셨숩니다.", "/restaurant/list?reset=true", MessageMethod.get, null,
					redirectAttributes);
		} else {
			if (jsoupManager.downloadRestaurant(searchKeyword)) {
				return messageRedirect("다운로드에 성공하셨숩니다.", "/restaurant/list?reset=true", MessageMethod.get, null,
						redirectAttributes);
			} else {
				return messageRedirect("다운로드에 실패하셨숩니다.", "/admin/restaurant/search", MessageMethod.get, null,
						redirectAttributes);
			}
		}
	}

	@PostMapping("/admin/recipe/download")
	public String downloadRecipe(RedirectAttributes redirectAttributes) {
		if (jsoupManager.downloadRecipe()) {
			return messageRedirect("다운로드에 성공하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("다운로드에 실패하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}

	@PostMapping("/admin/movie/download")
	public String downloadMovie(RedirectAttributes redirectAttributes) {
		if (jsoupManager.downloadMovie()) {
			return messageRedirect("다운로드에 성공하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("다운로드에 실패하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}

	@PostMapping("/admin/attractions/download")
	public String downloadAttractions(RedirectAttributes redirectAttributes) {
		if (jsoupManager.downloadAttractions()) {
			return messageRedirect("다운로드에 성공하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("다운로드에 실패하셨숩니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}
}