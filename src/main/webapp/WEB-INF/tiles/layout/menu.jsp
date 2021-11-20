<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<nav id="menu">
	<div style="color:white; margin-bottom:30px">
		<div>
			<c:if test="${not empty sessionScope.photo}">
				<img class="photo-circle" style="background-color: white" src="${sessionScope.photo}" />
			</c:if>
			<c:if test="${empty sessionScope.photo}">
				<img class="photo-circle" style="background-color: white" src="<c:url value="/images/guest.png" />" />
			</c:if>
		</div>
		<sec:authorize access="isAuthenticated()">
			<a> email : <b>${sessionScope.email}</b><br>
				<b>${sessionScope.nickname}</b>님 안녕하세요.<br>
				접속하신 ip는 <b>${sessionScope.clientIp}</b>입니다.
			</a>
		</sec:authorize>
	</div>
	<ul class="links">
		<li><a href="<c:url value="/" />" style="color:white">홈</a></li>
		<li><a href="<c:url value="/restaurant/list" />" style="color:white">맛집 찾기</a></li>
		<li><a href="<c:url value="/recipe/list" />" style="color:white">레시피 검색</a></li>
		<li><a href="<c:url value="/board/list" />" style="color:white">게시판</a></li>
	</ul>
	<ul class="actions stacked">
		<sec:authorize access="isAnonymous()">
			<li>
				<a href="<c:url value="/member/findPassword" />" class="button asset fit">비밀번호 찾기</a>
			</li>
			<li>
				<a href="<c:url value="/member/signup" />" class="button asset fit">회원가입</a>
			</li>
			<li>
				<a href="<c:url value="/member/login" />" class="button asset primary fit">로그인</a>
			</li>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<li>
				<a href="<c:url value="/member/info" />" class="button asset fit">내 정보</a></li>
			<li>
				<a href="javascript:void(0)" onclick="deleteMember()" class="button asset fit">회원탈퇴</a>
			</li>
			<li>
				<a href="javascript:void(0)" onclick="logout()" class="button asset primary fit">로그아웃</a>
			</li>
		</sec:authorize>
	</ul>
</nav>