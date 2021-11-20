package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.BasicReservationDTO;

// 메소드명과 같은 BasicReservationMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface BasicReservationMapper {

	public int insertReservation(BasicReservationDTO reservationDTO);
	public List<BasicReservationDTO> searchReservation(Map<String, Object> map);
	public List<BasicReservationDTO> searchReservationAdmin(String targetType);
	public int updateReservation(Map<String, Integer> map);
	public int deleteReservation(int idx);
}