<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div id="mySidenav" class="sidenav">
	<a href="javascript:void(0)" class="closebtn" onclick="changeNav()">&times;</a>
	<!--<sec:authorize access="hasRole('ROLE_ADMIN')"><a href="<c:url value="/member/info" />">관리자</a></sec:authorize>-->
	<!--<sec:authorize access="isAuthenticated()"><a href="<c:url value="/game/list" />">미니게임</a></sec:authorize>-->
	<a href="<c:url value="/shopping/list" />">쇼핑</a>
	<a href="<c:url value="/movie/main" />">상영영화</a>
	<a href="<c:url value="/music/list" />">뮤직 Top 200</a>
	<a href="<c:url value="/seoulAttractions/list" />">서울 명소</a>
	<a href="<c:url value="/news/list" />">실시간 뉴스</a>
	<a href="<c:url value="/chat/list" />">채팅</a>
	<a href="<c:url value="/calendar/view" />">달력</a>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<div class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">다운로드
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="javascript:void(0)" onclick="downloadData('/admin/music/download')">&nbsp; -실시간 음악 다운로드</a>
				<li><a href="<c:url value="/admin/restaurant/search" />">&nbsp; -맛집 다운로드</a>
				<li><a href="javascript:void(0)" onclick="downloadData('/admin/movie/download')">&nbsp; -영화 다운로드</a>
				<li><a href="javascript:void(0)" onclick="downloadData('/admin/recipe/download')">&nbsp; -레시피 다운로드</a>
				<li><a href="javascript:void(0)" onclick="downloadData('/admin/attractions/download')">&nbsp; -서울 명소 다운로드</a>
			</ul>
		</div>
	</sec:authorize>
</div>