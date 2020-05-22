<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<link rel="stylesheet" href="<%=cp%>/resource/css/foodcreated.css" type="text/css">

<script type="text/javascript">
    function sendFood() {
        var f = document.FoodMenuForm;

    	var str = f.f_name.value;
        if(!str) {
            alert("메뉴를 입력하세요.");
            f.f_name.focus();
            return;
        }
        
        var str = f.f_price.value;
        if(!str) {
            alert("가격을 입력하세요.");
            f.f_price.focus();
            return;
        }

        str = f.f_type.value;
    	str = str.trim();
        if(!str) {
            alert("종류를 선택해주세요 ");
            f.f_type.focus();
            return;
        }
        
    	str = f.f_intro.value;
        if(!str) {
            alert("설명을 입력하세요.");
            f.f_intro.focus();
            return;
        }
        
        str = f.f_food.value;
        if(!str) {
        	alert("사진을 첨부해주세요.");
        	f.f_food.focus();
        	return;
        }
        
        f.action="<%=cp%>/foodmenu/${mode}_ok.do?f_num=${dto.f_num}";
        
        f.submit();
	}
    
    <c:if test="${mode == 'update'}">
		function deleteFile(f_num) {
			var url = "<%=cp%>/foodmenu/deleteFile.do?f_num=" + f_num + "&page=${page}";
			location.href = url;
		}
	</c:if>
</script>
</head>
<body>
	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
	<div class="container">
		<div class="body-container">
			<div class="body_title">
				<h3>| Registration</h3>
			</div>
			
			<div class="menu">
				<form name="FoodMenuForm" method="post" enctype="multipart/form-data">
					<table class="menu_list">
						<tr class="title">
							<td class="name">메&nbsp;&nbsp;&nbsp;뉴</td>
							<td class="write">
								<input type="text" name="f_name" value="${dto.f_name}">
							</td>
						</tr>
						<tr class="title">
							<td class="name">가&nbsp;&nbsp;&nbsp;격</td>
							<td class="write">
								<input type="text" name="f_price" value="${dto.f_price}">
							</td>
						</tr>
						<tr class="title">
							<td class="name">종&nbsp;&nbsp;&nbsp;류</td>
							<td class="write">
								<select name="f_type">
									<option value="">종류선택</option>
									<option value="main" ${dto.f_type == "main" ? "selected='selected'" : ""}>Steak</option>
									<option value="side" ${dto.f_type == "side" ? "selected='selected'" : ""}>Side</option>
									<option value="desserts" ${dto.f_type == "desserts" ? "selected='selected'" : ""}>Desserts</option>
									<option value="others" ${dto.f_type == "others" ? "selected='selected'" : ""}>Others</option>
								</select>
							</td>
						</tr>
						<tr class="title">
							<td class="name">설&nbsp;&nbsp;&nbsp;명</td>
							<td class="write">
								<textarea rows="12" name="f_intro">${dto.f_intro}</textarea>
							</td>
						</tr>
						<tr class="title">
							<td class="name">이미지</td>
							<td class="write">
								<input type="file" name="f_food">
							</td>
						</tr>
						
						<c:if test="${mode == 'update'}">
							<tr class="title">
								<td class="name">첨부 된 파일</td>
								<td class="image_delete">
									${dto.f_image}
										| <a href="javascript:deleteFile('${dto.f_num}');">삭제</a>
								</td>
							</tr>
						</c:if>
					</table>
					<table class="btn_box">
						<tr>
							<td>
								<button type="button" class="btn" onclick="sendFood();">${mode == 'update' ? '수정완료' : '등록하기'}</button>
								<button type="reset" class="btn">다시입력</button>
			        			<button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/foodmenu/list.do';">${mode == 'update' ? '수정취소' : '등록취소'}</button>
			         			
			         			<c:if test="${mode == 'update'}">
                             		<input type="hidden" name="f_num" value="${dto.f_num}">
                            		<input type="hidden" name="f_image" value="${dto.f_image}">
									<input type="hidden" name="page" value="${page}">
			        			</c:if>
			      			</td>
			    		</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>