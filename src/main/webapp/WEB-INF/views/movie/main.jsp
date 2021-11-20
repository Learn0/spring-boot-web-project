<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>상영영화</title>
		<style>
			.card {
				box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
			 	transition: 0.3s;
			  	width: 40%;
			}
			.card > div > h4 > * {
				background-color: #fee;
			}
			.card > .card-img {
				padding-bottom: 140%;
			}
			.card > .card-img > img {
				min-height: 100%;
			}
			.card:hover {
			  	box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
			}
		</style>
	</head>
	<body>
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}" />
				<div class="nav navbar-nav" style="margin:0px">
					<a class="navbar-brand" href="${uri}?idx=0">상영 영화</a>
					<a class="navbar-brand" href="${uri}?idx=1">박스오피스</a>
					<a class="navbar-brand" href="${uri}?idx=2">실시간 예매율</a>
					<a class="navbar-brand" href="${uri}?idx=3">독립 예술</a>
				</div>
				<div class="nav navbar-nav" style="margin:0px">
					<a class="navbar-brand" href="${uri}?idx=4">독릭 예술 예매율</a>
					<a class="navbar-brand" href="${uri}?idx=5">좌석 점유율</a>
					<a class="navbar-brand" href="${uri}?idx=6">온라인 상영관</a>
				</div>
			</div>
		</nav>
		<c:if test="${not empty movieVO}">
			<%@ include file="../layout/commentModal.jsp"%>
		</c:if>
		<div id="main">
			<div id="maxWidth">
				<div class="col-xs-12">
					<div class="box-content">
						<div class="sidebtn" style="margin-bottom:20px">
						  	<span style="font-size:30px;cursor:pointer" onclick="changeNav()">&#9776;</span>
						</div>
						<jsp:include page="${url}" />
					</div>
					<c:if test="${not empty movieVO}">
						<%@ include file="../layout/comment.jsp"%>
					</c:if>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			$(function(){
				$(".navbar-nav:nth-child("+parseInt(${1 + (idx / 4)})+") > a.navbar-brand:nth-child("+parseInt(${1 + (idx % 4)})+")").addClass("active");
			});
		</script>
	</body>
</html>