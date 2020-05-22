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
function findMyQnA() {
	var f = document.searchForm;
}

function searchList() {
	var f = document.searchForm;
	f.submit();
}

</script>
<link rel="stylesheet" type="text/css" href="<%=cp%>/resource/css/qna.css"/>
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
			<tr class="qna-sub">
				<td class="qna-num">Num!!</td>
				<td class="qna-title">Title!!</td>
				<td class="qna-writer">Writer!!</td>
				<td class="qna-date">Date!!</td>
				<td class="qna-reply">ReplyCount</td>
				<td class="qna-hit">Hit!!</td>
			</tr>
		</table>
		<table>
			<tr>
				<td></td>
			</tr>
		</table>
			<div class="qna-last">
				<button name="keyword" onclick="findMyQnA();">내글보기</button>
				
			<c:if test="${sessionScope.login.Mem.loginId == 'admin'}">
				<form name="searchForm" action="<%=cp %>/" method="post">
					<select name="condition">
						<option value="writer">작성자</option>
						<option value="content">내용</option>
						<option value="created">작성일</option>
					</select>
					<input type="text" name="keyword">
					<button type="button" onclick="searchList()">검색</button>
				</form>
			</c:if>	
				<a href="<%=cp%>/qna/created.do">글쓰기</a>
			</div>
	</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</div>

</body>
</html>