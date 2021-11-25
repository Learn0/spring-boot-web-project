package shop.ourshopping.page;

import lombok.Getter;
import lombok.Setter;

// 페이지를 편하게 나누기 위해 사용
@Getter
@Setter
public class PageInfo {

	private static final int rowCount = 15;
	private static final int pageSize = 5;

	public int getRowCount() {
		return rowCount;
	}

	private int pageCount;
	private int firstPage;
	private int lastPage;
	private int firstRow;
	private boolean previousCheck;
	private boolean nextCheck;

	public int reset(int count, int page) {
		if (count > 0) {
			pageCount = ((count - 1) / rowCount) + 1;
			if (page > pageCount) {
				page = pageCount;
			} else if (page < 1) {
				page = 1;
			}
			firstRow = (page - 1) * rowCount;
			firstPage = ((page - 1) / pageSize) * pageSize + 1;
			lastPage = firstPage + pageSize - 1;
			if (lastPage > pageCount) {
				lastPage = pageCount;
			}
			previousCheck = firstPage > 1;
			nextCheck = lastPage < pageCount;
		} else {
			pageCount = 0;
		}
		return page;
	}
}