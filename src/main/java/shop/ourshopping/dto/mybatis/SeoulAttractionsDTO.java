package shop.ourshopping.dto.mybatis;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

// 서울 명소와 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class SeoulAttractionsDTO {

	private Integer idx;
	private String type;
	private String title;
	private String content;
	private BigDecimal score;
	private Integer viewCount;
	private String poster;
	private String address;
	private String phoneNumber;
	private String businessTime;
	private String businessWeek;
	private String site;
	private String tip;
}