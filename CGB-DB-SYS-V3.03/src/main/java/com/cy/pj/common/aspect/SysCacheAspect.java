package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.ClearCache;
import com.cy.pj.common.annotation.RequiredCache;

@Aspect
@Component
public class SysCacheAspect {
	
	private Map<Object,Object> cache=new ConcurrentHashMap<>();
	
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	public void doCache() {}
	
	@Pointcut("@annotation(com.cy.pj.common.annotation.ClearCache)")
	public void doClear() {}
	
	/**业务正常结束会执行
	 * @throws SecurityException 
	 * @throws NoSuchMethodException */
	@AfterReturning("doClear()")
	public void doAfterReturning(JoinPoint jp) throws NoSuchMethodException, SecurityException {
		//获取目标方法对象
		Class<?> targetCls=jp.getTarget().getClass();
		MethodSignature ms=(MethodSignature) jp.getSignature();
		//Method targetMethod=ms.getMethod();(代理方式不同结果也不相同，JDK代理获取的是接口方法)
		Method targetMethod=
		targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
		System.out.println("targetMethod="+targetMethod);
		//获取目标方法对象上的注解
		ClearCache requiredCache=
				targetMethod.getAnnotation(ClearCache.class);
		Object key=requiredCache.key();
		System.out.println("doAfterReturning.Cache.key="+key);
		//cache.clear();
		cache.remove(key);
	}
	@Around("doCache()")
	public Object doAround(ProceedingJoinPoint jp)throws Throwable{
		
		//获取目标方法对象
		Class<?> targetCls=jp.getTarget().getClass();
		MethodSignature ms=(MethodSignature) jp.getSignature();
		//Method targetMethod=ms.getMethod();(代理方式不同结果也不相同，JDK代理获取的是接口方法)
		Method targetMethod=
		targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
		System.out.println("targetMethod="+targetMethod);
		//获取目标方法对象上的注解
		RequiredCache requiredCache=
		targetMethod.getAnnotation(RequiredCache.class);
		Object key=requiredCache.key();
		System.out.println("Cache.key="+key);
		//==========================================
		System.out.println("Get Data from Cache");
		Object result=cache.get(key);
		if(result!=null)return result;
		result=jp.proceed();
		System.out.println("Put Data to Cache");
		cache.put(key, result);
		
		return result;
	}
}
