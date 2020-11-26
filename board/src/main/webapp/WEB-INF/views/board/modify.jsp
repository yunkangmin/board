<!-- 수정 페이지. -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정</title>
</head>
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<body>
<form role="form" method="post">
	<table border="1">
		<tr>
			<td>제목</td>
			<td><input type="text"name='title' value="${boardVO.title}"></td>
		</tr>
		<tr>
			<td>내용</td>
			<td><textarea name="content" rows="3">${boardVO.content}</textarea></td>
		</tr>
		<tr>
			<td>작성자</td>
			<td><input type="text" name="writer" value="${boardVO.writer}"></td>
		</tr>
		<tr>
			<td colspan="2">
				<button type="submit" class="btn-primary">SAVE</button>
				<button type="submit" class="btn-warning">CANCEL</button>
			</td>
		</tr>
	</table>
</form>
<script>
$(document).ready(function() {

	var formObj = $("form[role='form']");

	console.log(formObj);

	$(".btn-warning").on("click", function() {
		self.location = "/board/listAll";
	});

	$(".btn-primary").on("click", function() {
		formObj.submit();
	});

});
</script>
</body>
</html>
