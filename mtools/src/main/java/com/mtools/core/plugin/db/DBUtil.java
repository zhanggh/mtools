package com.mtools.core.plugin.db;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mtools.core.plugin.annotation.Temporary;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.Auxs;
import com.mtools.core.plugin.helper.FuncUtil;


@SuppressWarnings({"unchecked","rawtypes"})
public class DBUtil
{
	private static Map tableNameMap = new HashMap();
	private static Map tableKeysMap = new HashMap();
	private static Map tableAlisMap = new HashMap();
	public static int updateObjCond(boolean undercore,JdbcOperations dbop,String tab,Class clzz,Object obj,String... keys)
	{
		ArrayList ls=new ArrayList();
		String sql=createUpdateSql(undercore,tab,clzz, obj, ls, IG_NULL|IG_MIN, keys);
		return updateEx(dbop,sql,ls.toArray());
	}
	public static int updateObjFull(JdbcOperations dbop,String tab,Class clzz,Object obj,String... keys)
	{
		return updateObjFull(false,dbop, tab, clzz, obj, keys);
	}
	public static int updateObjFull(boolean undercore,JdbcOperations dbop,String tab,Class clzz,Object obj,String... keys)
	{
		ArrayList ls=new ArrayList();
		String sql=createUpdateSql(undercore,tab, clzz, obj, ls, IG_NULL|IG_MIN, keys);
		return updateEx(dbop,sql,ls.toArray());
	}
	public static int updateObj(boolean undercore,JdbcOperations dbop,String tab,Class clzz,Object obj,int flags,String... keys)
	{
		ArrayList ls=new ArrayList();
		String sql=createUpdateSql(undercore,tab, clzz, obj, ls, flags, keys);
		return updateEx(dbop,sql,ls.toArray());
	}
	public static int updateEx(JdbcOperations dbop,String sql,Object...args)
	{
		if(logupdate) log.debug(sql+" "+FuncUtil.filteSepcStr(Arrays.toString(args)));
		return dbop.update(sql, args);
	}
	public static int[] updateBatch(JdbcOperations dbop,String sql,Object[]... objs)
	{
		return dbop.batchUpdate(sql, Arrays.asList(objs));
	}
	public static KeyHolder updateX(JdbcOperations dbop,final String sql,final String[] retCols,final Object... args)
	{
		return update(dbop,sql,args,retCols);
	}
	public static KeyHolder update(JdbcOperations dbop,final String sql,final Object[] args,final String[] retCols)
	{
		KeyHolder kh=new GeneratedKeyHolder();
		dbop.update(new PreparedStatementCreator()
		{
  			public PreparedStatement createPreparedStatement(Connection con) throws SQLException
  			{
  				int i=1;
  				PreparedStatement ps;
  				ps=con.prepareStatement(sql,retCols);
  				for(Object o:args)
  				{
  					StatementCreatorUtils.setParameterValue(ps, i++, SqlTypeValue.TYPE_UNKNOWN, o);
  				}
  				return ps;
  			}			
		}, kh);
		return kh;
	}
	public static List getList(JdbcOperations dbop, String sql,Class clz,Object... args)
	{
		BeanPropRowMap mp=new BeanPropRowMap(clz);
		mp.setPrimitivesDefaultedForNullValue(true);
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		return dbop.query(sql, args,mp);		
	}
	public static List getPage(boolean isOrcl,JdbcOperations dbop, String sql,Class clz,int start,int size, Object... args)
	{
		if(isOrcl){
			sql = "select * from (select rownum myrownum,newtable.* from ("
				+ sql + ") newtable where rownum <= " + (start + size)
				+ ") where myrownum > " + start;
		}else{
			sql += " limit "+start+","+size;
		}
		BeanPropRowMap mp=new BeanPropRowMap(clz);
		mp.setPrimitivesDefaultedForNullValue(true);
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		return dbop.query(sql, args,mp);
	}
	
