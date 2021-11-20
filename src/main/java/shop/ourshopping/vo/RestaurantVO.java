package shop.ourshopping.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

// 레스토랑 관련된 데이터를 DAO를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class RestaurantVO {
	
	private Integer idx;
	private String title;
	private String content;
	private BigDecimal score;
	private Integer viewCount;
	private String poster;
	private String hashtag;
	private String address;
	private String oldAddress;
	private String phoneNumber;
	private String foodType, price, parking, businessTime, menu, site;
	private Integer good, soso, bad;
}