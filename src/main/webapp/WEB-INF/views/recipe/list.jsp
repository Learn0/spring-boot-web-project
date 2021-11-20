<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<title>레시피 리스트</title>
		<style>
			.table > thead > tr > th:first-child {
				width: 80px;
			}
			.table > thead > tr > th:last-child {
				width: 70px;
			}
			.table > tbody > tr > td:nth-child(2) {
				max-width: 300px;
			}
			.table > tbody > tr > td:nth-child(3) {
				width: 150px;
			}
			.table > tbody > tr > td > img {
				min-width: 60px;
				width: 60px;
				height: 60px;
			}
			.table > tbody > tr {
				cursor: pointer;
			}
		</style>
	</head>
	<body>
		<div id="main">
			<div id="maxWidth">
				<div class="col-xs-12">
					<div class="box-content">
						<div class="sidebtn" style="margin-bottom:20px">
						  	<span style="font-size:30px;cursor:pointer" onclick="changeNav()">&#9776;</span>
						</div>
						<form action="<c:url value="/recipe/list" />" method="get" class="form-horizontal">
							<div class="input-group">
								<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" value="${searchDTO.searchKeyword}" placeholder="키워드를 입력해 주세요." />
								<input type="hidden" name="page" value="1" />
								<div class="input-group-btn">
									<button type="submit" class="btn btn-success">검색</button>
								</div>
							</div>
						</form>
						<table class="table table-hover">
							<thead>
								<tr class="danger">
									<th></th>
									<th>제목</th>
									<th>저자</th>
									<th>조회 수</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(recipeList) > 0}">
										<c:forEach var="recipeVO" items="${recipeList}">
											<tr onclick="viewRecipe(${recipeVO.idx})">
												<c:choose>
													<c:when test="${not empty recipeVO.poster}">
														<td>
															<c:choose>
																<c:when test="${fn:contains(recipeVO.poster, 'a')}">
																	<c:set var="posterArr" value="${fn:split(recipeVO.poster,'^')}" />
																	<img src="${posterArr[fn:length(posterArr)-1]}" onError="this.src='<c:url value="/images/pic08.jpg" />'" class="img-circle" />
																</c:when>
																<c:otherwise>
																	<img src="${recipeVO.poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" class="img-circle" />
																</c:otherwise>
															</c:choose>
														</td>
													</c:when>
													<c:otherwise>
														<td>-</td>
													</c:otherwise>
												</c:choose>
												<td><span class="ellipsis-2">${recipeVO.title}</span></td>
												<td>${recipeVO.writer}</td>
												<td>${recipeVO.viewCount}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="100%">조회된 결과가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<c:if test="${not empty searchDTO && searchDTO.pageInfo.pageCount > 0}">
							<nav class="text-center">
								<ul class="pagination">
									<c:if test="${searchDTO.pageInfo.previousCheck}">
										<li onclick="movePage(1)">
											<a href="javascript:void(0)"><span>&laquo;</span></a>
										</li>
										<li onclick="movePage(${searchDTO.pageInfo.firstPage - 1})">
											<a href="javascript:void(0)"><span>&lsaquo;</span></a>
										</li>
									</c:if>
									<c:forEach var="pageNo" begin="${searchDTO.pageInfo.firstPage}" end="${searchDTO.pageInfo.lastPage}">
										<li onclick="movePage('${pageNo}')" class="${(pageNo eq searchDTO.page) ? 'active' : '' }">
											<a href="javascript:void(0)">${pageNo}</a>
										</li>
									</c:forEach>
									<c:if test="${searchDTO.pageInfo.nextCheck}">
										<li onclick="movePage(${searchDTO.pageInfo.lastPage + 1})">
											<a href="javascript:void(0)"><span>&rsaquo;</span></a>
										</li>
										<li onclick="movePage(${searchDTO.pageInfo.pageCount})">
											<a href="javascript:void(0)"><span>&raquo;</span></a>
										</li>
									</c:if>
								</ul>
							</nav>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		
		<script src="<c:url value="/js/hangul.js" />"></script>
		<script src="<c:url value="/js/hashtag.js" />"></script>
		<script type="text/javascript">
			let hashtagList = [];
			<c:forEach var="hashtag" items="${hashtagList}">
				hashtagList.push("${hashtag}");
			</c:forEach>
			
			const viewRecipe=(idx)=>{
				let uri = contextRoot + "/recipe/viewCount";
				let form = $("<form></form>");
				form.attr("action", uri);
				form.attr("method", "get");
				form.appendTo("body");
				form.append("<input type='hidden' name='idx' value="+idx+" />");
				form.submit();
			}
	
			const movePage=(page)=>{
				let uri = "${requestScope['javax.servlet.forward.request_uri']}";
				uri += "?page=" + page;
				uri += "&searchKeyword=";
				uri += encodeURIComponent("${searchDTO.searchKeyword}");
				location.replace(uri);
			}
		</script>
	</body>
</html>