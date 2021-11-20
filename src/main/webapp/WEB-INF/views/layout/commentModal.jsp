<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div id="commentModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
		   <div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" style="font-size:40px">&times;</button>
				<h4 class="modal-title" style="font-size:25px">댓글 수정</h4>
		   </div>
			<div class="modal-body">
				<input type="text" id="modalContent" class="form-control" placeholder="내용을 입력해 주세요." maxlength="50" />
			</div>
			<div class="modal-footer">
				<input type="button" id="btnCommentUpdate" class="btn btn-primary" value="수정하기" />
				<input type="button" id="btnCommentDelete" class="btn btn-danger" value="삭제하기" />
			</div>
		</div>
	</div>
</div>