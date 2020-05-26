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
<jsp:include page="/WEB-INF/views/layout/sns.jsp"></jsp:include>
<script type="text/javascript">


function checkInfo() {
	var f = document.memberForm;
	
	var val = f.memberId.value;
	val = val.trim();
	
	if(!val){
		alert("아이디를 입력하세요.");
		val.memberId.focus();
		return;
	}
	
	if(!/^[a-z][a-z0-9_]{4,9}$/i.test(val)){
		alert("아이디 형식 오류입니다.");
		val.memberId.focus();
		return;
	}
	
	val = f.memberPassword.value;
	val = val.trim();
	if(!val){
		alert("패스워드를 입력하세요.");
		val.memberPassword.focus();
		return;
	}
	if(!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(val)){
		alert("패스워드 형식 오류입니다.");
		val.memberPassword.focus();
		return;
	}
	
	if(val != f.memberPasswordCh.value){
		alert("패스워드가 일치하지 않습니다.");
		f.memberPasswordCh.focus();
		return;
	}
	
	val = f.memberName.value;
	val = val.trim();
	
	if(!val){
		alert("이름을 입력하세요.");
		f.memberName.focus();
		return;
	}
	
	val = f.memberBirth.value;
	val = val.trim();
	if(!val){
		alert("생년월일을 입력하세요.");
		f.memberBirth.focus();
		return;
	}
	// 생년월일 체크
	
	val = f.tel1.value;
	val = val.trim();
	
	if(!val){
		alert("전화번호를 입력하세요.");
		f.tel1.focus();
		return;
	}
	
	
	val = f.tel2.value;
	val = val.trim();
	if(!val){
		alert("전화번호를 입력하세요.");
		f.tel2.focus();
		return;
	}
	if(!/^(\d+)$/.test(val)){
		alert("숫자만 가능합니다.");
		f.tel2.focus();
		return;
	}
	
	val = f.tel3.value;
	val = val.trim();
	if(!val){
		alert("전화번호를 입력하세요.");
		f.tel3.focus();
		return;
	}
	if(!/^(\d+)$/.test(val)){
		alert("숫자만 가능합니다.");
		f.tel3.focus();
		return;
	}
	
	
	val = f.email1.value;
	
	if(!val){
		alert("이메일을 입력하세요.");
		f.email1.focus();
		return;
	}
	val = f.email2.value;
	
	if(!val){
		alert("이메일을 입력하세요.");
		f.email2.focus();
		return;
	}
	
	var mode = "${mode}";
	if(mode=="created"){
		f.action="<%=cp%>/memberSj/insertSubmit.mem";
	}else if(mode=="update"){
		f.action="<%=cp%>/memberSj/updateSubmit.mem";
	}
	
   
	
	f.submit()
}


function chooseEmail() {
	var f = document.memberForm;
	
	var str = f.selectEmail.value
	
	if(str != 'direct'){
		f.email2.value = str;
		f.email2.readOnly = true;
		f.email1.focus();
	}
	else{
		f.email2.value="";
		f.email2.readOnly=false;
		f.email1.focus();
	}
}

function deleteInfo() {
	var f = document.memberForm;
	
	var b = confirm("탈퇴하시겠습니까?");
	if(b){
		f.action="<%=cp%>/memberSj/delete.mem";
		
		f.submit();
	}

}

</script>

<style type="text/css">
td{
	padding-bottom: 15px;
}
</style>

