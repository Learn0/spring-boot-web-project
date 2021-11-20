<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Loading</title>	
		<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
	</head>
	<body>
		<c:if test="${not empty redirectUri}">
			<c:choose>
				<c:when test="${method == 'get' || method == 'post'}">
					<form action="<c:url value="${redirectUri}" />" method="${method}" style="display:none">
						<c:if test="${method == 'post'}">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</c:if>
						<c:forEach var="key" items="${params}">
							<input type="hidden" name="${key.key}" value="${key.value}" />
						</c:forEach>
					</form>
				</c:when>
				<c:otherwise>
					<form action="<c:url value="${redirectUri}" />" method="post" style="display:none">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<input type="hidden" name="_method" value="${method}" />
						<c:forEach var="key" items="${params}">
							<input type="hidden" name="${key.key}" value="${key.value}" />
						</c:forEach>
					</form>
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${empty redirectUri}">
			<form action="<c:url value="/" />" method="get" style="display:none">
			</form>
		</c:if>
		<script>
			$(function(){
				let message = "${message}";
				if (message != null) {
					alert(message);
				}
				$("form").submit();
			});
		</script>
	</body>
</html>