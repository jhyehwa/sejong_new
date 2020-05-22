<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/board.css" type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="<%=cp%>/resource/css/information.css"
	type="text/css">
<script type="text/javascript">

	
	
	function updateBoard(num) {
		<c:if test="${sessionScope.loginMem.loginId == 'admin'}">
		var page = "${page}";
		var rows = "${rows}";
		var query = "num=" + ${num} + "&page=" + page + "&rows=" + rows;
		var url = "<%=cp%>/board/update.board?" + query;
		location.href = url;
		</c:if>
		
		<c:if test="${sessionScope.loginMem.loginId != 'dto.id'}">
		alert("수정할 권한이 없습니다.");
	</c:if>
		
	}
</script>
</head>


<div class="header">
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>


<body>
	<form>
		<div class="info-title">| Free Board</div>
		<div class="info">
			<ul class="board">
				<li class="board-qeustion">작성자</li>
				<li class="board-answer-long">${dto.writer}</li>
				<li class="board-qeustion-left">등록일</li>
				<li class="board-answer-short">${dto.created}</li>
				<li class="board-qeustion">게시물제목</li>
				<li class="board-answer-long">${dto.title}</li>
				<li class="board-qeustion-left">조회수</li>
				<li class="board-answer-short">${dto.hitCount}</li>
				<li class="board-content">${dto.content}</li>
			</ul>
			<table style="margin-bottom: 30px;">
				<tr>
					<td style="color: white; font-size: 17px; padding-bottom: 15px;">이전글
						: <c:if test="${not empty preDTO}">
							<a href="<%=cp%>/board/article.board?${query}&num=${preDTO.num}"
								style="color: white; font-size: 17px;"> ${preDTO.title}</a>
						</c:if>
					</td>
				</tr>
				<tr>
					<td style="color: white; font-size: 17px; padding-bottom: 15px;">다음글
						: <c:if test="${not empty nextDTO}">
							<a href="<%=cp%>/board/article.board?${query}&num=${nextDTO.num}"
								style="color: white; font-size: 17px;"> ${nextDTO.title}</a>
						</c:if>
					</td>

				</tr>
				<tr>
					<td style="color: white; font-size: 17px;">

						<button type="button" class="btn"
							onclick="javascript:location.href='<%=cp%>/board/list.board?page=${page}&rows=${rows}';">목록</button>

						<button type="button" class="btn"
							onclick="javascript:location.href='<%=cp%>/board/update.board?page=${page}&num=${dto.num}';">수정</button>

						<button type="button" class="btn"
							onclick="javascript:location.href='<%=cp%>/board/delete.board?num=${dto.num}'">삭제</button>
					</td>
				</tr>
			</table>
			<input type="hidden" name="num" value="${dto.num}" readonly="readonly">
		<input type="hidden" name="page" value="${page}" readonly="readonly">
		<input type="hidden" name="rows" value="${rows}" readonly="readonly">

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
			</div>
	</form>
	
</body>
</html>