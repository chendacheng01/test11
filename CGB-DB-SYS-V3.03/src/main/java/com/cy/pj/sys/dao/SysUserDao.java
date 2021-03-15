package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {
	@Update("update sys_users set password=#{password},salt=#{salt},modifiedTime=now() where id=#{id}")
 	int updatePassword(
			@Param("password")String password,
			@Param("salt")String salt,
			@Param("id")Long id);

	   @Select("select * from sys_users where username=#{username}")
	   SysUser findUserByUserName(String username);
	
	   /**
	    * 基于用户id查找用户以及用户对应的部门信息
	    * @param id
	    * @return
	    */
	   SysUserDeptVo findObjectById(Long id);
	
	   /**
	    * 更新用户自身信息
	    * @param entity
	    * @return
	    */
	   int updateObject(SysUser entity);
	   /**
	    * 保存用户自身信息
	    * @param entity
	    * @return
	    */
	   int insertObject(SysUser entity);
	    
	   @Update("update sys_users "
	   		+ " set valid=#{valid},modifiedUser=#{username},modifiedTime=now() "
	   		+ " where id=#{id}")
	   int validById(Long id,Integer valid,String username);
       /**
        * 基于条件查询总记录数
        * @param username
        * @return
        */
	   long getRowCount(String username);
	   /**
	    * 基于条件查询当前页记录
	    * @param username
	    * @param startIndex
	    * @param pageSize
	    * @return
	    */
	   List<SysUserDeptVo> findPageObjects(String username,long startIndex,int pageSize);
}
