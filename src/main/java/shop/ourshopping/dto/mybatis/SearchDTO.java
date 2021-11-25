package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;
import shop.ourshopping.page.PageInfo;

// 검색과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class SearchDTO {

	private PageInfo pageInfo = new PageInfo();
	private int page = 1;
	private String searchKeyword;
	private String[] searchCheckBox;
	private String searchType;
}