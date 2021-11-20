package shop.ourshopping.parsingVO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// 파싱한 json 데이터를 보관하기 위해 사용
@Getter
@Setter
public class HashtagVO {
	
	private String type;
	private List<String> name;
}