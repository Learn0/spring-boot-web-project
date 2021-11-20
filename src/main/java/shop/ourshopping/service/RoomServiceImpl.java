package shop.ourshopping.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.dto.mybatis.RoomDTO;
import shop.ourshopping.mapper.RoomMapper;

// 채팅방에 참여한 인원수, 닉네임 및 채팅방 관련 DB 비지니스 처리
@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomMapper roomMapper;

	private Map<String, String> userRoomIdMap = new HashMap<String, String>();
	private Map<String, Map<String, MemberDTO>> memberMap = new HashMap<String, Map<String, MemberDTO>>();

	@Override
	public boolean insertRoom(String title) {
		RoomDTO roomDTO = RoomDTO.create(title);
		return roomMapper.insertRoom(roomDTO) > 0;
	}

	public RoomDTO selectRoom(String roomId) {
		return roomMapper.selectRoom(roomId);
	}

	@Override
	public List<RoomDTO> searchRoom() {
		return roomMapper.searchRoom();
	}

	@Override
	public boolean updateRoom(String roomId, int value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roomId", roomId);
		map.put("value", value);
		boolean sqlCheck = roomMapper.updateRoom(map) > 0;
		if (sqlCheck && value > 0) {
			if (roomMapper.selectRoom(roomId).getPeopleNumber() > 10) {
				throw new RuntimeException();
			}
		}

		return sqlCheck;
	}

	// 세션ID를 키 값으로 Room ID 저장
	@Override
	public void setUserRoomId(String sessionId, String roomId, MemberDTO memberDTO) {
		userRoomIdMap.put(sessionId, roomId);
		if (memberMap.containsKey(roomId)) {
			memberMap.get(roomId).put(sessionId, memberDTO);
		} else {
			Map<String, MemberDTO> nicknameMap = new HashMap<String, MemberDTO>();
			nicknameMap.put(sessionId, memberDTO);
			memberMap.put(roomId, nicknameMap);
		}
	}

	// 세션ID로 Room ID를 찾은 후 삭제
	@Override
	public String getUserRoomId(String sessionId) {
		String roomId = userRoomIdMap.get(sessionId);
		return roomId;
	}

	// RoomId로 닉네임 검색
	@Override
	public List<MemberDTO> getMemberList(String roomId, int memberIdx) {
		List<MemberDTO> memberList = new ArrayList<MemberDTO>();
		if (memberMap.containsKey(roomId)) {
			Map<String, MemberDTO> nicknameMap = memberMap.get(roomId);
			for (String key : nicknameMap.keySet()) {
				memberList.add(nicknameMap.get(key));
			}
		}
		return memberList;
	}

	// RoomId와 SessionId로 닉네임 검색
	@Override
	public String getNickname(String roomId, String sessionId) {
		String nickname = memberMap.get(roomId).get(sessionId).getNickname();
		userRoomIdMap.remove(sessionId);
		memberMap.get(roomId).remove(sessionId);
		return nickname;
	}
}