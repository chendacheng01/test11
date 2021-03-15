package com.cy.pj.sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoxClassTests {

	  @Test
	  public void testIntegerLong() {
		  int a1=100;
		  Integer a2=a1;//自定封箱
		  long l1=a1;
		  //Long l2=a1;//int类型不能自动封箱为Long
	  }
}
