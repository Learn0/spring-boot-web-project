package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

// 장바구니와 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class ShoppingBasketDTO {

	private Integer idx;
	private String itemName;
	private Integer itemPrice;
	private String itemLink;
	private String itemImage;
	private Integer itemNumber;
	private Integer memberIdx;
}