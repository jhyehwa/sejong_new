<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=cp %>/resource/css/board.css" type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function send() {
	var f = document.noticeForm;
	
	var str = f.title.value;
	if(!str) {
		alert("제목을 입력하세요.");
		f.title.focus();
		return;
	}
	
	str = f.content.value;
	if(!str){
		alert("내용을 입력하세요. ");
		f.content.focus();
		return;
	}
	
	f.action="<%=cp%>/board/${mode}Submit.board";
	
	f.submit();
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="info-title">| Free Board</div>
<form name="noticeForm" method="post">

	<div class="info">
		<ul class="info-content-title">
			<li class="input-question">제 목</li>
			<li class="input-answer"><input type="text" id="tag" name="title" value="${dto.title}" style="padding-left: 10px;" ></li>
			<li class="input-question">작 성 자</li>
			<li class="input-answer-little"><input type="text" id="tag" name="writer" placeholder="${sessionScope.loginMem.loginName}" value="${dto.id}" readonly="readonly" style="padding-left:10px;">
			<li class="input-question-large">내용</li>
			<li class="input-answer-content"><textarea name="content" cols="80" rows="25" style="resize: none; border-radius: 10px; padding-left: 10px; font-weight: 600; ">${dto.content}</textarea></li>
		</ul>
		<div class="info-content-last">
			<ul class="info-content-button">
				<li><button type="button" onclick="send();">${mode=='update' ? '수정' : '등록' }</button></li>
				<li><button type="reset">다시입력</button></li>
				<li><button type="button" onclick="javascript:location.href='<%=cp%>/notice/list.do';">취소</button></li>
			</ul>
		</div>
		<c:if test="${mode=='update'}">
		<input type="hidden" name="num" value="${dto.num}" readonly="readonly">
		<input type="hidden" name="page" value="${page}" readonly="readonly">
		</c:if>

	</div>
</form>

	<div class="text-sub1">
			<p class="hold-menu">Menu</p>
		<ul class="sub-menu">
			<li><a href="<%=cp%>/notice/list.do">Notice/Event</a></li>
			<li><a href="<%=cp%>/board/list.board">FreeBoard</a></li>
			<li><a href="information_directions.html">Directions</a></li>
		</ul>
	</div>

	<div class="footer">
   	 	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>