package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * 借助此对象封装控制层返回客户端数据：要求这些数据必须有状态信息
 * @author qilei
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class JsonResult implements Serializable{
	private static final long serialVersionUID = -993375706215222253L;
	private int state=1;
	private String message="ok";
	private Object data;
	
	public JsonResult(String message) {
		this.message=message;
	}
	public JsonResult(Object data) {
		this.data=data;
	}
	public JsonResult(Throwable e) {
		this.state=0;
		this.message=e.getMessage();
	}
	
}





