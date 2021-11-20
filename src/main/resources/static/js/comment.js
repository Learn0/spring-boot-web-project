const viewScroll=(selector)=>{
	let location = $("#"+selector).offset().top;
	$("html").animate({scrollTop : location}, 400);
}

const openModal=(idx, content)=>{
	$("#commentModal").modal("toggle");

	$("#modalContent").val(content);

	$("#modalContent").attr("onKeyDown", "event.keyCode === 13 ? updateComment("+ idx +") : ''");
	$("#btnCommentUpdate").attr("onclick", "updateComment("+ idx +")");
	$("#btnCommentDelete").attr("onclick", "deleteComment("+ idx +")");
}

let openInputIdx = -1;
const openInput=(idx)=>{
	if(openInputIdx >= 0){
		//$("span[id^='comment']").html("");
		$("span#comment"+openInputIdx).html("");
	}
	if(openInputIdx != idx){
		$("#comment"+idx).html(`
				<div class="input-group" style="margin-top:10px">
					<input type="text" id="commentInput" class="form-control" maxlength="50" onKeyDown="event.keyCode === 13 ? insertComment(`+idx+`,'comment') : ''" />
					<div class="input-group-btn">
						<input type="button" class="btn btn-success" onclick="insertComment(`+idx+`,'comment')" value="답글" />
					</div>
				</div>
				`);
		$("#commentInput").focus();
		openInputIdx = idx;
	}else{
		openInputIdx = -1;
	}
}