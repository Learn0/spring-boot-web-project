package shop.ourshopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.BasicCommentDTO;
import shop.ourshopping.mapper.BasicCommentMapper;

//댓글 관련 DB 비지니스 처리
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private BasicCommentMapper commentMapper;

	@Override
	public boolean insertComment(BasicCommentDTO comment) {
		int sqlCheck;
		if (comment.getIdx() == null) {
			sqlCheck = commentMapper.insertComment(comment);
		} else {
			sqlCheck = commentMapper.updateComment(comment);
		}

		return (sqlCheck > 0);
	}

	@Override
	public boolean deleteComment(int idx) {
		return (commentMapper.deleteComment(idx) > 0);
	}

	@Override
	public List<BasicCommentDTO> searchComment(BasicCommentDTO commentDTO) {
		return commentMapper.searchComment(commentDTO);
	}
}