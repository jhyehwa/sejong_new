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
<link rel="stylesheet" href="<%=cp %>/resource/css/information.css" type="text/css">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function searchList() {
	var f = document.searchForm;
	f.submit();
}

</script>


</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="info-title">| Notice/Event</div>

	<div class="info">
		<div class="info-select">
			<form name="searchForm" action="<%=cp %>/notice/list.do"  method="post">
			<select name="condition" class="serch-option" onchange="searchList();">
				<option value="">전체보기</option>
				<option value="notice" ${condition == "notice" ? "selected='selected'" : " "}>공지사항</option>					
				<option value="created" ${condition == "created" ? "selected='selected'" : " "}>진행중인 이벤트</option>
				<option value="closed" ${condition == "closed" ? "selected='selected'" : " "}>종료된 이벤트</option>
			</select>
			</form>
		</div>
		<ul class="info-sub">
			<li class="num">Num</li>
			<li class="title">Title</li>
			<li class="name">Writer</li>
			<li class="date">Date</li>
			<li class="hit">Hit</li>
		</ul>
	<c:forEach var="dto" items="${listNotice}">
		<ul class="info-input">
			<li class="num">공지</li>
			<li class="title"><a href="${articleUrl}&num=${dto.num}">${dto.title}</a></li>
			<li class="name">${dto.writer}</li>
			<li class="date">${dto.startDate}</li>
			<li class="hit">${dto.hitCount}</li>
		</ul>
	</c:forEach>

	<c:forEach var="dto" items="${listEvent}">
		<ul class="info-input">
			<li class="num">${dto.listNum}</li>
			<li class="title"><a href="${articleUrl}&num=${dto.num}">${dto.title}</a></li>
			<li class="name">${dto.writer}</li>
			<li class="date">${dto.startDate}</li>
			<li class="hit">${dto.hitCount}</li>
		</ul>
	</c:forEach>
		
		<ul class="nextpage">
			<li style="color: white;">
				${condition!="notice" and dataCount==0 ? "등록된 공지사항&이벤트가 없습니다.":paging}
			</li>
		</ul>
		
 	    <jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>		
		
		<ul class="info-bottom">
			<c:if test="${sessionScope.loginMem.loginId == 'admin'}">
			<li><a href="<%=cp%>/notice/created.do" class="write-board1">글쓰기</a></li>
			</c:if>
		</ul>
	</div>

	<div class="text-sub1">
			<p class="hold-menu">Menu</p>
		<ul class="sub-menu">
			<li><a href="<%=cp %>/notice/list.do"><span style="color: #B2FA5C;">Notice/Event</span></a></li>
			<li><a href="<%=cp %>/qna/list.do">QnA</a></li>
			<li><a href="<%=cp%>/board/list.board">FreeBoard</a></li>
			<li><a href="<%=cp%>/direction/direction.do">Directions</a></li>
		</ul>
	</div>
	
	<div class="footer">
   	 	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	</div>
	
</body>
</html>