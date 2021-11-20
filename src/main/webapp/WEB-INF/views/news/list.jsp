<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<!-- %@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"% -->
<!DOCTYPE html>
<html>
	<head>
		<title>뉴스</title>
		<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.min.js"></script>
		<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
		<style>
			.table.pointer {
				background: inherit;
				margin-bottom: 0px;
				width: 100%;
				cursor: pointer;
			}
			.table.pointer tr > th {
				font-size: 25px;
			}
			.table.pointer tr,
			.table.pointer tr > th, 
			.table.pointer tr > td {
			    border: solid 1px #fff;
				padding: 0 5px 0;
			}
			.table.pointer tr > td > span {
				font-size: 23px;
			}
			.table.pointer tr:last-child > td {
				font-size: 18px;
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
						<div class="input-group">
							<input type="text" id="searchKeyword" class="form-control" v-model="searchKeyword" v-on:keyup.enter="searchNews()" placeholder="키워드를 입력해 주세요." />
							<div class="input-group-btn">
								<div class="btn-group">
									<input type="button" class="btn btn-success" v-on:click="searchNews()" value="검색" />
								</div>
							</div>
						</div>
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr class="success">
										<th>{{searchKeywordSave}}</th>
									</tr>
								</thead>
								<tbody>
									<tr v-if="!newsList">
										<td>조회된 결과가 없습니다.</td>
									</tr>
									<tr v-else v-for="item in newsList">
										<td>
											<table class="table pointer" v-on:click="viewNews(item.link)">
												<thead>
													<tr>
														<th style="text-align:left" colspan="3">{{item.title}}</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td colspan="3">
											          		<span style="text-align:left; width:100%; overflow:hidden; text-overflow:ellipsis; white-space:normal; line-height:1.2; height:2.4em; word-wrap:break-word; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical">
											          			{{item.description}}
											          		</span>
														</td>
													</tr>
													<tr>
														<!-- 
														JSTL
														<fmt:setLocale value="en_US" scope="session"/>
														<fmt:parseDate value="${item.pubDate}" var="dateFmt" pattern="E, dd MMM yyyy HH:mm:ss Z" />
														<td style="width:60%"><fmt:formatDate value="${dateFmt}" pattern="yy-MM-dd HH:mm" /></td>
														 -->
														<td style="width:40%">{{dateFormatReturn(item.pubDate)}}</td>
														<td style="width:35%">{{item.author}}</td>
														<td style="width:25%">{{item.category}}</td>
													</tr>
												</tbody>
											</table>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			new Vue({
				el:"#main",
				data:{
					searchKeyword:'코로나',
					searchKeywordSave:'',
					newsList:[]
				},
				beforeMount:function(){
					console.log("HTML이 가상 DOM에 저장하기 전 상태");
					this.searchNews();
				},
				methods:{
					// 사용자 정의
					searchNews:function(){
						axios.get(contextRoot + "/news/search", {
							params:{
								searchKeyword:this.searchKeyword
							}
						})
						.then(response=>{
							this.searchKeywordSave = this.searchKeyword;
							this.newsList=response.data;
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
					viewNews:function(url){
						window.open(url);
					},
					dateFormatReturn:function(value){
						let date = new Date(value);
						return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
					}
				}
			})
		</script>
	</body>
</html>