<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<div id="movieDrop" class="dropdown" style="position:absolute">
	<div class="dropdown-menu dropdown-menu-center">
      	<table class="table" style="width:600px; margin-bottom:0px">
	      	<tbody>
        		<tr>
	          		<td rowspan="5" style="width:30%; height:100%; padding:10px; vertical-align:top">
	           			<img src="" style="width:100%; height:240px" id="movie_img" />
	          		</td>
	          		<td colspan="2" style="max-width:420px">
	          			<span class="ellipsis" style="font-size:30px" id="movie_name"></span>
	          		</td>
        		</tr>
	        	<tr>
	          		<th style="width:20%">감독명</th>
		          	<td style="width:50%" id="movie_director"></td>
		        </tr>
		        <tr>
		          	<th style="width:20%">장르</th>
		          	<td style="width:50%" id="movie_genre"></td>
		        </tr>
		        <tr>
		          	<th style="width:20%">등급</th>
		          	<td style="width:50%" id="movie_grade"></td>
		        </tr>
	        	<tr>
		          	<th style="width:20%">상영일</th>
		          	<td style="width:50%" id="movie_regdate"></td>
		        </tr>
		        <tr>
		          	<td colspan="3">
		          		<span class="ellipsis-3" id="movie_story">
		          		</span>
		          	</td>
	        	</tr>
        	</tbody>
      	</table>
	</div>
</div>
<div class="table-responsive">
	<table class="table table-hover" id="listTable">
	</table>
</div>

<script type="text/javascript">
	let json = ${json};
	let idx = ${idx};
	$(function(){
	    let index = 0;
		let data = `
		<thead>
			<tr style="background-color:#CFFCFF">`;
    	if(idx === 2 || idx === 4){
			data += "<th style='width:60px'>랭킹</th>";
		}else{
			data += "<th style='width:80px' colspan=2>랭킹</th>";
		}
		data += `
			<th>제목</th>
			<th style="min-width:100px">장르</th>`;
    	if(idx === 5){
			data += `
				<th>좌석수</th>
				<th>좌석점유율</th>`;
		}else if(idx != 6){
			data += "<th>일별 관객수</th>";
			if(idx === 2 || idx === 4){
				data += "<th>예매율</th>";
			}
		}
		data += `
			</tr>
		</thead>
		<tbody>`;
		json.map(function(vo){
	    	data += "<tr onmousemove=mouseIn("+index+") onmouseout=mouseOut() onclick=\"window.open('"+vo.link+"')\">";
	    	data += "<td>"+vo.rank+"</td>"
			if(idx != 2 && idx != 4){
		    	let s = "";
				let rank = Number(vo.rankInten);
				if(vo.rankOldAndNew === "NEW"){
					s = "<font style='color:gray'>new</font>";
				}else if(rank === 0){
					s = "<font style='color:gray'>-</font>";
				}else if(rank > 0){
					s = "<font style='color:blue'>▲</font>"+rank;
				}else if(rank < 0){
					s = "<font style='color:red'>▼</font>"+rank;
				}
		    	data += "<td style='width:60px'>"+s+"</td>";
			}
	    	data += `<td style="max-width:400px"><span class="ellipsis">`+vo.movieNm+`(`+vo.movieNmEn+`)</span></td>
	    		<td>`+vo.genre+`</td>`;
	    	if(idx === 5){
		    	data += `<td>`+vo.totSeatCnt+`</td>
					<td>`+vo.totSeatCntRatio+`%</td>`;
			}else if(idx != 6){
		    	data += "<td>"+vo.audiCnt+"</td>";
				if(idx === 2 || idx === 4){
			    	data += "<td>"+vo.totIssuCntRatio+"%</td>";
				}
			}
	    	data += "</tr>"
	    	index++;
	    });
    	data += "</tbody>"
		$("#listTable").html(data);
	});
	
	const mouseIn=(index)=>{
		$("#movieDrop").addClass("open");
		let vo = json[index];
		if(vo.thumbUrl != null){
			$("#movie_img").attr("src", "https://www.kobis.or.kr"+vo.thumbUrl);
		}else{
			$("#movie_img").attr("src", "");
		}
		$("#movie_name").html(vo.movieNm+"("+vo.movieNmEn+")");
		$("#movie_genre").html(vo.genre);
	    $("#movie_grade").html(vo.watchGradeNm);
	    $("#movie_nation").html(vo.repNationCd);
	    $("#movie_loc").html(vo.dtNm);
	    $("#movie_regdate").html(vo.openDt);
		$("#movie_director").html(vo.director);
		$("#movie_story").html(vo.synop);

		var windowX = $(window).width();
		var windowY = $(window).height();
		
		let mouseX = event.pageX - 30;
		if(windowX > 1200){
			mouseX -= (windowX-1200)*0.5;
		}
		mouseX += "px";
		
		let mouseY;
		if(windowX <= 767){
			if(windowY - event.clientY >= 400){
				mouseY = event.pageY - 150 + "px";
			}else{
				mouseY = event.pageY - 550 + "px";
			}
		}else{
			if(windowY - event.clientY >= 400){
				mouseY = event.pageY - 100 + "px";
			}else{
				mouseY = event.pageY - 500 + "px";
			}
		}
		$("#movieDrop").css("left", mouseX);
		$("#movieDrop").css("top", mouseY);
	}
	
	const mouseOut=()=>{
		$("#movieDrop").removeClass("open");
	}
</script>