package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}
	
	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("参数无效");
		//2.查询数据并校验
		//2.1查找角色自身信息
		SysRoleMenuVo rm=sysRoleDao.findObjectById(id);
		if(rm==null)
			throw new ServiceException("对象可能已经不存在");
		//2.2查找角色对应的菜单id
		//List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);
		//rm.setMenuIds(menuIds);
		//3.返回查询结果
		return rm;
	}
	
	
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new IllegalArgumentException("必须为角色授权权限");
		//2.保存数据
		//2.1保存角色自身信息
		int rows=sysRoleDao.updateObject(entity);
		//2.2保存角色菜单关系数据
		//2.2.1先删除原有关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
		//2.2.1再添加新的关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	@RequiredLog(operation = "保存角色")
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new IllegalArgumentException("必须为角色授权权限");
		//2.保存数据
		//2.1保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//2.2保存角色菜单关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	
	@Override
	public int deleteObject(Integer id) {
		//1.参数校验
		if(id==null||id<1)throw new IllegalArgumentException("参数值不正确");
		//2.删除数据
		//2.1删除角色菜单关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(id);
		//2.2删除用户角色关系数据
		sysUserRoleDao.deleteObjectsByRoleId(id);
		//2.3删除角色自身信息
		int rows=sysRoleDao.deleteObject(id);
		if(rows==0)throw new ServiceException("记录可能已经不存在");//message
		return rows;
	}
	
	@RequiredLog(operation = "角色查询")
	@Override
	public PageObject<SysRole> findPageObjects(String name, Long pageCurrent) {
		//1.参数校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值不正确");
		//2.查询总记录数并进行判断
		long rowCount=sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("没有查询到记录");
		//3.查询当前页记录
		int pageSize=3;
		long startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records=
		sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4.封装查询结果
		return new PageObject<>(records, rowCount, pageSize, pageCurrent);
	}

}




