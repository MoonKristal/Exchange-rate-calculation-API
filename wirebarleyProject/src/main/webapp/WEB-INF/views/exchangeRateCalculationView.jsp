<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>환율 계산기</title>
<link rel="stylesheet" href="resources/css/exchangeRateCalculationView.css">
</head>
<body>
	<div class="wrap">
		<h1>환율 계산</h1><br>
		<div class="calculation">
			송금국가 : 미국(USD) <br>
			수취국가 : <select>
				<option value="krw">한국(KRW)</option>
				<option value="jpy">일본(JPY)</option>
				<option value="php">필리핀(PHP)</option>
			</select><br>
			환율 : 1,119.93 KRW/USD <br>
			송금액 : <input type="text" name="remittance"> USD <br>
			<button type="submit">Submit</button>
		</div>
		<br>
		<div class="result">
			수취금액은 111,993.00 KRW 입니다.
		</div>
	</div>


</body>
</html>