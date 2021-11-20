package shop.ourshopping.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ourshopping.dto.MemberDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

// 회원 데이터를 JPA를 통해 DB와 주고받기 위해 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@Column(columnDefinition = "INT COMMENT '회원 번호(PK)'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idx;

	@Column(columnDefinition = "VARCHAR(30) NOT NULL UNIQUE COMMENT '이메일'")
	private String email;

	@Column(columnDefinition = "VARCHAR(100) COMMENT '비밀번호'")
	private String password;

	@Column(columnDefinition = "VARCHAR(10) NOT NULL COMMENT '닉네임'")
	private String nickname;

	@Column(columnDefinition = "DATE NOT NULL COMMENT '생년월일'")
	private LocalDate birthday;

	@Column(name = "phone_number", columnDefinition = "VARCHAR(15) COMMENT '전화번호'")
    private String phoneNumber;

	@Column(columnDefinition = "VARCHAR(200) COMMENT '사진'")
    private String photo;

	@Column(columnDefinition = "VARCHAR(15) COMMENT '우편번호'")
	private String postcode;

	@Column(columnDefinition = "VARCHAR(40) COMMENT '주소'")
	private String address;

	@Column(name = "detail_address", columnDefinition = "VARCHAR(20) COMMENT '상세주소'")
	private String detailAddress;

	@Column(name = "extra_address", columnDefinition = "VARCHAR(20) COMMENT '참고항목'")
	private String extraAddress;

	@Column(name = "login_check", insertable=false, columnDefinition = "ENUM('Y', 'N') DEFAULT 'N' COMMENT '접속 여부'")
	private String loginCheck;

	@Column(name = "delete_check", insertable=false, columnDefinition = "ENUM('Y', 'N') DEFAULT 'N' COMMENT '탈퇴 여부'")
	private String deleteCheck;

	@Column(name = "admin_check", insertable=false, columnDefinition = "ENUM('Y', 'N') DEFAULT 'N' COMMENT '관리자 여부'")
	private String adminCheck;

	@Column(name = "lock_count", insertable=false, columnDefinition = "INT DEFAULT 0 COMMENT '로그인 실패 수'")
	private Integer lockCount;

	@Column(name = "insert_time", insertable=false, columnDefinition = "DATE DEFAULT NOW() COMMENT '가입일'")
	private LocalDateTime insertTime;

	@Column(name = "delete_time", columnDefinition = "DATE COMMENT '탈퇴일'")
	private LocalDateTime deleteTime;

	@Builder
	public MemberEntity(MemberDTO memberDTO) {
		this.idx = memberDTO.getIdx();
		this.email = memberDTO.getEmail();
		this.password = memberDTO.getPassword();
		this.nickname = memberDTO.getNickname();
		this.birthday = memberDTO.getBirthday();
		this.phoneNumber = memberDTO.getPhoneNumber();
		this.photo = memberDTO.getPhoto();
		this.postcode = memberDTO.getPostcode();
		this.address = memberDTO.getAddress();
		this.detailAddress = memberDTO.getDetailAddress();
		this.extraAddress = memberDTO.getExtraAddress();
		this.loginCheck = memberDTO.getLoginCheck();
		this.deleteCheck = memberDTO.getDeleteCheck();
		this.adminCheck = memberDTO.getAdminCheck();
		this.lockCount = memberDTO.getLockCount();
		this.insertTime = memberDTO.getInsertTime();
		this.deleteTime = memberDTO.getDeleteTime();
	}

	public MemberEntity update(String nickname, String photo) {
        this.nickname = nickname;
        this.photo = photo;
        this.password = null;

        return this;
    }

	public MemberEntity delete() {
        this.deleteCheck = "Y";

        return this;
    }
}