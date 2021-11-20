package shop.ourshopping.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.BasicReservationDTO;
import shop.ourshopping.mapper.BasicReservationMapper;

// 예약 관련 DB 비지니스 처리
@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private BasicReservationMapper reservationMapper;

	@Override
	public boolean insertReservation(BasicReservationDTO reservationDTO) {
		return (reservationMapper.insertReservation(reservationDTO) > 0);
	}

	@Override
	public List<BasicReservationDTO> searchReservation(Map<String, Object> map) {
		List<BasicReservationDTO> reservationList = reservationMapper.searchReservation(map);
		int idx = reservationList.size();
		for(int i = 0; i < reservationList.size(); i++) {
			reservationList.get(i).setIdx(idx);
			idx -= 1;
		}
		return reservationMapper.searchReservation(map);
	}

	@Override
	public List<BasicReservationDTO> searchReservationAdmin(String targetType) {
		return reservationMapper.searchReservationAdmin(targetType);
	}

	@Override
	public boolean updateReservation(int idx, int status) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("idx", idx);
		map.put("status", status);
		
		return (reservationMapper.updateReservation(map) > 0);
	}

	@Override
	public boolean deleteReservation(int idx) {
		return (reservationMapper.deleteReservation(idx) > 0);
	}
}