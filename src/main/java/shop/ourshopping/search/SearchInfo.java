package shop.ourshopping.search;

import lombok.Getter;
import lombok.Setter;

// 페이지를 나누기 위한 데이터 보관
@Getter
@Setter
public class SearchInfo {

	private static final int rowCount = 15;
	private static final int pageSize = 5;
	private int page = 1;

	public int getRowCount() {

		return rowCount;
	}

	public int getPageSize() {
		
		return pageSize;
	}
}