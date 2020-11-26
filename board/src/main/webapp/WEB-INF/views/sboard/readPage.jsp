<!-- 
	검색기능이 추가된 조회페이지.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<title>게시글 읽기</title>
</head>
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<!-- handlebars를 사용하기 위해서는 아래 라이브러리가 필요하다. -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
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
<body>
<form role="form" method="post">
	<!-- 삭제나 수정작업을 위해 게시물번호를 가지고 있는다.  -->
	<input type='hidden' name='bno' value="${boardVO.bno}">
	<!-- 조회 후, 수정, 삭제 화면에서 다시 목록으로 돌아갈 때도 페이징 번호를 유지하기 위해 페이지 번호와 페이지 당 보여줄 게시글 개수를 설정한다. -->
	<input type='hidden' name='page' value ="${cri.page}">
    <input type='hidden' name='perPageNum' value ="${cri.perPageNum}">
    <!-- 검색 파라미터가 추가되었다. -->
   	<input type='hidden' name='searchType' value="${cri.searchType}">
	<input type='hidden' name='keyword' value="${cri.keyword}">
</form>
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
<table border="1">
	<tr>
		<td>제목</td>
		<td><input type="text"name='title' value="${boardVO.title}" readonly="readonly"></td>
	</tr>
	<tr>
		<td>내용</td>
		<td><textarea name="content" rows="3" readonly="readonly">${boardVO.content}</textarea></td>
	</tr>
	<tr>
		<td>작성자</td>
		<td><input type="text" name="writer" value="${boardVO.writer}" readonly="readonly"></td>
	</tr>
	<tr>
		<td colspan="2">
			<button type="submit" class="btn-warning">Modify</button>
			<button type="submit" class="btn-danger">REMOVE</button>
			<button type="submit" class="btn-primary">LIST ALL</button>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<button type="button" id="repliesDiv">Replies List [ ${boardVO.replycnt} ]</button>
		</td>
	</tr>
    
</table>
<!-- 댓글 등록을 위한 form이다. -->
<div class="reply" style="display:none;">
<h4>댓글 등록하기</h4>
	<div>
		REPLYER <input type='text' name='replyer' id='newReplyWriter'>
	</div>
	<div>
		REPLY TEXT <input type='text' name='replytext' id='newReplyText'>
	</div>
	<button id="replyAddBtn">ADD REPLY</button>
</div>
<!-- 댓글 목록이 출력될 부분이다. -->
<ul id="replies" class="reply" style="display:none;"></ul>
<!-- 댓글 페이지 번호 부분이 출력된 부분이다. -->
<ul class='pagination reply' style="display:none;"></ul>
<!-- 
handlebars 템플릿 코드이다. 
하단에 Modify 버튼을 누르면 a태그 속성에 data-target="#modifyModal"이 모달창을 호출함.
모달창에 id는 modifyModal임.
로그인한 사용자만 수정버튼이 보이게 처리.
-->
<script id="template" type="text/x-handlebars-template">
{{#each .}}
	<li data-rno="{{rno}}" class="replyLi">
		<font size="2em" color="blue">{{rno}}</font>:
		<span>{{replytext}}</span>
		<font size="2em" color="gray">-{{prettifyDate regdate}}</font>
		<button>MOD</button>
		작성자:{{replyer}}
	</li>
{{/each}}
</script>
<script>

//날짜를 원하는 형식으로 만들어준다.
//registerHelper로 원하는 기능을 만든다.
Handlebars.registerHelper("prettifyDate", function(timeValue) {
	var dateObj = new Date(timeValue);
	var year = dateObj.getFullYear();
	var month = dateObj.getMonth() + 1;
	var date = dateObj.getDate();
	return year + "/" + month + "/" + date;
});

//댓글 출력
var printData = function(replyArr, target, templateObject) {
    //댓글 템플릿을 가져온다.
	var template = Handlebars.compile(templateObject.html());
	//템플릿에 데이터를 세팅한다.
	var html = template(replyArr);
	//기존 댓글들을 지운다. replyLi 클래스 속성을 가진 댓글 모두 다.
	$(".replyLi").remove();
	//새로 세팅한다.
	target.html(html);
}


//특정 게시물 번호를 지정하여 테스트를 진행한다.
var bno = ${boardVO.bno};

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
	  printData(data.list, $("#replies"), $("#template"));
	  
	  //var str ="";
	  //결과값인 data에서 댓글목록이 들어있는 list를 전체 순회하며
	  //str변수에 html로 만든 하나씩 댓글을 넣는다.
	 // $(data.list).each(function(){
		//  str+= "<li data-rno='"+this.rno+"' class='replyLi'>" 
		//  +this.rno+":"+ "<span>" + this.replytext + "</span>" +
		//  "<button>MOD</button></li>";
	 // });
	  
	  //댓글목록을 화면에 뿌려준다.
	  //$("#replies").html(str);
	  //결과값인 data에 있는 pageMaker를 이용해서 
	  //화면하단 댓글 페이징 처리를 한다.
	  printPaging(data.pageMaker);
	  
	  //댓글 추가나 삭제시 목록을 갱신할 때 댓글 숫자도 같이 갱신시켜준다.
	  $("#repliesDiv").text("Replies List [ " + data.pageMaker.totalCount +" ]");
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
			var strClass= pageMaker.cri.page == i?'<font size=\'4\' color=\'red\'>' + i +'</font>' : i;
		  str += "<li><a href='" + i + "'>"+ strClass +"</a></li>";
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

//댓글목록 버튼 클릭시
$("#repliesDiv").on("click", function() {
	$(".reply").toggle();
	
	//게시물 번호와 페이지 번호를 요청한다.
	getPageList(replyPage);
});

$(document).ready(function(){
	var formObj = $("form[role='form']");
	
	//Modify 버튼 클릭 시 
	$(".btn-warning").on("click", function(){
		formObj.attr("action", "/sboard/modifyPage");
		formObj.attr("method", "get");		
		formObj.submit();
	});
	
	//REMOVE 클릭 시
	$(".btn-danger").on("click", function(){
		formObj.attr("action", "/sboard/removePage");
		formObj.submit();
	});
	
	//GO LIST 버튼 클릭 시 
	$(".btn-primary").on("click", function(){
		formObj.attr("method", "get");
		formObj.attr("action", "/sboard/list");
		formObj.submit();
	});
	
});
</script>
</body>
</html>


