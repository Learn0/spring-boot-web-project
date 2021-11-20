package shop.ourshopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shop.ourshopping.dto.mybatis.BasicCommentDTO;
import shop.ourshopping.service.CommentService;

// 댓글 관련 기능을 ajax로 주고 받으며 동작
@RestController
@RequestMapping("/comment")
public class CommentController {

	@Resource(name = "commentServiceImpl")
	private CommentService commentService;

	@PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean insertComment(@RequestBody final BasicCommentDTO commentDTO) {
		return commentService.insertComment(commentDTO);
	}

	@GetMapping("/{targetIdx}")
	public List<BasicCommentDTO> searchComment(final BasicCommentDTO commentDTO) {
		return commentService.searchComment(commentDTO);
	}

	@DeleteMapping("/{idx}")
	public boolean deleteComment(@PathVariable("idx") final Integer idx) {
		return commentService.deleteComment(idx);
	}
}