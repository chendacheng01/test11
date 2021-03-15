package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
/**
 * 通过此接口对象操作角色菜单关系表(sys_role_menus)数据
 * @author qilei
 */
@Mapper
public interface SysRoleMenuDao {
	
	@Select("select menu_id from sys_role_menus where role_id=#{id}")
	List<Integer> findMenuIdsByRoleId(Integer id);
	
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")Integer[] roleIds);

	
	/**
	 * 保存角色和菜单的关系数据
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	 int insertObjects(@Param("roleId")Integer roleId,
			          @Param("menuIds")Integer[] menuIds);
	/**
	 * 基于角色id删除角色菜单关系数据
	 * @param roleId
	 * @return
	 * sqlsession.update(statement,args)
	 */
	 @Delete("delete from sys_role_menus where role_id=#{roleId}")
	 int deleteObjectsByRoleId(Integer roleId);
     /**
      * 基于菜单id删除角色菜单关系数据
      * @param menuId
      * @return
      */
	 @Delete("delete from sys_role_menus where menu_id=#{menuId}")
	 int deleteObjectsByMenuId(Integer menuId);
}
