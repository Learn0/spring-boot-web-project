package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.dto.mybatis.RoomDTO;

public interface RoomService {

	public boolean insertRoom(String title);
	public RoomDTO selectRoom(String roomId);
	public List<RoomDTO> searchRoom();
	public boolean updateRoom(String roomId, int value);
	public void setUserRoomId(String sessionId, String roomId, MemberDTO memberDTO);
	public String getUserRoomId(String sessionId);
	public List<MemberDTO> getMemberList(String roomId, int memberIdx);
	public String getNickname(String roomId, String sessionId);
}