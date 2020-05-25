<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
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
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/reserve.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">

$(function(){
	$(".list-table-menu .ltm-tr2:nth-child(2n+1)").css("background", "#353535");
});

function searchList() {
	var f=document.searchForm;
	f.submit();
}	
</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="text-sub1">
	
	<jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>
	
		<p class="hold-menu">Reservation</p>
		<ul class="sub-menu">
			<li><a href="<%=cp%>/reserve/reserve.do"><span>Reserve</span></a></li>
			<li><a href="<%=cp%>/reserve/checked.do">Check</a></li>		
			<c:if test="${sessionScope.loginMem.loginId == 'admin'}">
				<li><a href="<%=cp%>/reserve/list.do"><span style="color: #B2FA5C;">List</span></a></li>	
			</c:if>			
		</ul>
	</div>

	<div class="container">
		<div class="body-container">

			<div class="reserve">

			<h3>| 예약 리스트</h3>			
				<table class="list-table-top">
					<tr height="28">
						<td class="ltt-td" align="left" width="14%">
							${dataCount}개(${page}/${total_page} 페이지)</td>	
						<td align="center">&nbsp;</td>
					<td class="ltt-td" align="right" width="9%">
						<a href="javascript:location.href='<%=cp%>/reserve/list.do';" style="text-decoration: none;">
						    새로고침</a></td>								
					</tr>	
					<tr> <td></td> <td></td> <td></td>  </tr>
				</table>	

				<table class="list-table-menu">
					<tr class="ltm-tr1" align="center" height="40">
						<th class="ltm-th" width="90">예약번호</th>
						<th class="ltm-th" width="90">예약자명</th>
						<th class="ltm-th" width="90">예약날짜</th>
						<th class="ltm-th" width="90">예약시간</th>
						<th class="ltm-th" width="90">예약인원</th>
						<th class="ltm-th" width="150">요청사항</th>
					</tr>
					<c:forEach var="dto" items="${list}">
						<tr class="ltm-tr2" align="center" height="38">
							<td class="ltm-td">${dto.r_num}</td>
							<td class="ltm-td">${dto.r_name}</td>
							<td class="ltm-td">${dto.r_date}</td>
							<td class="ltm-td">${dto.r_time}</td>
							<td class="ltm-td">${dto.r_count}</td>
							<td class="ltm-td">${dto.r_request}</td>
						</tr>
					</c:forEach>
				</table>

				<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
					<tr height="35">
						<td align="center" style="color: white;" >${dataCount==0?"예약 내역이 없습니다.":paging}</td>
					</tr>
				</table>

				<table style="width: 700px; margin: 0px auto; border-spacing: 0px;">
					<tr height="40">					
						<td align="center">
							<form name="searchForm" action="<%=cp%>/reserve/list.do" method="post">
								<select name="condition" class="selectField2" style="width: 80px; height: 30px; font-size: 14px;">
									<option value="r_name"
										${condition=="r_name"?"selected='selected'":"" }>예약자명</option>									
									<option value="r_date"
										${condition=="r_date"?"selected='selected'":"" }>예약일자</option>
								</select>
								 <input type="text" name="keyword" class="boxTF" style="width: 120px; height: 21px;"
									value="${keyword}">
								<button type="button" class="btn" style="height: 30px; width: 60px; font-size: 14px;" onclick="searchList()">검색</button>
							</form>
						</td>								
					</tr>
					
					
				</table>
			</div>

		</div>

	</div>


	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>