package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleDao {
	
	  /**
	   * 获取所有角色的id和name
	   * @return
	   */
	  @Select("select id,name from sys_roles")
	  List<CheckBox> findObjects();
	
	  SysRoleMenuVo findObjectById(Integer id);

	  /**
	   * 更新角色自身信息
	   * @param entity
	   * @return
	   */
	  @Update("update sys_roles set name=#{name},note=#{note},modifiedTime=now(),modifiedUser=#{modifiedUser} where id=#{id}")
	  int updateObject(SysRole entity);
	  
	  /**
	   * 保存角色自身信息
	   * @param entity
	   * @return
	   */
	  int insertObject(SysRole entity);
	  /**
	   * 基于角色id删除角色自身信息
	   * @param id
	   * @return
	   */
	   @Delete("delete from sys_roles where id=#{id}")
	   int deleteObject(Integer id);
       /**
        * 查询总记录数
        * @param name
        * @return
        */
	   long getRowCount(String name);
	   /**
	    * 查询当前页记录
	    * @param name
	    * @param startIndex
	    * @param pageSize
	    * @return
	    */
	   List<SysRole> findPageObjects(String name,long startIndex,int pageSize);
}



