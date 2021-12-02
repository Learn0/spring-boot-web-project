package shop.ourshopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import shop.ourshopping.dto.mybatis.MusicDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.service.MusicService;
import shop.ourshopping.utils.YoutubeUtil;

// 음악과 관련된 기능을 동작하며 검색부분은 axios를 통한 rest 사용
@Controller
@RequestMapping("/music")
public class MusicController extends BasicController {

	@Resource(name = "musicServiceImpl")
	private MusicService musicService;

	@GetMapping("/list")
	public String musicList() {
		return "music/list";
	}

	// @CrossOrigin(value = "http://localhost:3000")
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchMusic(SearchDTO searchDTO) {
		List<MusicDTO> musicList = musicService.searchMusic(searchDTO);
		for (int i = 0; i < musicList.size(); i++) {
			musicList.get(i).setYoutubeKey(YoutubeUtil.getURL(musicList.get(i).getYoutubeKey(), true));
		}
		Gson gson = new Gson();
		JsonArray array = new JsonArray();
		array.add(gson.toJson(musicList));
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("page", searchDTO.getPage());
		jsonObject.addProperty("searchKeyword", searchDTO.getSearchKeyword());
		jsonObject.addProperty("pageCount", searchDTO.getPageInfo().getPageCount());
		jsonObject.addProperty("previousCheck", searchDTO.getPageInfo().isPreviousCheck());
		jsonObject.addProperty("nextCheck", searchDTO.getPageInfo().isNextCheck());
		jsonObject.addProperty("firstPage", searchDTO.getPageInfo().getFirstPage());
		jsonObject.addProperty("lastPage", searchDTO.getPageInfo().getLastPage());
		array.add(jsonObject);
		
		return array.toString();
	}
}