package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;
import shop.ourshopping.search.PageInfo;
import shop.ourshopping.search.SearchInfo;

// 검색과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class SearchDTO extends SearchInfo {

	private PageInfo pageInfo;
	private String searchKeyword;
	private String[] searchCheckBox;
	private String searchType;
}