<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>์ํ์๋งค</title>
		<style>
			.pagination > li {
				cursor: pointer;
			}
			.pagination > li:first-child > a {
				border-top-left-radius: 0px !important;
				border-bottom-left-radius: 0px !important;
			}
			.pagination > li:last-child > a {
				border-top-right-radius: 0px !important;
				border-bottom-right-radius: 0px !important;
			}
			#category > div.col-xs-2 {
				border: solid 0.5px;
				overflow: auto
			}
			#category > div.col-xs-2 ul {
				list-style: none;
				padding-left: 0px;
				height: 300px;
				color: white;
			}
			#category > div.col-xs-2 li {
				border-bottom: solid;
				border-width: 0.5;
				cursor: pointer;
				font-size: 25px;
			}
			#category > div.col-xs-2 li.active {
				font-weight: bold;
				color: red;
			}
			.seat-container {
				clear:both;
				display: flex;
				justify-content: center;
			}
			.seat-wrapper {
				margin-top: 40px;
				padding-top: 20px;
				background-color: #2a2f4a;
				width: 100%;
				height: 380px;
			}
			.seat {
				font-size: 13px;
				background-color: #555;
				color: white;
				border: 1px solid #388;
			}
			.seat.active {
				background-color: green !important;
			}
			.seat.not {
				background-color: red !important;
			}
			.seat:hover {
				background-color: blue;
				cursor: pointer;
			}
			.seatButtonWrapper {
				text-align: center;
			}
			.left-margin {
				margin-left: 30px;
			}
			.right-margin {
				margin-right: 30px;
			}
			.top-margin {
				margin-top: 30px;
			}
			.movieInfo * {
				color: white;
			}
			.movieInfo div {
				margin-left: 10px;
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
						<div id="category" style="margin-bottom:30px; height:300px">
							<div class="col-xs-1">
							</div>
							<div class="col-xs-2">
								<ul id="type0">
									<c:forEach var="area" items="${areaList}">
										<li onclick="typeClick(this)">${area}</li>
									</c:forEach>
								</ul>
							</div>
							<div class="col-xs-2">
								<ul id="type1">
	
								</ul>
							</div>
							<div class="col-xs-2">
								<ul id="type2">
	
								</ul>
							</div>
							<div class="col-xs-2">
								<ul id="type3">
	
								</ul>
							</div>
							<div class="col-xs-2">
								<ul id="type4">
	
								</ul>
							</div>
						</div>
						<div class="select-wrapper">
							<div class="col-xs-1"></div>
							<div class="col-xs-3">
								<div>
									<h3 style="color:white">์ฑ์ธ</h3>
									<ul id="adult" class="pagination" style="margin:0px">
										<li class="active">
											<a href="#none" onclick="selectPeople(this)">0</a>
										</li>
										<c:forEach var="item" begin="1" end="5" step="1">
											<li>
												<a href="#none" onclick="selectPeople(this)">${item}</a>
											</li>
										</c:forEach>
									</ul>
								</div>
								<div>
									<h3 style="color:white">์ฒญ์๋</h3>
									<ul id="minors" class="pagination" style="margin:0px">
										<li class="active">
											<a href="#none" onclick="selectPeople(this)">0</a>
										</li>
										<c:forEach var="item" begin="1" end="5" step="1">
											<li>
												<a href="#none" onclick="selectPeople(this)">${item}</a>
											</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<div class="col-xs-8">
								<div style="width:130px;margin-top:10px;margin-right:10px;float:left">
									<img src="${movieVO.poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" style="width:100%; min-height:100%" />
								</div>
								<div class="movieInfo" style="float:right; width:calc(100% - 150px)">
									<span class="ellipsis" style="font-size:30px">${movieVO.title}</span>
									<div><span class="ellipsis" id="area">์ํ๊ด</span></div>
									<div>๋?์ง : <span id="date"></span></div>
									<div>์๊ฐ : <span id="time"></span></div>
									<div>์๊ธ : <span id="price">0์</span></div>
									<div>์?ํ ์ข์ : <span id="selectSeat">0 / 0</span></div>
									<div>๋จ์ ์ข์ : <span id="allSeat">132</span> / 132</div>
								</div>
							</div>
							<div class="seat-container">
								<div class="seat-wrapper">
								</div>
							</div>
						</div>
						<div class="text-center table-btn">
							<input type="button" class="btn btn-default" onclick="window.history.back()" value="๋ค๋ก๊ฐ๊ธฐ" />
							<input type="button" class="btn btn-primary" onclick="reservation()" value="์๋งคํ๊ธฐ" />
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			$(function() {
				let abc = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"]
				for (let i = 0; i < abc.length; i++) {
					let div = $("<div></div>");
					div.attr("class", "seatButtonWrapper");
					$(".seat-wrapper").append(div);
	
					for (let j = 1; j <= 12; j++) {
						let input = $("<input type='button' name='seats' class='seat' value="+(abc[i]+j)+" onclick='seatClick("+(i*12+j)+")' />");
						div.append(input);
					}
					$(".all-seats").html($(".seat").length);
				}
	
				$(".seat").each(function(index, data) {
					let name = $(data).val();
					if (name.substring(1, name.length) === "3") {
						$(data).addClass("left-margin");
					} else if (name.substring(1, name.length) === "10") {
						$(data).addClass("right-margin");
					}
					if (name.substring(0, 1) === "E") {
						$(data).addClass("top-margin");
					}
				});
				$(".seat-wrapper").hide();
				
				targetAdult = $("#adult>.active");
				targetMinors = $("#minors>.active");
			});

			let area = [];
			let date, time;
			const typeClick=(obj)=>{
				$(obj).siblings().removeClass("active");
				$(obj).addClass("active");
				
				let targetId = $(obj).closest("ul").attr("id");
				if(targetId === "type4"){
					time = $(obj).text();
					$("#time").text(time);
				}else{
					if(targetId === "type3"){
						nextTarget = "type4"
						date = $(obj).text();
						$("#date").text(date);
					}else{
						if(targetId === "type2"){
							nextTarget = "type3"
							area[2] = $(obj).text();
							$("#area").text(area[0]+" "+area[1]+" "+area[2]);
						}else{
							if(targetId === "type1"){
								nextTarget = "type2"
								area[1] = $(obj).text();
								$("#area").text(area[0]+" "+area[1]);
							}else{
								nextTarget = "type1"
								area[0] = $(obj).text();
								area[1] = "";
								$("#area").text(area[0]);
								$("#type2").html("");
							}
							area[2] = "";
							$("#type3").html("");
						}
						date = "";
						$("#date").text(date);
						$("#type4").html("");
					}
					time = "";
					$("#time").text(time);
				}
				if(time != ""){
					reservationReset();
					$(".seat-wrapper").show();
				}else{
					$(".seat-wrapper").hide();
					
					let uri = contextRoot + "/movie/category";
					let headers = {};
					headers["X-CSRF-TOKEN"] = "${_csrf.token}";
					headers["X-HTTP-Method-Override"] = "POST";
					let type;
					if(nextTarget === "type2" && area[0] === area[1]){
						type = "type=";
					}else{
						type = "type="+$(obj).text();
					}

					$.ajax({
						url : uri,
						type : "POST",
						headers : headers,
						dataType : "json",
						data : type,
						success : function(response) {
							// JSON.parse ๋์? dataType : "json"์ ์ฌ์ฉ
							// response = JSON.parse(response);
							let html = "";
							$.each(response, function(index, category) {
								html += `<li onclick="typeClick(this)">`+category.name+`</li>`;
							});
							$("#"+nextTarget).html(html);
						},
						error : function(request, status, error) {
							console.log("code:" + request.status + "\n" + "message:"
									+ request.responseText + "\n" + "error:" + error);
							alert("์๋ฌ๊ฐ ๋ฐ์ํ์์ต๋๋ค.");
							location.reload();
						}
					});
				}
			}
			
			let adult = 0, minors = 0;
			let targetAdult, targetMinors;
			const selectPeople=(obj)=>{
				if($(obj).closest("ul").attr("id") === "adult"){
					if(seatList.length > Number($(obj).text())+Number(minors)){
						alert("์?ํํ ์๋ฆฌ๋ฅผ ์ทจ์ํด์ฃผ์ธ์. ์ธ์์๊ฐ ๋ง์ง ์์ต๋๋ค.")
						return;
					}
					if(targetAdult != null){
						targetAdult.removeClass("active");
						if(targetAdult === $(obj).parent){
							targetAdult = null;
							adult = 0;
							return;
						}
					}
					targetAdult = $(obj).parent();
					targetAdult.addClass("active");
					adult = $(obj).text();
				}else{
					if(seatList.length > Number(adult)+Number($(obj).text())){
						alert("์?ํํ ์๋ฆฌ๋ฅผ ์ทจ์ํด์ฃผ์ธ์. ์ธ์์๊ฐ ๋ง์ง ์์ต๋๋ค.")
						return;
					}
					if(targetMinors != null){
						targetMinors.removeClass("active");
						if(targetMinors === $(obj).parent){
							targetMinors = null;
							minors = 0;
							return;
						}
					}
					targetMinors = $(obj).parent();
					targetMinors.addClass("active");
					minors = $(obj).text();
				}
				$("#price").text(String((Number(adult)*10000)+(Number(minors)*7000)).replace(/\B(?=(\d{3})+(?!\d))/g, ",")+"์");
				$("#selectSeat").text(seatList.length+" / "+(Number(adult)+Number(minors)));
			}
			
			let seatList = [];
			const seatClick=(idx)=>{
				if(seatList.indexOf(idx) >= 0){
					$($(".seat")[idx-1]).removeClass("active");
					seatList.splice(seatList.indexOf(idx), 1);
				}else{
					if(notSeatList.indexOf(idx) >= 0){
						alert("์ด๋ฏธ ์์ฝ๋ ์๋ฆฌ์๋๋ค.");
						return;
					}
					if(seatList.length >= Number(adult)+Number(minors)){
						alert("์?ํ ์ธ์์ ์ด๊ณผํ์์ต๋๋ค.");
						return;
					}
					$($(".seat")[idx-1]).addClass("active");
					seatList.push(idx);
				}
				$("#selectSeat").text(seatList.length+" / "+(Number(adult)+Number(minors)));
				$("#allSeat").text(132-(seatList.length+notSeatList.length));
			}
			
			const reservation=()=>{
				if(seatList.length != Number(adult)+Number(minors)){
					alert("์?๋ถ ์?ํํด์ฃผ์ธ์.");
					return;
				}else if(0 === Number(adult)+Number(minors)){
					alert("์ธ์์ ์?ํํด์ฃผ์ธ์.");
					return;
				}

				let uri = contextRoot + "/movie/reservationCheck";
				let headers = {};
				headers["X-CSRF-TOKEN"] = "${_csrf.token}";
				headers["X-HTTP-Method-Override"] = "POST";
				let movieReservationVO = {
					"movieIdx" : "${movieVO.idx}",
					"area" : $("#area").text(),
					"date" : date,
					"time" : time,
					"adult" : adult,
					"minors" : minors,
					"seat" : JSON.stringify(seatList)
				};

				$.ajax({
					url : uri,
					type : "POST",
					headers : headers,
					data : movieReservationVO,
					success : function(response) {
						if(response){
							alert("์๋งค๊ฐ ์๋ฃ๋์์ต๋๋ค.");
							location.href = contextRoot + "/member/info?type=3";
						}else{
							alert("์ด๋ฏธ ์์ฝ๋ ์๋ฆฌ์๋๋ค.");
							reservationReset();
						}
					},
					error : function(request, status, error) {
						console.log("code:" + request.status + "\n" + "message:"
								+ request.responseText + "\n" + "error:" + error);
						alert("์๋ฌ๊ฐ ๋ฐ์ํ์์ต๋๋ค.");
						location.reload();
					}
				});
			}
			
			let notSeatList = [];
			const reservationReset=()=>{
				let uri = contextRoot + "/movie/reservationReset";
				let headers = {};
				headers["X-CSRF-TOKEN"] = "${_csrf.token}";
				headers["X-HTTP-Method-Override"] = "POST";
				let movieReservationVO = {
					"movieIdx" : "${movieVO.idx}",
					"area" : $("#area").text(),
					"date" : date,
					"time" : time
				};

				$.ajax({
					url : uri,
					type : "POST",
					headers : headers,
					data : movieReservationVO,
					success : function(response) {
						$.each(notSeatList, function(index, seat) {
							$($(".seat")[seat-1]).removeClass("not");
						});
						notSeatList = JSON.parse(response);
						$.each(notSeatList, function(index, seat) {
							notSeatList[index] = seat;
							$($(".seat")[seat-1]).addClass("not");
							
							if(seatList.indexOf(seat) >= 0){
								$($(".seat")[seat-1]).removeClass("active");
								seatList.splice(seatList.indexOf(seat), 1);
								$("#selectSeat").text(seatList.length+" / "+(Number(adult)+Number(minors)));
							}
						});
						$("#allSeat").text(132-(seatList.length+notSeatList.length));
					},
					error : function(request, status, error) {
						console.log("code:" + request.status + "\n" + "message:"
								+ request.responseText + "\n" + "error:" + error);
						alert("์๋ฌ๊ฐ ๋ฐ์ํ์์ต๋๋ค.");
						location.reload();
					}
				});
			}
		</script>
	</body>
</html>