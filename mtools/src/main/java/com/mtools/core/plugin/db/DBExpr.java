package com.mtools.core.plugin.db;
import java.util.List;


@SuppressWarnings("unchecked")
public class DBExpr
{
	private StringBuffer sql;
	private List param;
	public StringBuffer getSql()
	{
		return sql;
	}
	public void setSql(StringBuffer sql)
	{
		this.sql = sql;
	}
	public List getParam()
	{
		return param;
	}
	public void setParam(List param)
	{
		this.param = param;
	}
	public DBExpr addBracket()
	{
		sql.insert(0, "(");
		sql.append(")");
		return this;
	}
	public String sql()
	{
		if(sql!=null) return sql.toString();
		return "";
	}
	public boolean hasSql()
	{
		return sql!=null;
	}
	/**
	 * @param kname 列名
	 * @param opers 操作符
	 * @param exp
	 * @param val
	 * @return
	 */
	public DBExpr append(Object val,String kname,String oper,String exp,String nulname,String nulop,String nulexp)
	{
		if(kname!=null) sql.append(val!=null?kname:nulname);
		if(oper!=null) sql.append(val!=null?oper:nulop);
		if(exp!=null) sql.append(val!=null?exp:nulexp);
		if(val!=null) param.add(val);
		return this;
	}
	public DBExpr append(Object val,String kname,String oper,String exp)
	{
		if(val==null) return this;
		if(kname!=null) sql.append(kname);
		if(oper!=null) sql.append(oper);
		if(exp!=null) sql.append(exp);
		if(val!=null) param.add(val);
		return this;
	}
	public DBExpr apsql(String... strs)
	{
		for(String s:strs) sql.append(s);
		return this;
	}
	public DBExpr apval(Object...vals)
	{
		for(Object v:vals) param.add(v);
		return this;
	}
	public DBExpr()
	{
	}
	public DBExpr(Object val,String kname,String oper,String exp,String nulname,String nulop,String nulexp)
	{
		append(val, kname, oper, exp, nulname, nulop, nulexp);
	}
}
