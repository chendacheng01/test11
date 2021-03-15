package com.cy.pj.sys.dao;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.cy.pj.sys.entity.SysLog;

@Mapper
public interface SysLogDao {
	
	 int insertObject(SysLog entity);
	
	 /**
	  * 基于id值执行删除操作
	  * @param ids
	  * @return
	  * mybatis中的处理可变参数时，底层默认会将可变参数的存储到一个array对象中
	  */
	 int deleteObjects(int ...ids); 
	
	 /**
	  * 基于查询条件统计用户行为日志记录
	  * @param username 用户名
	  * @return 查询到的记录总数
	  */
	 long getRowCount(String username);//
	 /**
	  * 查询当前页要呈现的用户行为日志
	  * @param username 查询条件
	  * @param startIndex 当前页的起始位置
	  * @param pageSize 页面大小(每页最多显示多少条记录)
	  * @return 当前页的用户行为日志
	  */
	 List<SysLog> findPageObjects(String username,
			      long startIndex,int pageSize);
}




