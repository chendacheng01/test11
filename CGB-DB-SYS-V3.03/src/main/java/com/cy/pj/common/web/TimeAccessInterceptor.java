package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import com.cy.pj.common.exception.ServiceException;

public class TimeAccessInterceptor implements HandlerInterceptor {
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
    	System.out.println("===preHandle===");
    	//获取当前时间的日历对象
    	Calendar c=Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY,6);
    	c.set(Calendar.MINUTE,0);
    	c.set(Calendar.SECOND,0);
    	long start=c.getTimeInMillis();
    	c.set(Calendar.HOUR_OF_DAY, 23);
    	long end=c.getTimeInMillis();
    	long current=System.currentTimeMillis();
    	if(current<start||current>end)
    		throw new ServiceException("不在访问时间:6:00~23:00");//false
		return true;//true表示放行(去执行handler方法),false表示请求到此结束
	}
	   
}
