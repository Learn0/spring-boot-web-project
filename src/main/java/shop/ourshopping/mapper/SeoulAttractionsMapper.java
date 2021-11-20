package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.SeoulAttractionsDTO;

// 메소드명과 같은 SeoulAttractionsMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface SeoulAttractionsMapper {

	public int insertAttractions(SeoulAttractionsDTO seoulAttractionsDTO);
	public List<SeoulAttractionsDTO> searchAttractions(Map<String, Object> map);
	public int searchAttractionsCount(String type);
	public SeoulAttractionsDTO selectAttractions(int idx);
}