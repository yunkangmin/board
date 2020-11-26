<!-- 페이지 번호가 유지되는 수정 페이지. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정</title>
</head>
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<body>
<form role="form" action="modifyPage" method="post">
	<!-- 페이지 번호가 유지되기 위해 page와 perPageNum을 hidden으로 유지한다. -->
	<input type='hidden' name='page' value="${cri.page}"> 
	<input type='hidden' name='perPageNum' value="${cri.perPageNum}">
	
	<table border="1">
		<tr>
			<td>게시글 번호</td>
			<td><input type="text" name='bno' value="${boardVO.bno}" readonly="readonly"></td>
		</tr>
		<tr>
			<td>제목</td>
			<td><input type="text" name='title' value="${boardVO.title}"></td>
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
	//CANCEL 버튼을 누르면 다시 페이지 번호를 유지하면서 목록 페이지로 간다. 
	$(".btn-warning").on("click", function() {
		self.location = "/board/listPage?page=${cri.page}&perPageNum=${cri.perPageNum}";
	});
	
	//수정 작업을 처리한다.
	$(".btn-primary").on("click", function() {
		formObj.submit();
	});

});
</script>
</body>
</html>
