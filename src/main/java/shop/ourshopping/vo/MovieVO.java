package shop.ourshopping.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/*
 * VO는 DTO에서 readonly 속성이 추가된 클래스로 알고 있지만 JDBC를 구별하기 위해 사용
 * 영화와 관련된 데이터를 DAO를 통해 DB와 주고받기 위해 사용
 */
@Getter
@Setter
public class MovieVO {

	private Integer idx;
	private String title;
	private BigDecimal score;
	private String grade;
	private String reserve;
	private String genre;
	private String time;
	private String regdate;
	private String director;
	private String actor;
	private String showUser;
	private Integer viewCount;
	private String poster;
	private String story;
	private String youtubeKey;
}