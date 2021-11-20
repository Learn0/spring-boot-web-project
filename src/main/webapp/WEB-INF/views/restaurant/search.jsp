<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>식당 다운로드</title>
	</head>
	<body>
		<div id="main">
			<div id="maxWidth">
				<div class="col-xs-12">
					<div class="box-content">
						<div class="sidebtn" style="margin-bottom:20px">
						  	<span style="font-size:30px;cursor:pointer" onclick="changeNav()">&#9776;</span>
						</div>
						<div class="clearfix">
							<form action="<c:url value="/admin/restaurant/download" />" method="post" onsubmit="return formCheck()" autocomplete="on">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" placeholder="키워드를 입력해주세요." />
								<button type="submit" class="btn btn-primary">확인</button>
								<a href="<c:url value="/" />" class="btn btn-default">뒤로가기</a>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script src="<c:url value="/js/hangul.js" />"></script>
		<script src="<c:url value="/js/hashtag.js" />"></script>
		<script type="text/javascript">
			let hashtagList = [];
			<c:forEach var="subway" items="${subwayList}">
				hashtagList.push("#${subway}역");
			</c:forEach>

			const formCheck=()=>{
				let type = $("input[name=searchKeyword]");
				if (type.val() === "") {
					alert("타입을 골라야합니다.");
					return false;
				}
			}
		</script>
	</body>
</html>