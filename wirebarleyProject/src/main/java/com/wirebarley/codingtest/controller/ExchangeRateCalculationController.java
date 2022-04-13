package com.wirebarley.codingtest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wirebarley.codingtest.model.service.ExchangeRateCalculationService;
import com.wirebarley.codingtest.model.vo.RateVo;
import com.wirebarley.codingtest.model.vo.ResultVo;

@Controller
public class ExchangeRateCalculationController {
	
	@Autowired
	private ExchangeRateCalculationService exchangeRateCalculationService;
	
	
	/**
	 * 선택된 수취국가에 따른 환율정보
	 * @param nation
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getInfoRate.ex", produces="application/json; charset=UTF-8")
	public String getInfoRate(String nation) throws IOException {
		// api연결 후 실시간 환율정보 받기
		RateVo rate = exchangeRateCalculationService.getInfoRate(nation);
		
		return new Gson().toJson(rate);
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
	@RequestMapping(value = "ercApi.ex", produces="application/json; charset=UTF-8")
	public String ercApi(float remittance, String nation, HttpServletResponse response) throws IOException {
		
		ResultVo result = exchangeRateCalculationService.ercApi(remittance, nation, response);
		
		return new Gson().toJson(result);
	}
	
	
	
	

}
