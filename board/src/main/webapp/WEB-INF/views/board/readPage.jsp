<!-- 
	페이징 처리 후 조회페이지. 
	조회 후 다시 목록으로 돌아갈 때 페이지 번호를 유지시키기 위한 기능이 추가됨.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>게시글 읽기</title>
</head>
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<body>
<form role="form" method="post">
	<!-- 삭제나 수정작업을 위해 게시물번호를 가지고 있는다.  -->
	<input type='hidden' name='bno' value="${boardVO.bno}">
	<!-- 조회 후, 수정, 삭제 화면에서 다시 목록으로 돌아갈 때도 페이징 번호를 유지하기 위해 페이지 번호와 페이지 당 보여줄 게시글 개수를 설정한다. -->
	<input type='hidden' name='page' value ="${cri.page}">
    <input type='hidden' name='perPageNum' value ="${cri.perPageNum}">
</form>
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
</table>
<script>
$(document).ready(function(){
	var formObj = $("form[role='form']");
	
	//Modify 버튼 클릭 시 
	$(".btn-warning").on("click", function(){
		formObj.attr("action", "/board/modifyPage");
		formObj.attr("method", "get");		
		formObj.submit();
	});
	
	//REMOVE 클릭 시
	$(".btn-danger").on("click", function(){
		formObj.attr("action", "/board/removePage");
		formObj.submit();
	});
	
	//GO LIST 버튼 클릭 시 
	$(".btn-primary").on("click", function(){
		formObj.attr("method", "get");
		formObj.attr("action", "/board/listPage");
		formObj.submit();
	});
	
});
</script>
</body>
</html>


