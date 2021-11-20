package shop.ourshopping.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shop.ourshopping.constant.MemberRole;
import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.dto.mybatis.BasicReservationDTO;
import shop.ourshopping.dto.mybatis.ShoppingBasketDTO;
import shop.ourshopping.entity.MemberEntity;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.service.MovieService;
import shop.ourshopping.service.RecipeService;
import shop.ourshopping.service.ReservationService;
import shop.ourshopping.service.ShoppingBasketService;
import shop.ourshopping.utils.ValidUtils;
import shop.ourshopping.vo.MovieReservationVO;
import shop.ourshopping.vo.RecipeVO;

// 계정과 관련된 기능을 동작
@Controller
@RequestMapping("/member")
public class MemberController extends BasicController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "shoppingBasketServiceImpl")
	private ShoppingBasketService shoppingBasketService;
	@Resource(name = "recipeServiceImpl")
	private RecipeService recipeService;
	@Resource(name = "movieServiceImpl")
	private MovieService movieService;
	@Resource(name = "reservationServiceImpl")
	private ReservationService reservationService;

	@GetMapping("/login")
	public String login(@RequestParam(value = "msg", required = false) String msg, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String referrer = request.getHeader("Referer");
		if (referrer != null && !referrer.contains("/member/login") && !referrer.contains("/member/signup")
				&& !referrer.contains("/member/findPassword")) {
			session.setAttribute("prevPage", referrer);
		}
		String loginFailMsg = (String) session.getAttribute("loginFailMsg");
		if (loginFailMsg != null) {
			session.removeAttribute("loginFailMsg");
			model.addAttribute("loginFailMsg", loginFailMsg);
		}else if(msg != null) {
			if(msg.equals("loginCheck")) {
				model.addAttribute("loginFailMsg", "다른 기기에서 접속으로 로그아웃되았습니다.");
			}
		}

		return "thymeleaf/member/login";
	}

	@GetMapping("/findPassword")
	public String findPassword() {
		return "thymeleaf/member/findPassword";
	}

	@GetMapping("/signup")
	public String singup(@ModelAttribute final MemberDTO memberDTO) {
		return "thymeleaf/member/signup";
	}

	@GetMapping("/denied")
	public String denied() {
		return "thymeleaf/member/denied";
	}

	@GetMapping("/info")
	public String info(@RequestParam(value = "type", required = false) Integer type, HttpSession session, 
			Authentication authentication, Model model) {
		if (type == null) {
			type = 0;
		}
		String path;
		String authorities = "";
		switch (type) {
		case 1:
			path = "thymeleaf/member/info/shoppingBasket";
			List<ShoppingBasketDTO> shoppingBasketList = shoppingBasketService.searchShoppingBasket((Integer) session.getAttribute("memberIdx"));
			model.addAttribute("shoppingBasketList", shoppingBasketList);
			break;
		case 2:
			path = "thymeleaf/member/info/recipe";
			List<RecipeVO> recipeList = recipeService.searchRecipeSave((Integer) session.getAttribute("memberIdx"));
			model.addAttribute("recipeList", recipeList);
			break;
		case 3:
			path = "thymeleaf/member/info/movie";
			if (authentication != null && authentication.isAuthenticated()) {
				authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
			}
			List<MovieReservationVO> movieReservationList = movieService.searchMovieReservation((Integer) session.getAttribute("memberIdx"), authorities.equals(MemberRole.ADMIN.getValue()));
			model.addAttribute("movieReservationList", movieReservationList);
			break;
		case 4:
		case 5:
			path = "thymeleaf/member/info/basicReservation";
			model.addAttribute("type", type);
			List<BasicReservationDTO> reservationList;
			String tagetType;
			if(type == 4) {
				tagetType = "restaurant";
			} else {
				tagetType = "attractions";
			}
			if (authentication != null && authentication.isAuthenticated()) {
				authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
			}
			if(authorities.equals(MemberRole.ADMIN.getValue())) {
				reservationList = reservationService.searchReservationAdmin(tagetType);	
			} else {
				Map<String,Object> reservationMap = new HashMap<String, Object>();
				reservationMap.put("memberIdx", (Integer) session.getAttribute("memberIdx"));
				reservationMap.put("targetType", tagetType);
				reservationList = reservationService.searchReservation(reservationMap);
			}
			model.addAttribute("reservationList", reservationList);
			break;
		default:
			path = "thymeleaf/member/info/basic";
			type = 0;
			MemberEntity memberEntity = memberService.selectMemberEntity((Integer) session.getAttribute("memberIdx"));
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setIdx(memberEntity.getIdx());
			memberDTO.setEmail(memberEntity.getEmail());
			memberDTO.setPassword(memberEntity.getPassword());
			memberDTO.setNickname(memberEntity.getNickname());
			memberDTO.setBirthday(memberEntity.getBirthday());
			memberDTO.setPhoto(memberEntity.getPhoto());
			memberDTO.setPhoneNumber(memberEntity.getPhoneNumber());
			memberDTO.setPostcode(memberEntity.getPostcode());
			memberDTO.setAddress(memberEntity.getAddress());
			memberDTO.setDetailAddress(memberEntity.getDetailAddress());
			memberDTO.setExtraAddress(memberEntity.getExtraAddress());
			model.addAttribute("memberDTO", memberDTO);
			String[] phoneNumber = new String[3];
			switch (memberDTO.getPhoneNumber().length()) {
				case 9: {
					phoneNumber[0] = memberDTO.getPhoneNumber().substring(0, 2);
					phoneNumber[1] = memberDTO.getPhoneNumber().substring(2, 5);
					phoneNumber[2] = memberDTO.getPhoneNumber().substring(5, 9);
					break;
				}
				case 10: {
					phoneNumber[0] = memberDTO.getPhoneNumber().substring(0, 3);
					phoneNumber[1] = memberDTO.getPhoneNumber().substring(3, 6);
					phoneNumber[2] = memberDTO.getPhoneNumber().substring(6, 10);
					break;
				}
				case 11: {
					phoneNumber[0] = memberDTO.getPhoneNumber().substring(0, 3);
					phoneNumber[1] = memberDTO.getPhoneNumber().substring(3, 7);
					phoneNumber[2] = memberDTO.getPhoneNumber().substring(7, 11);
					break;
				}
			}
			model.addAttribute("phoneNumber", phoneNumber);
			model.addAttribute("type", type);
		}

		return path;
	}

	@PostMapping("/signup")
	public String signup(final MemberDTO memberDTO, final MultipartFile file, RedirectAttributes redirectAttributes) {
		memberDTO.setPhoneNumber(memberDTO.getPhoneNumber().replace(",", ""));
		if (memberService.signup(memberDTO, file)) {
			return messageRedirect("회원가입을 성공하였습니다.", "/member/login", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("회원가입을 실패하였습니다.", "/member/signup", MessageMethod.get, null, redirectAttributes);
		}
	}

	@PostMapping("/update")
	public String update(final MemberDTO memberDTO, final MultipartFile file, HttpSession session,
			RedirectAttributes redirectAttributes) {
		boolean check;
		memberDTO.setPhoneNumber(memberDTO.getPhoneNumber().replace(",", ""));
		if (memberDTO.getPassword().isEmpty()) {
			check = memberService.update(memberDTO, file);
		} else {
			check = memberService.updateAll(memberDTO, file);
		}
		if (check) {
			session.setAttribute("nickname", memberDTO.getNickname());
			session.setAttribute("photo", memberDTO.getPhoto());
			return messageRedirect("계정정보를 변경하였습니다.", "/", MessageMethod.get, null, redirectAttributes);
		} else {
			return messageRedirect("계정정보 변경을 실패하였습니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}

	@DeleteMapping("/delete")
	public String delete(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		if (memberService.delete((Integer) session.getAttribute("memberIdx"))) {
			return messageRedirect("탈퇴를 완료하였습니다.", "/member/logout", MessageMethod.post, null, redirectAttributes);
		} else {
			return messageRedirect("탈퇴를 실패하였습니다.", "/", MessageMethod.get, null, redirectAttributes);
		}
	}

	@PostMapping("/checkValidation")
	@ResponseBody
	public Map<String, String> checkValidation(String updateCheck, @Valid final MemberDTO memberDTO, Errors errors) {
		Map<String, String> validatorResult = ValidUtils.validateHandling(errors);
		if (updateCheck != null) {
			validatorResult.remove("valid_birthday");
			if (memberDTO.getPassword().isEmpty()) {
				validatorResult.remove("valid_password");
			}
		}

		return validatorResult;
	}

	@PostMapping("/checkEmailDuplicate/{email}/exists")
	@ResponseBody
	public int checkEmailDuplicate(@PathVariable String email) {
		int check = 0;
		if(memberService.checkEmailDuplicate(email)) {
			if(memberService.selectMemberEntity(email).getDeleteCheck().equals("Y")) {
				check = 2;
			}else {
				check = 1;
			}
		}
		return check;
	}

	@PostMapping("/passwordReset")
	@ResponseBody
	public String passwordReset(@RequestParam(value = "email") String email) {
		String password;
		if (memberService.selectMemberEntity(email).getPassword() == null) {
			password = "소셜로그인 전용 계정입니다.";
		} else {
			StringBuffer sb = new StringBuffer();
			Random random = new Random();
			for (int i = 0; i < 8; i++) {
				sb.append((char) ((int) (random.nextInt(26)) + 97));
			}
			sb.append("@");
			password = sb.toString();

			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setEmail(email);
			memberDTO.setPassword(password);
			memberService.updatePassword(memberDTO);
		}

		return password;
	}
}