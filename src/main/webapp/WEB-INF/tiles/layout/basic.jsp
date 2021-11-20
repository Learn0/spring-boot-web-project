<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- 
	<meta name="viewport" content="width=device-width, user-scalable=no">
	-->
	<meta name="viewport" content="width=600, user-scalable=yes">
	
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap">
	<link rel="stylesheet" href="<c:url value="/plugin/jquery-ui-1.12.0/jquery-ui.min.css" />" />
	<link rel="stylesheet" href="<c:url value="/plugin/assets/css/main.css" />" />
	<link rel="stylesheet" href="<c:url value="/plugin/bootstrap/css/bootstrap.min.css" />" />
	<link rel="stylesheet" href="<c:url value="/css/basic.css" />" />
	
	<script src="<c:url value="/js/jquery.min.js" />"></script>
	<script src="<c:url value="/plugin/jquery-ui-1.12.0/jquery-ui.min.js" />"></script>
	<script src="<c:url value="/plugin/assets/js/jquery.scrolly.min.js" />"></script>
	<script src="<c:url value="/plugin/assets/js/jquery.scrollex.min.js" />"></script>
	<script src="<c:url value="/plugin/assets/js/browser.min.js" />"></script>
	<script src="<c:url value="/plugin/assets/js/breakpoints.min.js" />"></script>
	<script src="<c:url value="/plugin/assets/js/util.js" />"></script>
	<script src="<c:url value="/plugin/bootstrap/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="/js/basic.js" />"></script>
	<script type="text/javascript">
		const contextRoot = "${pageContext.request.contextPath}";
	
		const logout=()=>{
			if (confirm("로그아웃하시겠습니까?")) {
				let parameterName = "${_csrf.parameterName}";
				let token = "${_csrf.token}";
				let uri = contextRoot + "/member/logout";
				let form = $("<form></form>");
				form.attr("action", uri);
				form.attr("method", "post");
				form.appendTo("body");
				form.append("<input type='hidden' name="+parameterName+" value="+token+" />");
				form.submit();
			}
		}
	
		const deleteMember=()=>{
			if (confirm("정말로 탈퇴하시겠습니까?")) {
				let parameterName = "${_csrf.parameterName}";
				let token = "${_csrf.token}";
				let uri = contextRoot + "/member/delete";
				let form = $("<form></form>");
				form.attr("action", uri);
				form.attr("method", "post");
				form.appendTo("body");
				form.append("<input type='hidden' name="+parameterName+" value="+token+" />");
				form.append("<input type='hidden' name='_method' value='DELETE' />");
				form.submit();
			}
		}
		
		const downloadData=(uri)=>{
			let parameterName = "${_csrf.parameterName}";
			let token = "${_csrf.token}";
			let form = $("<form></form>");
			form.attr("action", contextRoot + uri);
			form.attr("method", "post");
			form.appendTo("body");
			form.append("<input type='hidden' name="+parameterName+" value="+token+" />");
			form.submit();
		}
	</script>
</head>
<body class="is-preload">
	<div id="wrapper">
		<tiles:insertAttribute name="header"/>
		<tiles:insertAttribute name="menu"/>
		<tiles:insertAttribute name="sideMenu"/>
		<tiles:insertAttribute name="body"/>
		<tiles:insertAttribute name="footer"/>
    </div>
    <script src="<c:url value="/plugin/assets/js/main.js" />"></script>
</body>
</html>