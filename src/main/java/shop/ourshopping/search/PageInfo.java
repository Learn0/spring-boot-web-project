package shop.ourshopping.search;

import lombok.Getter;
import lombok.Setter;

// 페이지를 편하게 나누기 위해 사용
@Getter
@Setter
public class PageInfo {

	private SearchInfo searchInfo;
	private int pageCount;
	private int firstPage;
	private int lastPage;
	private int firstRow;
	private boolean previousCheck;
	private boolean nextCheck;

	public PageInfo(SearchInfo searchInfo) {
		this.searchInfo = searchInfo;
	}

	public void resetPage(int rowCount) {
		if (rowCount > 0) {
			pageCount = ((rowCount - 1) / searchInfo.getRowCount()) + 1;
			if (searchInfo.getPage() > pageCount) {
				searchInfo.setPage(pageCount);
			} else if (searchInfo.getPage() < 1) {
				searchInfo.setPage(1);
			}
			firstRow = (searchInfo.getPage() - 1) * searchInfo.getRowCount();
			firstPage = ((searchInfo.getPage() - 1) / searchInfo.getPageSize()) * searchInfo.getPageSize() + 1;
			lastPage = firstPage + searchInfo.getPageSize() - 1;
			if (lastPage > pageCount) {
				lastPage = pageCount;
			}
			previousCheck = firstPage > 1;
			nextCheck = lastPage < pageCount;
		} else {
			pageCount = 0;
		}
	}
}