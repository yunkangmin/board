<!-- 댓글 페이징, 등록, 수정, 삭제 테스트를 위한 페이지.  -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<style>
#modDiv {
	width: 300px;
	height: 100px;
	background-color: gray;
	position: absolute;
	top: 50%;
	left: 50%;
	margin-top: -50px;
	margin-left: -150px;
	padding: 10px;
	z-index: 1000;
}

.pagination {
  width: 100%;
}

.pagination li{
  list-style: none;
  float: left; 
  padding: 3px; 
  border: 1px solid blue;
  margin:3px;  
}

.pagination li a{
  margin: 3px;
  text-decoration: none;  
}
</style>
</head>
<body>
<div id='modDiv' style="display: none;">
	<div class='modal-title'></div>
	<div>
		<input type='text' id='replytext'>
	</div>
	<div>
		<button type="button" id="replyModBtn">Modify</button>
		<button type="button" id="replyDelBtn">DELETE</button>
		<button type="button" id='closeBtn'>Close</button>
	</div>
</div>
<!-- 댓글 등록을 위한 form이다. -->
<h2>Ajax Test Page</h2>
<div>
	<div>
		REPLYER <input type='text' name='replyer' id='newReplyWriter'>
	</div>
	<div>
		REPLY TEXT <input type='text' name='replytext' id='newReplyText'>
	</div>
	<button id="replyAddBtn">ADD REPLY</button>
