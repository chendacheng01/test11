package com.cy.pj.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
/**
 * 泛型中的限定通配符
 * 1)? 无界限定通配符
 * 2)? extends 上届限定通配符
 * 3)? super   下届限定通配符
 * @author qilei
 *
 */
@SpringBootTest
public class GenericTests {

	static void doPrint(List<? extends Object> list) {
		
	}
	static void doSort(List<? super String> list) {
		
	}
	
	public static void main(String[] args) {
		List<String> list=Arrays.asList("A","B");
		doPrint(list);//List<Object> lst=new ArrayList<String>();
		
		List<Object> list2=new ArrayList<>();
		list2.add("ABC");
		list2.add("DEFMNL");
		doSort(list2);
		
		
	}
}
