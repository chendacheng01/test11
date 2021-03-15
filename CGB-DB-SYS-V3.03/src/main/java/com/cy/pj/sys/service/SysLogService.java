package com.cy.pj.sys.service;
import java.util.concurrent.Future;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;

public interface SysLogService {
	
	void saveObject(SysLog entity);
	
	int deleteObjects(int... ids);
	 /**
	  * 基于条件进行日志信息的分页查询操作
	  * @param username 查询条件
	  * @param pageCurrent 当前页码值
	  * @return 当前页记录+分页信息
	  */
	 PageObject<SysLog> findPageObjects(
			 String username,Long pageCurrent);
}
