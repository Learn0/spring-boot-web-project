package shop.ourshopping.dto.mybatis;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

// 댓글과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class BasicCommentDTO extends BasicDTO {

	private Integer targetIdx;
	private String targetType;
	private String content;
	private LocalDateTime updateTime;
	private LocalDateTime deleteTime;
	private String writer;
}