</div>
<!-- 댓글 목록이 출력될 부분이다. -->
<ul id="replies"></ul>
<!-- 댓글 페이지 번호 부분이 출력된 부분이다. -->
<ul class='pagination'></ul>	
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<script>
	//특정 게시물 번호를 지정하여 테스트를 진행한다.
	var bno = 6127;
	//맨 처음 페이지가 호출될 때 페이저 번호 1인 댓글 목록을 요청한다.
	getPageList(1);
	
	//페이지 처리가 되지 않은 모든 댓글 목록을 가져온다.
	function getAllList() {
		$.getJSON("/replies/all/" + bno, function(data) {
			var str = "";
			$(data).each(function() {
				//data-로 시작하는 속성은 이름이나 개수에 상관없이 자유롭게 생성이 가능하다.
				//id나 name 속성 대신 사용하기 편리하다.
				str += "<li data-rno='"+this.rno+"' class='replyLi'>"
						+ this.rno
						+ ":"
						+ "<span>"
						+ this.replytext
						+ "</span>"
						+ "<button>MOD</button></li>";
			});

			$("#replies").html(str);
		});
	}

	//댓글 등록시 작동한다.
	$("#replyAddBtn").on("click", function() {
		//작성자와 댓글 내용이다.
		var replyer = $("#newReplyWriter").val();
		var replytext = $("#newReplyText").val();

		$.ajax({
			type : 'post',
			url : '/replies',
			headers : {
				"Content-Type" : "application/json",
				"X-HTTP-Method-Override" : "POST"
			},
			dataType : 'text', //서버에서 반환될 데이터 형식을 지정한다.
			data : JSON.stringify({
				bno : bno,
				replyer : replyer,
				replytext : replytext
			}),
			success : function(result) {
				if (result == 'SUCCESS') {
					alert("등록 되었습니다.");
					getPageList(1);
				}
			}
		});
	});

	//댓글 수정버튼 클릭 시 작동한다.
	//댓글의 각 항목을 의미하는 <li>의 경우 Ajax 통신 이후에 생기는 요소들이기 때문에
	//이벤트 처리를 할 때 기존에 존재하는 <ul>을 이용해서 이벤트를 등록한다.
	//위임 방식으로 이벤트를 전달한다.
	//제이쿼리는 아래와 같이 아직 존재하지 않는 요소에 이벤트를 위임해줄 수 있다.
	$("#replies").on("click", ".replyLi button", function() {

		var reply = $(this).parent();

		var rno = reply.attr("data-rno");
		var replytext = $(reply).children('span').text();

		$(".modal-title").html(rno);
		$("#replytext").val(replytext);
		$("#modDiv").show("slow");

	});

	//삭제 버튼 클릭 시 작동한다.
	$("#replyDelBtn").on("click", function() {
		var rno = $(".modal-title").html();

		$.ajax({
			type : 'delete',
			url : '/replies/' + rno,
			headers : {
				"Content-Type" : "application/json",
				"X-HTTP-Method-Override" : "DELETE"
			},
			dataType : 'text',
			success : function(result) {
				console.log("result: " + result);
				if (result == 'SUCCESS') {
					alert("삭제 되었습니다.");
					$("#modDiv").hide("slow");
					getPageList(replyPage);
				}
			}
		});
	});
	
	//수정버튼 클릭 시 작동한다.
	$("#replyModBtn").on("click",function(){
		  
		  var rno = $(".modal-title").html();
		  var replytext = $("#replytext").val();
		  
		  $.ajax({
				type:'put',
				url:'/replies/'+rno,
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "PUT" },
				data:JSON.stringify({replytext:replytext}), 
				dataType:'text', 
				success:function(result){
					if(result == 'SUCCESS'){
						alert("수정 되었습니다.");
						 $("#modDiv").hide("slow");
					     //replyPage는 사용자가 누른 페이지 번호이다.
					     //페이지 번호를 누르면 페이지 번호가 저장된다.
						 getPageList(replyPage);
					}
			}});
	});		
	
	//요청한 페이지 번호에 해당하는 댓글 목록을 보여주는 작업을 한다.
	function getPageList(page){
	  //결과가 json이다.
	  $.getJSON("/replies/"+bno+"/"+page , function(data){
		  var str ="";
		  //결과값인 data에서 댓글목록이 들어있는 list를 전체 순회하며
		  //str변수에 html로 만든 하나씩 댓글을 넣는다.
		  $(data.list).each(function(){
			  str+= "<li data-rno='"+this.rno+"' class='replyLi'>" 
			  +this.rno+":"+ "<span>" + this.replytext + "</span>" +
			  "<button>MOD</button></li>";
		  });
		  
		  //댓글목록을 화면에 뿌려준다.
		  $("#replies").html(str);
		  //결과값인 data에 있는 pageMaker를 이용해서 
		  //화면하단 댓글 페이징 처리를 한다.
		  printPaging(data.pageMaker);
	  });
  }		
	
	//화면하단 페이징 처리를 한다.
	function printPaging(pageMaker){
		var str = "";
		
		//이전 버튼 유무를 결정한다.
		if(pageMaker.prev){
			str += "<li><a href='"+(pageMaker.startPage-1)+"'> << </a></li>";
		}
		
		//페이지 번호를 생성한다.
		for(var i=pageMaker.startPage, len = pageMaker.endPage; i <= len; i++){				
				var strClass= pageMaker.cri.page == i?'class=active':'';
			  str += "<li "+strClass+"><a href='"+i+"'>"+i+"</a></li>";
		}
		
		//다음 버튼 유무를 결정한다.
		if(pageMaker.next){
			str += "<li><a href='"+(pageMaker.endPage + 1)+"'> >> </a></li>";
		}
		
		//최종적으로 화면에 완성된 페이지 번호를 뿌려준다.
		$('.pagination').html(str);				
	}
	
	//사용자가 누른 페이지 번호가 설정될 변수이다.
	var replyPage = 1;
	//위임방식으로 이벤트를 처리했다.
	$(".pagination").on("click", "li a", function(event){
		//a링크의 이동을 막는다.
		event.preventDefault();
		//사용자가 누른 페이지 번호가 설정된다.
		replyPage = $(this).attr("href");
		//댓글 페이징 번호에 맞는 댓글 목록을 요청한다.
		getPageList(replyPage);
	});
	
	//댓글 수정 버튼 클릭 시 뜨는 창에서 close버튼 클릭 시 작동한다.
	$("#closeBtn").on("click",function(){
		$("#modDiv").hide("slow");
	});
</script>
</body>
</html>

