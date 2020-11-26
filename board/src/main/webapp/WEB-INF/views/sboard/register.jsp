<!-- 검색기능이 추가된 게시글 등록페이지.  -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 쓰기</title>
</head>
<body>
<form role="form" method="post">
	<p>
		제목:<br/><input type="text"name='title'  placeholder="Enter Title">
	</p>
	<p>
		내용:<br/>
		<textarea name="content" rows="3" placeholder="Enter ..."></textarea>
	</p>
	<p>
		글쓴이:<br/>
		<input type="text" name="writer" placeholder="Enter Writer">
	</p>
	<input type="submit" value="새 글 등록">
</form>
</body>
</html>