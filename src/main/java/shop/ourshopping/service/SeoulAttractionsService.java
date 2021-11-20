package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.dto.mybatis.SeoulAttractionsDTO;

public interface SeoulAttractionsService {

	public int insertAttractions(SeoulAttractionsDTO seoulAttractionsDTO);
	public SeoulAttractionsDTO selectAttractions(int idx);
	public List<SeoulAttractionsDTO> searchAttractions(SearchDTO searchDTO, int type);
}