<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<title>레시피</title>
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		<style>
			@media ( min-width : 992px) {
				#main {
					transition: margin-right .5s;
				}
				.col-md-8 {
					width: calc(100% - 400px);
				}
				.col-md-4 {
					width: 400px;
				}
			}
			.col-md-4>.box-content>div {
				height: auto;
				max-height: 500px;
			}
			.pager a {
			 	margin: 5px 0;
			 }
		</style>
	</head>
	<body>
		<%@ include file="../layout/commentModal.jsp"%>
		<div id="main">
			<div id="maxWidth">
				<div class="col-md-8">
					<div class="box-content">
						<div class="sidebtn" style="margin-bottom:20px">
						  	<span style="font-size:30px;cursor:pointer" onclick="changeNav()">&#9776;</span>
						</div>
						<table class="table">
							<tbody>
								<tr>
									<td colspan=2>
										<h2>${recipeVO.title}</h2>
									</td>
								</tr>
								<c:if test="${not empty recipeVO.content}">
									<tr>
										<th style="width: 20%" class=text-right>설명</th>
										<td style="width: 80%">${recipeVO.content}</td>
									</tr>
								</c:if>
								<c:if test="${not empty recipeVO.amount}">
									<tr>
										<th style="width: 20%" class=text-right>음식량</th>
										<td style="width: 80%">${recipeVO.amount}</td>
									</tr>
								</c:if>
								<c:if test="${not empty recipeVO.time}">
									<tr>
										<th style="width: 20%" class=text-right>조리 시간</th>
										<td style="width: 80%">${recipeVO.time}</td>
									</tr>
								</c:if>
								<c:if test="${not empty recipeVO.difficulty}">
									<tr>
										<th style="width: 20%" class=text-right>난이도</th>
										<td style="width: 80%">${recipeVO.difficulty}</td>
									</tr>
								</c:if>
								<tr>
									<td colspan=2>
										<ul class="pager" style="margin: 0px auto">
											<c:forTokens var="st" items="${recipeVO.hashtag}"
												delims="#">
												<li>
													<a href="<c:url value="/recipe/list?searchKeyword=%23${st}&page=1" />">#${st}</a>&nbsp;
												</li>
											</c:forTokens>
										</ul>
									</td>
								</tr>
								<tr>
									<td colspan=2 id="evaluation">
										<span id="good">
											<input type="button" class="btn btn-default" value="좋아요" /> : <span style="color: orange"></span>&nbsp;&nbsp;
										</span>
										<span id="soso">
											<input type="button" class="btn btn-default" value="보통이에요" /> : <span style="color: orange"></span>&nbsp;&nbsp;
										</span>
										<span id="bad">
											<input type="button" class="btn btn-default" value="싫어요" /> : <span style="color: orange"></span>
										</span>
									</td>
								</tr>
								<tr>
									<td colspan=2>
										<div id="chart_div"></div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="col-md-4">
					<div class="box-content">
						<h3 style="color:white">최근 레시피</h3>
						<div style="overflow: auto">
							<c:forEach var="vo" items="${historyList}">
								<table class="table" style="cursor: pointer; margin-bottom: 0px" onclick="viewRecipe(${vo.idx})">
									<tbody>
										<tr>
											<td style="width: 120px" class="text-center" rowspan="2">
												<c:choose>
													<c:when test="${not empty vo.poster}">
														<img src="${fn:substring(vo.poster, 0, fn:indexOf(vo.poster, '^'))}" onError="this.src='<c:url value="/images/pic08.jpg" />'" style="width:100%" />
													</c:when>
													<c:otherwise>
														<span style="display: inline-block; border: solid; width: 90px; height: 90px">
															<span style="width: 90px; height: 90px; display: table-cell; vertical-align: middle">-</span>
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td style="max-width: 0px"><span class="ellipsis">${vo.title}</span></td>
										</tr>
										<tr>
											<td style="max-width: 0px"><span class="ellipsis">${vo.writer}</span></td>
										</tr>
									</tbody>
								</table>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="col-xs-12">
					<div class="box-content">
						<div style="width: 100%; margin-top:10px; text-align: center; color:white">
							<c:set var="posterArr" value="${fn:split(recipeVO.poster,'^')}" />
							<c:set var="stArr" value="${fn:split(recipeVO.cookingOrder,'^')}" />
							<c:forEach var="poster" items="${posterArr}" varStatus="status">
								<div class="media" style="background-color:#242943">
									<div class="media-left">
										<img src="${poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" class="media-object" style="min-width:150px" />
									</div>
									<div class="media-body" style="padding-right:10px">
										<p style="margin-top:10px">${stArr[status.index]}</p>
									</div>
								</div>
							</c:forEach>
							<c:if test="${not empty recipeVO.tip}">
								<div style="padding:20px 0; margin-top:30px; background-color:#242943">
									<p style="margin-bottom:0px">${recipeVO.tip}</p>
								</div>
							</c:if>
						</div>
						<div class="text-center table-btn">
							<input type="button" class="btn btn-default" onclick="window.history.back()" value="뒤로가기" />
							<input type="button" class="btn btn-success" id="save" onclick="recipeSave()" />
						</div>
					</div>
				</div>
				<div class="col-xs-12">
					<%@ include file="../layout/comment.jsp"%>
				</div>
			</div>
		</div>
	
		<script type="text/javascript">
			$(function(){
				commentIdx = "${recipeVO.idx}";
				commentType = "recipe";
				resetCommentList();
				
				mainSideCheck();

				evaluationReset(${evaluationCheck});
				recipeSaveReset(${saveCheck});
			});
			
			$(window).resize(function() {
				mainSideCheck();
			});

			let sizeCheck = false;
			const mainSideCheck=()=>{
				var windowX = $(window).width();
				if(windowX >= 992){
					if(!sizeCheck){
						sizeCheck = true;
						$("#main").attr("class","not");
						$("#main").css("margin-right","auto");
					}

					let height = $(".col-md-8>.box-content").height()-$(".col-md-4>.box-content>h3").height();
					$(".col-md-4>.box-content>div").css("height",height+"px");
					$(".col-md-4>.box-content>div").css("max-height","none");
				}else{
					if(sizeCheck){
						sizeCheck = false;
						$("#main").attr("class","");
						if(nav){
							$("#main").css("margin-right","250px");
						}else{
							$("#main").css("margin-right","auto");	
						}

						$(".col-md-4>.box-content>div").css("height","auto");
						$(".col-md-4>.box-content>div").css("max-height","500px");
					}
				}
			}
			
			const viewRecipe=(idx)=>{
				let uri = contextRoot + "/recipe/viewCount?idx="+idx;
				location.replace(uri);
			}

			let evaluation = [0,0,0];
			const evaluationReset=(evaluationCheck)=>{
				$("#good>input").attr("class", "btn btn-default");
				$("#soso>input").attr("class", "btn btn-default");
				$("#bad>input").attr("class", "btn btn-default");
				for (let key in evaluationCheck) {
					switch(key){
					case "good":
						evaluation[0] = parseInt(evaluationCheck[key]);
						$("#good>span").text(evaluation[0]);
						break;
					case "soso":
						evaluation[1] = parseInt(evaluationCheck[key]);
						$("#soso>span").text(evaluation[1]);
						break;
					case "bad":
						evaluation[2] = parseInt(evaluationCheck[key]);
						$("#bad>span").text(evaluation[2]);
						break;
					case "my":
						switch(evaluationCheck[key]){
						case "good":
							$("#good>input").attr("class", "btn btn-success");
							break;
						case "soso":
							$("#soso>input").attr("class", "btn btn-primary");
							break;
						case "bad":
							$("#bad>input").attr("class", "btn btn-danger");
							break;
						}
						break;
					}
				}
				if(chartLoad){
					drawChart();
				}
			}
			
			const recipeSave=()=>{
				if("${sessionScope.memberIdx}" === ""){
					alert("로그인 후 가능합니다.");
					return;
				}
			
				let uri = contextRoot + "/recipe/save";
				let headers = {};
				headers["X-CSRF-TOKEN"] = "${_csrf.token}";
				headers["X-HTTP-Method-Override"] = "POST";
				let recipeIdx = "recipeIdx=" + ${recipeVO.idx}
	
				$.ajax({
					url : uri,
					type : "POST",
					headers : headers,
					data : recipeIdx,
					success : function(response) {
						if(response){
							alert("찜하였습니다.");
						}else{
							alert("취소하였습니다.");
						}
						recipeSaveReset(response);
					},
					error : function(request, status, error) {
						console.log("code:" + request.status + "\n" + "message:"
								+ request.responseText + "\n" + "error:" + error);
						alert("에러가 발생하였습니다.");
						location.reload();
					}
				});
			}
			
			const recipeSaveReset=(saveCheck)=>{
				if(saveCheck){
					$("#save").val("찜취소하기");
				}else{
					$("#save").val("찜하기");
				}
			}
	
			$("#evaluation input").click(function(){
				if("${sessionScope.memberIdx}" === ""){
					alert("로그인 후 가능합니다.");
					return;
				}
				
				let uri = contextRoot + "/recipe/evaluation";
				let headers = {};
				headers["X-CSRF-TOKEN"] = "${_csrf.token}";
				headers["X-HTTP-Method-Override"] = "POST";
				let data = {
						"recipeIdx" : ${recipeVO.idx},
						"evaluation" : $(this).closest("span").attr("id")
					};
	
				$.ajax({
					url : uri,
					type : "POST",
					headers : headers,
					dataType : "json",
					data : data,
					success : function(response) {
						evaluationReset(response);
					},
					error : function(request, status, error) {
						console.log("code:" + request.status + "\n" + "message:"
								+ request.responseText + "\n" + "error:" + error);
						alert("에러가 발생하였습니다.");
						location.reload();
					}
				});
			});
			
			google.charts.load('current', {'packages':['corechart']});
			
			// Set a callback to run when the Google Visualization API is loaded.
			google.charts.setOnLoadCallback(drawChart);
			
			// Callback that creates and populates a data table,
			// instantiates the pie chart, passes in the data and
			// draws it.
			let chartLoad = false;
			function drawChart() {
				chartLoad = true;
				if(evaluation[0] != 0 || evaluation[1] != 0 || evaluation[2] != 0){
					$("#chart_div").attr("style","width:400px;height:300px;display:inline-block");					
					// Create the data table.
					var data = new google.visualization.DataTable();
					data.addColumn('string', 'Topping');
					data.addColumn('number', 'Slices');
					data.addRows([
						['좋아요', evaluation[0]],
						['보통이에요', evaluation[1]],
						['싫어요', evaluation[2]]
						]);
								
					// Set chart options
					var options = {'title':'평가 점수','width':'100%','height':300,'is3D':true,colors:['green', 'blue', 'red']};
	
					// Instantiate and draw our chart, passing in some options.
					var chart = new google.visualization.PieChart($("#chart_div")[0]);
					chart.draw(data, options);
				}else{
					$("#chart_div").attr("style","");
					$("#chart_div").empty();
				}
				mainSideCheck();
			}
		</script>
	</body>
</html>