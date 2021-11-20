<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="clearfix">
	<div>
		<embed src="http://youtube.com/embed/${movieVO.youtubeKey}" style="width:100%; height:350px">
	</div>
	<table class="table">
		<tr>
			<td colspan="3">
				<h1>${movieVO.title}&nbsp;<span style="color: orange">${movieVO.score}</span></h1>
			</td>
		</tr>
		<tr>
			<td style="min-width:250px;width:250px" rowspan="8" class="text-center">
				<img src="${movieVO.poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" style="width:100%" />
			</td>
			<th class="text-center" style="width:30%">감독</th>
			<td style="width:70%">${movieVO.director}</td>
		</tr>
		<tr>
			<th class="text-center" style="width:30%">출연</th>
			<td style="width:70%">${movieVO.actor}</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>장르</th>
			<td style="width:70%">${movieVO.genre}</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>시간</th>
			<td style="width:70%">${movieVO.time}분</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>등급</th>
			<td style="width:70%">${movieVO.grade}</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>상영일</th>
			<td style="width:70%">${movieVO.regdate}</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>예매율</th>
			<td style="width:70%">${movieVO.reserve}</td>
		</tr>
		<tr>
			<th class="text-center" width=10%>누적관객</th>
			<td style="width:70%">${movieVO.showUser}</td>
		</tr>
		<tr>
			<td colspan="3">${movieVO.story}</td>
		</tr>
	</table>
	<div class="text-center table-btn">
				<input type="button" class="btn btn-default" onclick="window.history.back()" value="뒤로가기" />
		<input type="button" class="btn btn-success" onclick="viewReservation()" value="예약하기" />
	</div>
</div>

<script type="text/javascript">
	$(function(){
		commentIdx = "${movieVO.idx}";
		commentType = "movie";
		resetCommentList();
	});
	
	const viewReservation=()=>{
		if("${sessionScope.memberIdx}" === ""){
			alert("로그인 후 가능합니다.");
			return;
		}
		let idx = "${movieVO.idx}";
		let parameterName = "${_csrf.parameterName}";
		let token = "${_csrf.token}";
		let uri = contextRoot + "/movie/reservation";
		let form = $("<form></form>");
		form.attr("action", uri);
		form.attr("method", "post");
		form.appendTo("body");
		form.append("<input type='hidden' name="+parameterName+" value="+token+" />");
		form.append("<input type='hidden' name='idx' value="+idx+" />");
		form.submit();
	}
</script>