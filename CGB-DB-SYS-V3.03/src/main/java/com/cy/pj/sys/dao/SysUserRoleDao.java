package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserRoleDao {
	
	/**
	 * 基于用户id查询角色id
	 * @param id
	 * @return
	 */
	@Select("select role_id from sys_user_roles where user_id=#{id}")
	List<Integer> findRoleIdsByUserId(Long id);
	
	/**
	 * 保存用户和角色关系数据
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int insertObjects(@Param("userId")Long userId,
			          @Param("roleIds")Integer[]roleIds);

	/**
	 * 基于用户id删除角色用户关系数据
	 * @param roleId
	 * @return
	 */
	@Delete("delete from sys_user_roles where user_id=#{userId}")
	int deleteObjectsByUserId(Long userId);
	/**
	 * 基于角色id删除角色用户关系数据
	 * @param roleId
	 * @return
	 */
	 @Delete("delete from sys_user_roles where role_id=#{roleId}")
	 int deleteObjectsByRoleId(Integer roleId);
}
