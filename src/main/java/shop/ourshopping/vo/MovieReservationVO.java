package shop.ourshopping.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

// 영화 예약과 관련된 데이터를 DAO를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class MovieReservationVO {

	private Integer idx;
	private Integer movieIdx;
	private Integer memberIdx;
	private Integer adultNumber;
	private Integer minorsNumber;
	private String movieTheater;
	private LocalDateTime reservationTime;
	private List<Integer> seat;
	private String title;
	private String phoneNumber;
	private String writer;
}