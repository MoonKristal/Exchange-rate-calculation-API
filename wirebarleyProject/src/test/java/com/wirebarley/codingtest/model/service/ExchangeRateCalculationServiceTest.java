package com.wirebarley.codingtest.model.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wirebarley.codingtest.model.vo.RateVo;
import com.wirebarley.codingtest.model.vo.ResultVo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateCalculationServiceTest {

	// 전체 실행시 응답결과를 받기 위해 Thread.spleep()를 넣은 setUp()메소드를 만들었습니다.
	// 만약 setUp()메소드가 실행되지 않는다면 API응답결과가 NullPointException이 발생하기 때문에 개별 단위테스트로 진행해주세요.
	
    @Autowired
    private ExchangeRateCalculationService exchangeRateCalculationService;

    @BeforeEach
    void setUp(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
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