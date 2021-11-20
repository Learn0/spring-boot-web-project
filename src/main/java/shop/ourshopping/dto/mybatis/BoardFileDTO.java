package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

// 파일 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class BoardFileDTO {

	private Integer idx;
	private Integer boardIdx;
	private String originalName;
	private String saveFile;
	private Long size;
}