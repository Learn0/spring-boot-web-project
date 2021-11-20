package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.BasicCommentDTO;

public interface CommentService {

	public boolean insertComment(BasicCommentDTO commentDTO);
	public boolean deleteComment(int idx);
	public List<BasicCommentDTO> searchComment(BasicCommentDTO commentDTO);
}