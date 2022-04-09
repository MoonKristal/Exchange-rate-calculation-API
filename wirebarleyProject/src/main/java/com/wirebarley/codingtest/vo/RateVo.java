package com.wirebarley.codingtest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateVo {
	
	private float usdKrw;
	private float usdJpy;
	private float usdPhp;
	
	private String exchangeResult;
	
}
