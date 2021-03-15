package com.cy.pj.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

@RestController
@RequestMapping("/user/")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;//$Proxy35
	
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(String pwd,String newPwd,String cfgPwd) {
		sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		return new JsonResult("update ok");
	}
	
	
	@RequestMapping("doLogin")
	public JsonResult doLogin(String username,String password,
			boolean isRememberMe) {
		//1.获取subject对象
		Subject subject=SecurityUtils.getSubject();
		//2.提交用户请求
		UsernamePasswordToken token=new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		token.setRememberMe(isRememberMe);
		subject.login(token);//提交给securityManager
		return new JsonResult("login ok");
	}
	
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Long id) {
		return new JsonResult(sysUserService.findObjectById(id));
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,Integer[] roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,Integer[] roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doValidById")
	public JsonResult doValidById(Long id,Integer valid) {
		sysUserService.validById(id, valid);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Long pageCurrent) {
		System.out.println(sysUserService.getClass().getName());
		//controller-->sysUserService(cglib)-->SysLogAspect-->SysUserSerivceImpl
		PageObject<SysUserDeptVo> pageObject=sysUserService.findPageObjects(username, pageCurrent);
		return new JsonResult(pageObject);
	}
	

}
