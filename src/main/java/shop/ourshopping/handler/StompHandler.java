package shop.ourshopping.handler;

import javax.annotation.Resource;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import shop.ourshopping.controller.RoomController;
import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.dto.mybatis.ChatDTO;
import shop.ourshopping.entity.MemberEntity;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.service.RoomService;

/*
 * 채팅방 연결, 구독, 해제시 발생하는 이벤트로 인원수,
 * 참가한 닉네임 등 여러 정보를 관리
 */
@Component
public class StompHandler implements ChannelInterceptor {

	@Resource(name="roomServiceImpl")
	private RoomService roomService;
	@Resource(name="memberServiceImpl")
	private MemberService memberService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if (accessor.getCommand() == StompCommand.CONNECT) {
			System.out.println("채팅방 연결 요청");
		} else if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
			System.out.println("채팅방 구독 요청");
			if(message.getHeaders().get("simpDestination") != null) {
				String roomId = destination(message.getHeaders().get("simpDestination").toString());
				if(roomService.updateRoom(roomId, 1)) {
					String userInfo = message.getHeaders().get("simpUser").toString();
					String email = userInfo.substring(userInfo.indexOf("Username=")+9);
					email = email.substring(0, email.indexOf(","));
					
					MemberEntity memberEntity = memberService.selectMemberEntity(email);
					MemberDTO memberDTO = new MemberDTO();
					memberDTO.setIdx(memberEntity.getIdx());
					memberDTO.setNickname(memberEntity.getNickname());
					roomService.setUserRoomId(accessor.getSessionId(), roomId, memberDTO);
				}
			}
		} else if (accessor.getCommand() == StompCommand.DISCONNECT) {
			System.out.println("채팅방 연결 종료");
			String sessionId = message.getHeaders().get("simpSessionId").toString();
			if(sessionId != null) {
				String roomId = roomService.getUserRoomId(sessionId);
				if(roomService.updateRoom(roomId, -1)) {
					ChatDTO chatDTO = new ChatDTO();
					chatDTO.setRoomId(roomId);
			    	chatDTO.setSystem("exit");
					chatDTO.setNickname(roomService.getNickname(roomId, sessionId));
			    	chatDTO.setMessage(chatDTO.getNickname() + "님이 채팅방에서 나가셨습니다.");
			    	RoomController.getTemplate().convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);
				}
			}
		}
		return message;
	}
	
	private String destination(String roomId) {
		if(roomId != null) {
			int index = roomId.lastIndexOf("/")+1;
			roomId = roomId.substring(index);
		}
		return roomId;
	}
}