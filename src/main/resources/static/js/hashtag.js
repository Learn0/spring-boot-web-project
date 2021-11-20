$(function(){
	if(typeof hashtagList !== 'undefined'){
		let source = $.map(hashtagList, function(item) {
			chosung = "";
			//Hangul.d(item, true) 을 하게 되면 item이 분해가 되어서 
			//["ㄱ", "ㅣ", "ㅁ"],["ㅊ", "ㅣ"],[" "],["ㅂ", "ㅗ", "ㄲ"],["ㅇ", "ㅡ", "ㅁ"],["ㅂ", "ㅏ", "ㅂ"]
			//으로 나오는데 이중 0번째 인덱스만 가지고 오면 초성이다.
			
			full = Hangul.disassemble(item).join("").replace(/ /gi, "");
			//공백제거된 ㄱㅣㅁㅊㅣㅂㅗㄲㅇㅡㅁㅂㅏㅂ
			Hangul.d(item, true).forEach(function(strItem, index) {
				
				if(strItem[0] != " "){	//띄어 쓰기가 아니면
					chosung += strItem[0];//초성 추가
				}
			});
			
			return {
				label : chosung + "|" + (item).replace(/ /gi, "") +"|" + full, //실제 검색어랑 비교 대상 ㄱㅊㅂㅇㅂ|김치볶음밥|ㄱㅣㅁㅊㅣㅂㅗㄲㅇㅡㅁㅂㅏㅂ 이 저장된다.
				value : item, //김치 볶음밥
				chosung : chosung,	//ㄱㅊㅂㅇㅂ,
				full : full
			}
		});
		
	    $("#searchKeyword").autocomplete({ // autocomplete 구현 시작부
	        //source : source, //source 는 자동완성의 대상
	    	source : function(request, response){
	      		response(
					$.grep(source, function(v){
						if(request.term.indexOf("#") === 0){
							if(v.label.includes(request.term)){
								return true;
							}
						}
					})
				);
	        },
	        select : function(event, ui){
	            if(document.selection) { 
	                this.focus(); 
	                let oSel = document.selection.createRange(); 
	                oSel.moveStart("character",this.value.length); 
	                oSel.moveEnd("character",0); 
	                oSel.select(); 
	            } 
	        },
	        focus : function(event, ui) { // 포커스 시 이벤트
	            return false;
	        },
	        minLength : 2, // 최소 글자 수
	        autoFocus : true, // true로 설정 시 메뉴가 표시 될 때, 첫 번째 항목에 자동으로 초점이 맞춰짐
	        delay : 500, // 입력창에 글자가 써지고 나서 autocomplete 이벤트 발생될 떄 까지 지연 시간(ms)
			close : function(event) { // 자동완성 창 닫아질 때의 이벤트
	            console.log(event);
	        }
	    }).autocomplete( "instance" )._renderItem=function( ul, item ) {
	    	return $( "<li>" ).append( "<div>" + item.value + "</div>" ).appendTo( ul );
	    }
	}
});