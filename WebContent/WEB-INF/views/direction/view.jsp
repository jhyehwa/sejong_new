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
</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		<div class="info-title">| Directions</div>
			<div class="address_main">
				<img class="address" src="<%=cp%>/resource/image/map.PNG">
				<ul class="address-info" >
					<li>주변 대중교통</li>
					<li>7016번, 7711번, 7737번</li>
					<li>홍대입구역 2호선, 공항철도, 경의중앙선</li>
				</ul>
			</div>

			<div class="text-sub1">
					<p class="hold-menu">Menu</p>
				<ul class="sub-menu">
			<li><a href="<%=cp %>/notice/list.do">Notice/Event</a></li>
			<li><a href="<%=cp %>/qna/list.do">QnA</a></li>
			<li><a href="<%=cp%>/board/list.board">FreeBoard</a></li>
			<li><a href="<%=cp%>/direction/view.do"><span style="color: #B2FA5C;">Directions</span></a></li>
				</ul>
			</div>

			<div class="text-sub2">
				<ul class="image">
					<li><a href="#"><img alt="" src="img/instargram.png"></a></li>
					<li><a href="#"><img alt="" src="img/facebook.png"></a></li>
					<li><a href="#"><img alt="" src="img/youtube.png"></a></li>
					<li><a href="#"><img alt="" src="img/twitter.png"></a></li>
				</ul>
			</div>
	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</div>
</body>
</html>