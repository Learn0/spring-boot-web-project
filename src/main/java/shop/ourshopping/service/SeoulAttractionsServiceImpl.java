package shop.ourshopping.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.dto.mybatis.SeoulAttractionsDTO;
import shop.ourshopping.mapper.SeoulAttractionsMapper;
import shop.ourshopping.search.PageInfo;

// 서울 명소 관련 DB 비지니스 처리
@Service
public class SeoulAttractionsServiceImpl implements SeoulAttractionsService {

	@Autowired
	private SeoulAttractionsMapper seoulAttractionsMapper;

	@Override
	public int insertAttractions(SeoulAttractionsDTO seoulAttractionsDTO) {
		return seoulAttractionsMapper.insertAttractions(seoulAttractionsDTO);
	}

	@Override
	public SeoulAttractionsDTO selectAttractions(int idx) {
		
		return seoulAttractionsMapper.selectAttractions(idx);
	}

	@Override
	public List<SeoulAttractionsDTO> searchAttractions(SearchDTO searchDTO, int type) {
		String[] attractionsArr = { "attractions", "entertainment", "hotels", "nature", "restaurants", "seoul-stay",
		"shopping" };
		int count = seoulAttractionsMapper.searchAttractionsCount(attractionsArr[type]);

		PageInfo pageInfo = new PageInfo(searchDTO);
		pageInfo.resetPage(count);
		searchDTO.setPageInfo(pageInfo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", attractionsArr[type]);
		map.put("min", pageInfo.getFirstRow());
		map.put("max", searchDTO.getRowCount());
		List<SeoulAttractionsDTO> attractionsList = seoulAttractionsMapper.searchAttractions(map);
		
		return attractionsList;
	}
}