package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

// 채팅 정보를 보유
@Getter
@Setter
public class ChatDTO {

	private String roomId;
	private String system;
	private String nickname;
	private String message;
}