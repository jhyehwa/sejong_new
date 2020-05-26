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
<link rel="stylesheet" href="<%=cp %>/resource/css/information.css" type="text/css">
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
	
	f.action="<%=cp%>/notice/${mode}_ok.do";
	
	f.submit();
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="info-title">| Notice/Event</div>
<form name="noticeForm" method="post">
	<div class="info">
		<ul class="info-content-title">
			<li class="input-question">제 목</li>
			<li class="input-answer"><input type="text" id="tag" name="title" value="${dto.title}"></li>
			<li class="input-question">작 성 자</li>
			<li class="input-answer-little"><input type="text" id="tag" name="writer" value="${sessionScope.loginMem.loginName}"></li>
			<li class="input-question-middle">종 류</li>
			<li class="input-answer-middle">
			<input type="checkbox" name="enabled" value="1">이벤트
			&nbsp;&nbsp;&nbsp;종료 날짜<input type="text" name="finishDate" class="inputDate" value="${dto.finishDate}">
			</li>
			<li class="input-question-large">내용</li>
			<li class="input-answer-content"><textarea rows="25" cols="93" id="content" name="content" style="resize: none">${dto.content}</textarea></li>
			<li class="input-question">패스워드</li>
			<li class="input-answer-little"><input type="password" id="tag" name="password"></li>
		</ul>
		<div class="info-content-last">
			<ul class="info-content-button">
				<li><button type="button" onclick="send();">${mode=='update' ? '수정완료' : '등록하기'}</button></li>
				<li><button type="reset">다시입력</button></li>
				<li><button type="button" onclick="javascript:location.href='<%=cp%>/notice/list.do';">${mode=='update' ? '수정취소' : '등록취소'}</button></li>
			</ul>
			<c:if test="${mode=='update'}">
				<input type="hidden" name="num" value="${dto.num}">
				<input type="hidden" name="page" value="${page}">			
			</c:if>
		</div>

	</div>
</form>

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
   	 	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>