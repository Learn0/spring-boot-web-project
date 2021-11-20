package shop.ourshopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import shop.ourshopping.manager.XmlJAXBManager;
import shop.ourshopping.parsingVO.NewsItemVO;

// 뉴스와 관련된 기능을 동작
@Controller
@RequestMapping("/news")
public class NewsController extends BasicController {

	@Autowired
	private XmlJAXBManager xmlJAXBManager;

	@GetMapping("/list")
	public String newsList() {

		return "news/list";
	}
	
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchNews(@RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
		String json = "";
		try {
			NewsItemVO[] newsItem = xmlJAXBManager.readNews(searchKeyword);
			Gson gson = new Gson();
			json = gson.toJson(newsItem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}
}