/**
 * 通联支付-研发中心
 * SysConfigPlugin.java
 * 2014-5-6
 */
package com.mtools.core.plugin.sys;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.Department;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entiy.vo.DepartVo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.FuncUtil;

/**
 * @author zhang
 *  功能：系统基本组件
 * @date 2014-5-6
 */
@Component("sysPlugin")
public class SysConfigPlugin extends BasePlugin {

	
	public List<DepartVo> getDeps(Department dep,PageInfo page){
		String sql = "select c.*, p.depname pdepname from department c left join department p on c.parentid=p.depid where 1=1 ";
		if (!FuncUtil.isEmpty(dep.getDepname())) {
			sql += " and c.depname like '%" + dep.getDepname() + "%'";
		}
		if (!FuncUtil.isEmpty(dep.getDepid())) {
			sql += "  and c.depid=" + dep.getDepid();
		}
		if (!FuncUtil.isEmpty(page.getSort().getName())) {
			sql += " order by c.depname " + page.getSort().getName();
		}
		if (!FuncUtil.isEmpty(page.getSort().getId())) {
			sql += " order by c.depid " + page.getSort().getId();
		}
		// 总笔数
		int count = this.dao.count(sql,null);
		page.setItemCount(count);
		List<DepartVo> deps=this.dao.searchPage(sql, DepartVo.class, Integer.parseInt(page.getPageIndex()), Integer.parseInt(page.getPageSize()), null);
		if(deps==null||deps.size()==0){
			deps=Lists.newArrayList();
			deps.add(new DepartVo());
		}
		return deps;
	}

	/**
	 * 功能：新增部门（组织）
	 * 2014-5-6
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void addDepartment(DepartVo depvo, ModelMap model) throws AIPGException {
		try {
			Department dep=new Department();
			dep.setDepname(depvo.getDepname());
			dep.setPrincipal(depvo.getPrincipal());
			this.dao.add(dep);
			model.addAttribute(CoreConstans.OPTRESULT, "新增部门成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "新增部门成功!");
		} catch (Exception ex) {
			log.error("新增部门失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "新增部门失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "新增部门失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "新增部门失败!");
		}
	}
	
	
	/**
	 * 功能：修改部门信息
	 * 2014-5-6
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void updateDepartment(Department dep, ModelMap model){
		try {
			this.dao.update(dep);
			this.errorMsg="修改部门成功";
			model.addAttribute(CoreConstans.OPTRESULT,errorMsg);
			model.put(CoreConstans.SUCCESSMESSAGE, errorMsg);
		} catch (Exception ex) {
			this.errorMsg="修改部门失败";
			log.error(this.errorMsg+"\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, errorMsg);
			model.put(CoreConstans.ERROR_MESSAGE, errorMsg);
		}
	}
	/**
	 * 功能：删除部门信息
	 * 2014-5-6
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void deleteDepartment(Department dep, ModelMap model){
		try {
			String[] ids=dep.getDepid().split(",");
			for(String id:ids){
				dep.setDepid(id);
				this.dao.delete(dep);
			}
			this.errorMsg="删除部门成功!";
			model.addAttribute(CoreConstans.OPTRESULT,errorMsg);
			model.put(CoreConstans.SUCCESSMESSAGE, errorMsg);
		} catch (Exception ex) {
			log.error("删除部门失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "删除部门失败");
			model.put(CoreConstans.ERROR_MESSAGE, "删除部门失败!");
		}
	}
	
	/**
	 * 功能：组织信息
	 * 2014-5-7
	 */
	public Map<String, String> getDepsFoMap() {
		String sql = "select t.depid,t.depname from department t";
		List<Object[]> deps = this.dao.searchForArray(sql, null);
		Map<String, String> depsMap = Maps.newConcurrentMap();
		for (Object[] value : deps) {
			depsMap.put(value[0].toString(), value[1].toString());
		}
		return depsMap;
	}
}
