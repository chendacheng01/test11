package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class SysExceptionAspect {
	   /**
	    * 通过如下方法进行异常信息的记录，但不会截断异常。
	    * @param ex 要与@AfterThrowing注解中throwing属性的值相同。
	    * @throws SecurityException 
	    * @throws NoSuchMethodException 
	    */
	   @AfterThrowing(value="bean(sysUserServiceImpl)",throwing = "ex")
	   public void doLogException(JoinPoint jp,Exception ex) throws NoSuchMethodException, SecurityException {
		   //获取目标方法签名
		   MethodSignature ms=(MethodSignature)jp.getSignature();
		   //String methodName=ms.getDeclaringTypeName()+"."+ms.getName();
		   Class<?> targetClass=jp.getTarget().getClass();
		   //Method targetMethod=
		   //targetClass.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
		   String methodName=targetClass.getName()+"."+ms.getName();
		   log.error("SysExceptionAspect---> {}'s exception {} ",methodName,ex.getMessage());
	   }
}


