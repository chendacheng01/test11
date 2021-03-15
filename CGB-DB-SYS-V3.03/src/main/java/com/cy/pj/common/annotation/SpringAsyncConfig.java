package com.cy.pj.common.annotation;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Configuration //此注解描述的类的要交给spring管理的一个配置对象
//读取spring配置文件中以spring.async.task为前缀的数据，并通过set方法注入给属性
@ConfigurationProperties("spring.async.task")
public class SpringAsyncConfig implements AsyncConfigurer {

	private int corePoolSize=5;
	private int maxPoolSize=100;
	private int keepAliveSeconds=60;
	private int queueCapacity=128;
	private String threadNamePrefix="tast===>";
	
	@Override
	public Executor getAsyncExecutor() {
		System.out.println("corePoolSize="+corePoolSize);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);
		//自定义拒绝处理策略
		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				log.error("队列已满并且已无线程可用");
			}
		});
        executor.initialize();
		return executor;
	}
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				log.error("任务执行时出现了 {}",ex.getMessage());
			}
		};
	}
}
