package shop.ourshopping.parsingVO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// 파싱한 json, xml 데이터를 보관하기 위해 사용
@Getter
@Setter
public class AreaVO {
	
	private String area;
	private List<String> borough;
}