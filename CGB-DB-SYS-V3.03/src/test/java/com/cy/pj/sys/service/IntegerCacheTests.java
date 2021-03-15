package com.cy.pj.sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegerCacheTests {

	@Test
	public void testIntegerCache() {
		Integer t1=100;//Integer.valueOf(100)
		Integer t2=100;
		//对于Integer类有一个整数池(-128~+127)
		System.out.println(t1==t2);//true
		Integer t3=200;//Integer.valueOf(100);
		Integer t4=200;
		System.out.println(t3==t4);//false
	}
	@Test
	public void testLongCache() {
		Long t1=100L;//Long.valueOf(100)
		Long t2=100L;
		//对于Long类有一个整数池(-128~+127)
		System.out.println(t1==t2);//true
		Long t3=200L;//Long.valueOf(100);
		Long t4=200L;
		System.out.println(t3==t4);//false
	}
}
