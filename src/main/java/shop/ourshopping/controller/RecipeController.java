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
import shop.ourshopping.service.RecipeService;
import shop.ourshopping.vo.RecipeVO;

// 레시피와 관련된 기능을 동작
@Controller
@RequestMapping("/recipe")
public class RecipeController extends BasicController {

	@Resource(name = "recipeServiceImpl")
	private RecipeService recipeService;

	@GetMapping("/list")
	public String recipeList(@ModelAttribute final SearchDTO searchDTO, Model model) {
		List<RecipeVO> recipeList = recipeService.searchRecipe(searchDTO);
		String[] hashtagList = recipeService.recipeHashtag();
		model.addAttribute("recipeList", recipeList);
		model.addAttribute("hashtagList", hashtagList);

		return "recipe/list";
	}

	@GetMapping("/view")
	public String viewRecipe(@RequestParam(value = "idx") final Integer idx, HttpServletRequest request,
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
		String cookieKey = "recipe" + memberIdx;
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

		RecipeVO recipeVO = recipeService.selectRecipe(idx);
		model.addAttribute("recipeVO", recipeVO);

		List<RecipeVO> historyList;
		if (count.size() > 0) {
			List<Integer> cookieList = new ArrayList<Integer>();
			for (int i = 0; i < count.size(); i++) {
				cookieList.add(0, Integer.parseInt(cookies[count.get(i)].getValue()));
			}
			historyList = recipeService
					.searchRecipeHistory((Integer[]) cookieList.toArray(new Integer[cookieList.size()]));
		} else {
			historyList = new ArrayList<RecipeVO>();
		}
		model.addAttribute("historyList", historyList);

		Cookie cookie = new Cookie(cookieKey + idx, idx.toString());
		cookie.setMaxAge(60 * 60 * 24 * 7);
		// cookie.setPath("/");
		// cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		Gson gson = new Gson();
		model.addAttribute("evaluationCheck", gson.toJson(recipeService.recipeEvaluation(recipeVO.getIdx(), memberIdx)));
		model.addAttribute("saveCheck",recipeService.recipeSaveCheck(recipeVO.getIdx(), memberIdx));

		return "recipe/view";
	}

	@GetMapping("/viewCount")
	public String viewCount(@RequestParam(value = "idx") final Integer idx) {
		recipeService.recipeViewCount(idx);

		return "redirect:/recipe/view?idx=" + idx;
	}

	@PostMapping("/evaluation")
	public ResponseEntity<String> checkEvaluation(@RequestParam(value = "recipeIdx") final Integer recipeIdx,
			@RequestParam(value = "evaluation") final String evaluation,
			HttpSession session) {
		int memberIdx = (Integer)session.getAttribute("memberIdx");
		recipeService.recipeEvaluationInsert(recipeIdx, evaluation, memberIdx);

		Gson gson = new Gson();
		return new ResponseEntity<String>(gson.toJson(recipeService.recipeEvaluation(recipeIdx, memberIdx)),
				HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<Boolean> recipeSave(@RequestParam(value = "recipeIdx") final Integer recipeIdx,
			HttpSession session) {
			int memberIdx = (Integer)session.getAttribute("memberIdx");
			recipeService.recipeSave(recipeIdx, memberIdx);

		return new ResponseEntity<Boolean>(recipeService.recipeSaveCheck(recipeIdx, memberIdx),
				HttpStatus.OK);
	}
}