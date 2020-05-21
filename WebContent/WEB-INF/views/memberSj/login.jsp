<%@page contentType="text/html; charset=UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/login.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery.min.js"></script>

<script type="text/javascript">
function sendLogin() {
	var f = document.memberForm;
	
	var val = f.userId.value;
	
	if(! val){
		alert("아이디를 입력하세요.");
		f.userId.focus();
		return;
	}
	
	val = f.userPwd.value;
	
	if(! val){
		alert("비밀번호를 입력하세요.");
		f.userPwd.focus();
		return;
	}
	
	f.action = "<%=cp%>/memberSj/loginSubmit.mem";
	f.submit();
	
}
</script>

</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>


	<div class="ex1">
		<header class="section_header">
			<h2>
				<span>|</span> 로그인
			</h2>
		</header>
		<article class="article">
			<form name="memberForm" method="post">
				<table>
					<tr class="list-row-2">
						<td class="td-left">아&nbsp;이&nbsp;디</td>
						<td class="td-right"><input type="text" name="userId"
							id="userId" maxlength="10" class="boxTF"> <span
							id="userIdState" style='display: none;'></span></td>
						<td rowspan="2"><button type="button" class="td-btn" onclick="sendLogin();">LOGIN</button></td>
					</tr>

					<tr class="list-row-1">
						<td class="td-left">비밀번호</td>
						<td class="td-right"><input type="password" name="userPwd"
							class="boxTF" maxlength="10"></td>
					</tr>

				</table>
			</form>
		</article>
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