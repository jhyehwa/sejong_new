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
<link rel="stylesheet" href="<%=cp%>/resource/css/information.css"
	type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
	function boardSearch() {
		var f = document.boardSearch;
		f.submit();
	}
</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="info-title">| Free Board</div>
	<div style="color: white; margin: 0 auto; text-align: left; font: 15px; width: 960px;">${dataCnt}개(${page}/${totalPage}p)</div>
	<div class="info">
		<ul class="info-sub">
			<li class="num">Num</li>
			<li class="title">Title</li>
			<li class="name">Writer</li>
			<li class="date">Date</li>
			<li class="hit">Hit</li>
		</ul>

		<c:forEach var="dto" items="${list}">
			<ul class="info-input">
				<li class="num">${dto.num}</li>
				<li class="title"><a href="${articleUrl}&num=${dto.num}">${dto.title}</a></li>
				<li class="name">${dto.writer}</li>
				<li class="date">${dto.created}</li>
				<li class="hit">${dto.hitCount}</li>
			</ul>
		</c:forEach>

		<ul class="nextpage">
			<li><a href="#">1</a></li>
		</ul>

		<form action="<%=cp%>/board/list.board" method="post"
			name="boardSearch">
			<ul class="info-bottom">
				<li><select class="serch-option" name="type">
						<option value="title" ${type=="title"?"selected='selected'":""}>제목</option>
						<option value="content"
							${type=="content"?"selected='selected'":""}>내용</option>
						<option value="writer" ${type=="writer"?"selected='selected'":""}>작성자</option>
						<option value="created"
							${type=="created"?"selected='selected'":""}>작성날짜</option>
				</select></li>
				<li><input class="serch-text" type="text" id="keyword"
					value="${keyword}"></li>
				<li><button class="serch-button" onclick="">검색</button> <a
					href="<%=cp%>/board/insert.board" class="write-board">글쓰기</a></li>

			</ul>
		</form>

	</div>

	<div class="text-sub1">
		<p class="hold-menu">Menu</p>
		<ul class="sub-menu">
			<li><a href="information_event.html"><span
					style="color: #B2FA5C;">Notice/Event</span></a></li>
			<li><a href="information_noticeboard.html">FreeBoard</a></li>
			<li><a href="information_directions.html">Directions</a></li>
		</ul>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

</body>
</html>