<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<!DOCTYPE html>
<html>
	<head>
		<title>달력</title>
		<style>
			.monthMove span:first-child {
				float: left;
				width: 33.3%;
				margin: 0px auto;
			}
			.monthMove span:nth-child(2) {
				float: left;
				width: 33.3%;
				margin: 0px auto;
				font-size: 40px;
				text-align: center;
			}
			.monthMove span:last-child {
				float: right;
				width: 33.3%;
				margin: 0px auto;
			}
			.table {
				background-color: #aaffa8;
				border-collapse: separate;
				border-spacing: 0;
			}
			.table > tbody > tr > td{
				background-color: white;
				color: black;
				width: 14.2%
			}
			.table > tbody > tr > td.now{
				background-color: #aaffa8;
				font-weight: bold;
			}
			.table > thead > tr > th.sun,
			.table > tbody > tr > td.sun{
				color: red;
			}
			.table > thead > tr > th.sat,
			.table > tbody > tr > td.sat{
				color: blue;
			}
			.table > tbody > tr > td.nan{
				color: gray;
			}
			.table-hover-large > tbody > tr > td {
			    border: solid 1px #000;
			    border-style: none solid solid none;
			    padding: 10px;
			    border-radius: 10px;
				font-size:30px;
			}
			.table-hover-large > tbody > tr > td > p {
				font-size:20px;
			}
			.table-hover-large > tbody > tr > td:hover {
  				background-color: #f5f5f5;
				cursor: pointer;
			}
			.table-hover-large > tbody > tr > td.now:hover {
				background-color: #99dda7;
			}
			
			.table-hover > tbody > tr > td {
				padding: 3px;
			}
			.table-hover > tbody > tr:hover {
				cursor: pointer;
			}
			.table-hover > tbody > tr:hover > td {
				padding: 0px;
				border: orange 3px;
			    border-style: solid none solid none;
			}
			.table-hover > tbody > tr:hover > td:first-child {
			    border-style: solid none solid solid;
  				border-bottom-left-radius: 40%;
  				border-top-left-radius: 40%;
			}
			.table-hover > tbody > tr:hover > td:last-child {
			    border-style: solid solid solid none;
  				border-bottom-right-radius: 40%;
  				border-top-right-radius: 40%;
			}
			.table-hover {
				float:left;
			}
			#calendar {
				clear:both;
			}
			#calendar > div {
				width: calc(100% - 390px);
				height: 230px;
				min-width:450px;
			}
			@media (max-width: 1025px) {
				#calendar > div {
	    			position: Relative;
	    			top: -30px;
				}
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
						<div class="clearfix">
							<div class="dropdown">
								<button type="button" data-toggle="dropdown"><span class="caret"></span></button>
								<div class="dropdown-menu" style="min-width:0px; width:100px; text-align:center">
								   	<form>
								   		<span id="yOptions"></span>
								   		<span id="mOptions"></span>
								   	</form>
								</div>
							</div>
							<div>
								<span class="monthMove">
									<span>
										<Input type="button" onclick="monthMove(-1)" style="float:right; width:100px" value="◀" />
									</span>
									<span id="date" style="color:white"></span>
									<span>
										<Input type="button" onclick="monthMove(+1)" style="float:left; width:100px" value="▶"/>
									</span>
								</span>
								<div id="bigCalendar"></div>
							</div>
							<div>
								<div id="smallCalendar"></div>
								<div id="calendar" class="dropdown">
									<div class="dropdown-menu dropdown-menu-right" onmouseover=mouseIn(null,null) onmouseout=mouseOut()>
								      	<ul style="float:left; width:50%; padding:33px">
								          	<li>내용1</li>
								          	<li>내용2</li>
								          	<li>내용3</li>
								          	<li>내용4</li>
								          	<li>내용5</li>
							 	          	<li>내용6</li>
								      	</ul>
								      	<ul style="float:left; width:50%; padding:33px">
								          	<li>내용7</li>
								          	<li>내용8</li>
								          	<li>내용9</li>
								          	<li>내용10</li>
								          	<li>내용11</li>
								          	<li>내용12</li>
								      	</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			let nowYear;
			let nowMonth;
			let nowDay;
			
			let year;
			let month;

			$(function(){
				nowYear = ${nowYear};
				nowMonth = ${nowMonth};
				nowDay = ${nowDay};
				
				year = ${year};
				month = ${month};
				
				reset();
			});
			
			const reset=()=>{
				let yOptions = "<select id='year' name='year' onchange='formCalendar()'>";
				for (let year_ = (nowYear - 10); year_ <= (nowYear + 10); year_++) {
					if (year_ === year) {
						yOptions += '<option value='+year_+' selected="selected">'+year_+'</option>';
					} else {
						yOptions += '<option value='+year_+'>'+year_+'</option>';
					}
				}
				yOptions += "</select>년";

				let mOptions = "<select id='month' name='month' onchange='formCalendar()'>";
				for (let month_ = 1; month_ <= 12; month_++) {
					if (month_ === month) {
						mOptions += '<option value='+month_+' selected="selected">'+month_+'</option>';
					} else {
						mOptions += '<option value='+month_+'>'+month_+'</option>';
					}
				}
				mOptions += "</select>월";
				
				let months = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
				if (Math.floor(year % 4) === 0 && Math.floor(year % 100) != 0 || Math.floor(year % 400) === 0) {
					months[1] = 29;
				}

				let nalsu = (year - 1) * 365 + Math.floor((year - 1) / 4) - Math.floor((year - 1) / 100) + Math.floor((year - 1) / 400);
				for (let i = 0; i < (month - 1); i++) {
					nalsu += months[i];
				}
				nalsu += 1;

				let dayOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
				let week = Math.floor(nalsu % dayOfWeek.length);
				let lastDay = months[month - 1];
				let previousMonthLastDay;
				if (month === 1) {
					previousMonthLastDay = months[11];
				} else {
					previousMonthLastDay = months[month - 2];
				}

				let bigCalendar = "<table class='table table-hover-large' style='height:700px'><thead><tr>";
				for (let i = 0; i < dayOfWeek.length; i++) {
					if (i === 0) {
						bigCalendar += "<th class='sun'>";
					} else if (i === 6) {
						bigCalendar += "<th class='sat'>";
					} else {
						bigCalendar += "<th>";
					}
					bigCalendar += dayOfWeek[i] + "</th>";
				}
				bigCalendar += "</tr></thead><tbody><tr>";
				for (let i = 1; i <= week; i++) {
					bigCalendar += "<td class='nan'>" + (previousMonthLastDay - week + i)
							+ "<p style='color:black'>일정이 없습니다.</p></td>";
				}
				let weekCount = week;
				for (let i = 1; i <= lastDay; i++, weekCount++) {
					if (Math.floor(weekCount % dayOfWeek.length) === 6) {
						bigCalendar += "<td class='sat ";
					} else if (Math.floor(weekCount % dayOfWeek.length) === 0) {
						weekCount = 0;
						bigCalendar += "<td class='sun ";
					} else {
						bigCalendar += "<td class='";
					}
					if (year === nowYear && month === nowMonth && i === nowDay) {
						bigCalendar += "now'>";
					} else {
						bigCalendar += "'>";
					}
					bigCalendar += i + "<p style='color:black'>일정이 없습니다.</p></td>";
					if (Math.floor(weekCount % dayOfWeek.length) === 6) {
						bigCalendar += "</tr><tr>";
					}
				}
				for (let i = 1; i <= dayOfWeek.length - weekCount; i++) {
					bigCalendar += "<td class='nan'>" + i + "<p style='color:black'>일정이 없습니다.</p></td>";
				}
				bigCalendar += "</tr>";

				let date;
				let month_;
				if (month === 1) {
					date = String(year - 1);
					month_ = "12";
				} else {
					date = String(year);
					month_ = String(month - 1);
				}
				if (month_.length <= 1) {
					date += "0";
				}
				date += month_;

				let firstDate = date;
				let day = String(previousMonthLastDay - week + 1);
				if (day.length <= 1) {
					firstDate += "0";
				}
				firstDate += day;

				date = String(year);
				month_ = String(month);
				if (month_.length <= 1) {
					date += "0";
				}
				date += month_;

				let lastDate = date;
				day = String(dayOfWeek.length - week);
				if (day.length <= 1) {
					lastDate += "0";
				}
				lastDate += day;

				let smallCalendar = "<table class='table table-hover' style='width:400px;height:300p'><thead><tr>";
				for (let i = 0; i < dayOfWeek.length; i++) {
					if (i === 0) {
						smallCalendar += "<th class='sun'>";
					} else if (i === 6) {
						smallCalendar += "<th class='sat'>";
					} else {
						smallCalendar += "<th>";
					}
					smallCalendar += dayOfWeek[i] + "</th>";
				}
				smallCalendar += "</tr></thead><tbody><tr onmouseover=mouseIn(" + firstDate + "," + lastDate
						+ ") onmouseout=mouseOut()>";
				for (let i = 1; i <= week; i++) {
					smallCalendar += "<td class='nan'>" + (previousMonthLastDay - week + i) + "</td>";
				}
				weekCount = week;
				for (let i = 1; i <= lastDay; i++, weekCount++) {
					if (Math.floor(weekCount % dayOfWeek.length) === 6) {
						smallCalendar += "<td class='sat ";
					} else if (Math.floor(weekCount % dayOfWeek.length) === 0) {
						weekCount = 0;
						smallCalendar += "<td class='sun ";
					} else {
						smallCalendar += "<td class='";
					}
					if (year === nowYear && month === nowMonth && i === nowDay) {
						smallCalendar += "now'>";
					} else {
						smallCalendar += "'>";
					}
					smallCalendar += i + "</td>";
					if (Math.floor(weekCount % dayOfWeek.length) === 6) {
						firstDate = date;
						day = String(i + 1);
						if (day.length <= 1) {
							firstDate += "0";
						}
						firstDate += day;

						if (i + dayOfWeek.length > lastDay) {
							if (month === 12) {
								date = String(year + 1);
								month_ = "1";
							} else {
								date = String(year);
								month_ = String(month + 1);
							}
							if (month_.length <= 1) {
								date += "0";
							}
							date += month_;

							lastDate = date;
							day = String((i + dayOfWeek.length) - lastDay);
						} else {
							lastDate = date;
							day = String(i + dayOfWeek.length);
						}
						if (day.length <= 1) {
							lastDate += "0";
						}
						lastDate += day;
						smallCalendar += "</tr><tr onmouseover=mouseIn(" + firstDate + "," + lastDate + ") onmouseout=mouseOut()>";
					}
				}
				for (let i = 1; i <= dayOfWeek.length - weekCount; i++) {
					smallCalendar += "<td class='nan'>" + i + "</td>";
				}
				smallCalendar += "</tr></tbody></table>";

				$("#date").text(year+"년 "+month+"월");
				$("#yOptions").html(yOptions);
				$("#mOptions").html(mOptions);
				$("#bigCalendar").html(bigCalendar);
				$("#smallCalendar").html(smallCalendar);
			}
		
		   	const formCalendar=()=>{
				year = parseInt($("#year").val());
				month = parseInt($("#month").val());
				reset();
		   	}
		   	
			const mouseIn=(firstDate, lastDate)=>{
				$("#calendar").addClass("open");
				if(firstDate != null && lastDate != null){
					let list = $("#calendar").find("li");
					list.each(function() {
						    $(this).text(firstDate + "~" + lastDate);
						});
				}
			}
			
			const mouseOut=()=>{
				$("#calendar").removeClass("open");
			}
			
			const monthMove=(num)=>{
				month += num;
				if(month === 0){
					month = 12;
					year -= 1;
				}else if(month === 13){
					month = 1;
					year += 1;
				}
				reset();
			}
		</script>
	</body>
</html>