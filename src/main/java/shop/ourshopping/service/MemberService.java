package shop.ourshopping.service;

import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.entity.MemberEntity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService extends UserDetailsService {

	public boolean signup(MemberDTO memberDTO, MultipartFile files);
	public MemberEntity selectMemberEntity(int idx);
	public MemberEntity selectMemberEntity(String email);
	public boolean update(MemberDTO memberDTO, MultipartFile files);
	public boolean updateAll(MemberDTO memberDTO, MultipartFile files);
	public boolean updatePassword(MemberDTO memberDTO);
	public boolean delete(int idx);
	public boolean checkEmailDuplicate(String email);
	public String loginCheck(int idx);
	public int searchLock(String email);
	public boolean loginSuccess(int idx);
	public boolean lockCount(String email);
}