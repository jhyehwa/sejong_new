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
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/foodlist.css" type="text/css">
<script type="text/javascript">
	function article(f_num) {
		var url = "${articleUrl}&f_num=" + f_num;
		location.href = url;
	}
</script>
</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
	
	<jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>

	<div class="text-sub1">
		<p class="hold-menu">Menu</p>
			<ul class="sub-menu">
				<li value="main" ><a href="<%=cp%>/foodmenu/list.do?f_type=main"<c:if test="${f_type == 'main'}">style="color: #B2FA5C;"</c:if>>Main</a></li>
                <li value="side"><a href="<%=cp%>/foodmenu/list.do?f_type=side"<c:if test="${f_type == 'side'}">style="color: #B2FA5C;"</c:if>>Side</a></li>
                <li value="desserts"><a href="<%=cp%>/foodmenu/list.do?f_type=desserts"<c:if test="${f_type == 'desserts'}">style="color: #B2FA5C;"</c:if>>Desserts</a></li>
                <li value="others"><a href="<%=cp%>/foodmenu/list.do?f_type=others"<c:if test="${f_type == 'others'}">style="color: #B2FA5C;"</c:if>>Others</a></li>
			</ul>
	</div>
	<div class="container">
		<div class="body-container">
			<div>
				<h2><img class="menu_logo" src="/sejong_new/resource/image/menu.png"> Menu</h2>
			</div>
			
			<table class="menu_">
				<c:forEach var="dto" items="${list}" varStatus="status">
					<c:if test="${status.index == 0}">
						<tr>
					</c:if>
					<c:if test="${status.index != 0 && status.index % 2 == 0}">
						<c:out value="</tr><tr>" escapeXml="false"/>
					</c:if>
					<td class="image_box">
						<div class="image_list">
							<img class="image" src="<%=cp%>/uploads/f_food/${dto.f_image}">
						</div>
						<div class="name">
							<span onclick="javascript:article('${dto.f_num}');">
								${dto.f_name}
							</span>
						</div>
					</td>
				</c:forEach>
				<c:set var="n" value="${list.size()}"/>
				<c:if test="${n > 0 && n % 2 != 0}">
					<c:forEach var="i" begin="${n % 2 + 1}" end="2" step="1">
						<td>
							<!-- <div>&nbsp;</div> -->
						</td>
					</c:forEach>
				</c:if>
				<c:if test="${n != 0}">
					<c:out value="</tr>" escapeXml="false"/>
				</c:if>
			</table>
			<table class="num_page">
				<tr>
					<td class="page_num">
						${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
					</td>
				</tr>
			</table>
			<table class="photo">
				<tr>
					<td>
						<c:if test="${sessionScope.loginMem.loginId == 'admin'}">
							<button type="button" class="photo_btn" onclick="javascript:location.href='<%=cp%>/foodmenu/created.do';">사진올리기</button>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>