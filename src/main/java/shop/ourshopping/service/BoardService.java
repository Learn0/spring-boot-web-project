package shop.ourshopping.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import shop.ourshopping.dto.mybatis.BoardFileDTO;
import shop.ourshopping.dto.mybatis.BoardDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;

public interface BoardService {

	public boolean insertBoard(BoardDTO boardDTO, MultipartFile[] files);
	public BoardDTO selectBoard(int idx);
	public List<BoardDTO> searchBoard(SearchDTO searchDTO);
	public boolean deleteBoard(int idx);
	public boolean viewCount(int idx);
	public BoardFileDTO selectFile(int idx);
	public List<BoardFileDTO> searchFile(int boardIdx);
}