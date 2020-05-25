<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
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
<title>spring</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/reserve.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.min.js"></script>

<style type="text/css">
input:focus, textarea:focus {
	outline: none;
}
</style>

<script type="text/javascript">
$(function(){
	$("input").not($(":button")).not($(":reset")).keypress(function(evt){
		if(evt.keyCode==13) {
			var fields = $(this).parents("form, body").find("button, input, select");		// parents : 모든 아버지 
			var index = fields.index(this);  // 자신의 인덱스 
			
			if(index > -1 && (index+1) < fields.length) {  // index가 존재하고 마지막이 아니면 
				fields.eq(index+1).focus();
			}
			return false; // 이벤트 취소 
		}		
	});
});

$(function(){
	$("form[name=checkForm] input").focus(function(){
		$(this).css("border", "2px solid #B2FA5C");		
	});
	
	$("form[name=checkForm] input").blur(function(){
		$(this).css("border", "1px solid #EAEAEA");	
	});	
});

function sendOk() {
    var f = document.checkForm;   

 	var str = f.r_name.value;
    if(!str) {
        alert("예약자 이름을 입력 해 주세요.");
        f.r_name.focus();
        return;
    }

	str = f.r_tel.value;
    if(!str) {
        alert("연락처를 입력 해 주세요.");
        f.r_tel.focus();
        return;
    }     
   
	f.action="<%=cp%>/reserve/checked.do";

    f.submit();
} 

function deleteReserve(num) {
    var query = "num="+num+"&${query}";
    var url = "<%=cp%>/reserve/delete.do?r_num=${dto.r_num}";

    if(confirm("예약을 취소 하시겠습니까 ? "))
    	location.href=url;
    
    alert("예약이 취소 되었습니다.");
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="text-sub1">
	<p class="hold-menu">Reservation</p>
	<ul class="sub-menu">
		<li><a href="<%=cp%>/reserve/reserve.do">Reserve</a></li>
		<li><a href="<%=cp%>/reserve/checked.do"><span style="color: #B2FA5C;">Check</span></a></li>
		<c:if test="${sessionScope.loginMem.loginId == 'admin'}">
			<li><a href="<%=cp%>/reserve/list.do">List</a></li>	
		</c:if>		
	</ul>
</div>
	
<div class="container">
    <div class="body-container">   
   
   <jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>
   
   <div class="reserve">   
		<h3>| 예약 확인 </h3>			
		
		<div class="search">		
			<p> 예약 정보 입력 </p>		
		</div>
  	 <form name="checkForm" method="post">
		<div class="searchinput">			
			<p>이&nbsp;&nbsp;&nbsp;&nbsp;름&nbsp; |&nbsp;  
				<input type="text" name="r_name" maxlength="20" class="selectField2">
			</p> 
			<p style="margin-bottom: 20px;">연 락 처&nbsp; |&nbsp;  
				<input type="text" name="r_tel" maxlength="20" placeholder=" 010-0000-0000" class="selectField2">
			</p>			
		</div>
	</form>	
<!-- 	<p style="border: 1px solid white; width: 400px; margin: 0px auto;"></p>	 -->		

	<div class="button">
		<button type="button" class="btn" onclick="sendOk();">예약 조회</button>
	</div>	


<c:if test="${not empty dto and not empty dto.r_name and not empty dto.r_tel }">		
	<div class=blank2>
		<div class="title1">
			<p>예약 일시 </p>
		</div>
		<div class="reserve-info1">
			<p>예약 일자  | ${dto.r_date}</p>
			<p>예약 시간  | ${dto.r_time}</p>		
			<p>인원 | ${dto.r_count}	</p>					
		</div>
	</div>

	<div class=blank2 style="margin-left:35px; margin-bottom: 0px;">
		<div class="title2" style="margin-left: 0;">
			<p> 예약자 정보 </p>
		</div>
		<div class="reserve-info2" style="margin-left: 0;">
		
			<p>이&nbsp;&nbsp;&nbsp;&nbsp;름&nbsp; |&nbsp;	${dto.r_name} </p> 
			<p>연 락 처&nbsp; |&nbsp; ${dto.r_tel} </p>
			<p>이 메 일&nbsp; |&nbsp; ${dto.r_email} </p>
			<p>요청사항 |&nbsp; ${dto.r_request} </p>		
		</div>					
	</div>	
		<div class="button">
			<a href="<%=cp%>/reserve/update.do?r_num=${dto.r_num}"><button type="button" class="btn">예약 수정</button></a>
			<button type="button" style="margin-left: 60px;" class="btn" onclick="deleteReserve('${dto.r_num}');" >예약  취소</button>
		</div>	
</c:if>
	<p class="msg ${not empty msg? 'yb':''}"> ${msg} </p> 	 	
	</div>  
   </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>