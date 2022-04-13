package com.wirebarley.codingtest.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateVo {
	
	// 숫자형인 계산을 위한 환율변수
	private float usdKrw;
	private float usdJpy;
	private float usdPhp;
	
	private String chosenNation;
	
	// 문자형인 환율변수
	private String strUsdKrw;
	private String strUsdJpy;
	private String strUsdPhp;
	
}
