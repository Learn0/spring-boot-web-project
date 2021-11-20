<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<title>게임 리스트</title>
		<script src="https://unpkg.com/react@15/dist/react.js"></script>
		<script src="https://unpkg.com/react-dom@15/dist/react-dom.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/babel-core/5.8.34/browser.js"></script>
		<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
		<style>
			.table > thead > tr > th:first-child {
				min-width: 50px;
			}
			.table > tbody > tr > td:nth-child(2) {
				min-width: 50px;
			}
			.table > tbody > tr > td:last-child {
				max-width: 200px;
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
						<div id="bodyContent"></div>
						<!-- 
						<table class="table table-hover">
							<thead>
								<tr class="info">
									<th>번호</th>
									<th>게임 이름</th>
									<th>게임 설명</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(gameList) > 0}">
										<c:forEach var="gameDTO" items="${gameList}">
											<tr onclick="window.open('<c:url value="${gameDTO.link}" />')">
												<td>${gameDTO.idx}</td>
												<td>${gameDTO.title}</td>
												<td><span class="ellipsis">${gameDTO.content}</span></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="100%">조회된 결과가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						 -->
					</div>
				</div>
			</div>
		</div>

		<script type="text/babel">
			// 함수형
			/*
			function App()
    		{
        		return (
          			<h1>Hello React Function으로 처리</h1>
        		)
    		}*/
			// 메소드형
    		class App extends React.Component{
    			constructor(props) {
      				super(props)
      				this.state={
						/*
						출력 : {}
						정수 : 0
						실수 : 0.0
						문자 : ''
						배열 : []
						객체 : {}
						*/
          				gameList: []
     				}
           			this.viewGame=this.viewGame.bind(this)
    			}
        		componentWillMount(){ 
					console.log("HTML이 가상 DOM에 저장하기 전 상태")
					axios.get(contextRoot + "/game/search")
					.then(response=>{
						this.setState({gameList: response.data})
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
    			componentDidMount() {
					console.log("메모리 저장 완료, window.onload(), $(function())")
    			}
   				render() {
        			let html
					if(this.state.gameList && this.state.gameList.length > 0){
						html=this.state.gameList.map((gameDTO)=>
            				<tr key={gameDTO.idx} onClick={()=>{this.viewGame(gameDTO.link)}}>
								<td>{gameDTO.idx}</td>
								<td>{gameDTO.title}</td>
								<td><span className={"ellipsis"}>{gameDTO.content}</span></td>
							</tr>
        				)
					}else{
						html=(<tr>
								<td colSpan="100%">조회된 결과가 없습니다.</td>
							</tr>)
					}
      				return (
						<table className={"table table-hover"}>
							<thead>
								<tr className={"info"}>
									<th>번호</th>
									<th>게임 이름</th>
									<th>게임 설명</th>
								</tr>
							</thead>
							<tbody>
								{html}
							</tbody>
						</table>
      				)
   				}
        		viewGame(url){
					window.open(contextRoot+url)
        		}
   		 	}
    		ReactDOM.render(<App/>,document.querySelector('#bodyContent'))
		</script>
	</body>
</html>