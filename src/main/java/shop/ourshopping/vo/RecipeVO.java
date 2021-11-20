package shop.ourshopping.vo;

import lombok.Getter;
import lombok.Setter;

// 레시피와 관련된 데이터를 DAO를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class RecipeVO {

	private Integer idx;
	private String title;
	private String writer;
	private Integer viewCount;
	private String poster;
	private String hashtag;
	private String content;
	private String amount;
	private String time;
	private String difficulty;
	private String cookingOrder;
	private String tip;
}