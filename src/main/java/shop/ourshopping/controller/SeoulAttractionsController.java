package shop.ourshopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.dto.mybatis.SeoulAttractionsDTO;
import shop.ourshopping.service.SeoulAttractionsService;

// 서울 명소와 관련된 url 매핑
@Controller
@RequestMapping("/seoulAttractions")
public class SeoulAttractionsController {

	@Resource(name = "seoulAttractionsServiceImpl")
	private SeoulAttractionsService seoulAttractionsService;

	@GetMapping("/list")
	public String attractionsList(@RequestParam(value = "type", required = false) Integer type,
			@ModelAttribute final SearchDTO searchDTO, Model model) {
		if (type == null) {
			type = 0;
		}
		List<SeoulAttractionsDTO> attractionsList = seoulAttractionsService.searchAttractions(searchDTO, type);

		String[] title = { "명소", "엔터테인먼트", "호텔", "관광", "음식", "게스트 하우스", "쇼핑" };
		if(type >= title.length) {
			type = 0;
		}
		model.addAttribute("title", title[type]);
		model.addAttribute("type", type);
		model.addAttribute("attractionsList", attractionsList);

		return "thymeleaf/seoulAttractions/list";
	}

	@GetMapping("/view")
	public String viewAttractions(@RequestParam(value = "idx") Integer idx,
			@RequestParam(value = "type", required = false) Integer type, Model model) {
		if (type == null) {
			type = 0;
		}
		SeoulAttractionsDTO attractionsDTO = seoulAttractionsService.selectAttractions(idx);

		String[] title = { "명소", "엔터테인먼트", "호텔", "관광", "음식", "게스트 하우스", "쇼핑" };
		if(type >= title.length) {
			type = 0;
		}
		model.addAttribute("title", title[type]);
		model.addAttribute("type", type);
		model.addAttribute("idx", idx);
		model.addAttribute("attractionsDTO", attractionsDTO);

		return "thymeleaf/seoulAttractions/view";
	}
}