package com.mtools.core.plugin.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import com.mtools.core.plugin.helper.Auxs;

/**
 * {@link RowMapper} implementation that converts a row into a new instance
 * of the specified mapped target class. The mapped target class must be a
 * top-level class and it must have a default or no-arg constructor.
 *
 * <p>Column values are mapped based on matching the column name as obtained from result set
 * metadata to public setters for the corresponding properties. The names are matched either
 * directly or by transforming a name separating the parts with underscores to the same name
 * using "camel" case.
 *
 * <p>Mapping is provided for fields in the target class for many common types, e.g.:
 * String, boolean, Boolean, byte, Byte, short, Short, int, Integer, long, Long,
 * float, Float, double, Double, BigDecimal, <code>java.util.Date</code>, etc.
 *
 * <p>To facilitate mapping between columns and fields that don't have matching names,
 * try using column aliases in the SQL statement like "select fname as first_name from customer".
 *
 * <p>For 'null' values read from the databasem, we will attempt to call the setter, but in the case of
 * Java primitives, this causes a TypeMismatchException. This class can be configured (using the
 * primitivesDefaultedForNullValue property) to trap this exception and use the primitives default value.
 * Be aware that if you use the values from the generated bean to update the database the primitive value
 * will have been set to the primitive's default value instead of null.
 *
 * <p>Please note that this class is designed to provide convenience rather than high performance.
 * For best performance consider using a custom RowMapper.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @since 2.5
 */
public class BeanPropRowMap<T> implements RowMapper<T> {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** The class we are mapping to */
	private Class<T> mappedClass;

	/** Whether we're strictly validating */
	private boolean checkFullyPopulated = false;

	/** Whether we're defaulting primitives when mapping a null value */
	private boolean primitivesDefaultedForNullValue = true;

	/** Map of the fields we provide mapping for */
	private Map<String, PropertyDescriptor> mappedFields;
	private Map<String, PropertyDescriptor> subMappedFields;
	private Map<String, Class> subMappedFieldsSuperClz;

	/** Set of bean properties we provide mapping for */
	private Set<String> mappedProperties;
	private Set<String> subMappedProperties;

	private PropertyDescriptor[] pds ;
	private List<PropertyDescriptor> subPds ;

	/**
	 * Create a new BeanPropertyRowMapper for bean-style configuration.
	 * @see #setMappedClass
	 * @see #setCheckFullyPopulated
	 */
	public BeanPropRowMap() {
	}

	/**
	 * Create a new BeanPropertyRowMapper, accepting unpopulated properties
	 * in the target bean.
	 * <p>Consider using the {@link #newInstance} factory method instead,
	 * which allows for specifying the mapped type once only.
	 * @param mappedClass the class that each row should be mapped to
	 */
	public BeanPropRowMap(Class<T> mappedClass) {
		initialize(mappedClass);
	}

	/**
	 * Create a new BeanPropertyRowMapper.
	 * @param mappedClass the class that each row should be mapped to
	 * @param checkFullyPopulated whether we're strictly validating that
	 * all bean properties have been mapped from corresponding database fields
	 */
	public BeanPropRowMap(Class<T> mappedClass, boolean checkFullyPopulated) {
		initialize(mappedClass);
		this.checkFullyPopulated = checkFullyPopulated;
	}


