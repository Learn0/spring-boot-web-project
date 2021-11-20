package shop.ourshopping.service;

import shop.ourshopping.component.FileUpload;
import shop.ourshopping.component.OSPath;
import shop.ourshopping.constant.MemberRole;
import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.entity.MemberEntity;
import shop.ourshopping.repository.MemberRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

// 관리자 체크 및 계정관리 관련 DB 비지니스 처리
@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

	private FileUpload fileUpload;
	private MemberRepository memberRepository;
	private HttpServletRequest request;
	private PasswordEncoder passwordEncoder;
	private OSPath osPath;

	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// DaoAuthenticationProvider.additionalAuthenticationChekcs(UserDetails, UsernamePasswordAuthenticationToken) 함수에서 자동으로 비밀번호 확인
		Optional<MemberEntity> memberEntityWrapper = memberRepository.findByEmail(email);
		if (memberEntityWrapper != null && !memberEntityWrapper.isEmpty()) {
			MemberEntity memberEntity = memberEntityWrapper.get();
			List<GrantedAuthority> authorities = new ArrayList<>();
			if (memberEntity.getAdminCheck().equals("Y")) {
				authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
			} else {
				authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
			}
			request.setAttribute("memberEntity",memberEntity);

			return new User(memberEntity.getEmail(), memberEntity.getPassword(), authorities);
		}
		throw new UsernameNotFoundException("");
	}

	// @Transactional로 Repository가 사용하는 각각의 트랜잭션을 하나의 트랜잭션으로 통합
	@Transactional
	public boolean signup(MemberDTO memberDTO, MultipartFile file) {
		memberDTO.setPhoto(fileUpload.uploadMemberImage(file));
		memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

		return (memberRepository.save(memberDTO.toEntity()).getIdx() > 0);
	}

	@Override
	public MemberEntity selectMemberEntity(int idx) {
		MemberEntity memberEntity = null;
		Optional<MemberEntity> memberEntityWrapper = memberRepository.findByIdx(idx);
		if (memberEntityWrapper != null && !memberEntityWrapper.isEmpty()) {
			memberEntity = memberEntityWrapper.get();
		}

		return memberEntity;
	}

	@Override
	public MemberEntity selectMemberEntity(String email) {
		MemberEntity memberEntity = null;
		Optional<MemberEntity> memberEntityWrapper = memberRepository.findByEmail(email);
		if (memberEntityWrapper != null && !memberEntityWrapper.isEmpty()) {
			memberEntity = memberEntityWrapper.get();
		}

		return memberEntity;
	}

	@Override
	public boolean update(MemberDTO memberDTO, MultipartFile file) {
		if(!file.getOriginalFilename().isEmpty()) {
			if(!memberDTO.getPhoto().isEmpty()) {
				osPath.deletePath(memberDTO.getPhoto());
			}
			memberDTO.setPhoto(fileUpload.uploadMemberImage(file));
		}
		
		return (memberRepository.updateMember(memberDTO.toEntity()) > 0);
	}

	@Override
	public boolean updateAll(MemberDTO memberDTO, MultipartFile file) {
		if(!file.getOriginalFilename().isEmpty()) {
			if(!memberDTO.getPhoto().isEmpty()) {
				osPath.deletePath(memberDTO.getPhoto());
			}
			memberDTO.setPhoto(fileUpload.uploadMemberImage(file));
		}
		memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
		
		return (memberRepository.updateMemberAll(memberDTO.toEntity()) > 0);
	}

	@Transactional
	public boolean updatePassword(MemberDTO memberDTO) {
		memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
		int sqlCheck = memberRepository.updateLockReset(memberDTO.getEmail());
		if (sqlCheck == 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		sqlCheck = memberRepository.updatePassword(memberDTO.toEntity());
		if (sqlCheck == 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return (sqlCheck > 0);
	}

	@Override
	public boolean delete(int idx) {
		MemberEntity memberEntity = selectMemberEntity(idx);
		if (memberEntity != null) {
			// delete 삭제
			// memberRepository.deleteById(memberEntity.getIdx());
			// return true;
			return (memberRepository.deleteMember(memberEntity.delete()) > 0);
		}

		return false;
	}

	@Override
	public boolean checkEmailDuplicate(String email) {

		return memberRepository.existsByEmail(email);
	}

	@Override
	public String loginCheck(int idx) {

		return memberRepository.selectLoginCheck(idx);
	}

	@Override
	public int searchLock(String email) {

		return memberRepository.selectLockCheck(email);
	}

	@Override
	public boolean loginSuccess(int idx) {

		return (memberRepository.updateLoginSuccess(idx) > 0);
	}

	@Override
	public boolean lockCount(String email) {

		return (memberRepository.updateLockCount(email) > 0);
	}
}