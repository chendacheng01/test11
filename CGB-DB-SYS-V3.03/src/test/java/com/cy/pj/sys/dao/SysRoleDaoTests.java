package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.vo.SysRoleMenuVo;

@SpringBootTest
public class SysRoleDaoTests {

	 @Autowired
	 private SysRoleMenuDao sysRoleMenuDao;
	 @Autowired
	 private SysRoleDao sysRoleDao;
	 //单表多次查询
	 @Test
	 public void testFindObjectById01() {
		 SysRoleMenuVo rm=sysRoleDao.findObjectById(47);
		 List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(47);
		 rm.setMenuIds(menuIds);
		 System.out.println(rm);
	 }
	 //嵌套查询
	 @Test
	 public void testFindObjectById02() {
		 SysRoleMenuVo rm=sysRoleDao.findObjectById(47);
		 System.out.println(rm);
	 }
}
