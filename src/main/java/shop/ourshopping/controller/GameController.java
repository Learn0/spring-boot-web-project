package shop.ourshopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import shop.ourshopping.dto.mybatis.GameDTO;
import shop.ourshopping.service.GameService;

// 게임 관련 컨트롤러
@Controller
public class GameController extends BasicController {

	@Resource(name = "gameServiceImpl")
	private GameService gameService;

	@GetMapping("/game/list")
	public String gameList() {
		return "game/list";
	}

	@GetMapping(value = "/game/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String gameSearch() {
		List<GameDTO> gameList = gameService.searchGame();
		Gson gson = new Gson();
		return gson.toJson(gameList);
	}
}