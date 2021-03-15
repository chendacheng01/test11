package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/log/")
public class SysLogController {

	 @Autowired
	 private SysLogService sysLogService;
	 
	 //http://localhost/log/doDeleteObjects?ids=1,2,3
	 @RequestMapping("doDeleteObjects")
	 @ResponseBody
	 public JsonResult doDeleteObjects(int... ids) {
		 sysLogService.deleteObjects(ids);
		 return new JsonResult("delete ok");
	 }
	 
	 //http://localhost/log/doFindPageObjects?pageCurrent=1
	 @RequestMapping(value="doFindPageObjects",method = RequestMethod.GET)
	 @ResponseBody
	 public JsonResult doFindPageObjects(String username,Long pageCurrent)throws Exception {
		 PageObject<SysLog> pageObject=sysLogService.findPageObjects(username, pageCurrent);
		// JsonResult jsonResult=new JsonResult();
		// jsonResult.setData(pageObject);
		 return new JsonResult(pageObject);
	 }//
	 
//	 @ExceptionHandler(RuntimeException.class)
//	 @ResponseBody
//	 public JsonResult doHandleRuntimeException(RuntimeException e) {
//		 System.out.println("SysLogController.doHandleRuntimeException");
//		 e.printStackTrace();
//		 return new JsonResult(e);
//	 }
	 
}
