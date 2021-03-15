package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Controller
@RequestMapping("/")
public class PageController {
	@Autowired
    private SysUserService sysUserService;
	
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		SysUser user=ShiroUtils.getUser();
		model.addAttribute("username",user.getUsername());
		List<SysUserMenuVo> userMenus=
		sysUserService.findUserMenusByUserId(user.getId());
		model.addAttribute("userMenus", userMenus);
		return "starter";
	}
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	@RequestMapping("doLoginUI")
	public String doLoginUI() {
		return "login";
	}
	//==============================
	
//	@RequestMapping("/log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}
//	@RequestMapping("/menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}//templates/pages/sys/menu_list.html
	/**
	 * REST风格的url映射:REST是一种软件架构编码风格，在这种风格
	 * 下的url定义，可以使用{变量}的方式让url更加简单通用。在
	 * 方法参数中需要url中的{变量}值时，需要实用@PathVariable注解
	 * 对方法参数进行描述，并且要求方法参数的名字要与{变量}表达式中
	 * 的变量名相同。
	 * @param moduleUI
	 * @return
	 */
	//http://localhost/menu/menu_edit
	@RequestMapping("/{module}/{moduleUI}")
	public String doModuleUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}
	
	
}
