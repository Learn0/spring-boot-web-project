<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>뮤직 Top 200</title>
		<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.min.js"></script>
		<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
		<style>
			.table > thead > tr > th:first-child {
				width: 100px;
			}
			.table > tbody > tr > td:first-child {
				width: 40px;
			}
			.table > tbody > tr > td:nth-child(2) {
				width: 60px;
			}
			.table > thead > tr > th:nth-child(2) {
				width: 80px;
			}
			.table > tbody > tr > td > img {
				min-width: 60px;
				width: 60px;
				height: 60px;
			}
			.table > tbody > tr {
				cursor: pointer;
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
						<input type="text" class="form-control" v-model="searchKeyword" v-on:keyup="searchMusic()" placeholder="노래를 검색하세요." />
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr class="success">
										<th colspan=2>랭킹</th>
										<th>앨범</th>
										<th>곡명</th>
										<th>가수명</th>
										<th>앨범</th>
									</tr>
								</thead>
								<tbody>
									<tr v-if="musicList.length > 0" v-for="musicDTO in musicList" v-on:click="viewMusic(musicDTO.youtubeKey)">
										<td class="rank">{{musicDTO.idx}}</td>
										<td v-if="musicDTO.state == 'new'"><font style="color:green">new</font></td>
										<td v-else-if="musicDTO.state == '유지'"><font style="color:gray">-</font></td>
										<td v-else-if="musicDTO.state == '상승'"><font style="color:blue">▲</font>{{musicDTO.increment}}</td>
										<td v-else-if="musicDTO.state == '하강'"><font style="color:red">▼</font>{{musicDTO.increment}}</td>
										<td><img :src="musicDTO.poster" onError="this.src='<c:url value="/images/pic08.jpg" />'" class="img-circle" /></td>
										<td class="title">{{musicDTO.title}}</td>
										<td class="singer">{{musicDTO.singer}}</td>
										<td>{{musicDTO.album}}</td>
									</tr>
									<tr v-else>
										<td colspan="100%">조회된 결과가 없습니다.</td>
									</tr>
								</tbody>
							</table>
						</div>
						<nav v-if="pageCount > 0" class="text-center">
							<ul class="pagination">
								<li v-if="previousCheck" v-on:click="movePage(1)">
									<a href="javascript:void(0)"><span>&laquo;</span></a>
								</li>
								<li v-if="previousCheck" v-on:click="movePage((firstPage - 1))">
									<a href="javascript:void(0)"><span>&lsaquo;</span></a>
								</li>
								<li v-for="pageNo in (lastPage-firstPage+1)" v-on:click="movePage(firstPage+pageNo-1)" :class="[((firstPage+pageNo-1) == page) ? 'active' : '' ]">
									<a href="javascript:void(0)">{{firstPage+pageNo-1}}</a>
								</li>
								<li v-if="nextCheck" v-on:click="movePage((lastPage + 1))">
									<a href="javascript:void(0)"><span>&rsaquo;</span></a>
								</li>
								<li v-if="nextCheck" v-on:click="movePage(pageCount)">
									<a href="javascript:void(0)"><span>&raquo;</span></a>
								</li>
							</ul>
						</nav>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			new Vue({
				el:"#main",
				data:{
					/*
					출력 : {{ }}
					정수 : 0
					실수 : 0.0
					문자 : ''
					배열 : []
					객체 : {}
					---------------
					v-model=""
					v-show=""
					v-hide=""
					v-if=""
					v-else=""
					v-for="i in test"
					v-bind:src="path"
					:src, :title, :class... (v-bind는 생략 가능)
					v-on:click="prev()"
					v-on:mouseover="prev()"
					v-on:change="prev()"
					*/
					musicList:{},
					searchKeyword:'',
					pageCount:0,
					previousCheck:false,
					nextCheck:false,
					firstPage:0,
					lastPage:0,
					page:1
				},
				beforeCreate:function(){
					console.log("이벤트 등록 , 인스턴스 초기화 전");
				},
				created:function(){
					console.log("인스턴스 메모리 할당");
				},
				beforeMount:function(){
					console.log("HTML이 가상 DOM에 저장하기 전 상태");
					this.searchMusic();
				},
				mounted:function(){
					console.log("메모리 저장 완료, window.onload(), $(function())");
				},
				beforeUpdate:function(){
					console.log("데이터 수정 전(HTML이 아직은 변경되지 않은 상태)");
				},
				updated:function(){
					console.log("변경된 데이터를 출력하는 상태");
				},
				beforeDestory:function(){
					console.log("다른 페이지로 이동 전(메모리 해제 전)");
				},
				destoryed:function(){
					console.log("메모리 해제");
				},
				methods:{
					// 사용자 정의
					searchMusic:function(){
						axios.get(contextRoot + "/music/search", {
							params:{
								searchKeyword:this.searchKeyword,
								page:this.page
							}
						})
						.then(response=>{
							this.musicList=JSON.parse(response.data[0]);
							this.pageCount = response.data[1].pageCount;
							this.previousCheck = response.data[1].previousCheck;
							this.nextCheck = response.data[1].nextCheck;
							this.firstPage = response.data[1].firstPage;
							this.lastPage = response.data[1].lastPage;
							this.page = response.data[1].page;
						})
						.catch((error)=>{
							if (error.response) {
								console.log(error.response.data);
								console.log(error.response.status);
								console.log(error.response.headers);
							} else if (error.request) {
								console.log(error.request);
							} else {
								console.log(error.message);
							}
							alert("에러가 발생하였습니다.");
							location.reload();
						});
					},				
					viewMusic:function(url){
						window.open(url);
					},				
					movePage:function(page){
						this.page = page;
						this.searchMusic();
					}
				}
			})
			/*
			javascript로 검색 <input onkeyup="filter() /> 
			const filter=()=>{
	            let search = $("#search").val().toLowerCase();
	            let tableList = $(".table > tbody > tr");
	            for (let i = 0; i < tableList.length; i++) {
	                let rank = tableList[i].getElementsByClassName("rank");
	                let title = tableList[i].getElementsByClassName("title");
	                let singer = tableList[i].getElementsByClassName("singer");
	                if (rank[0].innerHTML.includes(search) ||
	                		title[0].innerHTML.toLowerCase().includes(search) ||
	                		singer[0].innerHTML.toLowerCase().includes(search)
	                   ) {
	                    tableList[i].style.display = "";
	                } else {
	                    tableList[i].style.display = "none";
	                }
	            }
	        }
			*/
		</script>
	</body>
</html>