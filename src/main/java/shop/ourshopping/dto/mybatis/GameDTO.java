package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

// 게임과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class GameDTO {
	
	private Integer idx;
	private String title;
	private String content;
	private String link;
}