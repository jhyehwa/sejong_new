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
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function findMyQnA(name) {
	var f = document.searchForm;
	f.submit(name);
}

function searchList() {
	var f = document.searchForm;
	f.submit();
}

</script>
<link rel="stylesheet" type="text/css" href="<%=cp%>/resource/css/qna.css"/>
<link rel="stylesheet" type="text/css" href="<%=cp%>/resource/css/information.css"/>
</head>
<body>

<div class="header">
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="qna-subject">| QnA </div>
	<div>
		<table class="qna-holdmenu">
			<tr class="qna-sub">
				<td class="qna-num">Num</td>
				<td class="qna-title">Title</td>
				<td class="qna-writer">Writer</td>
				<td class="qna-date">Date</td>
				<td class="qna-reply">ReplyCount</td>
				<td class="qna-hit">Hit</td>
			</tr>
		</table>
		<table class="qna-input">
		<c:forEach var="dto" items="${list}">
			<tr class="qna-sub">
				<td class="qna-num">${dto.listNum}</td>
				<td class="qna-title"> 
				  <c:forEach var="n" begin="1" end="${dto.depth }">&nbsp;&nbsp;</c:forEach>
				<c:if test="${dto.depth!=0}">└&nbsp;</c:if>
				<a href="${articleUrl}&num=${dto.num}">${dto.title}</a></td>
				<td class="qna-writer">${dto.name}</td>
				<td class="qna-date">${dto.created}</td>
				<td class="qna-reply">${dto.depth}</td>
				<td class="qna-hit">${dto.hitCount}</td>
			</tr>
		</c:forEach>
		</table>
		<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			<tr height="35">
				<td align="center" style="color: white"> ${dataCount!=0?paging:"등록된 게시물이 없습니다."} </td>
			</tr>
		</table>
			<div class="qna-last">
			<form name="searchForm" action="<%=cp %>/qna/list.do" method="post">
				<button type="button" onclick="javascript:location.href='<%=cp%>/qna/list.do';">새로고침</button>
						<select name="condition">
							<option value="q_title">제목</option>
							<option value="q_writer">작성자</option>
							<option value="q_content">내용</option>
							<option value="q_created">작성일</option>
						</select>
						<input type="text" name="keyword">
						<button type="button" onclick="searchList()">검색</button>
				<a href="<%=cp%>/qna/created.do?page=${page}">글쓰기</a>
			</form>
			</div>
	</div>
	
	<div class="text-sub1">
			<p class="hold-menu">Menu</p>
		<ul class="sub-menu">
			<li><a href="<%=cp %>/notice/list.do">Notice/Event</a></li>
			<li><a href="<%=cp %>/qna/list.do"><span style="color: #B2FA5C;">QnA</span></a></li>
			<li><a href="<%=cp%>/board/list.board">FreeBoard</a></li>
			<li><a href="information_directions.html">Directions</a></li>
		</ul>
	</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</div>

</body>
</html>