	/**
	 * Set the class that each row should be mapped to.
	 */
	public void setMappedClass(Class<T> mappedClass) {
		if (this.mappedClass == null) {
			initialize(mappedClass);
		}
		else {
			if (!this.mappedClass.equals(mappedClass)) {
				throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
						mappedClass + " since it is already providing mapping for " + this.mappedClass);
			}
		}
	}

	/**
	 * Initialize the mapping metadata for the given class.
	 * @param mappedClass the mapped class.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initialize(Class<T> mappedClass) {
		//vo
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		this.mappedProperties = new HashSet<String>();
		//子类使用的相关变量
		this.subMappedFields = new HashMap<String, PropertyDescriptor>();
		this.subMappedProperties = new HashSet<String>();
		this.subMappedFieldsSuperClz = new HashMap<String, Class>();;
		
		pds = BeanUtils.getPropertyDescriptors(mappedClass);
		subPds=new ArrayList<PropertyDescriptor>();
		List<Class> subClzs=new ArrayList<Class>();
		for (PropertyDescriptor pd : pds) {
//			 System.out.println("字段名："+pd.getDisplayName());
			 Type type = pd.getPropertyType().getGenericSuperclass();
			 
			 if(type!=null&&"class com.mtools.core.plugin.entity.BaseDbStruct".equals(type.toString())){
//				 System.out.println("type："+type);
				 Class clz = pd.getPropertyType();
				 subPds.add(pd);
				 subClzs.add(clz);
				 initializeExt(clz);
			 }
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
				this.mappedProperties.add(pd.getName());
			}
		}
	}
	/**
	 * Initialize the mapping metadata for the given class.
	 * @param mappedClass the mapped class.
	 */
	protected void initializeExt(Class<T> mappedClass) {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (PropertyDescriptor pd : pds) {
//			System.out.println("字段名："+pd.getDisplayName());
			if (pd.getWriteMethod() != null) {
				this.subMappedFields.put(pd.getName().toLowerCase(), pd);
				this.subMappedFieldsSuperClz.put(pd.getName(), mappedClass);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.subMappedFields.put(underscoredName, pd);
					this.subMappedFieldsSuperClz.put(underscoredName, mappedClass);
				}
				this.subMappedProperties.add(pd.getName());
			}
		}
	}

	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param name the string containing original name
	 * @return the converted name
	 */
	private String underscoreName(String name)
	{
		return Auxs.underscoreName(name);
	}

	/**
	 * Get the class that we are mapping to.
	 */
	public final Class<T> getMappedClass() {
		return this.mappedClass;
	}

	/**
	 * Set whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 * <p>Default is <code>false</code>, accepting unpopulated properties in the
	 * target bean.
	 */
	public void setCheckFullyPopulated(boolean checkFullyPopulated) {
		this.checkFullyPopulated = checkFullyPopulated;
	}

	/**
	 * Return whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 */
	public boolean isCheckFullyPopulated() {
		return this.checkFullyPopulated;
	}

	/**
	 * Set whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 * <p>Default is <code>false</code>, throwing an exception when nulls are mapped to Java primitives.
	 */
	public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
		this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
	}

	/**
	 * Return whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 */
	public boolean isPrimitivesDefaultedForNullValue() {
		return primitivesDefaultedForNullValue;
	}


	/**
	 * Extract the values for all columns in the current row.
	 * <p>Utilizes public setters and result set metadata.
	 * @see java.sql.ResultSetMetaData
	 */
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
			if (pd != null) {
				try {
					Object value = getColumnValue(rs, index, pd);
					if (logger.isDebugEnabled() && rowNumber == 0) {
						//logger.debug("Mapping column '" + column + "' to property '" +
						//		pd.getName() + "' of type " + pd.getPropertyType());
					}
					try {
						bw.setPropertyValue(pd.getName(), value);
					}catch (TypeMismatchException e) {
						if (value == null && primitivesDefaultedForNullValue) {
							logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
									" and column '" + column + "' with value " + value +
									" when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() +
									" on object: " + mappedObject);
						}
						else {
							throw e;
						}
					}
					if (populatedProperties != null) {
						populatedProperties.add(pd.getName());
					}
				}
				catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException(
							"Unable to map column " + column + " to property " + pd.getName(), ex);
				}
			} 
		}

		if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
					"necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
		}

		//构建子对象
		for(PropertyDescriptor pd:subPds){
			Object obj = mapRow2SubObj(rs, rowNumber, pd.getPropertyType());
			bw.setPropertyValue(pd.getName(), obj);
		}
		return mappedObject;
	}
	/**
	 * Extract the values for all columns in the current row.
	 * <p>Utilizes public setters and result set metadata.
	 * @see java.sql.ResultSetMetaData
	 */
	@SuppressWarnings("rawtypes")
	public Object mapRow2SubObj(ResultSet rs, int rowNumber,Class subClz) throws SQLException {
		Assert.state(subClz != null, "Mapped class was not specified");
		Object mappedObject = BeanUtils.instantiate(subClz);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);
		
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			PropertyDescriptor pd = this.subMappedFields.get(column.replaceAll(" ", "").toLowerCase());
			Class clz = this.subMappedFieldsSuperClz.get(column.replaceAll(" ", "").toLowerCase());
			if (pd != null&&clz==subClz) {
				try {
					Object value = getColumnValue(rs, index, pd);
					if (logger.isDebugEnabled() && rowNumber == 0) {
						//logger.debug("Mapping column '" + column + "' to property '" +
						//		pd.getName() + "' of type " + pd.getPropertyType());
					}
					try {
						bw.setPropertyValue(pd.getName(), value);
					}catch (TypeMismatchException e) {
						if (value == null && primitivesDefaultedForNullValue) {
							logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
									" and column '" + column + "' with value " + value +
									" when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() +
									" on object: " + mappedObject);
						}
						else {
							throw e;
						}
					}
					if (populatedProperties != null) {
						populatedProperties.add(pd.getName());
					}
				}
				catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException(
							"Unable to map column " + column + " to property " + pd.getName(), ex);
				}
			} 
		}
		
		if (populatedProperties != null && !populatedProperties.equals(this.subMappedProperties)) {
			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
					"necessary to populate object of class [" + subClz+ "]: " + this.subMappedProperties);
		}
		
		return mappedObject;
	}

	/**
	 * Initialize the given BeanWrapper to be used for row mapping.
	 * To be called for each row.
	 * <p>The default implementation is empty. Can be overridden in subclasses.
	 * @param bw the BeanWrapper to initialize
	 */
	protected void initBeanWrapper(BeanWrapper bw) {
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation calls
	 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
	 * Subclasses may override this to check specific value types upfront,
	 * or to post-process values return from <code>getResultSetValue</code>.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @param pd the bean property that each result object is expected to match
	 * (or <code>null</code> if none specified)
	 * @return the Object value
	 * @throws SQLException in case of extraction failure
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
	 */
	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
	}


	/**
	 * Static factory method to create a new BeanPropertyRowMapper
	 * (with the mapped class specified only once).
	 * @param mappedClass the class that each row should be mapped to
	 */
	public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
		BeanPropertyRowMapper<T> newInstance = new BeanPropertyRowMapper<T>();
		newInstance.setMappedClass(mappedClass);
		return newInstance;
	}

	 
}
