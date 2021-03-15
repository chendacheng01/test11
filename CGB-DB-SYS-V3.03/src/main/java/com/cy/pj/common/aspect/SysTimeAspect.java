package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@Aspect
public class SysTimeAspect {
	   @Pointcut("bean(sysUserServiceImpl)")
	   public void doTime() {}
	   
	   @Before("doTime()")
	   public void doBefore() {
		   System.out.println("@Before");//2
	   }
	   @After("doTime()")
	   public void doAfter() {//3
		   System.out.println("@After");
	   }
	   @AfterReturning("doTime()")
	   public void doAfterReturning() {
		   System.out.println("@AfterReturning");
	   }
	   @AfterThrowing("doTime()")
	   public void doAfterThrowing() {
		   System.out.println("@AfterThrowing");
	   }
	   /**
	    * 环绕通知
	    * @param jp 连接点对象，但ProceedingJoinPoint对象只能应用于环绕通知方法参数
	    * @return
	    * @throws Throwable
	    */
	   @Around("doTime()")
	   public Object doAround(ProceedingJoinPoint jp)throws Throwable{
		   System.out.println("SysTimeAspect.around.before");//1
		   try {
		   Object result=jp.proceed();
		   System.out.println("SysTimeAspect.around.after");
		   return result;
		   }catch (Throwable e) {
		   System.out.println("SysTimeAspect.around.exception");
		   throw e;
		   }
	   }
}