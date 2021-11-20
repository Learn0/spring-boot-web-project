package shop.ourshopping.dto.mybatis;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

// 공통으로 사용되는 변수를 상속하기 위한 부모클래스
@Getter
@Setter
public class BasicDTO {

	private Integer idx;
	private Integer memberIdx;
	private String deleteCheck;
	private LocalDateTime insertTime;
}