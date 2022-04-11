<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>환율 계산기</title>
<link rel="stylesheet" href="resources/css/exchangeRateCalculationView.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<div class="wrap">
		<h1>환율 계산</h1><br>
		<table align="center">
			<tr>
				<td>송금국가 : </td>
				<td>미국(USD)</td>
			</tr>
			<tr>
				<td>수취국가 : </td>
				<td>
					<select name="nation" id="nation">
						<option value="krw">한국(KRW)</option>
						<option value="jpy">일본(JPY)</option>
						<option value="php">필리핀(PHP)</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>환율 : </td>
				<td class="exchangeRate"></td>
			</tr>
			<tr>
				<td>송금액 : </td>
				<td><input type="text" name="remittance" id="remittance" onkeyup="enterKey();" maxlength="5" placeholder="1~10,000의 금액을 입력하세요."> USD</td>
			</tr>
			<tr>
				<td colspan="2"><button onclick="submit();" class="btn" disabled>Submit</button></td>
			</tr>
		</table>
		<div class="result">
			
		</div>
		<div class="condition" hidden>1~10,000사이의 값을 넣어주세요.</div>
	</div>
	
	<script>
		// 서버가 켜지자마자 보여줄 환율 정보 메소드 호출
		$(function(){
			searchExchangeRate();
		})
		// select의 값이 변경될 때마다 메소드호출로 환율 정보 변경
		$('#nation').on('change',function(){
			searchExchangeRate();
		})
		// 환율정보를 가져올 ajax
		function searchExchangeRate(){
			$.ajax({
				url : "getInfoRate.ex", // 요청을 보낼 url값
				data : {
					nation : $("#nation").val() // select의 value값 (사용자가 선택한 수취국가)
				}, success : function(result){
					var exchangeRate = ""; // 환율정보 변수 선언
					// 사용자가 선택한 수취국가의 정보 변수 대입
					if($("#nation").val() == "krw"){
						exchangeRate += result.strUsdKrw;
					} else if($("#nation").val() == "jpy"){
						exchangeRate += result.strUsdJpy;
					} else{
						exchangeRate += result.strUsdPhp;
					}
					// 해당 국가의 환율정보 띄워주기
					$(".exchangeRate").html(exchangeRate + " " + $("#nation").val().toUpperCase() + "/USD");
				}, error : function(){
					console.log("ajax 통신 실패");
				}
			})
		}
		// input태그에서 enter키를 누르면 바로 submit메소드 호출
		function enterKey(){
			if(window.event.keyCode == 13){
				$(function(){
					submit();
				})
			}
		}
		// ajax로 Controller에 환율계산요청
		function submit(){
			$.ajax({
				url : "ercApi.ex", // 요청을 보낼 url값
				data : {
					nation : $("#nation").val(), // select의 value값 (사용자가 선택한 수취국가)
					remittance : $("#remittance").val() // input의 value값 (사용자가 입력한 송금액)
				}, success : function(rate){
					// 계산된 수취금액 띄워주기
					$(".result").html("수취금액은 " + rate.exchangeResult + "입니다.");
				}, error : function(){
					console.log("ajax 통신 실패");
				}
			})
		}
		// 1~10000사이의 금액 입력
		$('#remittance').keyup(function(e){
			var inputVal = $(this).val(); // input태그에 입력한 value값
			$(this).val(inputVal.replace(/[^0-9]/gi,'')); // 0-9를 제외한 모든 문자(g:전역에서 검색, i:대소문자 구분x)를 입력할 경우 '' 공백으로 대체
			// input태그 value값을 1~10000으로 제한
			if(0 < inputVal && inputVal < 10001 ){
				$('.btn').attr('disabled', false); // 사이값이라면 버튼 활성화
				$('.condition').hide(); // 사이값이 아니라면 해당 문구가 보이지 않지만 show()메소드로 한번 보여졌다면 다시 감추기 위한 코드
			}else{
				// 사이값이 아니라면 버튼 비활성화 => 이미 html태그에서 비활성화로 설정해뒀지만 활성화로 변경됐다면 다시 비활성화로 바꾸기 위한 코드
				$('.btn').attr('disabled', true); 
				$('.condition').show(); // 사이값이 아니면 보여질 문구 노출
			}

		})
		





	</script>
	


</body>
</html>