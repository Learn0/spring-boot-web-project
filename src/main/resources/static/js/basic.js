$(function(){
	sideNavTop();
});
			
$(window).resize(function() {
	sideNavTop();
});

$(window).scroll(function(){
	sideNavTop();
});

const sideNavTop=()=>{
	let scrollTop = (-$(window).scrollTop());
	scrollTop += $("#main").offset().top;
	let height = $("#header").height();
	if(scrollTop < height){	
		scrollTop = height;
	}
	$(".sidenav").css("margin-top", scrollTop);
}

let nav = false;
const changeNav=()=>{
	if(nav){
		$("#mySidenav").css("width","0px");
		$("#main").not(".not").css("margin-right","auto");
	}else{
		$("#mySidenav").css("width","250px");
		$("#main").not(".not").css("margin-right","250px");
	}
	nav = !nav;
}
			
const noSpaceForm=(obj)=>{
	const str_space = /\s/;
    if(str_space.exec(obj.value)) 
    {
        obj.focus();
        obj.value = obj.value.replace(" ","");
        
        return false;
    }
}

const noKoreanForm=(obj)=>{
	const str_korean = /[ㄱ-ㅎㅏ-ㅣ가-힣\s]/g;
    if(str_korean.exec(obj.value))
    {
        obj.focus();
        obj.value = obj.value.replace(str_korean,"");
        
        return false;
    }
}

const numberForm=(obj)=>{
	const str_number = /[^0-9]/g;
    if(str_number.exec(obj.value))
    {
        obj.focus();
        obj.value = obj.value.replace(str_number,"");  

        return false;
    }
	if (obj.value.length == obj.maxLength) {
		let nextName = "." + $(obj).attr("name");
		let next = $(obj).nextAll(nextName);
		if(next.length > 0){
			$(obj).nextAll(nextName)[0].focus();
		}
	}
}

const phoneNumberCheck=function(phoneNumber){
	return phoneNumber.replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})/,"$1-$2-$3").replace("--", "-");
}
			
const emailCheck=function(email) {
	var reg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	return reg.test(email);
}

const imageValidateType=function(img) {
  	return (img.type.indexOf("image/") === 0);
}

const getTimeStamp=function(date) {
	let data = new Date(date);
	let time =
		leadingZeros(data.getFullYear().toString().substring(2,4), 2) + "." +
		leadingZeros(data.getMonth() + 1, 2) + "." +
		leadingZeros(data.getDate(), 2) + " " +
		
		leadingZeros(data.getHours(), 2) + ":" +
		leadingZeros(data.getMinutes(), 2);
		//+ ":" + leadingZeros(data.getSeconds(), 2);
	
	return time;
}

const leadingZeros=function(n, digits) {
	let zero = "";
	n = n.toString();

	if (n.length < digits) {
		for (i = 0; i < digits - n.length; i++)
			zero += "0";
	}
	return zero + n;
}