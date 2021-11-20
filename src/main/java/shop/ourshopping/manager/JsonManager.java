package shop.ourshopping.manager;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shop.ourshopping.parsingVO.AreaVO;
import shop.ourshopping.parsingVO.HashtagVO;
import shop.ourshopping.parsingVO.ShoppingVO;
import shop.ourshopping.utils.AddressToCoordinates;

// JSON 파싱
@Component
public class JsonManager {

	@Autowired
	private JsoupManager jsoupManager;
	@Autowired
	private AddressToCoordinates addressToCoordinates;

	public String[] subwayJson() {
		List<String> list = new ArrayList<String>();
		try {
			ClassPathResource resource = new ClassPathResource("static/json/subway.json");
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8"))
					.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("DATA");
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				String stationName = tmp.get("station_nm").getAsString();
				if (!list.contains(stationName)) {
					list.add(stationName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	public List<AreaVO> areaJson() {
		List<AreaVO> list = new ArrayList<AreaVO>();
		try {
			List<String> saveName = new ArrayList<String>();
			ClassPathResource resource = new ClassPathResource("static/json/area.json");
			JsonParser jsonParser = new JsonParser();
			JsonArray array = jsonParser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8"))
					.getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				String areaName = tmp.get("area_nm").getAsString();
				String boroughName = tmp.get("borough_nm").getAsString();
				if (!saveName.contains(areaName)) {
					AreaVO vo = new AreaVO();
					vo.setArea(areaName);
					vo.setBorough(new ArrayList<String>());
					list.add(vo);
					saveName.add(areaName);
				}
				list.get(saveName.indexOf(areaName)).getBorough().add(boroughName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<HashtagVO> hashtagJson() {
		List<HashtagVO> list = new ArrayList<HashtagVO>();
		try {
			ClassPathResource resource = new ClassPathResource("static/json/hashtag.json");
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8")).getAsJsonObject();
			for (String key : jsonObject.keySet()) {
				JsonArray array = jsonObject.getAsJsonArray(key);
				List<String> nameList = new ArrayList<String>();
				for (int i = 0; i < array.size(); i++) {
					JsonObject tmp = array.get(i).getAsJsonObject();
					String name = tmp.get("name").getAsString();
					nameList.add(name);
				}
				HashtagVO hashtag = new HashtagVO();
				hashtag.setType(key);
				hashtag.setName(nameList);
				list.add(hashtag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public int[] reviewJson(String json) {
		int[] point = new int[3];
		try {
			JsonParser jsonParser = new JsonParser();
			JsonArray array = jsonParser.parse(json).getAsJsonArray();
			int loop = array.size() > point.length ? point.length : array.size();
			for (int i = 0; i < loop; i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				point[i] = tmp.get("count").getAsInt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return point;
	}

	public String boxOfficeJson(String json) {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = new JsonArray();
			JsonArray array = jsonParser.parse(json).getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				String title = tmp.get("movieNm").getAsString();
				tmp.addProperty("link", jsoupManager.searchMovieLink(title));
				jsonArray.add(tmp);
			}
			json = jsonArray.toString().replace("`", "'").replace("\",", "`,").replace(":\"", ":`").replace("\"}",
					"`}");
			// json = array.toString().replace("{\"", "{").replace(",\"",
			// ",").replace("\":", ":").replace("\",", "`,").replace(":\"",
			// ":`").replace("\"}", "`}");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	public int shoppingCountJson(String result) {
		Long count = 0l;
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject json = jsonParser.parse(result).getAsJsonObject();
			count = json.get("total").getAsLong();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (count > Long.valueOf(Integer.MAX_VALUE)) {
			return Integer.MAX_VALUE;
		} else {
			return count.intValue();
		}
	}

	public List<ShoppingVO> shoppingJson(String result) {
		List<ShoppingVO> shoppingList = new ArrayList<>();
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject json = jsonParser.parse(result).getAsJsonObject();
			JsonArray array = json.get("items").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				Gson gson = new Gson();
				ShoppingVO vo = gson.fromJson(tmp, ShoppingVO.class);
				shoppingList.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return shoppingList;
	}

	public List<String> naverSearchJson(String result) {
		List<String> list = new ArrayList<>();
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject json = jsonParser.parse(result).getAsJsonObject();
			JsonArray array = json.get("items").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				list.add(tmp.get("description").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<String> coordinatesJson(String address) {
		List<String> list = new ArrayList<String>();
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(addressToCoordinates.change(address)).getAsJsonObject();
			JsonArray array = jsonObject.get("documents").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject tmp = array.get(i).getAsJsonObject();
				list.add(tmp.get("y").getAsString());
				list.add(tmp.get("x").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}