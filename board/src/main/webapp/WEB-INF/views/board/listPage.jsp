<!-- PageMaker를 사용하여 하단 페이징 처리된 페이지 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 목록</title>
</head>
<script src="/resources/js/jquery-3.5.1.min.js"></script>
<body>
<table border="1">
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>작성일자</th>
		<th>조회수</th>
	</tr>
	<!-- 데이터가 많아 게시글 등록 성공 alert창이 뜨지 않을 경우  foreach에 begin="0" end="5" step="1"를 추가한다. -->
	<c:forEach items="${list}" var="boardVO">
		<tr>
			<td>${boardVO.bno}</td>
			<td><a href='/board/readPage${pageMaker.makeQuery(pageMaker.cri.page) }&bno=${boardVO.bno}'>${boardVO.title}</a></td>
			<td>${boardVO.writer}</td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
			<td><span>${boardVO.viewcnt }</span></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="5" class="pagination">
			<c:if test="${pageMaker.prev}">
				<a href="${pageMaker.startPage - 1}">&laquo;</a>
			</c:if>
			
			<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="idx">
				<c:choose>
				<%-- 
					pageMaker.cri.page는 현재 요청한 페이지 번호이다.
					페이지 번호를 하나씩 출력할 때 현재 요청한 페이지라면 색상을 빨강색으로 한다. 
				 --%>
				 <c:when test="${pageMaker.cri.page == idx}">
					<c:set var="bidx" value="<font size='4' color='red'>${idx}</font>" />
					<a href="${idx}">${bidx}</a>
			     </c:when>
				 <c:otherwise>
					<a href="${idx}">${idx}</a>
				 </c:otherwise>
				</c:choose>
			</c:forEach>
		
			<c:if test="${pageMaker.next && pageMaker.endPage > 0}">
				<a href="${pageMaker.endPage +1}">&raquo;</a>
			</c:if>
		</td>
	</tr>
</table>

<form id="jobForm">
  <input type='hidden' name="page" value=${pageMaker.cri.perPageNum}>
  <input type='hidden' name="perPageNum" value=${pageMaker.cri.perPageNum}>
</form>

<script>
	var result = '${msg}';
	
	if (result == 'SUCCESS') {
		alert("처리가 완료되었습니다.");
	}
	
	//하단 페이지 번호 클릭 시 작동.
	//직접 링크를 생성하지 않고 아래와 같은 방식으로 하면 링크를 쉽고 편하게 생성할 수 있다.
	$(".pagination a").on("click", function(event){
		//a 링크의 실제 화면의 이동을 막는다.
		event.preventDefault(); 
		
		//요청한 페이지 번호.
		var targetPage = $(this).attr("href");
		
		var jobForm = $("#jobForm");
		//요청한 페이지 번호를 파라미터 값에 세팅.
		jobForm.find("[name='page']").val(targetPage);
		jobForm.attr("action","/board/listPage").attr("method", "get");
		jobForm.submit();
	});
</script>
</body>
</html>



							
