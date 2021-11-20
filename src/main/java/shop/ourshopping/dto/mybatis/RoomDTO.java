package shop.ourshopping.dto.mybatis;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

// 채팅방과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class RoomDTO {

	private String roomId;
    private String title;
    private Integer peopleNumber;
    // 누가 접속 중인지 저장하기 위해 사용
    // private Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

    public static RoomDTO create(String title){
    	RoomDTO room = new RoomDTO();
        room.roomId = UUID.randomUUID().toString();
        room.title = title;
        return room;
    }
}