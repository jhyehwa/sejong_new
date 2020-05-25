<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">

<div class="header-top">
    <div class="header-right">
        <div class="left" style="width: 150px; text-align: right;">
            <c:if test="${empty sessionScope.loginMem}">
                <a href="<%=cp%>/memberSj/login.mem">LOGIN</a>
                    <span style="color: white;"> &nbsp;|&nbsp; </span>
                <a href="<%=cp%>/memberSj/insert.mem">JOIN</a>
            </c:if>
            <c:if test="${not empty sessionScope.loginMem}">                    
                    <a href="<%=cp%>/memberSj/logout.mem">LOGOUT</a>
                    <span style="color: white;"> &nbsp;|&nbsp; </span>
                    <a href="<%=cp%>/memberSj/update.mem">UPDATE</a>
                    <br>
                <span style="color:#FBF5C9;">${sessionScope.loginMem.loginName}님</span>
            </c:if> 
         </div>   
	   <div class="logo-main">
         <a href="<%=cp %>/main.do"><img class="logo" src="/sejong_new/resource/image/logo.png"></a>
      </div>            
    </div>
 </div>

<div class="menu">
    <ul>
        <li class="menu_li"> <a href="<%=cp%>/brandstory/brandstory.do">Brand Story</a> </li>        			
        <li class="menu_li">
            <a href="<%=cp%>/foodmenu/list.do?f_type=main">Menu</a>
            <ul class="menu_sub">
                <li value="main"><a href="<%=cp%>/foodmenu/list.do?f_type=main">Main</a></li>
                <li value="side"><a href="<%=cp%>/foodmenu/list.do?f_type=side">Side</a></li>
                <li value="desserts"><a href="<%=cp%>/foodmenu/list.do?f_type=desserts">Desserts</a></li>
                <li value="others"><a href="<%=cp%>/foodmenu/list.do?f_type=others">Others</a></li>
            </ul>
        </li>

        <li class="menu_li">
            <a href="<%=cp%>/reserve/reserve.do">Reservation</a>
            <ul class="menu_sub">
                <li><a href="<%=cp%>/reserve/reserve.do">Reserve</a></li>
                <li><a href="<%=cp%>/reserve/checked.do">Check</a></li>               
                <c:if test="${sessionScope.loginMem.loginId == 'admin'}">
      		    	<li><a href="<%=cp%>/reserve/list.do">List</a></li>   
      			</c:if>   						<!-- 관리자로 로그인 할 때만 보이는 메뉴 -->
            </ul>
        </li>

        <li class="menu_li">
            <a href="<%=cp %>/notice/list.do">Information</a>
            <ul class="menu_sub">
                <li><a href="<%=cp %>/notice/list.do">Notice/Event</a></li>
                <li><a href="<%=cp %>/qna/list.do">QnA</a></li>
                <li><a href="<%=cp %>/board/list.board">FreeBoard</a></li>
                <li><a href="<%=cp %>/map/showmap.do">Directions</a></li>      
            </ul>
        </li>   

    </ul>      
</div>
