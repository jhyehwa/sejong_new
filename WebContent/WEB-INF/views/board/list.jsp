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
<link rel="stylesheet" href="<%=cp%>/resource/css/board.css"
	type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>

<script type="text/javascript">
	function boardSearch() {
		var f = document.boardSearch;

		if (f.keyword == null) {
			alert("")
		}

		f.submit();
	}
	
	
	$(function() {
		$(".write-board").hover(function() {
			$(this).css("text-decoration","none");
		});
	});
	
</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="info-title">| Free Board
				
	</div>
	<div
		style="color: white; margin: 0 auto; text-align: left; font: 15px; width: 960px;">${dataCnt}개(${page}/${totalPage})</div>
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
				<li class="num">${dto.listNum}</li>
				<li class="title"><a
					href="${articleUrl}&num=${dto.num}&rows=${rows}">${dto.title}</a></li>
				<li class="name">${dto.writer}</li>
				<li class="date">${dto.created}</li>
				<li class="hit">${dto.hitCount}</li>
			</ul>
		</c:forEach>

		<ul class="nextpage" style="padding-bottom: 10px;">
			<li><span style="color: white;">${dataCount==0 ? "등록된 게시물이 존재하지 않습니다." : paging}</span></li>
		</ul>

		<form action="<%=cp%>/board/list.board" method="post"
			name="boardSearch">
			<ul class="info-bottom-1"
				style="magin-top: 10px; color: white; widows: 60%; height: 50px; margin-left: 250px;">
				<li style="float: left;"><select class="serch-option"
					name="type">
						<option value="" ${type==""?"selected='selected'":""}>선택</option>
						<option value="b_title"
							${type=="b_title"?"selected='selected'":""}>제목</option>
						<option value="b_content"
							${type=="b_content"?"selected='selected'":""}>내용</option>
						<option value="b_writer"
							${type=="b_writer"?"selected='selected'":""}>작성자</option>
						<option value="b_created"
							${type=="b_created"?"selected='selected'":""}>작성날짜</option>
				</select></li>

				<li style="float: left;"><input class="serch-text" type="text"
					id="keyword" name="keyword" value="${keyword}">
					<button class="serch-button" onclick="boardSearch()">검색</button>
					</li>
				<li style="float: left;">
				<a href="<%=cp%>/board/insert.board" class="write-board" style="margin-left: 90px;">글쓰기</a> <span>|</span>
				<a href="<%=cp%>/board/list.board" class="write-board" style="font-size: 16px; margin: 0px;"> 새로고침</a>
				</li>
			</ul>


		</form>

	</div>

	<div class="text-sub1">
		<p class="hold-menu">Menu</p>
		<ul class="sub-menu">
				<li><a href="<%=cp %>/notice/list.do">Notice/Event</a></li>
			<li><a href="<%=cp%>/qna/list.do">QNA</a></li>
			<li><a href="<%=cp%>/board/list.board"><span
					style="color: #B2FA5C;">FreeBoard</span></a></li>
			<li><a href="<%=cp%>/direction/direction.do">Directions</a></li>
		</ul>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

</body>
</html>