	public static List<Object[]> getListForArray(JdbcOperations dbop, String sql,Object... args){
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		List<Map<String,Object>> list = dbop.queryForList(sql, args);
		List<Object[]> data = new ArrayList<Object[]>();
		for (Map<String, Object> map : list) {
			List<Object> record = new ArrayList<Object>();
			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
			// 封装数据
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				// 只保留值,非key
				record.add(entry.getValue());
			}
			data.add(record.toArray());
		}
		return data;
	}
	
	public static List<Object[]> getListForArrayPage(boolean isOrcl,JdbcOperations dbop, String sql,int start,int size,Object... args){
		if(isOrcl){
			sql = "select * from (select newtable.*,rownum myrownum from ("
				+ sql + ") newtable where rownum <= " + (start + size)
				+ ") where myrownum > " + start;
		}else{
			sql+=" limit "+start+","+size;
		}
		
		return getListForArray(dbop,sql,args);
	}
	
	public static List<Map<String,Object>> getListForMap(JdbcOperations dbop, String sql,Object... args){
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		return dbop.queryForList(sql, args);
	}
	
	public static List<Map<String,Object>> getListForMapPage(boolean isOrcl,JdbcOperations dbop, String sql,int start,int size,Object... args){
		if(isOrcl){
			sql = "select * from (select rownum myrownum,newtable.* from ("
				+ sql + ") newtable where rownum <= " + (start + size)
				+ ") where myrownum > " + start;
		}else{//mysql的查询
			sql+=" limit "+start+","+size;
		}
		
		return getListForMap(dbop,sql, args);
	}
	
	public static Object getObj(JdbcOperations dbop, String sql,Class clz,Object... args)
	{
		BeanPropRowMap mp=new BeanPropRowMap(clz);
		mp.setPrimitivesDefaultedForNullValue(true);
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		List ls=dbop.query(sql, args,mp);
		if(ls.isEmpty()) return null;
		return ls.iterator().next();
	}
	public static int count(JdbcOperations dbop, String sql,Object... args)
	{
		
		sql = "select count(1) from ("+sql+")";
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		return (Integer)DBUtil.getSimpleObj(dbop, sql, Integer.class,args);
	}
	public static Object getSimpleObj(JdbcOperations dbop, String sql,Class clz,Object... args)
	{
		if(logquery) log.debug("查询列表SQL: " + sql + " 参数: " + Arrays.toString(args));
		List ls=dbop.queryForList(sql, args,clz);
		if(ls.isEmpty()) return null;
		return ls.iterator().next();
	}
	public static List search(boolean isOrcl,boolean underscore,JdbcOperations dbop,String tab,int start,int size,int flag,Object startObj,Object endObj,Class clz,String...ranges)
	{
		ArrayList ls=new ArrayList();
		String sql = createCondSqlEx(underscore,clz, startObj,endObj, ls, flag, ranges);
		return getPage(isOrcl,dbop, "select * from "+tab+sql, clz, start, size, ls.toArray());
		
	}
	public static long count(boolean underscore,JdbcOperations dbop,String tab,int flag,Object startObj,Object endObj,Class clz,String...ranges)
	{
		ArrayList ls=new ArrayList();
		String sql = createCondSqlEx(underscore,clz, startObj,endObj, ls, flag, ranges);
		Long lv=(Long) getSimpleObj(dbop, "select count(*) from "+tab+sql, Long.class, ls.toArray());
		return lv.longValue();
	}
	//args: name1 value1 name2 value2.....
	public static Object findObj(JdbcOperations dbop,String tab,Class clz,String...args)
	{
		ArrayList ls=new ArrayList();
		String sql = createCondSql(ls, 0, args);
		return getObj(dbop, "select * from "+tab+sql, clz, ls.toArray());
	}
	//args: name1 value1 name2 value2.....
	public static Object findList(JdbcOperations dbop,String tab,Class clz,String...args)
	{
		ArrayList ls=new ArrayList();
		String sql = createCondSql(ls, 0, args);
		return getList(dbop, "select * from "+tab+sql, clz, ls.toArray());
	}
	public static Object findObj(boolean underscore,JdbcOperations dbop,String tab,Object obj,Class clz,String...keys)
	{
		ArrayList ls=new ArrayList();
		String sql = createCondSql(underscore, obj, ls, 0, keys);
		return getObj(dbop, "select * from "+tab+sql, clz, ls.toArray());
	}
	public static int deleteObj(JdbcOperations dbop,String tab,Object obj,String...keys){
		ArrayList ls=new ArrayList();
		String sql = createDeleteSql(false,tab,obj,ls,keys);
		return DBUtil.updateEx(dbop, sql, ls.toArray());
	}
	public static int deleteObj(boolean underscore,JdbcOperations dbop,String tab,Object obj,String...keys){
		ArrayList ls=new ArrayList();
		String sql = createDeleteSql(underscore,tab,obj,ls,keys);
		return DBUtil.updateEx(dbop, sql, ls.toArray());
	}	
	public static String createDeleteSql(boolean underscore,String tab,Object o,List ls,String... keys)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("delete from ").append(tab);
		sql.append(createCondSql(underscore, o, ls, 0, keys));
		return sql.toString();
	}	
	public static int delete(JdbcOperations dbop,String sql,Object...args){
		return DBUtil.updateEx(dbop, sql, args);
	}
	public static int[] insertObjs(DataSource ds, String tab,Object... args)
	{
		return insertObjs(false, ds, tab, args);
	}
	public static int[] insertObjs(boolean undercore,DataSource ds, String tab,Object... args)
	{		
		BatchSqlUpdate bsu;
		DBUtil dbu=new DBUtil();
		List<Integer> vt=new Vector<Integer>();
		dbu.init(args[0].getClass(), null);
		String sql=dbu.buildInsert(undercore,tab,vt);
		Integer[] ivt=new Integer[vt.size()];
		bsu=new BatchSqlUpdate(ds,sql,ArrayUtils.toPrimitive(vt.toArray(ivt)));
		for(Object o:args)
		{
			bsu.update(dbu.buildParam(undercore,o).toArray());			
		}
		return bsu.flush();
	}
	public static KeyHolder insertObj(JdbcOperations dbop,String tab,Object o,String...retCol)
	{
		return insertObj(false, dbop, tab, o, retCol);
	}
	public static KeyHolder insertObjEx(boolean undercore,JdbcOperations dbop,String tab,Class clz,Object o,String...retCol)
	{
		String sql;
		ArrayList ls=new ArrayList();
		sql=DBUtil.createInsertSql(undercore,tab,clz,o, ls, 0);
		return DBUtil.update(dbop, sql, ls.toArray(), retCol);		
	}
	public static KeyHolder insertObj(boolean undercore,JdbcOperations dbop,String tab,Object o,String...retCol)
	{
		return insertObjEx(undercore, dbop, tab, o.getClass(), o, retCol);
	}
	public static int insertObj(JdbcOperations dbop,String tab,Object o)
	{
		return insertObj(false, dbop, tab, o);
	}
	public static int insertObjEx(boolean undercore,JdbcOperations dbop,String tab,Class clz,Object o)
	{
		String sql;
		ArrayList ls=new ArrayList();
		sql=DBUtil.createInsertSql(undercore,tab,clz,o, ls, 1);
		return DBUtil.updateEx(dbop, sql, ls.toArray());
	}
	public static int insertObj(boolean undercore,JdbcOperations dbop,String tab,Object o)
	{
		return insertObjEx(undercore,dbop,tab,o.getClass(),o);
	}

	private static Log log=LogFactory.getLog(DBUtil.class);
	private static boolean logupdate=true;
	private static boolean logquery=true;
	/**
	 * 初始化日志记录器
	 * @param logu
	 * @param logq
	 * @param needFilter
	 */
	public static void initlog(boolean logu,boolean logq)
	{
		logupdate=logu;
		logquery=logq;
	}
	public void init(Class clz,String[] k)
	{
		pdas=BeanTools.getPDs(clz);	
		keys=k;
	}
	public Vector buildParam(Object o)
	{
		return buildParam(false, o);
	}
	//把属性生成数组
	public Vector buildParam(boolean underscore,Object o)
	{
		Vector ls=new Vector();
		List kval=null;
		if(keys!=null)
		{
			kval=new LinkedList();
		}
		for(PropertyDescriptor pd:pdas)
		{
			String colName=pd.getName();
			if(pd.getName().equals("class")) continue;
			if(underscore) colName=underscoreName(colName);
			Object val=BeanTools.readProp(o,pd.getReadMethod());
			if(keys!=null)
			{
				for(String kname:keys)
				{
					if(kname.equalsIgnoreCase(colName))
					{
						kval.add(val);
						break;
					}
				}
			}
			ls.add(val);
		}
		if(kval!=null) ls.addAll(kval);
		return ls;
	}
	public String buildInsert(String tab,List ls)
	{
		return buildInsert(false, tab, ls);
	}
	public String buildInsert(boolean underscore,String tab,List ls)
	{
		int i,num=0;
		StringBuilder sql=new StringBuilder();
		sql.append("insert into ").append(tab).append("(");
		for(PropertyDescriptor pd:pdas)
		{
			String colName=pd.getName();
			if(pd.getName().equals("class")) continue;
			if(underscore) colName=underscoreName(colName);
			ls.add(new Integer(StatementCreatorUtils.javaTypeToSqlParameterType(pd.getPropertyType())));
			sql.append(colName).append(",");
			num++;
		}
		sql.setCharAt(sql.length()-1,')');
		sql.append(" values (");
		for(i=0;i<num;++i) sql.append("?,");
		sql.setCharAt(sql.length()-1,')');
		return sql.toString();
	}
	public String buildUpdate(boolean underscore,String tab,List ls)
	{
		List kvl=null;
		StringBuilder where=null;		
		StringBuilder sql=new StringBuilder();
		sql.append("update ").append(tab).append(" set ");
		if(keys!=null)
		{
			where=new StringBuilder();
			where.append("where ");
			kvl=new Vector();
		}
		for(PropertyDescriptor pd:pdas)
		{
			String colName=pd.getName();
			if(pd.getName().equals("class")) continue;
			if(underscore) colName=underscoreName(colName);
			if(keys!=null)
			{
				for(String kname:keys)
				{
					if(kname.equalsIgnoreCase(colName))
					{
						kvl.add(StatementCreatorUtils.javaTypeToSqlParameterType(pd.getPropertyType()));
						where.append(kname).append("=? and ");
					}
				}
			}
			ls.add(StatementCreatorUtils.javaTypeToSqlParameterType(pd.getPropertyType()));
			sql.append(colName).append("=?,");
		}
		sql.setCharAt(sql.length()-1,' ');
		if(where!=null&&where.length()>5)
		{
			where.delete(where.length()-3, where.length());
			sql.append(where);
		}
		if(kvl!=null) ls.addAll(kvl);
		return sql.toString();
	}
	public static String createUpdateSql(String tab,Object o,List ls,int flag,String... keys)
	{
		return createUpdateSql(tab,o.getClass(),ls,flag,keys);
	}
	protected static boolean appendCond(StringBuilder where,String colName,Object val,List lsv,String...keys)
	{
		if(keys==null) return false;
		if(Auxs.indexOfIg(keys, colName)==-1) return false;
		if(val!=null)
		{
			where.append(colName).append("=? and ");
			lsv.add(val);
		}
		else
		{
			where.append(colName).append("is null and ");
		}
		return true;
	}
	/**
	 * @param underscore
	 * @param tab
	 * @param clzz
	 * @param o
	 * @param ls
	 * @param flag
	 * @param keys
	 * @return
	 */
	public static String createUpdateSql(boolean underscore,String tab,Class clzz,Object o,List ls,int flag,String... keys)
	{
		List kval=null;
		StringBuilder where=null;		
		StringBuilder sql=new StringBuilder();
		if(clzz==null) clzz=o.getClass();
		PropertyDescriptor pda[]=BeanTools.getPDs(clzz);
		sql.append("update ").append(tab).append(" set ");
		if(keys!=null)
		{
			kval=new LinkedList();
			where=new StringBuilder();
			where.append("where ");
		}
		for(PropertyDescriptor pd:pda)
		{
			boolean isTemp=false;
			//使用了@Temporary注解的方法，则不需参与数据库操作
			Annotation[] annotations = pd.getReadMethod().getDeclaredAnnotations();
			if(annotations!=null){
				for(Annotation an:annotations){
					if(an.annotationType()==Temporary.class){
						isTemp=true;
					}
				}
			}
			if(!isTemp){
				String methodName=pd.getReadMethod().getName();
				if(methodName.startsWith("get")){
					String colName=pd.getName();
					if(pd.getName().equals("class")) continue;
					if(underscore) colName=underscoreName(colName);
					Object val=BeanTools.readProp(o,pd.getReadMethod());
					if(appendCond(where,colName,val,kval,keys)) continue;
					if(checkVal(val,flag)) continue;
					sql.append(colName).append("=?,");
					ls.add(val);
				}
			}
			
		}
		sql.setCharAt(sql.length()-1,' ');
		if(where!=null&&where.length()>5)
		{
			where.delete(where.length()-4, where.length());
			sql.append(where);
		}
		if(kval!=null) ls.addAll(kval);
		return sql.toString();
	}
	public static String createInsertSql(String tab,Object o,List ls,int flag)
	{
		return createInsertSql(false,tab,o.getClass(),o,ls,flag);		
	}
	public static String createInsertSql(boolean undercore,String tab,Object o,List ls,int flag)
	{
		return createInsertSql(undercore,tab,o.getClass(),o,ls,flag);
	}
	public static String underscoreName(String name)
	{
		return Auxs.underscoreName(name);
	}
	public static String createInsertSql(boolean underscore,String tab,Class clz,Object o,List ls,int flag)
	{
		int i,num=0;
		StringBuilder sql=new StringBuilder();
		sql.append("insert into ").append(tab).append("(");
		if(clz==null) clz=o.getClass();
		PropertyDescriptor pda[]=BeanTools.getPDs(clz);
		for(PropertyDescriptor pd:pda)
		{
			String methodname=pd.getReadMethod().getName();
			if(methodname.startsWith("get")){
				String colName=pd.getName();
				if(pd.getName().equals("class")) continue;
				if(underscore) colName=underscoreName(colName);
				Object val=BeanTools.readProp(o,pd.getReadMethod());
				if(checkVal(val,flag)) continue;
				sql.append(colName).append(",");
				num++;
				ls.add(val);
			}
		}
		sql.setCharAt(sql.length()-1,')');
		sql.append(" values (");
		for(i=0;i<num;++i) sql.append("?,");
		sql.setCharAt(sql.length()-1,')');
		return sql.toString();
	}
	public static String getSeq(JdbcOperations dbop,String seqname)
	{
		String sql="select "+seqname+".nextval from dual";
		return (String) getSimpleObj(dbop, sql,String.class);
	}
	public static String buildID(JdbcOperations dbop,String seqname,int len)
	{
		String seq=getSeq(dbop,seqname);
		if(seq.length()>len) return StringUtils.right(seq, len);
		else return StringUtils.leftPad(seq, len,'0');
	}
	public static String createCondSqlEx(boolean underscore,Class clz,Object startObj,Object endObj,List ls,int flag,String... ranges)
	{
		StringBuilder where=null;
		PropertyDescriptor pda[]=BeanTools.getPDs(clz);
		where=new StringBuilder();
		where.append(" where ");
		for(PropertyDescriptor pd:pda)
		{
			boolean isRange;
			Object valEnd;
			Object valStart;
			String colName;
			colName=pd.getName();
			if(pd.getName().equals("class")) continue;
			valStart=null;
			valEnd=null;
			if(underscore) colName=underscoreName(colName);
			if(startObj!=null) valStart=BeanTools.readProp(startObj,pd.getReadMethod());
			isRange=Auxs.indexOfIg(ranges,colName)!=-1;
			if(isRange)
			{
				valEnd=BeanTools.readProp(endObj,pd.getReadMethod());
				if(valStart!=null&&!checkVal(valStart, flag))
				{
					ls.add(valStart);
					where.append(colName).append(">=? and ");
				}
				if(valEnd!=null&&!checkVal(valEnd, flag))
				{
					ls.add(valStart);
					where.append(colName).append("<? and ");
				}
			}
			else
			{
				if(valStart!=null&&!checkVal(valStart, flag))
				{
					ls.add(valStart);
					where.append(colName).append("=? and ");
				}
			}
			
		}
		if(where.length()>7)
			where.delete(where.length()-4, where.length());
		else
			where.delete(0,where.length());
		return where==null?"":where.toString();
	}	
	public static String createCondSql(boolean underscore,Object o,List ls,int flag,String... keys)
	{
		StringBuilder where=null;
		PropertyDescriptor pda[]=BeanTools.getPDs(o.getClass());
		if(keys==null) return "";
		where=new StringBuilder();
		where.append(" where ");
		for(PropertyDescriptor pd:pda)
		{
			String colName=pd.getName();
			if(pd.getName().equals("class")) continue;
			if(underscore) colName=underscoreName(colName);
			Object val=BeanTools.readProp(o,pd.getReadMethod());
			appendCond(where, colName, val, ls, keys);
		}
		if(where!=null&&where.length()>5)
		{
			where.delete(where.length()-4, where.length());
		}
		return where.toString();
	}	
	public static String createCondSql(List ls,int flag,String... args)
	{
		StringBuilder where=null;
		where=new StringBuilder();
		where.append(" where ");
		int i;
		for(i=0;i<args.length/2;i++)
		{
			where.append(args[i*2]).append("=? and");
			ls.add(args[i*2+1]);
		}
		if(where!=null&&where.length()>8)
		{
			where.delete(where.length()-4, where.length());
		}
		return where.toString();
	}	
 
	 
	public static boolean checkVal(Object val,int flag)
	{
		if(flag == 10){
			flag = 16;
		}
		if((flag&IG_NULL)==IG_NULL&&(val==null||val.toString().equals(""))) return true;
		else if((flag&IG_NULLNOTBLANK)==IG_NULLNOTBLANK&&val==null) return true;
		else if((flag&IG_ZERO)==IG_ZERO&&val!=null&&val.toString().equals("0")) return true;
		else if((flag&IG_ONEn)==IG_ONEn&&val!=null&&val.toString().equals("-1")) return true;
		else if((flag&IG_MIN)==IG_MIN&&val!=null&&val.equals(BeanTools.getMin(val.getClass()))) return true;
		return false;
	}
	private PropertyDescriptor[] pdas;
	private String[] keys;
	public static int IG_NULL=1; //忽略null和空字符串
	public static int IG_ZERO=2; //忽略0
	public static int IG_ONEn=4; //忽略-1
	public static int IG_MIN=8;  //忽略该类型最小的
	public static int IG_NULLNOTBLANK=16;  //忽略null但不忽略空字符串
	
	public static String getTableName(Object obj) throws AIPGException
	{
		if(obj==null) return null;
		return getTableNameEx(obj.getClass());
	}
	public static String getTableNameEx(Class clz) throws AIPGException
	{
		if(DBUtil.tableNameMap.containsKey(clz))
			return (String)DBUtil.tableNameMap.get(clz);
		else{
			try{
				Field tnField = clz.getDeclaredField("TABLE_NAME");
				String name = tnField.get(clz).toString();
				tableNameMap.put(clz, name);
				return name;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "找不到对象的pojo类的表名属性:"+clz.getName());
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "无法获取对象的pojo类的表名属性:"+clz.getName());
			}
		}
		return null;
	}
	/**
	 * 获取表别名
	 * @param clz
	 * @return
	 * @throws AIPGException
	 */
	public static String getTableAlis(Class clz) throws AIPGException
	{
		if(DBUtil.tableAlisMap.containsKey(clz))
			return (String)DBUtil.tableAlisMap.get(clz);
		else{
			try{
				Field tnField = clz.getDeclaredField("TABLE_ALIAS");
				String name = tnField.get(clz).toString();
				tableAlisMap.put(clz, name);
				return name;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "找不到对象的pojo类的表名属性:"+clz.getName());
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "无法获取对象的pojo类的表名属性:"+clz.getName());
			}
		}
		return null;
	}
	
	
	public static String getTableName(Class c){
		if(c==null)
			return null;
		if(DBUtil.tableNameMap.containsKey(c))
			return (String)DBUtil.tableNameMap.get(c);
		else{
			try{
				Field tnField = c.getDeclaredField("TABLE_NAME");
				String name = tnField.get(null).toString();
				tableNameMap.put(c, name);
				return name;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+c.getClass().getName(),ex);
				return null;
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+c.getClass().getName(),ex);
				return null;
			}
		}
	}
	
	public static String getDataKeys(Object obj){
		if(obj==null)
			return null;
		try{
			Method method = obj.getClass().getDeclaredMethod("toDataKeys");
			return (String)method.invoke(obj);
		}
		catch(NoSuchMethodException ex){
			log.error("找不到对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
		catch(InvocationTargetException ex){
			log.error("找不到对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
		catch(IllegalAccessException ex){
			log.error("无法获取对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
	}	
	
	public static String[] getTableKeys(Object obj){
		if(obj==null)
			return null;
		if(DBUtil.tableKeysMap.containsKey(obj.getClass()))
			return (String[])DBUtil.tableKeysMap.get(obj.getClass());
		else{
			try{
				Field tnField = obj.getClass().getDeclaredField("TABLE_KEYS");
				String[] keys = (String[])tnField.get(obj);
				if(keys!=null&&keys.length>0)
					tableKeysMap.put(obj.getClass(), keys);
				return keys;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+obj.getClass().getName(),ex);
				return null;
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+obj.getClass().getName(),ex);
				return null;
			}
		}
	}
}
