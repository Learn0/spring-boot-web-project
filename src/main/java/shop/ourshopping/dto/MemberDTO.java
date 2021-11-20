package shop.ourshopping.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import shop.ourshopping.entity.MemberEntity;

// 유효성 검사 및 MemberEntity에 값을 전달하기 위해 사용
@Getter
@Setter
public class MemberDTO {

	private Integer idx;
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
    @Size(min = 1, max = 30, message = "30자로 이하로 입력하십시오.")
	private String email;
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 특수기호가 적어도 1개 이상 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
	private String password;
	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	@Size(min=2, max=10, message = "2 ~ 10자여야 합니다.")
	private String nickname;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	private LocalDate birthday;
	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^01(?:0|1|[6-9])[.,-]?(\\d{3}|\\d{4})[.,-]?(\\d{4})$", message = "올바르지 않는 형식입니다.")
	private String phoneNumber;
	private String photo;
	@NotBlank(message = "주소는 필수 입력 값입니다.")
	private String postcode;
	private String address;
	private String detailAddress;
	private String extraAddress;
	private String loginCheck;
	private String deleteCheck;
	private String adminCheck;
	private Integer lockCount;
	private LocalDateTime insertTime;
	private LocalDateTime deleteTime;

	public MemberEntity toEntity() {
		
		return MemberEntity.builder().memberDTO(this).build();
	}
}