</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="info-title">| ${subject} <span style="color: red; font-size: 15px; font-family: '굴림'">${msg}</span></div>
<div>
	<form name="memberForm" method="post">
		<table style="border-top : 1px solid white; margin: 30px auto; width: 810px; padding-top:40px;">
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">아이디</td>
				<td width="250" valign="top"
					style="text-align: left; padding-top: 5px;"><input
					type="text" name="memberId" id="memberId" value="${dto.id}" style="height: 25px;"> <span style="color: white; font-weight: bold;">&nbsp;&nbsp;아이디는
						5~10자 이내이며, 첫 글자는 영문자로 시작해야 합니다.</span></td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">패스워드</td>
				<td width="250" valign="top"
					style="text-align: left; padding-top: 5px;"><input
					type="password" name="memberPassword" id="memberPassword" value="${dto.password}" style="height: 25px;">
					<span style="color: white; font-weight: bold;">&nbsp;&nbsp;5~10이내, 하나이상의 숫자 or 특수문자가 포함되어야 합니다.</span></td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">패스워드 확인</td>
				<td width="100" valign="top"
					style="text-align: left; padding-top: 5px;"><input
					type="password" name="memberPasswordCh" id="memberPasswordCh"
					value="${ dto.password}" style="height: 25px;"> 	<span style="color: white; font-weight: bold;">&nbsp;&nbsp;패스워드를 한번 더 입력해주세요.</span></td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">이름</td>
				<td width="100" valign="top"
					style="text-align: left; padding-top: 5px;"><input
					type="text" name="memberName" id="memberName" value="${dto.name}" style="height: 25px;"> 	<span style="color: white; font-weight: bold;">&nbsp;&nbsp;예)
						노현호</span></td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">생년월일</td>
				<td style="text-align: left;"><input type="text" name="memberBirth" id="memberBirth"
					value="${dto.birth}" style="height: 25px;"> 	<span style="color: white; font-weight: bold;">&nbsp;&nbsp;생년월일은 2000-01-01 형식으로 입력하세요.</span></td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">이메일</td>
				<td width="100" valign="top"
					style="text-align: left; padding-top: 5px;">
					<select onchange="chooseEmail();"name="selectEmail" style="width: 100px; height: 25px;">
						<option value="">선택</option>
						<option value="naver.com" ${email2 == "naver.com" ? "selected='selected='":""}>네이버</option>
						<option value="gmail.com" ${email2 == "gmail.com" ? "selected='selected='":""}>구글</option>
						<option value="daum.net" ${email2 == "daum.net" ? "selected='selected='":""}>다음</option>
						<option value="hanmail.com" ${email2 == "hanmail.com" ? "selected='selected='":""}>한메일</option>
						<option value="direct">직접입력</option>
				</select> <input type="text" name="email1" value="${ema1}" size="13" maxlength="30"
					class="boxTF" style="margin-left: 10px;"> <span style="color: white;font-weight: bold;">@</span> <input type="text" name="email2" value="${ema2}"
					size="13" maxlength="30" class="boxTF" readonly="readonly">
				</td>
			</tr>
			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold; font-size: 18px;">전화번호</td>
				<td width="100" valign="top"
					style="text-align: left; padding-top: 5px;">
					<select name="tel1" style="width: 100px; height: 25px;">
						<option value="">선택</option>
						<option value="010"${tel1 == "010" ? "selected='selected='":""}>010</option>
						<option value="011" ${tel1 == "011" ? "selected='selected='":""}>011</option>
						<option value="016" ${tel1 == "016" ? "selected='selected='":""}>016</option>
						<option value="017" ${tel1 == "017" ? "selected='selected='":""}>017</option>
						<option value="018" ${tel1 == "018" ? "selected='selected='":""}>018</option>
						<option value="019" ${tel1 == "019" ? "selected='selected='":""}>019</option>
				</select><span style="color: white;font-weight: bold;">&nbsp;-&nbsp;</span><input type="text" name="tel2" value="${tel2}" class="boxTF"
					maxlength="4" style="width: 105px;"> <span style="color: white;font-weight: bold;">&nbsp;-&nbsp;</span> <input type="text" name="tel3" value="${tel3}"
					class="boxTF" maxlength="4" style="width: 105px;"></td>
			</tr>

			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold;">
					<label
					style="font-weight: 900; font-size: 18px;">우편번호</label></td>
				<td style="padding: 0 0 15px 0px; text-align: left;">
					<p style="margin-top: 1px; margin-bottom: 5px;">
						<input type="text" name="zip" id="zip" value="${dto.addr1}" class="boxTF"
							readonly="readonly" style="height: 25px;">
						<button type="button" class="btn" onclick="daumPostcode();" style="height: 35px;">우편번호</button>
					</p>
				</td>
			</tr>

			<tr>
				<td width="100" valign="top"
					style="text-align: center; padding-top: 5px; color: white; font-weight: bold;"><label
					style="font-weight: 900; font-size: 18px;">주소</label></td>
				<td style="padding: 0 0 15px 0px;">
					<p style="margin-top: 1px; margin-bottom: 5px;">
						<input type="text" name="addr1" id="addr1" value="${dto.addr1}" maxlength="50"
							class="boxTF" style="width: 95%;" placeholder="기본 주소"
							readonly="readonly"style="height: 25px;">
					</p>
					<p style="margin-bottom: 5px;">
						<input type="text" name="addr2" id="addr2" value="${dto.addr2}" maxlength="50"
							class="boxTF" style="width: 95%;" placeholder="나머지 주소"style="height: 25px;">
					</p>
				</td>
			</tr>
		</table>
		
		<table style="margin: auto;">
			<tr>
				<td style="text-align: center;">
					<button type="button" name="sendInfo" onclick="checkInfo();" style="width: 100px; height:35px; border-radius: 5px; border: none; 
					">${mode == "update" ? "수정하기":"가입하기"}</button>&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="reset" style=" padding:6px; height:35px;border-radius: 5px; border: none; width: 100px;">다시입력</button>&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" onclick="javascript:location.href='<%=cp%>/';"  style="width: 100px; padding:6px; height:35px;border-radius: 5px; border: none;"
					>뒤로가기</button>
					<c:if test="${mode=='update'}">
					<button type="button" onclick="deleteInfo();" style="width: 100px; margin-left:15px;  padding:6px; height:35px;border-radius: 5px; border: none;">
					탈퇴하기
					</button>
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</div>
	<div class="footer" style="margin-top : 20px;">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

<script src="http://dmaps.daum.ndt/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('addr1').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('addr2').focus();
            }
        }).open();
    }
</script>   

</body>
</html>