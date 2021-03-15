package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Future;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * @Aspect 注解描述的类型为一个切面类型(AOP中的横切面类型),这样的切面类型
 * 中通常会定义两部分内容：
 * 1)切入点:切入扩展功能的点(例如业务对象中的一个或多个方法)
 * 2)通知：在切点对应的方法执行时，要织入的扩展功能。
 * @author qilei
 */
//@Order(1)
@Slf4j
@Aspect
@Component
public class SysLogAspect {
	/**
	 * @Pointcut 注解一般用于描述方法，在方法上定义切入点。
	 * bean(sysUserServiceImpl) 为一个切入点biao'da'shi
	 */
	 //@Pointcut("bean(sysUserServiceImpl)")
	 @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
     public void doPointCut() {}//方法内部不需要写任何内容
	 
	 @Around("doPointCut()")
	 public Object around(ProceedingJoinPoint jp)throws Throwable{
		 System.out.println("SysLogAspect.around");
		 //记录方法执行时的开始时间
		 long t1=System.currentTimeMillis();
		 try {
		 //调用目标方法
		 Object result=jp.proceed();//调用本切面中其它通知或下一个切面的通知或目标方法
		 //记录方法执行的的结束时间以及总时长。
		 long t2=System.currentTimeMillis();
		 log.info("method execute time {}",(t2-t1));
		 //将用户的正常行为信息写入到数据库
		 saveLog(jp,t2-t1);
		 return result;
		 }catch(Throwable e) {
		 //出现异常时还要输出错误日志。
		 log.error("error is {} ",e.getMessage());
		 throw e;
		 }
	 }
	 
	 @Autowired
	 private SysLogService sysLogService;
	 private void saveLog(ProceedingJoinPoint jp,long time)throws Exception {
		 //1.获取用户行为数据
		 //获取目标方法所在类的类型
		 Class<?> targetCls=jp.getTarget().getClass();
		 String targetClsName=targetCls.getName();
		 Signature s=jp.getSignature();
		 MethodSignature ms=(MethodSignature) s;
		 //获取操作名operation
		 Method targetMethod=
		 targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
		 RequiredLog requiredLog=targetMethod.getAnnotation(RequiredLog.class);
		 String operation="operation";
		 if(requiredLog!=null)operation=requiredLog.operation();
		 //获取目标类中的目标方法信息
		 String targetClsMethodName=targetClsName+"."+ms.getName();
		 //获取调用目标方法时传递的参数
		 //String params=Arrays.toString(jp.getArgs());
		 String params=new ObjectMapper().writeValueAsString(jp.getArgs());
		 //2.封装用户行为数据
		 SysLog entity=new SysLog();
		 entity.setIp(IPUtils.getIpAddr());
		 entity.setUsername(ShiroUtils.getUsername());//登陆以后获取登陆用户名
		 entity.setOperation(operation);
		 entity.setMethod(targetClsMethodName);
		 entity.setParams(params);
		 entity.setTime(time);
		 entity.setCreatedTime(new Date());
		 //3.保存用户行为数据(写入到数据库)
//		 new Thread() {//1M
//			 public void run() {
//				 sysLogService.saveObject(entity);
//			 };
//		 }.start();
		 sysLogService.saveObject(entity);
	 }
	 
}






