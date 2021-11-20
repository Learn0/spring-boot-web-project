package shop.ourshopping.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dao.RestaurantDAO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.RestaurantVO;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	@Override
	public void insertRestaurant(RestaurantVO restaurantVO) {
		restaurantDAO.insertRestaurant(restaurantVO);
	}

	@Override
	public List<RestaurantVO> searchRestaurant(SearchDTO searchDTO) {
		return restaurantDAO.searchRestaurant(searchDTO);
	}

	@Override
	public List<RestaurantVO> searchRestaurantArea(int idx, String address) {
		return restaurantDAO.searchRestaurantArea(idx, address);
	}

	@Override
	public List<RestaurantVO> searchRestaurantHistory(Integer[] idx) {
		return restaurantDAO.searchRestaurantHistory(idx);
	}

	@Override
	public List<RestaurantVO> searchRestaurantAI(String[] title) {
		return restaurantDAO.searchRestaurantAI(title);
	}

	@Override
	public List<String> searchRestaurantTitle(String address) {
		return restaurantDAO.searchRestaurantTitle(address);
	}

	@Override
	public RestaurantVO selectRestaurant(int idx) {
		return restaurantDAO.selectRestaurant(idx);
	}

	@Override
	public void restaurantViewCount(int idx) {
		restaurantDAO.restaurantViewCount(idx);
	}

	@Override
	public String[] restaurantHashtag() {
		return restaurantDAO.restaurantHashtag();
	}

	@Override
	public void restaurantEvaluationInsert(int idx, String evaluation, int memberIdx) {
		restaurantDAO.restaurantEvaluationInsert(idx, evaluation, memberIdx);
	}

	@Override
	public Map<String, String> restaurantEvaluation(int idx, int memberIdx) {
		return restaurantDAO.restaurantEvaluation(idx, memberIdx);
	}
}