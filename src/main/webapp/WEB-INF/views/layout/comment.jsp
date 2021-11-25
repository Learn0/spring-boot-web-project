<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div class="box-content">
	<div class="input-group">
		<input type="text" class="form-control" id="content" onKeyDown="event.keyCode === 13 ? insertComment() : ''" maxlength="50" />
		<div class="input-group-btn">
			<input type="button" class="btn btn-success" onclick="insertComment()" value="쓰기" />
		</div>
	</div>
	<ul class="notice-list"></ul>
</div>
<script src="<c:url value="/js/comment.js" />"></script>
<script type="text/javascript">
	let commentIdx;
	let commentType;
	const insertComment=(idx=null, type=null)=>{
		let content;
		if(type === "comment"){
			content = $("#commentInput");
		}else{
			content = $("#content");
		}
		if (content.val() === "") {
			content.attr("placeholder", "댓글을 입력해 주세요.");
			content.focus();
			return;
		}else if("${sessionScope.memberIdx}" === ""){
			alert("로그인 후 가능합니다.");
			content.val("");
			return;
		}

		if(idx == null){
			idx = commentIdx;
			type = commentType;
		}else{
			openInputIdx = -1;
		}
		
		let uri = contextRoot + "/comment/insert";
		let headers = {};
		headers["X-CSRF-TOKEN"] = "${_csrf.token}";
		headers["X-HTTP-Method-Override"] = "POST";
		headers["Content-Type"] = "application/json";
		let commentDTO = {
			"targetIdx" : idx,
			"targetType" : type,
			"content" : content.val(),
			"memberIdx" : "${sessionScope.memberIdx}"
		};

		$.ajax({
			url : uri,
			type : "POST",
			headers : headers,
			data : JSON.stringify(commentDTO),
			success : function(response) {
				if (!response) {
					alert("댓글 등록에 실패하였습니다.");
					return;
				}
				resetCommentList();
				content.val("");
				content.attr("placeholder", "");
			},
			error : function(request, status, error) {
				console.log("code:" + request.status + "\n" + "message:"
						+ request.responseText + "\n" + "error:" + error);
				alert("에러가 발생하였습니다.");
				location.reload();
			}
		});
	}

	const updateComment=(idx)=>{
		let content = $("#modalContent");
		if (content.val() === "") {
			content.focus();
			return;
		}

		let uri = contextRoot + "/comment/insert";
		let headers = {};
		headers["X-CSRF-TOKEN"] = "${_csrf.token}";
		headers["X-HTTP-Method-Override"] = "POST";
		headers["Content-Type"] = "application/json";
		let commentDTO = {
			"idx": idx,
			"content": content.val()
		};

		$.ajax({
			url : uri,
			type : "POST",
			headers : headers,
			data : JSON.stringify(commentDTO),
			success : function(response) {
				if (!response) {
					alert("댓글 수정에 실패하였습니다.");
					return;
				}
				resetCommentList();
				$("#commentModal").modal("hide");
			},
			error : function(request, status, error) {
				console.log("code:" + request.status + "\n" + "message:"
						+ request.responseText + "\n" + "error:" + error);
				alert("에러가 발생하였습니다.");
				location.reload();
			}
		});
	}

	const deleteComment=(idx)=>{
		if (!confirm("댓글을 삭제하시겠어요?"))
			return;

		let uri = contextRoot + "/comment/";
		uri += idx;
		let headers = {};
		headers["X-CSRF-TOKEN"] = "${_csrf.token}";
		headers["X-HTTP-Method-Override"] = "DELETE";

		$.ajax({
			url : uri,
			type : "DELETE",
			headers : headers,
			success : function(response) {
				if (!response) {
					alert("댓글 삭제에 실패하였습니다.");
					return;
				}
				resetCommentList();
				$("#commentModal").modal("hide");
			},
			error : function(request, status, error) {
				console.log("code:" + request.status + "\n" + "message:"
						+ request.responseText + "\n" + "error:" + error);
				alert("에러가 발생하였습니다.");
				location.reload();
			}
		});
	}

	const resetCommentList=()=>{
		let uri = contextRoot + "/comment/";
		uri += commentIdx+"?targetType="+commentType;
		$.get(uri, function(response) {
			let commentHtml = "";
				$.each(response, function(index, comment) {
					commentHtml += selectComment(null, comment, 0);
				});
			$(".notice-list").html(commentHtml);
		}, "json");
	}

	const selectComment=(parent, comment, depth)=>{
		let auth = ((comment.memberIdx === Number("${sessionScope.memberIdx}")) || ("${admin}" != "")) && (comment.deleteCheck === "N");
		let commentHtml = "";
		if(depth >= 1){
			if(auth){
				commentHtml += `<li id="`+(comment.idx+comment.writer)+`" style="min-height:110px; padding-left:`+(depth<7 ? depth*15 : 7*15)+`px">`;
			}else{
				commentHtml += `<li id="`+(comment.idx+comment.writer)+`" style="padding-left:`+(depth<7 ? depth*15 : 7*15)+`px">`;
			}
			let color = ["blue", "green", "red", "black"];
			commentHtml += `<img src="` + contextRoot + `/images/re_icon.png" style="float:left; filter:opacity(0.5) drop-shadow(0 0 0 `+color[(depth-1) % color.length]+`)">`;
		}else{
			if(auth){
				commentHtml += `<li id="`+(comment.idx+comment.writer)+`" style="min-height:110px">`;
			}else{
				commentHtml += `<li id="`+(comment.idx+comment.writer)+`">`;
			}
		}
		if(depth >= 1){
			commentHtml += `<span class="name" style="padding-right:24px"><a href="#" onclick="viewScroll('`+(parent.idx+parent.writer)+`')" style="color:gray">to:`+parent.writer+`</a> `+comment.writer+`</span>
			<span class="content" style="padding-left:24px">`+(comment.deleteCheck === "N" ? comment.content : "삭제된 댓글입니다.")+`</span>`;
		}else{
			commentHtml += `<span class="name" style="padding-right:70px">`+comment.writer+`</span>
			<span class="content">`+(comment.deleteCheck === "N" ? comment.content : "삭제된 댓글입니다.")+`</span>`;
		}
		commentHtml += `<span class="time">`+(comment.deleteCheck === "N" ? comment.updateTime!=null ? "수정:"+getTimeStamp(comment.updateTime) : getTimeStamp(comment.insertTime) : "삭제:"+getTimeStamp(comment.deleteTime))+`</span>`;
		if(auth){
			commentHtml += `<input type="button" class="btn btn-primary" style="width:50px; height:25px; position:absolute; right:15px; top:70px; padding:0px" onclick="openModal('`+comment.idx+`', '`+comment.content+`')" value="수정" />`;
		}
		if("${sessionScope.memberIdx}" != "" && comment.deleteCheck === "N"){
			commentHtml += `<input type="button" class="btn btn-success" style="width:50px; height:25px; position:absolute; right:15px; top:38px; padding:0px" onclick="openInput(`+comment.idx+`)" value="답글" />`;
		}
		commentHtml += "</li>";
		commentHtml += `
		<span id="comment`+comment.idx+`">
		</span>`;
		
		let uri = contextRoot + "/comment/";
		uri += comment.idx+"?targetType=comment";
		
		$.ajax({
			url : uri,
			type : "GET",
			dataType : "json",
			async : false,
			success : function(response) {
				parent = {
						idx: comment.idx,
						writer: comment.writer
				};
				$.each(response, function(index, comment) {
					commentHtml += selectComment(parent, comment, depth+1);
				});
			},
			error : function(request, status, error) {
				console.log("code:" + request.status + "\n" + "message:"
						+ request.responseText + "\n" + "error:" + error);
				alert("에러가 발생하였습니다.");
				location.reload();
			}
		});
		
		return commentHtml; 
	}
</script>