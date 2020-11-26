<!-- 
	iframe에 반환될 페이지이다.
	실제 동작은 alert 창을 호출하고 form을 초기화한다.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	var result = '${savedName}';
	
	//부모창인 uploadForm 페이지의 addFilePath 함수 호출.
	parent.addFilePath(result);
</script>

