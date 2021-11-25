<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" errorPage="/error"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="bodyContent">
</div>
<!-- 
<div class="clearfix">
	<h2 style="color:white">상영 영화</h2>
	<c:forEach var="movieVO" items="${movieList}">
		<div class="col-xs-4" style="margin-bottom:20px">
			<div class="card" style="background:white; width:100%; cursor:pointer" onclick="location.href='<c:url value="/movie/main?view=${movieVO.idx}" />'">
				<div class="card-img">
					<img src="${movieVO.poster}" onError="this.src='<c:url value="/images/pic08.jpg" />'" style="width:100%; min-height:100%" />
				</div>
				<div>
					<h4 style="margin:0px"><b style="width: calc(100% - 50px); padding-left:20px; float:left"><span class="ellipsis" style="color:black; text-align:right">${movieVO.title}</span></b><span style="width:50px; float:right; text-align:center; color:orange">${movieVO.score}</span></h4>
					<p style="background-color:#ffe"><span class="ellipsis-3">${movieVO.story}</span></p>
				</div>
			</div>
		</div>
	</c:forEach>
</div>
<c:if test="${not empty searchDTO && searchDTO.pageInfo.pageCount > 0}">
	<nav class="text-center">
		<ul class="pagination">
			<c:if test="${searchDTO.pageInfo.previousCheck}">
				<li onclick="movePage(1)">
					<a href="#"><span>&laquo;</span></a>
				</li>
				<li onclick="movePage(${searchDTO.pageInfo.firstPage - 1})">
					<a href="#"><span>&lsaquo;</span></a>
				</li>
			</c:if>
			<c:forEach var="pageNo" begin="${searchDTO.pageInfo.firstPage}" end="${searchDTO.pageInfo.lastPage}">
				<li onclick="movePage('${pageNo}')" class="${(pageNo eq searchDTO.page) ? 'active' : '' }">
					<a href="#">${pageNo}</a>
				</li>
			</c:forEach>
			<c:if test="${searchDTO.pageInfo.nextCheck}">
				<li onclick="movePage(${searchDTO.pageInfo.lastPage + 1})">
					<a href="#"><span>&rsaquo;</span></a>
				</li>
				<li onclick="movePage(${searchDTO.pageInfo.pageCount})">
					<a href="#"><span>&raquo;</span></a>
				</li>
			</c:if>
		</ul>
	</nav>
</c:if>
 -->

<script src="https://unpkg.com/react@15/dist/react.js"></script>
<script src="https://unpkg.com/react-dom@15/dist/react-dom.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/babel-core/5.8.34/browser.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script type="text/babel">
	class App extends React.Component{
		constructor(props) {
			super(props)
			this.state={
  				movieList: [],
				searchDTO: {}
			}
        	this.handlerPage=this.handlerPage.bind(this)
   			this.movePage=this.movePage.bind(this)
		}
		componentWillMount(){ 
			console.log("HTML이 가상 DOM에 저장하기 전 상태")
			this.movePage()
		}
		componentDidMount() {
			console.log("메모리 저장 완료, window.onload(), $(function())")
		}
		render() {
			return (
				<div>
					<div className={"clearfix"}>
						<h2 style={{"color":"white"}}>상영 영화</h2>
						<List movieList={this.state.movieList} />
					</div>
					<Page searchDTO={this.state.searchDTO} onPageInput={this.handlerPage} />
				</div>
			)
		}
        handlerPage(page){
			this.state.searchDTO.page = page;
			this.movePage()
       	}
		movePage(){
			axios.get(contextRoot + "/movie/search", {
				params:{
					page:this.state.searchDTO.page
				}
			})
			.then(response=>{
				this.setState({movieList: JSON.parse(response.data[0])})
				this.setState({searchDTO: response.data[1]})
			})
			.catch((error)=>{
				if (error.response) {
					console.log(error.response.data)
					console.log(error.response.status)
					console.log(error.response.headers)
				} else if (error.request) {
					console.log(error.request)
				} else {
					console.log(error.message)
				}
				alert("에러가 발생하였습니다.")
				location.reload()
			})
		}
 	}
	class List extends React.Component{
		constructor(props) {
			super(props)
			this.viewMovie=this.viewMovie.bind(this)
   		}
		render() {
			let html=this.props.movieList.map((movieVO)=>
				<div key={movieVO.idx} className={"col-xs-4"} style={{"marginBottom":"20px"}}>
					<div className={"card"} style={{"background":"white", "width":"100%", "cursor":"pointer"}} onClick={()=>{this.viewMovie('<c:url value="/movie/main?view=" />'+movieVO.idx)}}>
						<div className={"card-img"}>
							<img src={movieVO.poster} onError={(e)=>{e.target.src='<c:url value="/images/pic08.jpg" />'}} style={{"width":"100%", "minHeight":"100%"}} />
						</div>
						<div>
							<h4 style={{"margin":"0px"}}>
								<b style={{"width":"calc(100% - 50px)", "paddingLeft":"20px","float":"left"}}>
									<span className={"ellipsis"} style={{"color":"black", "textAlign":"right"}}>{movieVO.title}</span>
								</b>
								<span style={{"width":"50px", "float":"right", "textAlign":"center", "color":"orange"}}>{movieVO.score}</span>
							</h4>
							<p style={{"backgroundColor":"#ffe"}}><span className={"ellipsis-3"}>{movieVO.story}</span></p>
						</div>
					</div>
				</div>
			)
			return (
				<div>
					{html}
				</div>
			)
		}
        viewMovie(url){
			location.href=contextRoot+url
       	}
 	}
	class Page extends React.Component{
       	constructor(props)
       	{
         	super(props)
           	this.changePage=this.changePage.bind(this)
        }
		render() {
   			let html=[]
			if(this.props.searchDTO.previousCheck){
				html.push(
					<li key={1} onClick={()=>{this.changePage(1)}}>
						<a href="#none"><span>&laquo;</span></a>
					</li>
				)
				html.push(
					<li key={this.props.searchDTO.firstPage-1} onClick={()=>{this.changePage(this.props.searchDTO.firstPage-1)}}>
						<a href="#none"><span>&lsaquo;</span></a>
					</li>
				)
			}
    		for (let i = this.props.searchDTO.firstPage; i <= this.props.searchDTO.lastPage; i++) {
				html.push(
					<li key={i} onClick={()=>{this.changePage(i)}} className={ ((i == this.props.searchDTO.page) ? 'active' : '')}>
						<a href="#none">{i}</a>
					</li>
				)
    		}
			if(this.props.searchDTO.nextCheck){
				html.push(
					<li key={this.props.searchDTO.lastPage+1} onClick={()=>{this.changePage(this.props.searchDTO.lastPage+1)}}>
						<a href="#none"><span>&rsaquo;</span></a>
					</li>
				)
				html.push(
					<li key={this.props.searchDTO.pageCount} onClick={()=>{this.changePage(this.props.searchDTO.pageCount)}}>
						<a href="#none"><span>&raquo;</span></a>
					</li>
				)
			}
			return (
				<nav className={"text-center"}>
					<ul className={"pagination"}>
						{html}
					</ul>
				</nav>
			)
		}
        changePage(page)
       	{
       		this.props.onPageInput(page)
        }
 	}
	ReactDOM.render(<App/>,document.querySelector('#bodyContent'))
</script>