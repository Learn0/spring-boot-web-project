package shop.ourshopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.BasicCommentDTO;

// 메소드명과 같은 BasicCommentMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface BasicCommentMapper {

	public int insertComment(BasicCommentDTO commentDTO);
	public List<BasicCommentDTO> searchComment(BasicCommentDTO commentDTO);
	public int searchCommentCount(BasicCommentDTO commentDTO);
	public int updateComment(BasicCommentDTO commentDTO);
	public int deleteComment(int idx);
}