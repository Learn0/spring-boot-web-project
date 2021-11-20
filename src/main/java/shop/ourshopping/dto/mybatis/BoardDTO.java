package shop.ourshopping.dto.mybatis;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

// 게시판과 관련된 데이터를 Mybatis를 통해 DB와 주고받기 위해 사용
@Getter
@Setter
public class BoardDTO extends BasicDTO {

	private Integer groupIdx;
	private Integer groupOrder;
	private Integer groupDepth;
	@NotBlank(message = "타이틀은 필수 입력 값입니다.")
    @Size(min = 1, max = 30, message = "30자로 이하로 입력하십시오.")
	private String title;
	@NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(min = 1, max = 300, message = "300자로 이하로 입력하십시오.")
	private String content;
    @Size(max = 20, message = "20자로 이하로 입력하십시오.")
	private String password;
	private Integer viewCount;
	private String noticeCheck;
	private String changeCheck;
	private LocalDateTime updateTime;
	private LocalDateTime deleteTime;
	private List<Integer> fileIdxs;
	private Boolean newCheck;
	private String writer;
}