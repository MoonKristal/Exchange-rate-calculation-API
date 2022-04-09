package com.wirebarley.codingtest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wirebarley.codingtest.vo.RateVo;

@Controller
public class exchangeRateCalculationController {
	
	// 값 변경이 없을 것이기 때문에 상수로 처리
	public static final String ACCESS_KEY = "b5d7ba68bebeb848d3430b4373623d0f";
	
	/**
	 * api연결 후 실시간 환율정보 받기
	 * @return
	 * @throws IOException
	 */
	public RateVo connectApi() throws IOException {
		
		// 가독성을 위해 url분리
		String url = "http://apilayer.net/api/live";
		url += "?access_key=" + ACCESS_KEY; // API 액세스 키
		url += "&currencies=KRW,JPY,PHP"; // 변경할 통화 지정
		url += "&source=USD"; // 기본 통화 지정(USD)
		url += "&format=1"; // JSON형식으로 formatting
		
		// 1. 요청하고자하는 url을 전달하면서 java.net.URL 객체 생성
		URL requestUrl = new URL(url);
		// 2. 1번과정으로 생성된 URL객체를 사용해 HttpConnection 객체 생성
		// openConnection의 반환형이 URLConnection이기 때문에 HttpURLConnection에 담기위해 다운캐스팅
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection(); 
		// 3. 요청에 필요한 Header설정
		urlConnection.setRequestMethod("GET");
		// 4. 해당 OpenAPI서버로 요청을 보낸 후 입력데이터로 읽어오기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); 
		
		String responseText = "";
		String line;
		while((line = br.readLine()) != null) {
			responseText += line;
		}
		
		// 각각의 quotes정보를  rateVO객체에 담기
		JsonObject totalObj = JsonParser.parseString(responseText).getAsJsonObject();
		JsonObject quotesObj = totalObj.getAsJsonObject("quotes"); // quotes속성에 접근 : {} JsonObject
		
		RateVo rate = new RateVo();
		rate.setUsdKrw(quotesObj.get("USDKRW").getAsFloat());
		rate.setUsdJpy(quotesObj.get("USDJPY").getAsFloat());
		rate.setUsdPhp(quotesObj.get("USDPHP").getAsFloat());
		
		// 5. 사용완료한 스트림 반납
		br.close();
		urlConnection.disconnect();

		return rate;
	}
	
	
	/**
	 * jsp파일에서 ajax로 요청한 환율계산
	 * @param remittance
	 * @param nation
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "exchangeApi.ex", produces="application/json; charset=UTF-8")
	public String ercApi(float remittance, String nation, HttpServletResponse response) throws IOException {
		
		// api연결 후 실시간 환율정보 받기
		RateVo rate = connectApi();
		
		// 소숫점 2째자리까지 표현하기 위해 float형 변수 선언
		float exchangeResult = 0;
		
		// 사용자가 선택한 수취국가에따라 수취금액 계산 => 
		if(nation.equals("krw")) {
			exchangeResult = rate.getUsdKrw() * remittance;
		} else if(nation.equals("jpy")) {
			exchangeResult = rate.getUsdJpy() * remittance;
		} else {
			exchangeResult = rate.getUsdPhp() * remittance;
		}
		
		DecimalFormat df = new DecimalFormat("#,###.00");
		
		rate.setExchangeResult(df.format(exchangeResult));
		
//		response.getWriter().print(formatResult);
		return new Gson().toJson(rate);
	}

}
