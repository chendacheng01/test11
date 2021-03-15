package com.cy.pj.sys.service.realm;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysMenuDao sysMenuDao;
	
	/**负责获取登陆用户权限信息并进行封装*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("===doGetAuthorizationInfo===");
		//1.获取登陆用户身份信息
		SysUser user=(SysUser)principals.getPrimaryPrincipal();
		//2.基于用户id获取角色id
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于角色id获取对应的菜单id
		List<Integer> menuIds=
		sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		if(menuIds==null||menuIds.size()==0)
		throw new AuthorizationException();
		//4.基于菜单id获取授权标识
		List<String> permissions=
		sysMenuDao.findPermissions(menuIds.toArray(new Integer[] {}));
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.对用户权限信息进行封装
		Set<String> stringPermissions=new HashSet<>(); 
		for(String per:permissions) {
			if(!StringUtils.isEmpty(per)) {
				stringPermissions.add(per);
			}
		}
		System.out.println("stringPermissions="+stringPermissions);
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		info.setStringPermissions(stringPermissions);
		return info;//此对象会返回给授权管理器
	}
	/**基于此方法的返回值告诉shiro框架我们采用什么加密算法*/
	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
		cMatcher.setHashAlgorithmName("MD5");
		cMatcher.setHashIterations(1);
		return cMatcher;
	}
	
    /**此方法中负责认证信息的获取以及封装*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
	    AuthenticationToken token) throws AuthenticationException {
		//1.获取登陆用户信息(页面上用户输入的用户名)
		UsernamePasswordToken uToken=(UsernamePasswordToken) token;
		String username=uToken.getUsername();
		//2.基于用户名查询数据库获取用户对象信息并进行判定
		//2.1)获取用户对象
		SysUser user=sysUserDao.findUserByUserName(username);
		//2.2)验证对象是否存在
		if(user==null)throw new UnknownAccountException();
		//2.3)检查用户是否被禁用
		if(user.getValid()==0)throw new LockedAccountException();
		//3.封装用户信息并返回。
		ByteSource credentialsSalt=
				ByteSource.Util.bytes(user.getSalt().getBytes());
		SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(
				user, //principal 用户身份
				user.getPassword(),//hashedCredentials 已经加密的密码
				credentialsSalt, //credentialsSalt 盐值
				this.getName());//getName()为realm的名字
		return info;//返回给认证管理器
	}

}
