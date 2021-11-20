package shop.ourshopping.service;

import java.util.List;
import java.util.Map;

import shop.ourshopping.dto.mybatis.BasicReservationDTO;

public interface ReservationService {

	public boolean insertReservation(BasicReservationDTO reservationDTO);
	public List<BasicReservationDTO> searchReservation(Map<String, Object> map);
	public List<BasicReservationDTO> searchReservationAdmin(String targetType);
	public boolean updateReservation(int idx, int status);
	public boolean deleteReservation(int idx);
}