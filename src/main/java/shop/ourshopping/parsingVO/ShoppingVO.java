package shop.ourshopping.parsingVO;

import lombok.Getter;
import lombok.Setter;

// 파싱한 json 데이터를 보관하기 위해 사용
@Getter
@Setter
public class ShoppingVO {

	private String title;
	private String link;
	private String image;
	private Integer lprice;
	private String mallName;
	private String maker;
	private String brand;
	private String category1;
	private String category2;
	private String shoppingBasket;
}