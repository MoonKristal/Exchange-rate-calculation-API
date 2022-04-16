package com.wirebarley.codingtest.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateVo {
	
	//  계산을 위한 숫자형 환율변수
	private float usdKrw;
	private float usdJpy;
	private float usdPhp;
	
	// formatting된 계산결과를 넣을 문자형인 환율변수
	private String chosenNationRate;
	
}
