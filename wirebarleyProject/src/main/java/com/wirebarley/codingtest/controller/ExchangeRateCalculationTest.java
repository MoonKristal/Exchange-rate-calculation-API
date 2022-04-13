package com.wirebarley.codingtest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.wirebarley.codingtest.model.service.ExchangeRateCalculationService;
import com.wirebarley.codingtest.model.vo.RateVo;
import com.wirebarley.codingtest.model.vo.ResultVo;

public class ExchangeRateCalculationTest {
	
	@Autowired
	private MockMvc mockMvc; // mockMvc 생성
	
	@Autowired
	private ExchangeRateCalculationService exchangeRateCalculationService;
	
	@Autowired
	private Gson gson;
	
	@Test
	public void testController() throws Exception {
		// 메인화면으로 이동
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}
	
	@Test
	@DisplayName("환율정보 조회 테스트")
	public void testGetInfoRate() throws Exception {
		
		int ranNum = (int)(Math.random()*3)+1;
		
		String nation = (ranNum == 1)? "krw" : ((ranNum == 2)? "jpy" : "php");
		
		RateVo rate = exchangeRateCalculationService.getInfoRate(nation);
		
		String jsonRate = gson.toJson(rate);
		
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonRate));
		
	}
	
	@Test
	@DisplayName("수취액 계산 테스트")
	public void testCalculation(HttpServletResponse response) throws Exception {
		
		int remittance = (int)(Math.random()*10000)+1;
		
		int ranNum = (int)(Math.random()*3)+1;
		
		String nation = (ranNum == 1)? "krw" : ((ranNum == 2)? "jpy" : "php");

		ResultVo result = exchangeRateCalculationService.ercApi(remittance, nation, response);
		
		String jsonResult = gson.toJson(result);
		
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonResult));
		
	}
	
	
}
