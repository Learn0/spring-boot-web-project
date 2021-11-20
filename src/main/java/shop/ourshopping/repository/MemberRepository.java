package shop.ourshopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import shop.ourshopping.entity.MemberEntity;

import java.util.Optional;

// JPA 방식으로 SQL에 접근하기 위해 사용
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	public Optional<MemberEntity> findByIdx(int idx);

	public Optional<MemberEntity> findByEmail(String email);

	public boolean existsByEmail(String email);

	@Query(value = "SELECT login_check FROM member WHERE idx = :#{#idx}", nativeQuery = true)
	public String selectLoginCheck(@Param("idx") int idx);

	@Query(value = "SELECT lock_count FROM member WHERE email = :#{#email}", nativeQuery = true)
	public int selectLockCheck(@Param("email") String email);

	@Modifying
	@Query(value = "UPDATE member SET login_check = 'Y', lock_count = 0 WHERE idx = :#{#idx}", nativeQuery = true)
	public int updateLoginSuccess(@Param("idx") int idx);

	@Modifying
	@Query(value = "UPDATE member SET lock_count = 0 WHERE email = :#{#email}", nativeQuery = true)
	public int updateLockReset(@Param("email") String email);

	@Modifying
	@Query(value = "UPDATE member SET lock_count = lock_count+1 WHERE email = :#{#email}", nativeQuery = true)
	public int updateLockCount(@Param("email") String email);
	
	@Modifying
	@Query(value = "UPDATE member SET nickname = :#{#member.nickname}, photo = :#{#member.photo}, phone_number = :#{#member.phoneNumber}, postcode = :#{#member.postcode}, address = :#{#member.address}, detail_address = :#{#member.detailAddress}, extra_address = :#{#member.extraAddress} WHERE idx = :#{#member.idx}", nativeQuery = true)
	public int updateMember(@Param("member") MemberEntity member);
	
	@Modifying
	@Query(value = "UPDATE member SET nickname = :#{#member.nickname}, password = :#{#member.password}, photo = :#{#member.photo}, phone_number = :#{#member.phoneNumber}, postcode = :#{#member.postcode}, address = :#{#member.address}, detail_address = :#{#member.detailAddress}, extra_address = :#{#member.extraAddress} WHERE idx = :#{#member.idx}", nativeQuery = true)
	public int updateMemberAll(@Param("member") MemberEntity member);

	@Modifying
	@Query(value = "UPDATE member SET password = :#{#member.password} WHERE email = :#{#member.email}", nativeQuery = true)
	public int updatePassword(@Param("member") MemberEntity member);
	
	@Modifying
	@Query(value = "UPDATE member SET delete_check = :#{#member.deleteCheck}, delete_time = NOW() WHERE idx = :#{#member.idx}", nativeQuery = true)
	public int deleteMember(@Param("member") MemberEntity member);
}