package com.wirebarley.codingtest.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wirebarley.codingtest.model.vo.RateVo;
import com.wirebarley.codingtest.model.vo.ResultVo;

@Service
public class ExchangeRateCalculationService {
	
	@Autowired(required=false)
	private ResultVo result;
	
	// 값 변경이 없을 것이기 때문에 상수로 처리
	public static final String ACCESS_KEY = "620d5b120478987cd52599ff98826de1";
	
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
	
	
	public RateVo getInfoRate(String nation) throws IOException {
		// api연결 후 실시간 환율정보 받기
		RateVo rate = connectApi();
		
		// formatting을 위한 객체 생성(세자리마다','와 소수점아래 두번째자리까지 표시) => format메소드사용
		DecimalFormat df = new DecimalFormat("#,###.00");
		
		String chosenNation = (nation.equals("krw"))? "krw" : nation.equals("jpy")? "jpy" : "php";
		
		rate.setChosenNation(chosenNation); 
		
		// 사용자가 선택한 수취국가에 따라 각 나라의 환율 formatting
		if(nation.equals("krw")) {
			// connectApi메소드에서 가져온 환율정보 formatting -> String형 변수에 setter사용해서 값 저장(format메소드는 String형으로 반환-> ','와 '.'가 포함되어 있기 때문에)
			rate.setStrUsdKrw(df.format(rate.getUsdKrw())); 
		} else if(nation.equals("jpy")) {
			rate.setStrUsdJpy(df.format(rate.getUsdJpy()));
		} else {
			rate.setStrUsdPhp(df.format(rate.getUsdPhp()));
		}
		
		return rate;
	}
	
	
	public ResultVo ercApi(float remittance, String nation, HttpServletResponse response) throws IOException {
		
		// api연결 후 실시간 환율정보 받기
		RateVo rate = connectApi();
		
		// 소숫점 2째자리까지 표현하기 위해 float형 변수 선언
		float exchangeResult = 0;
		
		// exchangeResult에 connectApi에서 가져온 각 나라의 환율 저장
		if(nation.equals("krw")) {
			exchangeResult = rate.getUsdKrw();
		} else if(nation.equals("jpy")) {
			exchangeResult = rate.getUsdJpy();
		} else {
			exchangeResult = rate.getUsdPhp();
		}
		
		// exchangeResult에 있는 환율과 사용자에게 입력받은 송금액의 합계계산(수취금액)후 값 저장
		exchangeResult *= remittance;
		
		// formatting을 위한 객체 생성(세자리마다','와 소수점아래 두번째자리까지 표시) => format메소드사용
		DecimalFormat df = new DecimalFormat("#,###.00");
		
		// 수취금액 formatting후 사용자가 선택한 수취국가를 더해 String형 변수에 setter를 사용해서 값 저장
		result.setExchangeResult(df.format(exchangeResult) + " " + nation.toUpperCase());
		
		return result;
	}
	
}
