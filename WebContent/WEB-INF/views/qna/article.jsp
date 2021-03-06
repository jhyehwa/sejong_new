<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=cp%>/resource/css/information.css" type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function deleteNotice(num) {
	if(confirm("게시물을 삭제 하시겠습니까?")){
		var query = "num="+num+"&${query}";
		var url = "<%=cp%>/qna/delete.do?" + query;	

		location.href = url;
	}
 }
</script>
</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
	<div class="info-title">| QnA</div>
	<div class="info">
		<ul class="board">
			<li class="board-qeustion">작성자</li>
			<li class="board-answer-long">${dto.name}</li>
			<li class="board-qeustion-left">등록일</li>
			<li class="board-answer-short">${dto.created}</li>
			<li class="board-qeustion">게시물제목</li>
			<li class="board-answer-long">${dto.title}</li>
			<li class="board-qeustion-left">조회수</li>
			<li class="board-answer-short">${dto.hitCount}</li>
			<li class="board-content">${dto.content}</li>
		</ul>
		<div class="reply">
			<c:if test="${sessionScope.loginMem.loginId== 'admin' }">
				<a href="<%=cp %>/qna/reply.do?${query}&num=${dto.num}">답변</a>
			</c:if>
		</div>
		<div class="board-content-last">
			<c:if test="${not empty preReadDto}">
				<a href="<%=cp%>/qna/article.do?${query}&num=${preReadDto.num}">◀</a>
			</c:if>
			<c:if test="${sessionScope.loginMem.loginId==dto.id}">
				<button type="button" onclick="javascript:location.href='<%=cp%>/qna/update.do?num=${dto.num}&page=${page}'">수정하기</button>
				<button type="button" onclick="deleteNotice(${dto.num});">삭제하기</button>
			</c:if>
			<button type="button" onclick="javascript:location.href='<%=cp%>/qna/list.do?${query}'">목록으로</button>
			<c:if test="${not empty nextReadDto}">
				<a href="<%=cp%>/qna/article.do?${query}&num=${nextReadDto.num}">▶</a>
			</c:if>
		</div>
		<div>
			<ul>
			</ul>
		</div>
		<div class="text-sub1">
			<p class="hold-menu">Menu</p>
			<ul class="sub-menu">
				<li><a href="<%=cp%>/notice/list.do">Notice/Event</a></li>
				<li><a href="<%=cp %>/qna/list.do"><span style="color: #B2FA5C;">QnA</span></a></li>
				<li><a href="<%=cp%>/board/list.board">FreeBoard</a></li>
				<li><a href="<%=cp%>/direction/direction.do">Directions</a></li>
			</ul>
		</div>
	</div>
	
	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</div>
</body>
</html>