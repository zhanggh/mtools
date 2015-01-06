package com.mtools.core.plugin.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mtools.core.plugin.entity.BetDates;
import com.mtools.core.plugin.entity.SqlParam;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.Auxs;
import com.mtools.core.plugin.helper.FuncUtil;

/**
 * @author zhang
 * sql 构建工具
 * @param <T>
 */
public class DBSqlCreater<T>{

	/**
	 * 查询字段
	 */
	private List<String> selectFields=new ArrayList<String>();
	/**
	 * 关联的字段（在where条件中使用）
	 */
	private Map<String,String> connectFields=new HashMap<String, String>();
	/**
	 * 需要使用like的字段（在where条件中使用）
	 */
	private List<String> likeFields=new ArrayList<String>();
	/**
	 * 作为日期的条件字段（在where条件中使用）
	 */
	private List<String> dateFeilds=new ArrayList<String>();
	/**
	 * 作为不等值的字段
	 */
	@SuppressWarnings("rawtypes")
	private Map<String,String[]> notEqFeilds=new HashMap<String,String[]>();
	/**
	 * 带查询的对象-也就是对应查询的表的pojo
	 */
	@SuppressWarnings("rawtypes")
	private List tableObjs=new ArrayList();
	/**
	 * 与表对应的pojo
	 */
	@SuppressWarnings("rawtypes")
	private Class tableClz;

	private boolean addZeroVal=false;
	/**
	 * 排序字段
	 */
	private String orderbyField;
	
	
	/**
	 * 查询时间段
	 */
	Map<String,BetDates> betDate;
	/**
	 * 增加查询字段
	 * @param clz
	 * @param selectFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addSelField(String selectFieldName) throws AIPGException{
		selectFields.add(getTableCln(tableClz,selectFieldName));
	}
	
	/**
	 * 增加查询字段
	 * @param clz
	 * @param selectFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addSelField(Class clz,String selectFieldName) throws AIPGException{
		selectFields.add(getTableCln(clz,selectFieldName));
	}
	/**
	 * 增加关联条件
	 * 多表关联查询的时候会用上，单表查询的时候用不上
	 * @param clz
	 * @param connectFieldName1 对应表中的字段名
	 * @param connectFieldName2 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addConnectField(Class clz,String connectFieldName1,Class clz2,String connectFieldName2) throws AIPGException{
		connectFields.put(getTableCln(clz,connectFieldName1),getTableCln(clz2,connectFieldName2));
	}
	/**
	 * 指定需要使用like 的字段
	 * @param clz
	 * @param likeFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addLiketField(String likeFieldName) throws AIPGException{
		likeFields.add(getTableCln(tableClz,likeFieldName));
	}
	/**
	 * 指定需要使用like 的字段
	 * @param clz
	 * @param likeFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addLiketField(Class clz,String likeFieldName) throws AIPGException{
		likeFields.add(getTableCln(clz,likeFieldName));
	}
	/**
	 * 指定查询条件类型为日期的字段
	 * @param clz
	 * @param dateFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addDateField(String dateFieldName) throws AIPGException{
		dateFeilds.add(getTableCln(tableClz,dateFieldName));
	}
	/**
	 * 指定查询条件类型为日期的字段
	 * @param clz
	 * @param dateFieldName 对应表中的字段名
	 * @throws AIPGException
	 */
	public void addDateField(Class clz,String dateFieldName) throws AIPGException{
		dateFeilds.add(getTableCln(clz,dateFieldName));
	}
	/**
	 * 增加关联查询的表对应的pojo
	 * @param obj
	 * @throws AIPGException
	 */
	public void addTableObj(Object obj) throws AIPGException{
		tableObjs.add(obj);
		this.tableClz=obj.getClass();
		if(tableObjs.size()>1){
			this.tableClz=null;
		}
	}
	
