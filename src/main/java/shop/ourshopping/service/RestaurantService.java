package shop.ourshopping.service;

import java.util.List;
import java.util.Map;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.RestaurantVO;

public interface RestaurantService {

	public void insertRestaurant(RestaurantVO restaurantVO);
	public List<RestaurantVO> searchRestaurant(SearchDTO searchDTO);
	public List<RestaurantVO> searchRestaurantArea(int idx, String address);
	public List<RestaurantVO> searchRestaurantHistory(Integer[] idx);
	public List<RestaurantVO> searchRestaurantAI(String[] title);
	public List<String> searchRestaurantTitle(String address);
	public RestaurantVO selectRestaurant(int idx);
	public void restaurantViewCount(int idx);
	public String[] restaurantHashtag();
	public void restaurantEvaluationInsert(int idx, String evaluation, int memberIdx);
	public Map<String, String> restaurantEvaluation(int restaurantIdx, int memberIdx);
}