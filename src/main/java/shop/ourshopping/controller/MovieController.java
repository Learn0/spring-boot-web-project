package shop.ourshopping.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import shop.ourshopping.constant.MemberRole;
import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.manager.JsonManager;
import shop.ourshopping.manager.JsoupManager;
import shop.ourshopping.parsingVO.AreaVO;
import shop.ourshopping.service.MovieService;
import shop.ourshopping.vo.MovieVO;
import shop.ourshopping.vo.MovieReservationVO;

// 영화와 관련된 기능을 동작
@Controller
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController extends BasicController {

	@Qualifier("movieServiceImpl")
	private MovieService movieService;
	private JsonManager jsonManager;
	private JsoupManager jsoupManager;

	@GetMapping("/main")
	public String movie(@RequestParam(value = "idx", required = false) Integer idx,
			@RequestParam(value = "view", required = false) Integer view, @ModelAttribute final SearchDTO searchDTO,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
		model.addAttribute("idx", idx);
		if (idx != null && idx > 0) {
			String json = jsoupManager.readBoxOffice(idx);
			model.addAttribute("json", json);
			model.addAttribute("url", "../movie/boxOffice.jsp");
		} else {
			if (view != null) {
				if (authentication != null && authentication.isAuthenticated()) {
					HttpSession session = request.getSession(false);
					if (session == null) {
						return messageRedirect("세션 유지시간이 지났습니다.", "/member/logout", MessageMethod.post, null,
								redirectAttributes);
					}
					String authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
					if (authorities.equals(MemberRole.ADMIN.getValue())) {
						model.addAttribute("admin", true);
					}
				}
				MovieVO movieVO = movieService.selectMovie(view);
				model.addAttribute("movieVO", movieVO);
				model.addAttribute("url", "../movie/view.jsp");
			} else {
				model.addAttribute("url", "../movie/list.jsp");
			}
		}

		return "movie/main";
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchMovie(@ModelAttribute final SearchDTO searchDTO) {
		List<MovieVO> movieList = movieService.searchMovie(searchDTO);
		JsonArray array = new JsonArray();
		Gson gson = new Gson();
		array.add(gson.toJson(movieList));
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("page", searchDTO.getPage());
		jsonObject.addProperty("pageCount", searchDTO.getPageInfo().getPageCount());
		jsonObject.addProperty("previousCheck", searchDTO.getPageInfo().isPreviousCheck());
		jsonObject.addProperty("nextCheck", searchDTO.getPageInfo().isNextCheck());
		jsonObject.addProperty("firstPage", searchDTO.getPageInfo().getFirstPage());
		jsonObject.addProperty("lastPage", searchDTO.getPageInfo().getLastPage());
		array.add(jsonObject);
		
		return array.toString();
	}

	@PostMapping("/reservation")
	public String movieReservation(@RequestParam(value = "idx") Integer idx, Model model) {
		MovieVO movieVO = movieService.selectMovie(idx);
		model.addAttribute("movieVO", movieVO);

		List<String> areaNmList = new ArrayList<String>();
		List<AreaVO> areaList = jsonManager.areaJson();
		for (AreaVO vo : areaList) {
			areaNmList.add(vo.getArea());
		}
		model.addAttribute("areaList", areaNmList);

		return "movie/reservation";
	}

	@PostMapping("/category")
	@ResponseBody
	public String movieCategory(@RequestParam(value = "type") String type) {
		JsonArray array = new JsonArray();
		List<AreaVO> areaList = jsonManager.areaJson();
		for (AreaVO vo : areaList) {
			if (vo.getArea().equals(type)) {
				for (String st : vo.getBorough()) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("name", st);
					array.add(jsonObject);
				}
				break;
			}
		}
		if (array.size() == 0) {
			if (type.contains("-")) {
				for (int i = 6; i <= 22; i += 2) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("name", i + ":00");
					array.add(jsonObject);
				}
			} else if (type.startsWith("영화관")) {
				YearMonth yearMonth = YearMonth.from(LocalDate.now());
				for (int i = 1; i <= 5; i++) {
					JsonObject jsonObject = new JsonObject();
					int day = LocalDate.now().getDayOfMonth() + i;
					int month = LocalDate.now().getMonthValue();
					int year = LocalDate.now().getYear();
					if (day > yearMonth.lengthOfMonth()) {
						month += 1;
						if (month > 12) {
							month = 1;
							year += 1;
						}
						day -= yearMonth.lengthOfMonth();
					}
					jsonObject.addProperty("name", year + "-" + month + "-" + day);
					array.add(jsonObject);
				}
			} else {
				for (int i = 1; i <= 10; i++) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("name", "영화관" + i);
					array.add(jsonObject);
				}
			}
		}

		return array.toString();
	}

	@PostMapping("/reservationReset")
	@ResponseBody
	public String movieReservationReset(@RequestParam Map<String, Object> map, HttpSession session) {
		MovieReservationVO movieReservationVO = new MovieReservationVO();
		movieReservationVO.setMovieIdx(Integer.parseInt(map.get("movieIdx").toString()));
		movieReservationVO.setMovieTheater(map.get("area").toString());
		StringTokenizer st = new StringTokenizer(map.get("date").toString(), "-");
		List<Integer> dateList = new ArrayList<Integer>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			dateList.add(Integer.parseInt(token));
		}
		st = new StringTokenizer(map.get("time").toString(), ":");
		int hour = Integer.parseInt(st.nextToken());
		LocalDateTime localDateTime = LocalDateTime.of(dateList.get(0), dateList.get(1), dateList.get(2), hour, 0);
		movieReservationVO.setReservationTime(localDateTime);

		List<Integer> seatList = movieService.searchMovieReservationSeat(movieReservationVO);
		Gson gson = new Gson();
		return gson.toJson(seatList);
	}

	@PostMapping("/reservationCheck")
	@ResponseBody
	public boolean movieReservationCheck(@RequestParam Map<String, Object> map, HttpSession session) {
		MovieReservationVO movieReservationVO = new MovieReservationVO();
		movieReservationVO.setMovieIdx(Integer.parseInt(map.get("movieIdx").toString()));
		movieReservationVO.setMemberIdx((Integer) session.getAttribute("memberIdx"));
		movieReservationVO.setAdultNumber(Integer.parseInt(map.get("adult").toString()));
		movieReservationVO.setMinorsNumber(Integer.parseInt(map.get("minors").toString()));
		movieReservationVO.setMovieTheater(map.get("area").toString());
		StringTokenizer st = new StringTokenizer(map.get("date").toString(), "-");
		List<Integer> dateList = new ArrayList<Integer>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			dateList.add(Integer.parseInt(token));
		}
		st = new StringTokenizer(map.get("time").toString(), ":");
		int hour = Integer.parseInt(st.nextToken());
		LocalDateTime localDateTime = LocalDateTime.of(dateList.get(0), dateList.get(1), dateList.get(2), hour, 0);
		movieReservationVO.setReservationTime(localDateTime);
		List<Integer> seatList = new ArrayList<Integer>();
		JsonParser jsonParser = new JsonParser();
		JsonArray array = jsonParser.parse(map.get("seat").toString()).getAsJsonArray();
		for (int i = 0; i < array.size(); i++) {
			seatList.add(array.get(i).getAsInt());
		}
		movieReservationVO.setSeat(seatList);
		
		return movieService.insertMovieReservation(movieReservationVO);
	}
}