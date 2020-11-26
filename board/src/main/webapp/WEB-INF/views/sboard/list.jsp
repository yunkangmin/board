<!-- 
	검색기능이 추가된 게시글 목록 페이지.
-->
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
		<td colspan="5">
			<select name="searchType">
				<!--
					n: 검색 조건이 없음.
					t: 제목으로 검색.
					c: 내용으로 검색.
					w: 작성자로 검색.
					tc: 제목이나 내용으로 검색.
					cw: 내용이나 작성자로 검색.
					tcw: 제목이나 내용 혹은 작성자로 검색.
				  -->
				<option value="n"
					<c:out value="${cri.searchType == null?'selected':''}"/>>
					---</option>
				<option value="t"
					<c:out value="${cri.searchType eq 't'?'selected':''}"/>>
					Title</option>
				<option value="c"
					<c:out value="${cri.searchType eq 'c'?'selected':''}"/>>
					Content</option>
				<option value="w"
					<c:out value="${cri.searchType eq 'w'?'selected':''}"/>>
					Writer</option>
				<option value="tc"
					<c:out value="${cri.searchType eq 'tc'?'selected':''}"/>>
					Title OR Content</option>
				<option value="cw"
					<c:out value="${cri.searchType eq 'cw'?'selected':''}"/>>
					Content OR Writer</option>
				<option value="tcw"
					<c:out value="${cri.searchType eq 'tcw'?'selected':''}"/>>
					Title OR Content OR Writer</option>
			</select> 
			<input type="text" name='keyword' id="keywordInput" value='${cri.keyword }'/>
			<button id='searchBtn'>검색</button>
			<button id='newBtn'>게시글 등록</button>
		</td>
	</tr>
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
			<td><a href='/sboard/readPage${pageMaker.makeSearch(pageMaker.cri.page) }
			&bno=${boardVO.bno}' style="text-decoration: none;">${boardVO.title} [ ${boardVO.replycnt} ]</a></td>
			<td>${boardVO.writer}</td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
			<td><span>${boardVO.viewcnt }</span></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="5" class="pagination">
			<c:if test="${pageMaker.prev}">
			    <!--pageMaker.makeSearch()는 ?로 시작하는 쿼리스트링을 반환한다.  -->
				<a href="list${pageMaker.makeSearch(pageMaker.startPage - 1)}">&laquo;</a>
			</c:if>
			
			<c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
				<c:choose>
				<%-- 
					pageMaker.cri.page는 현재 요청한 페이지 번호이다.
					페이지 번호를 하나씩 출력할 때 현재 요청한 페이지라면 색상을 빨강색으로 한다. 
				 --%>
				 <c:when test="${pageMaker.cri.page == idx}">
					<c:set var="bidx" value="<font size='4' color='red'>${idx}</font>" />
					<a href="list${pageMaker.makeSearch(idx)}">${bidx}</a>
			     </c:when>
				 <c:otherwise>
					<a href="list${pageMaker.makeSearch(idx)}">${idx}</a>
				 </c:otherwise>
				</c:choose>
			</c:forEach>
		
			<c:if test="${pageMaker.next && pageMaker.endPage > 0}">
				<a href="list${pageMaker.makeSearch(pageMaker.endPage +1)}">&raquo;</a>
			</c:if>
		</td>
	</tr>
</table>

<script>
	var result = '${msg}';
	
	if (result == 'SUCCESS') {
		alert("처리가 완료되었습니다.");
	}
	
	//하단 페이지 번호 클릭 시 작동.
	//직접 링크를 생성하지 않고 아래와 같은 방식으로 하면 링크를 쉽고 편하게 생성할 수 있다.
	//$(".pagination a").on("click", function(event){
		//a 링크의 실제 화면의 이동을 막는다.
		//event.preventDefault(); 
		
		//요청한 페이지 번호.
		//var targetPage = $(this).attr("href");
		
		//var jobForm = $("#jobForm");
		//요청한 페이지 번호를 파라미터 값에 세팅.
		//jobForm.find("[name='page']").val(targetPage);
		//jobForm.attr("action","/board/listPage").attr("method", "get");
		//jobForm.submit();
	//});
	
	$(document).ready(function() {
		//검색 버튼 클릭 시
		$('#searchBtn').on("click",function(event) {
			//검색 버튼을 클릭하면 pageMaker.makeQuery(1)로 페이지 번호를 1로 호출하면 page, perPageNum을 파라미터로 갖는 URI가 추가된다.
			//조회페이지에서 입력한 searchType, keyword 값을 쿼리스트링으로 추가한다.
			self.location = "list"
					+ '${pageMaker.makeQuery(1)}'
					+ "&searchType="
					+ $("select option:selected").val()
					+ "&keyword=" + $('#keywordInput').val();
		});
		
		//글 등록 페이지로 이동한다.
		$('#newBtn').on("click", function(evt) {
			self.location = "register";
		});
	});
</script>
</body>
</html>



							
