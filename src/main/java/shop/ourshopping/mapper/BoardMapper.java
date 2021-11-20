package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.BoardDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;

// 메소드명과 같은 BoardMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface BoardMapper {

	public Map<String, Object> selectBoardGroup();
	public int insertBoard(BoardDTO boardDTO);
	public int updateBoardOrder(Map<String, Integer> map);
	public BoardDTO selectBoard(int idx);
	public List<BoardDTO> searchBoard(SearchDTO searchDTO);
	public int searchBoardCount(SearchDTO searchDTO);
	public int viewCount(int idx);
	public int updateBoard(BoardDTO boardDTO);
	public int deleteBoard(int idx);
}