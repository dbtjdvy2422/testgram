let index = {
	init: function() {
		$("#btn_save").on("click",()=>{
			this.save();
		});
	/*	$("#btn_login").on("click",()=>{
			this.login();
		});*/
		
		$("#btn_update").on("click",()=>{
			this.update();
		});
	},

	save:function(){
		let data ={
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		
		//alert(JSON.stringify(data));
		//ajax를 이용해서 3개의 데이터를 josn으로 변경하여 insert요청
		$.ajax({
			type:"POST",
			url:"/auth/joinProc",
			data: JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",//body타입이 어떤 타입인지(mime)
			dataType:"json"//요청을 서버로해서 응답이 왔을때 기본적으로 몬든것이 문자영(생긴게 josn이라면) => 자바스크립 오브젝트변경해줌
			//회원가입 수행 요청
		}).done(function(resp){
			alert("회원가입이 완료되었씁니다");
			location.href="/";
			console.log(resp);
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
	update:function(){
		let data ={
			id:$("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		
		//alert(JSON.stringify(data));
		//ajax를 이용해서 3개의 데이터를 josn으로 변경하여 insert요청
		$.ajax({
			type:"PUT",
			url:"/user",
			data: JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",//body타입이 어떤 타입인지(mime)
			dataType:"json"//요청을 서버로해서 응답이 왔을때 기본적으로 몬든것이 문자영(생긴게 josn이라면) => 자바스크립 오브젝트변경해줌
			//회원가입 수행 요청
		}).done(function(resp){
			alert("회원수정이 완료되었습니다.");
			location.href="/";
			console.log(resp);
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
/*	login:function(){
		let data ={
			username: $("#username").val(),
			password: $("#password").val()
		}
		
		
		//alert(JSON.stringify(data));
		//ajax를 이용해서 3개의 데이터를 josn으로 변경하여 insert요청
		$.ajax({
			type:"POST",
			url:"/api/user/login",
			data: JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",//body타입이 어떤 타입인지(mime)
			dataType:"json"//요청을 서버로해서 응답이 왔을때 기본적으로 몬든것이 문자영(생긴게 josn이라면) => 자바스크립 오브젝트변경해줌
			//회원가입 수행 요청
		}).done(function(resp){
			alert("로그인이 완료되었씁니다");
			location.href="/";
			console.log(resp);
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	}
	*/
	
	
}

index.init();


