package com.cy.pj.common.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 借助此对象封装数据层返回的分页查询结果，并计算分析信息
 * @author qilei
 * @param <T>
 */
@Data
@NoArgsConstructor
public class PageObject<T> implements Serializable{
	private static final long serialVersionUID = 2352271705547032307L;
	/**封装查询到的当前页记录*/
	private List<T> records;
	/**总记录数*/
	private Long rowCount;
	/**总页数*/
	private Long pageCount;
	/**页面大小*/
	private Integer pageSize;
	/**当前页码值*/
	private Long pageCurrent;
	public PageObject(List<T> records, Long rowCount,
			          Integer pageSize, Long pageCurrent) {
		super();
		this.records = records;
		this.rowCount = rowCount;
		this.pageSize = pageSize;
		this.pageCurrent = pageCurrent;
		//计算总页数
		//方法1
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0) {
//			this.pageCount++;
//		}
		//方法2
		this.pageCount=(rowCount-1)/pageSize+1;
	}
	
	
}
