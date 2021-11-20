package shop.ourshopping.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.dto.mybatis.ShoppingBasketDTO;
import shop.ourshopping.manager.JsonManager;
import shop.ourshopping.parsingVO.ShoppingVO;
import shop.ourshopping.search.PageInfo;
import shop.ourshopping.service.ShoppingBasketService;
import shop.ourshopping.utils.RestAPIUtils;

// 쇼핑과 관련된 url 매핑
@PropertySource("classpath:/application-naver.properties")
@Controller
@RequestMapping("/shopping")
public class ShoppingController {

	@Resource(name="shoppingBasketServiceImpl")
	private ShoppingBasketService shoppingBasketService;
	@Autowired
	private JsonManager jsonManager;
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String id;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String secret;

	@GetMapping("/list")
	public String shoppingList(@ModelAttribute final SearchDTO searchDTO, HttpSession session, Model model) {
		if (searchDTO.getSearchKeyword() == null) {
			searchDTO.setSearchKeyword("마스크");
		}
		if (searchDTO.getSearchType() == null) {
			searchDTO.setSearchType("sim");
		}
		String keyword = searchDTO.getSearchKeyword();
		try {
			keyword = URLEncoder.encode(searchDTO.getSearchKeyword(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!keyword.isEmpty()) {
			Map<String, String> requestHeaders = new HashMap<String, String>();
			requestHeaders.put("X-Naver-Client-Id", id);
			requestHeaders.put("X-Naver-Client-Secret", secret);

			String url = "https://openapi.naver.com/v1/search/shop.json?display=1&query=" + keyword + "&sort="
					+ searchDTO.getSearchType();
			String responseBody = RestAPIUtils.get(url, requestHeaders);

			int count = jsonManager.shoppingCountJson(responseBody);

			PageInfo pageInfo = new PageInfo(searchDTO);
			pageInfo.resetPage(count);
			searchDTO.setPageInfo(pageInfo);

			url = "https://openapi.naver.com/v1/search/shop.json?&start" + (searchDTO.getPageInfo().getFirstRow() + 1)
					+ "&display=" + searchDTO.getRowCount() + "&query=" + keyword + "&sort="
					+ searchDTO.getSearchType();
			responseBody = RestAPIUtils.get(url, requestHeaders);

			List<ShoppingVO> shoppingList = jsonManager.shoppingJson(responseBody);
			if(session.getAttribute("memberIdx") != null) {
				for(ShoppingVO shoppingVO : shoppingList) {
					if(shoppingBasketService.shoppingBasketCheck(shoppingVO.getLink(), (Integer) session.getAttribute("memberIdx"))){
						shoppingVO.setShoppingBasket("장바구니 취소");
					}else {
						shoppingVO.setShoppingBasket("장바구니 넣기");
					}
				}
			}
			model.addAttribute("shoppingList", shoppingList);
		} else {
			PageInfo pageInfo = new PageInfo(searchDTO);
			pageInfo.resetPage(0);
			searchDTO.setPageInfo(pageInfo);
		}

		return "thymeleaf/shopping/list";
	}

	@PostMapping(value = "/basket", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean shoppingBasket(@RequestBody final ShoppingVO shoppingVO, HttpSession session) {
		ShoppingBasketDTO shoppingBasketDTO = new ShoppingBasketDTO();
		shoppingBasketDTO.setItemName(shoppingVO.getTitle());
		// shoppingVO.getLprice().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")+"원"
		shoppingBasketDTO.setItemPrice(shoppingVO.getLprice());
		shoppingBasketDTO.setItemLink(shoppingVO.getLink());
		shoppingBasketDTO.setItemImage(shoppingVO.getImage());
		shoppingBasketDTO.setItemNumber(1);
		shoppingBasketDTO.setMemberIdx((Integer) session.getAttribute("memberIdx"));
		shoppingBasketService.shoppingBasket(shoppingBasketDTO);
		
		return shoppingBasketService.shoppingBasketCheck(shoppingBasketDTO.getItemLink(), shoppingBasketDTO.getMemberIdx());
	}

	@PostMapping("/searchBasket")
	@ResponseBody
	public String searchShoppingBasket(HttpSession session) {
		Gson gson = new Gson();
		return gson.toJson(shoppingBasketService.searchShoppingBasket((Integer) session.getAttribute("memberIdx")));
	}

	@PostMapping("/updateBasket")
	@ResponseBody
	public int updateShoppingBasket(@RequestParam(value = "idx") final Integer idx, @RequestParam(value = "itemNumber") final Integer itemNumber) {
		if(shoppingBasketService.updateShoppingBasket(idx, itemNumber)) {
			return shoppingBasketService.selectShoppingBasket(idx).getItemNumber();
		}else {
			return 0;
		}
	}

	@PostMapping("/deleteBasket")
	@ResponseBody
	public boolean deleteShoppingBasket(@RequestParam(value = "idx") final Integer idx) {
		return shoppingBasketService.deleteShoppingBasket(idx);
	}
}