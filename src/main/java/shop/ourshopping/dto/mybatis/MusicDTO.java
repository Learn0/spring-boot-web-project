package shop.ourshopping.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

// 음악과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class MusicDTO {
	
	private Integer idx;
	private String poster;
	private String title;
	private String singer;
	private String album;
	private String state;
	private Integer increment;
	private String youtubeKey;
}