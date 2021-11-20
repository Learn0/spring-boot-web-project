<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>엑셀 업로드</title>
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
							<form action="<c:url value="/excel/upload" />" method="post" enctype="multipart/form-data">
								<input multiple="multiple" type="file" name="files">
								<input type="submit" value="확인" />
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>