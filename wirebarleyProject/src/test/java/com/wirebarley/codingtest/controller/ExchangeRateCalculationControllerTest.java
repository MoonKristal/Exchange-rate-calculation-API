package com.wirebarley.codingtest.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wirebarley.codingtest.model.vo.RateVo;
import com.wirebarley.codingtest.model.vo.ResultVo;
import com.wirebarley.codingtest.service.ExchangeRateCalculationService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateCalculationControllerTest {

    @Autowired
    private ExchangeRateCalculationService exchangeRateCalculationService;
//    @Autowired
//    private MockMvc mockMvc; // mockMvc 생성
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
    
    @DisplayName("api연결 테스트")
    @Test
    @Order(1)
    void connectApi() throws Exception {
        RateVo rate = exchangeRateCalculationService.connectApi();
        Assertions.assertNotNull(rate);
    }

    @DisplayName("환율정보 조회 테스트")
    @Test
    @Order(2)
    void getInfoRate() throws Exception {
		RateVo rate = exchangeRateCalculationService.getInfoRate("krw");
		System.out.println(rate);
		Assertions.assertNotNull(rate);
    }

    @DisplayName("수취액 계산 테스트")
    @Test
    @Order(3)
    void ercApi() throws Exception {
    	ResultVo result = exchangeRateCalculationService.ercApi(1000.00f, "krw");
    	Assertions.assertNotNull(result);
    }
}