	/**
	 * 设置不等字段
	 * @param fieldName 表对应的字段名
	 * @param values 具体不等的值
	 * @throws AIPGException
	 */
	public void addNotEqField(String fieldName,String[] values) throws AIPGException{
		notEqFeilds.put(getTableCln(tableClz,fieldName), values);
	}
	/**
	 * 设置不等字段
	 * @param fieldName 表对应的字段名
	 * @param values 具体不等的值
	 * @throws AIPGException
	 */
	public void addNotEqField(Class clz,String fieldName,String[] values) throws AIPGException{
		notEqFeilds.put(getTableCln(clz,fieldName), values);
	}
	
	
	/**
	 * 构建 where 语句
	 * @param params 参数
	 * @param likeFeild 需要 使用like 的字段
	 * @param dateFeilds 数据类型为日期的字段
	 * @param t 数据对象
	 * @return
	 * @throws AIPGException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SqlParam buildWhereSql(T t) throws AIPGException{ //得到的sql 类似 and xxxx= and xxx=xx
		Map params = buildParams(t);
		String dateFormat="yyyymmdd hh24:mi:ss";//数据库的日期格式
		String dateFormat2="yyyyMMdd HH:mm:ss";//java的日期格式
		List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer whereSql=new StringBuffer();
        List sqlparams=new ArrayList();
        SqlParam sqlParam=new SqlParam();
        //日期类型的参数
        List dateParams=new ArrayList();
        List dateKey=new ArrayList();
//        //字符串类型的日期参数
//        List dateStrs=new ArrayList();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            StringBuffer tempStr=new StringBuffer();
            if(value!=null){
                 if(value instanceof String){
                	 String val=(String)value;
                	 if(!FuncUtil.isEmpty(val.trim())){
                		 String dateStr = val.trim();
                		 if(dateFeilds!=null&&dateFeilds.contains(key)){
                			 if(dateParams.size()==1){
                				 if(dateStr.length()==8){
                					 dateStr+=" 00:00:00";
                				 }
                				 Date date=FuncUtil.parseTime(dateStr, dateFormat2);
                				 if(date.after((Date)dateParams.get(0))){
                    				 dateParams.add(date);
                    				 dateKey.add(key);
                    			 }else{
                    				 dateParams.add(0,date);
                    				 dateKey.add(0,key);
                    			 }
                			 }else{
                				 dateParams.add(val.trim());
                				 dateKey.add(key);
                			 }
                		 }else if(likeFields!=null&&likeFields.contains(key)){
                     		 whereSql.append(" and ").append(key).append(" like ?");
                     		 sqlparams.add("%"+val.trim()+"%");
                     	 }else if(this.notEqFeilds!=null&&this.notEqFeilds.get(key)!=null){
                     		 String[] values=this.notEqFeilds.get(key);
                     		 tempStr.append(" and (");
                     		 for(String vl:values){
                     			tempStr.append(key).append(" <> ? and ");
                     			sqlparams.add(vl);
                     		 }
                     		 whereSql.append(tempStr.substring(0,  tempStr.length()-4)).append(")");
//                     		 System.err.println("**********"+tempStr.substring(0, tempStr.length()-4)+"**********");
                     	 }else{
                     		 whereSql.append(" and ").append(key).append(" = ?");
                     		 sqlparams.add(val.trim());
                     	 } 
                		 
                	 }
                 }else if(value instanceof Date){
                	 Date date=(Date)value;
                	 if(dateParams.size()==1){
                		 if(dateParams.get(0) instanceof Date){
                			 if(date.after((Date)dateParams.get(0))){
                				 dateParams.add(date);
                				 dateKey.add(key);
                			 }else{
                				 dateParams.add(0,date);
                				 dateKey.add(0,key);
                			 }
                		 }else if(dateParams.get(0) instanceof Timestamp){
                			 if(date.after((Timestamp)dateParams.get(0))){
                				 dateParams.add(date);
                				 dateKey.add(key);
                			 }else{
                				 dateParams.add(0,date);
                				 dateKey.add(0,key);
                			 }
                		 }else{
                			 throw new AIPGException("数据类型有误");
                		 }
                	 }else{
                		 dateParams.add(date); 
                		 dateKey.add(key);
                	 }
                 }else if(value instanceof Timestamp){
                	 Timestamp date=(Timestamp)value;
                	 if(dateParams.size()==1){
                		 if(dateParams.get(0) instanceof Date){
                			 if(date.after((Date)dateParams.get(0))){
                				 dateParams.add(date);
                				 dateKey.add(key);
                			 }else{
                				 dateParams.add(0,date);
                				 dateKey.add(0,key);
                			 }
                		 }else if(dateParams.get(0) instanceof Timestamp){
                			 if(date.after((Timestamp)dateParams.get(0))){
                				 dateParams.add(date);
                				 dateKey.add(key);
                			 }else{
                				 dateParams.add(0,date);
                				 dateKey.add(0,key);
                			 }
                		 }else{
                			 throw new AIPGException("数据类型有误");
                		 }
                	 }else{
                		 dateParams.add(date); 
                		 dateKey.add(key);
                	 }
                 }else if(value instanceof Integer){
                 	 whereSql.append(" and ").append(key).append(" = ?");
                 	 sqlparams.add((Integer)value);
                 }else if(value instanceof Float){
                 	 whereSql.append(" and ").append(key).append(" = ?");
                 	 sqlparams.add((Float)value);
                 }else if(value instanceof Double){
                 	 whereSql.append(" and ").append(key).append(" = ?");
                 	 sqlparams.add((Double)value);
                 }
            }
        }
	    //组装日期
        if(dateParams.size()==1){
        	if(dateParams.get(0) instanceof String){
        		 whereSql.append(" and ").append(dateKey.get(0)).append(" = to_date(?,'").append(dateFormat).append("')");
            	 sqlparams.add(dateParams.get(0));
        	}else{
        		 whereSql.append(" and ").append(dateKey.get(0)).append(" >= to_date(?,'").append(dateFormat).append("')");
        		 whereSql.append(" and ").append(dateKey.get(0)).append(" <= to_date(?,'").append(dateFormat).append("')");
	           	 if(dateParams.get(0) instanceof Date){
	           		 Date startDate = (Date) dateParams.get(0);
	           		 sqlparams.add(FuncUtil.formatTime(startDate, dateFormat2));
	           		 sqlparams.add(FuncUtil.formatTime(FuncUtil.nextDate(startDate),dateFormat2));
	           	 }else if(dateParams.get(0) instanceof Timestamp){
	           		Timestamp startDate = (Timestamp) dateParams.get(0);
	           		sqlparams.add(FuncUtil.formatTime(startDate, dateFormat2));
	           		sqlparams.add(FuncUtil.formatTime(FuncUtil.nextDate(startDate),dateFormat2));
	           	 }
        	}
        }
        if(dateParams.size()==2){
        	if(dateParams.get(0) instanceof String&&dateParams.get(1) instanceof String){
        		whereSql.append(" and ").append(dateKey.get(0)).append(" >= to_date(?,'").append(dateFormat).append("')");
        		whereSql.append(" and ").append(dateKey.get(1)).append(" <= to_date(?,'").append(dateFormat).append("')");
        		sqlparams.add(dateParams.get(0));
        		sqlparams.add(dateParams.get(1));
        	}else if((dateParams.get(0) instanceof Date||dateParams.get(0) instanceof Timestamp)&&dateParams.get(1) instanceof String){
        		whereSql.append(" and ").append(dateKey.get(0)).append(" >= ?");
        		whereSql.append(" and ").append(dateKey.get(1)).append(" <= to_date(?,'").append(dateFormat).append("')");
        		if(dateParams.get(0) instanceof Date){
              		 sqlparams.add((Date)dateParams.get(0));
       	       	}else if(dateParams.get(0) instanceof Timestamp){
       	       		 sqlparams.add((Timestamp)dateParams.get(0));
       	       	}
        		sqlparams.add(dateParams.get(1));
        	}else if((dateParams.get(1) instanceof Date||dateParams.get(1) instanceof Timestamp)&&dateParams.get(0) instanceof String){
        		whereSql.append(" and ").append(dateKey.get(0)).append(" >= to_date(?,'").append(dateFormat).append("')");
        		whereSql.append(" and ").append(dateKey.get(1)).append(" <= ?");
        		if(dateParams.get(1) instanceof Date){
              		 sqlparams.add((Date)dateParams.get(1));
       	       	}else if(dateParams.get(1) instanceof Timestamp){
       	       		 sqlparams.add((Timestamp)dateParams.get(1));
       	       	}
        		sqlparams.add(dateParams.get(0));
        	}else{
        		whereSql.append(" and ").append(dateKey.get(0)).append(" >= ?");
            	whereSql.append(" and ").append(dateKey.get(1)).append(" <= ?");
            	if(dateParams.get(0) instanceof Date){
           		 sqlparams.add((Date)dateParams.get(0));
    	       	}else if(dateParams.get(0) instanceof Timestamp){
    	       		 sqlparams.add((Timestamp)dateParams.get(0));
    	       	}
            	if(dateParams.get(1) instanceof Date){
            		sqlparams.add((Date)dateParams.get(1));
            	}else if(dateParams.get(1) instanceof Timestamp){
            		sqlparams.add((Timestamp)dateParams.get(1));
            	}
        	}
        }
        
        //时间段过滤
        if(betDate!=null&&betDate.size()>0){
        	Iterator<Map.Entry<String, BetDates>> bdate = betDate.entrySet().iterator();
        	while (bdate.hasNext()) {
    		   Map.Entry<String, BetDates> entry = bdate.next();
    		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
    		   if(entry.getValue().isStr()){
    			   if(entry.getValue().isDbTimeStr()){
    				   whereSql.append(" and ")
	       			    .append(" to_date(").append(entry.getKey()).append(",'yyyymmdd')")
	       			    .append(">= to_date(?,'yyyymmdd') and ")
	       			    .append(" to_date(").append(entry.getKey()).append(",'yyyymmdd')")
        			    .append(" <= to_date(?,'yyyymmdd') ");  
    			   }else{
    				   whereSql.append(" and ").append(entry.getKey())
        			   .append(">= to_date(?,' and ")
        			   .append(entry.getKey())
        			   .append(" <= to_date(?,'");  
    			   }
    			   sqlparams.add(entry.getValue().getStartDateTimeStr());
    			   sqlparams.add(entry.getValue().getEndDateTimeStr());
    		   }else{
    			   if(entry.getValue().isDbTimeStr()){
    				   whereSql.append(" and ")
        			    .append(" to_date(").append(entry.getKey()).append(",'yyyymmdd')")
        			    .append(">= ? and ")
        			    .append(" to_date(").append(entry.getKey()).append(",'yyyymmdd')")
        			    .append(" <= ? ");  
    			   }else{
    				   whereSql.append(" and ")
        			   .append(entry.getKey())
        			   .append(">= ? and ")
        			   .append(entry.getKey())
        			   .append(" <= ? ");  
    			   }
    			   sqlparams.add(entry.getValue().getStartDateTime());
    			   sqlparams.add(entry.getValue().getEndDateTime());
    		   }
        	}
        }
        
        if(!FuncUtil.isEmpty(orderbyField)){
        	whereSql.append(" order by ").append(orderbyField);
        }
        //返回对象
        sqlParam.setSql(whereSql.toString());
        sqlParam.setParams(sqlparams);
		return sqlParam;
	}
	
	
	
	/**
	 * 构建参数
	 * @param t 对象
	 * @param addZero 当字段为整型，并且值为零的时候，是否当作参数 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map buildParams(T t){
		Map params=new HashMap();
		Map retPrms=new HashMap();
		FuncUtil.cpObj2MapForDB(t, params);
		 Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
		  while (it.hasNext()) {
			   Map.Entry<String, String> entry = it.next();
			   retPrms.put(Auxs.underscoreName(entry.getKey()), entry.getValue());
		  }
		return retPrms;
	}
	
	
	/**
	 * 构造select 语句
	 * @param tableObjs 带查询的pojo 
	 * @param selectFields 要查询的字段 为空的时候查询全部 *
	 * @param connectFields 关联字段
	 * @return
	 * @throws AIPGException
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String buildSelect() throws AIPGException{
		StringBuffer tempSql=new StringBuffer("select ");
		StringBuffer selectSql=new StringBuffer();
		
		if(selectFields!=null&&selectFields.size()>0){
			for(String field:selectFields){
				tempSql.append(field).append(",");
			}
			selectSql.append(tempSql.substring(0, tempSql.length()-1));
		}else{
			tempSql.append(" * ");
			selectSql.append(tempSql);
		}
		tempSql=new StringBuffer();
		tempSql.append(" from ");
		for(Object table:tableObjs){
			tempSql.append(DBUtil.getTableName(table))
					 .append(" ")
					 .append(DBUtil.getTableAlis(table.getClass()))
					 .append(",");
		}
		
		selectSql.append(tempSql.substring(0, tempSql.length()-1));
		
		tempSql=new StringBuffer();
		tempSql.append(" where 1=1");
		if(connectFields!=null&&connectFields.size()>0){
			Iterator<Map.Entry<String, String>> it = connectFields.entrySet().iterator();
			  while (it.hasNext()) {
				   Map.Entry<String, String> entry = it.next();
				   tempSql.append(" and ").append(Auxs.underscoreName(entry.getKey())+"="+Auxs.underscoreName(entry.getValue()));
			  }
		}
		selectSql.append(tempSql);
		return selectSql.toString();
	}
	
	/**
	 * 构造count sql 语句
	 * @param tableObjs 带查询的pojo 
	 * @param selectFields 要查询的字段 为空的时候查询全部 *
	 * @param connectFields 关联字段
	 * @return
	 * @throws AIPGException
	 */
	public String buildCountSql() throws AIPGException{
		StringBuffer tempSql=new StringBuffer("select 1 ");
		StringBuffer selectSql=new StringBuffer();
		tempSql.append(" from ");
		for(Object table:tableObjs){
			tempSql.append(DBUtil.getTableName(table))
			.append(" ")
			.append(DBUtil.getTableAlis(table.getClass()))
			.append(",");
		}
		
		selectSql.append(tempSql.substring(0, tempSql.length()-1));
		tempSql=new StringBuffer();
		tempSql.append(" where 1=1");
		if(connectFields!=null&&connectFields.size()>0){
			Iterator<Map.Entry<String, String>> it = connectFields.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				tempSql.append(" and ").append(Auxs.underscoreName(entry.getKey())+"="+Auxs.underscoreName(entry.getValue()));
			}
		}
		selectSql.append(tempSql);
		return selectSql.toString();
	}
	
	
	/**
	 * 别名.字段
	 * @param fieldName
	 * @return
	 * @throws AIPGException
	 */
	public String getTableCln(Class clz,String fieldName) throws AIPGException{
		if(clz!=null){
			return DBUtil.getTableAlis(clz)+"."+fieldName;
		}else{
			return fieldName;
		}
		
	}
	
	public boolean isAddZeroVal() {
		return addZeroVal;
	}



	public void setAddZeroVal(boolean addZeroVal) {
		this.addZeroVal = addZeroVal;
	}



	public String getOrderbyField() {
		return orderbyField;
	}
	public void setOrderbyField(Class clz,String orderbyField,String ascOrDes) throws AIPGException {
		if(!FuncUtil.isEmpty(ascOrDes)){
			this.orderbyField = getTableCln(clz,orderbyField)+" "+ascOrDes;
		}else{
			setOrderbyField(clz, orderbyField);
		}
		
	}
	public void setOrderbyField(String orderbyField,String ascOrDes) throws AIPGException {
		if(!FuncUtil.isEmpty(ascOrDes)){
			this.orderbyField = getTableCln(this.tableClz,orderbyField)+" "+ascOrDes;
		}else{
			setOrderbyField(this.tableClz, orderbyField);
		}
		
	}
	public void setOrderbyField(Class clz,String orderbyField) throws AIPGException {
		this.orderbyField = getTableCln(clz,orderbyField);
	}
	
	public void setOrderbyField(String orderbyField) throws AIPGException {
		this.orderbyField = getTableCln(this.tableClz,orderbyField);
	}
	
	
	public Map<String, BetDates> getBetDate() {
		return betDate;
	}

	public void setBetDate(Map<String, BetDates> betDate) {
		this.betDate = betDate;
	}

	public Class getTableClz() {
		return tableClz;
	}
	public void setTableClz(Class tableClz) {
		this.tableClz = tableClz;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws AIPGException {
//		Map params=new HashMap();
//		params.put("name", "zhanggh");
//		params.put("age", 26);
//		params.put("thisYear","20141112");
//		params.put("birthday",FuncUtil.parseStampTime("19880210", "yyyyMMdd"));
//		params.put("name", "zhanggh");
//		params.put("alisName", "kanckzhang");
//		List<String> likeFields=new ArrayList<String>();
//		likeFields.add("alisName");
//		
//		List<String> dateFields=new ArrayList<String>();
//		dateFields.add("thisYear");
//		SqlParam sqlParam=buildWhereSql(params, likeFields, null,"");
		
//		System.err.println(sqlParam.getSql());
		
		
		 
	}
}
