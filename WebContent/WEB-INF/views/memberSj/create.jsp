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
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/board.css"
	type="text/css">
<script type="text/javascript"></script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="info-title">| 모드를 넣자</div>

	<form name="memberForm" method="post">
		<table>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="memberId" id="memberId"
					value="${dto값을 넣자}" ${모드를 넣자}> <span>아이디는 5~10자
						이내이며, 첫 글자는 영문자로 시작해야 합니다.</span></td>
			</tr>
			<tr>
				<td>패스워드</td>
				<td><input type="password" name="memberPassword"
					id="memberPassword" value="${dto값을 넣자}" ${모드를 넣자}> <span>아이디는
						5~10자 이내이며, 첫 글자는 영문자로 시작해야 합니다.</span></td>
			</tr>
			<tr>
				<td>패스워드 확인</td>
				<td><input type="password" name="memberPasswordCh"
					id="memberPasswordCh" value="${dto값을 넣자}" ${모드를 넣자}> <span>패스워드를
						한번 더 입력해주세요.</span></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="memberName" id="memberName"
					value="${dto값을 넣자}" ${모드를 넣자}> <span>예) 정우진</span></td>
			</tr>
			<tr>
				<td>생년월일</td>
				<td><input type="text" name="memberBirth" id="memberBirth"
					value="${dto값을 넣자}" ${모드를 넣자}> <span>생년월일은
						2000-01-01 형식으로 입력하세요.</span></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><select>
						<option value="">선택</option>
						<option value="naver.com"
							${dto.email2 == "naver.com" ? "selected='selected='":""}>네이버</option>
						<option value="gmail.com"
							${dto.email2 == "gmail.com" ? "selected='selected='":""}>구글</option>
						<option value="daum.net"
							${dto.email2 == "daum.net" ? "selected='selected='":""}>다음</option>
						<option value="hanmail.com"
							${dto.email2 == "hanmail.com" ? "selected='selected='":""}>한메일</option>
						<option value="direct">직접입력</option>
				</select></td>
			</tr>
			<tr>
				<td>전화번호</td>
				<td>
				<select>
						<option value="">선택</option>
						<option value="010"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>010</option>
						<option value="011"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>011</option>
						<option value="016"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>016</option>
						<option value="017"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>017</option>
						<option value="018"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>018</option>
						<option value="019"
							${dto.tel1 == "naver.com" ? "selected='selected='":""}>019</option>
				</select>
				 - <input type="text" name="tel2" value="${dto.tel2}" class="boxTF" maxlength="4">
				 - <input type="text" name="tel3" value="${dto.tel3}" class="boxTF" maxlength="4">
				 </td>
			</tr>
			
			<tr>
				<td>우편번호</td>
				<td><input type="text" name="memberId" id="memberId"
					value="${dto값을 넣자}" ${모드를 넣자}> <span>아이디는 5~10자
						이내이며, 첫 글자는 영문자로 시작해야 합니다.</span></td>
			</tr>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="memberId" id="memberId"
					value="${dto값을 넣자}" ${모드를 넣자}> <span>아이디는 5~10자
						이내이며, 첫 글자는 영문자로 시작해야 합니다.</span></td>
			</tr>
			<tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">우편번호</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="zip" id="zip" value="${dto.zip}"
			                       class="boxTF" readonly="readonly">
			            <button type="button" class="btn" onclick="daumPostcode();">우편번호</button>          
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">주소</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="addr1" id="addr1" value="${dto.addr1}" maxlength="50" 
			                       class="boxTF" style="width: 95%;" placeholder="기본 주소" readonly="readonly">
			        </p>
			        <p style="margin-bottom: 5px;">
			            <input type="text" name="addr2" id="addr2" value="${dto.addr2}" maxlength="50" 
			                       class="boxTF" style="width: 95%;" placeholder="나머지 주소">
			        </p>
			      </td>
			  </tr>

		</table>
	</form>


	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>


</body>
</html>