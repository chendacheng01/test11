package com.cy.pj.sys.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 * 基于角色id从数据库获取数据然后封装到此对象，方案分析
 * 1)业务层发起多次查询，最后将结果封装到SysRoleMenuVo对象。(最简单)
 * 2)业务层只发起一次请求，数据层执行嵌套查询，最后将结果封装到SysRoleMenuVo对象。(相对难一些)
 * 3)业务层只发起一次请求，数据层执行多表关联查询，最后将结果封装到SysRoleMenuVo对象。
 */
@Data
public class SysRoleMenuVo implements Serializable {
	private static final long serialVersionUID = -7213694248989299601L;
	private Integer id;
	private String name;
	private String note;
	//基于角色id从角色菜单中间表中进行获取
	private List<Integer> menuIds;
}
