package shop.ourshopping.dto.mybatis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

// 예약과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class BasicReservationDTO {

	private Integer idx;
	private Integer targetIdx;
	private String targetType;
	private Integer memberIdx;
	@Min(value = 1, message = "1 ~ 10명까지 가능합니다")
	@Max(value = 10, message = "1 ~ 10명까지 가능합니다")
	private Integer peopleNumber;
	private LocalDateTime reservationTime;
	private String parking;
	private Integer status;
	private String title;
	private String writer;
	private String phoneNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "예약날짜는 필수 입력 값입니다.")
	private LocalDate inputDate;
	@DateTimeFormat(pattern = "HH:mm")
	@NotNull(message = "예약시간은 필수 입력 값입니다.")
	private LocalTime inputTime;
}