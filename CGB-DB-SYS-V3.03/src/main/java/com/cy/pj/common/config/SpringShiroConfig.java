package com.cy.pj.common.config;
import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import  org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class SpringShiroConfig {
	
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager=
		new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(60*60*1000);
		return sessionManager;
	}
	
	@Bean
    public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager=new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*60*60);
		cManager.setCookie(cookie);
    	return cManager;
    }
	/**配置shiro中的核心对象：安全管理器*/
	@Bean//由此注解描述的方法会交给spring框架管理，默认bean的名字为方法名
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		securityManager.setCacheManager(cacheManager);
		securityManager.setRememberMeManager(rememberMeManager);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}
	/**配置ShiroFilterFactoryBean对象，
	 * 基于此对象创建过滤器工厂，通过滤器工厂创建过滤器，通过过滤器对请求进行过滤*/
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
		ShiroFilterFactoryBean sBean=new ShiroFilterFactoryBean();
		//设置安全管理器
		sBean.setSecurityManager(securityManager);
		//设置登陆页面url
		sBean.setLoginUrl("/doLoginUI");
		//设置过滤规则
		LinkedHashMap<String,String> map=new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"
		 map.put("/bower_components/**","anon");
		 map.put("/build/**","anon");
		 map.put("/dist/**","anon");
		 map.put("/plugins/**","anon");
		 map.put("/user/doLogin","anon");
		 map.put("/doLogout","logout");//logout由shiro提供
		 //除了匿名访问的资源,其它都要认证("authc")后访问
		 //map.put("/**","authc");
		 map.put("/**","user");//记住我功能需要使用user认证
		 sBean.setFilterChainDefinitionMap(map);
		return sBean;
	}
    //==================Shiro框架授权配置=================
    //Shiro框架中的授权是基于spring中AOP规范做了一个具体实现
    //AuthorizationAttributeSourceAdvisor中定义了切入点和通知
    //这里的切入点需要实用@RequiresPermissions注解进行描述
    //代理对象检测到方法上有@RequiresPermissions注解就会调用通知对象进行功能增强。
    @Bean
    public AuthorizationAttributeSourceAdvisor 
            authorizationAttributeSourceAdvisor (
    	    		    SecurityManager securityManager) {
    		        AuthorizationAttributeSourceAdvisor advisor=
    				new AuthorizationAttributeSourceAdvisor();
          advisor.setSecurityManager(securityManager);
    	return advisor;
    }
    //==============非核心业务实现==============================
    
    @Bean
    public CacheManager shiroCacheManager() {
    	return new MemoryConstrainedCacheManager();
    }
    

}
