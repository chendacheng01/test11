package com.cy.pj.sys.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Transactional(timeout = 30,
               isolation = Isolation.READ_COMMITTED,
               rollbackFor = Throwable.class,
               readOnly = false,
               propagation = Propagation.REQUIRED)
@Service //map<Key,ObjectInstance>
public class SysUserServiceImpl implements SysUserService {
	
	@Autowired
    private SysUserDao sysUserDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Override
	public List<SysUserMenuVo> findUserMenusByUserId(Long id) {
		//1.基于用户id获取用户对应的角色id
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//2.基于角色id获取角色对应的菜单id
		List<Integer> menuIds=
		sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		//3.基于菜单id获取用户对应的菜单
		return sysMenuDao.findMenusByIds(menuIds);
	}
	
	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		//1.参数校验
		//1.1非空校验
		if(StringUtils.isEmpty(password))
			throw new IllegalArgumentException("原密码不能为空");
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		//1.2等值校验
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("新密码与密码确认不一致");
		
		//1.3校验原密码是否正确
		SysUser user=ShiroUtils.getUser();
		String hashedPassword=user.getPassword();
		String salt=user.getSalt();
		SimpleHash sh=new SimpleHash("MD5", password, salt, 1);
		if(!hashedPassword.equals(sh.toHex()))
		throw new IllegalArgumentException("原密码不正确");
		//2.修改密码
		salt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5", newPassword, salt, 1);
		String hasedNewPassword=sh.toHex();
		int rows=
		sysUserDao.updatePassword(hasedNewPassword, salt, user.getId());
		//3.返回结果
		return rows;
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> findObjectById(Long id) {
		//1.参数校验
		if(id==null||id<1)throw new IllegalArgumentException("id值无效");
		//2.基于id执行查询操作
		SysUserDeptVo user=sysUserDao.findObjectById(id);
		if(user==null)throw new ServiceException("记录可能已经不存在");
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//3.封装结果并返回
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//connection
		//开启事务
		//1.参数校验
		if(entity==null)throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("必须为用户指定角色");
		//2.更新用户信息
		int rows=sysUserDao.updateObject(entity);
		//3.保存用户和角色关系数据
		//3.1基于用户id删除原有关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		//3.2查询用户和角色新的关系数据
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//提交事务
		//4.返回结果
		return rows;
	}
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		if(entity==null)throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new IllegalArgumentException("密码不能为空");
		//....
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("必须为用户指定角色");
		//2.保存用户信息
		//2.1对密码进行加密(md5盐值加密)
		//md5:一种消息摘要算法
		//md5特点：第一，不可逆；第二,相同内容加密结果相同。
		String salt=UUID.randomUUID().toString();
		//String hashedPassword=DigestUtils.md5DigestAsHex((salt+entity.getPassword()).getBytes());
		SimpleHash sh=new SimpleHash("MD5", entity.getPassword(), salt, 1);
		String hashedPassword=sh.toHex();//将加密结果转换为16进制
		entity.setSalt(salt);
		entity.setPassword(hashedPassword);
		//2.2保存用户对象
		int rows=sysUserDao.insertObject(entity);
		//3.保存用户和角色关系数据
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回结果
		return rows;
	}
	/**
	 * shiro框架通过@RequiresPermissions注解定义切入点，在这里表示
	 * 访问此方法需要进行授权，需要具备这个注解中的权限标识。
	 * 1)需要系统基于登陆用户获取用户权限{"sys:user:update","sys:user:view",...}
	 * 2)当用户权限中包含@RequiresPermissions注解中定义权限标识，就标识用户拥有访问这个方法的权限
	 * 3)拥有权限时则可以由shiro框架进行授权访问。
	 */
	@RequiresPermissions("sys:user:update")
	@RequiredLog(operation = "禁用启用")
	@Override
	public int validById(Long id, Integer valid) {
		//1.参数校验
		if(id==null||id<1)throw new IllegalArgumentException("id值无效");
		if(valid!=0&&valid!=1)throw new IllegalArgumentException("状态不正确");
		//2.更新状态
		int rows=sysUserDao.validById(id, valid,ShiroUtils.getUsername());//admin假设为登陆用户
		if(rows==0)throw new ServiceException("记录可能已经不存在");
		//3.返回结果
		return rows;
	}
	
	@RequiredLog(operation = "用户查询")
	@Transactional(readOnly = true)
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Long pageCurrent) {
		String tName=Thread.currentThread().getName();
		System.out.println("SysUserService.findPageObjects-->"+tName);
		//1.参数校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数并校验
		long rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		//3.查询当前页记录
		int pageSize=3;
		long startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records=
		sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果
		return new PageObject<>(records, rowCount, pageSize, pageCurrent);
	}
}
