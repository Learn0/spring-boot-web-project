<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<title>식당</title>
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=cd60f0d60f8ebf56777090aefd9990eb&libraries=services"></script>
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
			.img-thumbnail {
				width: calc(20% - 30px);
				margin: 10px;
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
						<div style="width: 100%; text-align: center">
							<c:forTokens var="poster" items="${restaurantVO.poster}" delims="^">
								<img src="${poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" class="img-thumbnail" />
							</c:forTokens>
						</div>
						<table class="table">
							<tbody>
								<tr>
									<td colspan=2>
										<h2>
											${restaurantVO.title} <span style="color: orange; font-size: 30px">${restaurantVO.score.unscaledValue() != 0 ? restaurantVO.score : "?.?"}</span>
										</h2>
									</td>
								</tr>
								<tr>
									<th style="width: 20%" class=text-right>도로명 주소</th>
									<td style="width: 80%">${restaurantVO.address}</td>
								</tr>
								<c:if test="${not empty restaurantVO.oldAddress}">
									<tr>
										<th style="width: 20%" class=text-right>번지 주소</th>
										<td style="width: 80%">${restaurantVO.oldAddress}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.phoneNumber}">
									<tr>
										<th style="width: 20%" class=text-right>전화 번호</th>
										<td style="width: 80%">${restaurantVO.phoneNumber}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.foodType}">
									<tr>
										<th style="width: 20%" class=text-right>음식 종류</th>
										<td style="width: 80%">${restaurantVO.foodType}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.price}">
									<tr>
										<th style="width: 20%" class=text-right>가격대</th>
										<td style="width: 80%">${restaurantVO.price}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.parking}">
									<tr>
										<th style="width: 20%" class=text-right>주차</th>
										<td style="width: 80%">${restaurantVO.parking}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.businessTime}">
									<tr>
										<th style="width: 20%" class=text-right>영업시간</th>
										<td style="width: 80%">${restaurantVO.businessTime}</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.menu}">
									<tr>
										<th style="width: 20%" class=text-right>메뉴</th>
										<td style="width: 80%">
											<ul>
												<c:forTokens var="st" items="${restaurantVO.menu}" delims="원">
													<li style="display: table; margin-left: auto; margin-right: auto">ㆍ${st}원</li>
												</c:forTokens>
											</ul>
										</td>
									</tr>
								</c:if>
								<c:if test="${not empty restaurantVO.site}">
									<tr>
										<th style="width: 20%" class=text-right>홈페이지</th>
										<td style="width: 80%"><a href="${restaurantVO.site}"
											target="_blank">${restaurantVO.site}</a></td>
									</tr>
								</c:if>
								<tr>
									<td colspan=2>
										<ul class="pager" style="margin: 0px auto">
											<c:forTokens var="st" items="${restaurantVO.hashtag}"
												delims="#">
												<li>
													<a href="<c:url value="/restaurant/list?searchKeyword=%23${st}&page=1" />">#${st}</a>&nbsp;
												</li>
											</c:forTokens>
										</ul>
									</td>
								</tr>
								<tr>
									<td colspan=2 id="evaluation"><span id="good"> <input
											type="button" class="btn btn-default" value="좋아요" /> : <span
											style="color: orange"></span>&nbsp;&nbsp;
									</span> <span id="soso"> <input type="button"
											class="btn btn-default" value="보통이에요" /> : <span
											style="color: orange"></span>&nbsp;&nbsp;
									</span> <span id="bad"> <input type="button"
											class="btn btn-default" value="싫어요" /> : <span
											style="color: orange"></span>
									</span></td>
								</tr>
								<tr>
									<td colspan=2>
										<div>
											<div id="chart_div"></div>
											<div id="map" style="width: calc(100% - 450px); min-width: 400px; height: 300px; display: inline-block"></div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						<div class="text-center table-btn">
							<input type="button" class="btn btn-default" onclick="window.history.back()" value="뒤로가기" />
							<input type="button" class="btn btn-success" onclick="viewReservation()" value="예약하기" />
							<input type="button" class="btn btn-primary" onclick="roadMap()" value="크게보기" />
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="box-content">
						<h3 style="color:white">주변 맛집</h3>
						<div style="overflow: auto">
							<c:forEach var="vo" items="${areaList}">
								<table class="table" style="cursor: pointer; margin-bottom: 0px"
									onclick="viewRestaurant(${vo.idx})">
									<tbody>
										<tr>
											<td style="width: 120px" class="text-center" rowspan="3">
												<c:choose>
													<c:when test="${not empty vo.poster}">
														<img src="${fn:substring(vo.poster, 0, fn:indexOf(vo.poster, '^'))}" onError="this.src='<c:url value="/images/pic08.jpg" />'"style="width:100%" />
													</c:when>
													<c:otherwise>
														<span style="display: inline-block; border: solid; width: 90px; height: 90px">
															<span style="width: 90px; height: 90px; display: table-cell; vertical-align: middle">-</span>
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td style="max-width: 0px"><span class="ellipsis">${vo.title}&nbsp;<span
												style="color: orange">${vo.score.unscaledValue() != 0 ? vo.score : "미정"}</span></span></td>
										</tr>
										<tr>
											<td style="max-width: 0px"><span class="ellipsis">주소:${vo.address}</span></td>
										</tr>
										<tr>
											<td style="max-width: 0px"><span class="ellipsis">지역/카테고리:${vo.content}</span></td>
										</tr>
									</tbody>
								</table>
							</c:forEach>
						</div>
					</div>
					<div class="box-content">
						<h3 style="color:white">최근 맛집</h3>
						<div style="overflow: auto">
							<c:forEach var="vo" items="${historyList}">
								<table class="table" style="cursor: pointer; margin-bottom: 0px"
									onclick="viewRestaurant(${vo.idx})">
									<tbody>
										<tr>
											<td style="width: 120px" class="text-center" rowspan="3">
												<c:choose>
													<c:when test="${not empty vo.poster}">
														<img src="${fn:substring(vo.poster, 0, fn:indexOf(vo.poster, '^'))}" onError="this.src='<c:url value="/images/pic08.jpg" />'"style="width:100%" />
													</c:when>
													<c:otherwise>
														<span style="display: inline-block; border: solid; width: 90px; height: 90px">
															<span style="width: 90px; height: 90px; display: table-cell; vertical-align: middle">-</span>
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td style="max-width: 0px"><span class="ellipsis">${vo.title}&nbsp;<span
												style="color: orange">${vo.score.unscaledValue() != 0 ? vo.score : "미정"}</span></span></td>
										</tr>
										<tr>
											<td style="max-width: 0px"><span class="ellipsis">주소:${vo.address}</span></td>
										</tr>
										<tr>
											<td style="max-width: 0px"><span class="ellipsis">지역/카테고리:${vo.content}</span></td>
										</tr>
									</tbody>
								</table>
							</c:forEach>
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
				commentIdx = "${restaurantVO.idx}";
				commentType = "restaurant";
				resetCommentList();
				
				mainSideCheck();

				evaluationReset(${evaluationCheck});
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

					let height = $(".col-md-8>.box-content").height()-$(".col-md-4>.box-content>h3").height()-20;
					$(".col-md-4>.box-content>div").css("height",(height*0.5)-43+"px");
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
			
			const viewRestaurant=(idx)=>{
				let uri = contextRoot + "/restaurant/viewCount?idx="+idx;
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
						evaluation[0] = parseInt(evaluationCheck[key]) + parseInt(${restaurantVO.good});
						$("#good>span").text(evaluation[0]);
						break;
					case "soso":
						evaluation[1] = parseInt(evaluationCheck[key]) + parseInt(${restaurantVO.soso});
						$("#soso>span").text(evaluation[1]);
						break;
					case "bad":
						evaluation[2] = parseInt(evaluationCheck[key]) + parseInt(${restaurantVO.bad});
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
	
			$("#evaluation input").click(function(){
				if("${sessionScope.memberIdx}" === ""){
					alert("로그인 후 가능합니다.");
					return;
				}
				
				let uri = contextRoot + "/restaurant/evaluation";
				let headers = {};
				headers["X-CSRF-TOKEN"] = "${_csrf.token}";
				headers["X-HTTP-Method-Override"] = "POST";
				let data = {
						"restaurantIdx" : ${restaurantVO.idx},
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

		
	        let latitude = null;
			let longitude = null;
			// 주소-좌표 변환 객체를 생성합니다
			let geocoder = new kakao.maps.services.Geocoder();
	
			// 주소로 좌표를 검색합니다
			geocoder.addressSearch("${mapAddress}", function(result, status) {
			    // 정상적으로 검색이 완료됐으면 
			     if (status === kakao.maps.services.Status.OK) {
			        let coords = new kakao.maps.LatLng(result[0].y, result[0].x);
			        latitude = result[0].y;
			        longitude = result[0].x;

					let mapContainer = $("#map")[0], // 지도를 표시할 div 
				    mapOption = {
				        center: coords, // 지도의 중심좌표
				        level: 3 // 지도의 확대 레벨
				    };
					
					// 지도를 생성합니다    
					let map = new kakao.maps.Map(mapContainer, mapOption); 
					
			        // 결과값으로 받은 위치를 마커로 표시합니다
			        let marker = new kakao.maps.Marker({
			            map: map,
			            position: coords
			        });

					let title = "${restaurantVO.title}";
			        // 인포윈도우로 장소에 대한 설명을 표시합니다
			        let infowindow = new kakao.maps.InfoWindow({
			            content: '<div style="width:150px; text-align:center; padding:6px 0; color:black">'+title+'</div>'
			        });
			        infowindow.open(map, marker);
	
			        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
			        map.setCenter(coords);
			    } 
			});
	
			const roadMap=()=>{
				let form = $("#roadMap");
				if(form.length > 0){
					let uri = contextRoot + "/php/roadMap.php";
					form = $("<form></form>");
					form.attr("id", "roadMap");
					form.attr("action", uri);
					form.attr("method", "get");
					form.appendTo("body");
					if(latitude != null && longitude != null){
						form.append("<input type='hidden' name='latitude' value="+latitude+" />");
						form.append("<input type='hidden' name='longitude' value="+longitude+" />");
						form.append("<input type='hidden' name='title' value="+title+" />");
					}
					form.attr("target", "_blank");
				}
				form.submit();
			}
			
			const viewReservation=()=>{
				if("${sessionScope.memberIdx}" === ""){
					alert("로그인 후 가능합니다.");
					return;
				}
				let parameterName = "${_csrf.parameterName}";
				let token = "${_csrf.token}";
				let title = "${restaurantVO.title}";
				let targetIdx = "${restaurantVO.idx}";
				let targetType = "restaurant";
				let uri = contextRoot + "/reservation/write";
				let form = $("<form></form>");
				form.attr("action", uri);
				form.attr("method", "post");
				form.appendTo('body');
				form.append("<input type='hidden' name="+parameterName+" value="+token+" />");
				form.append("<input type='hidden' name='title' value="+title+" />");
				form.append("<input type='hidden' name='targetIdx' value="+targetIdx+" />");
				form.append("<input type='hidden' name='targetType' value="+targetType+" />");
				form.submit();
			}
		</script>
	</body>
</html>