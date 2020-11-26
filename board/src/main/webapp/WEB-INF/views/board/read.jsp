<!-- 조회페이지. -->
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
	
	console.log(formObj);
	
	$(".btn-warning").on("click", function(){
		formObj.attr("action", "/board/modify");
		formObj.attr("method", "get");		
		formObj.submit();
	});
	
	$(".btn-danger").on("click", function(){
		formObj.attr("action", "/board/remove");
		formObj.submit();
	});
	
	$(".btn-primary").on("click", function(){
		self.location = "/board/listAll";
	});
	
});
</script>
</body>
</html>


