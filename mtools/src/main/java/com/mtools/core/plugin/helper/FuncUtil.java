package com.mtools.core.plugin.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

public class FuncUtil {
	public static XmlConverter xmlConvert = new XmlConverter();

	 private static DecimalFormat df = new DecimalFormat("#0.00");

	  @SuppressWarnings("unchecked")
	public static String[] split(String line, char split_char)
	  {
	    @SuppressWarnings("rawtypes")
		List segments = new ArrayList();

	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < line.length(); i++) {
	      char c = line.charAt(i);
	      if (c == split_char) {
	        segments.add(dropNull(sb.toString()));
	        sb = new StringBuffer();
	      } else {
	        sb.append(c);
	      }
	    }
	    segments.add(sb.toString());

	    return (String[])segments.toArray(new String[0]);
	  }

	  public static String dropNull(String input) {
	    if (input == null) {
	      input = "";
	    }
	    return input.trim();
	  }

	 
	  public static Date parseTime(String datestr, String formatter) throws AIPGException {
	    AIPGException oe;
	    try { SimpleDateFormat sdf = new SimpleDateFormat(formatter);
	      return sdf.parse(datestr);
	    } catch (Exception ex)
	    {
	      oe = new AIPGException("时间解析错误:" + ex.getMessage());
	      oe.setRootCause(ex);
	    }throw oe;
	  }
	  public static Timestamp parseStampTime(String datestr, String formatter) throws AIPGException {
	    AIPGException oe;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat(formatter);
	      return new Timestamp(sdf.parse(datestr).getTime());
	    }
	    catch (Exception ex) {
	      oe = new AIPGException("时间解析错误:" + ex.getMessage());
	      oe.setRootCause(ex);
	    }throw oe;
	  }

	  public static Timestamp parseStampTime1(String datestr)
	  {
	    if (isEmpty(datestr))
	      return null;
	    try {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	      return new Timestamp(sdf.parse(datestr).getTime());
	    } catch (Exception ex) {
	    }
	    return null;
	  }

	  public static String getDealType(String dealtype_signal) {
	    if (dealtype_signal.equals("S"))
	      return "101003";
	    if (dealtype_signal.equals("F"))
	      return "101104";
	    return null;
	  }

	public static boolean isEmpty(String str, String fieldName)
			throws AIPGException {
		if (str == null || "".equals(str)) {
			throw new AIPGException(fieldName + "不能为空!");
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 * @author: Vinci
	 * @time: 2012-5-3 下午03:31:08
	 * @modifylog:
	 */
	public static Timestamp getCurrTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public static String getRandomCode(int count) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		// System.out.println(getRandomCode(6));
		// System.out.println("abcdefghijklmnopqrstuvwxyz".toUpperCase());
		//
		String pth = "/login/fshfkjsd/";
//		System.out.println(checkStr(pth, "/login/fshfkjsd/"));
		
		System.out.println(FuncUtil.idcardVerify("440881198802103838"));

	}

	/**
	 * 根据路径创建一系列的目录
	 * 
	 * @param path
	 */
	public static void mkDir(String path) {
		File file;
		try {
			file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
	}

	/**
	 * 文件名过滤特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String SpecStrFilter(String str) {
		String regEx = "[*:\\/?<>|\"]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("_").trim();
	}

	public static String formatTime(Date date, String formatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}

	public static String formatTime(Timestamp date, String formatter) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		Date date_new = new Date(date.getTime());
		return sdf.format(date_new);
	}

	public static Timestamp paresTime(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(sdf.parse(strDate).getTime());
		return ts;
	}

	/**
	 * 功能：截取部分字符串
	 * 2014-6-30
	 */
	public static String getPexfStr(String text, int length, String endWith) {
		int textLength = text.length();
		int byteLength = 0;
		StringBuffer returnStr = new StringBuffer();
		for (int i = 0; i < textLength && byteLength < length * 2; i++) {
			String str_i = text.substring(i, i + 1);
			if (str_i.getBytes().length == 1) {// 英文
				byteLength++;
			} else {// 中文
				byteLength += 2;
			}
			returnStr.append(str_i);
		}
		try {
			if (byteLength < text.getBytes("GBK").length) {// getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3
				returnStr.append(endWith);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnStr.toString();

	}

	public static boolean checkStr(String org, String reg) {
		String regular = "\\/[a-z|A-Z|0-9]*";//
		if (reg != null && !"".equals(reg))
			regular = reg;
		Pattern emailer = Pattern.compile(regular);
		if (emailer.matcher(org).matches())
			return true;
		else
			return false;
	}

	public static String htmlFilter(String html) {
		return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	  public static boolean isNumber(String str)
	  {
	    if (isEmpty(str))
	      return false;
	    boolean flag = false;
	    Pattern pat = Pattern.compile("^[0-9|.]+$");
	    Matcher mat = null;
	    mat = pat.matcher(str);
	    flag = mat.matches();
	    return flag;
	  }

	  public static BigDecimal ftoy(BigDecimal num) {
	    return num.divide(new BigDecimal(100), 2, 2);
	  }

	  public static BigDecimal ftoy(long num) {
	    return ftoy(new BigDecimal(num));
	  }

	  public static BigDecimal ftoy(String num) {
	    return ftoy(new BigDecimal(num));
	  }

	  public static BigDecimal ytof(String y) {
	    return new BigDecimal(Math.round(new BigDecimal(y).multiply(new BigDecimal(100)).doubleValue()));
	  }

	  public static BigDecimal ytof(double y)
	  {
	    return ytof(String.valueOf(y));
	  }

	  public static BigDecimal ytof(BigDecimal y) {
	    return ytof(y.toString());
	  }

	  public static SortedMap<String, String> mapSortByKey(Map<String, String> unsort_map)
	  {
	    TreeMap result = new TreeMap();
	    Object[] unsort_key = unsort_map.keySet().toArray();
	    Arrays.sort(unsort_key);
	    for (int i = 0; i < unsort_key.length; i++) {
	      result.put(unsort_key[i].toString(), (String)unsort_map.get(unsort_key[i]));
	    }
	    return result.tailMap((String)result.firstKey());
	  }

	  

	  public static String formatFillStr(int dir, String str, String fillinstr, int length)
	  {
	    if (str == null)
	      str = "";
	    String tmp = new String(str);
	    byte[] bt = tmp.getBytes();
	    if (dir == 1) {
	      for (int i = bt.length; i < length; i++)
	        tmp = fillinstr + tmp;
	    }
	    else if (dir == 2) {
	      for (int i = bt.length; i < length; i++)
	        tmp = tmp + fillinstr;
	    }
	    return tmp;
	  }

	  public static String buildInStr(Object[] str)
	  {
	    if ((str == null) || (str.length == 0))
	      return null;
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < str.length; i++) {
	      if ((str[i] == null) || (str[i].toString().trim().length() == 0))
	        continue;
	      sb.append("'");
	      sb.append(str[i]);
	      sb.append("',");
	    }
	    String tmp = sb.toString();
	    if ((tmp != null) && (tmp.length() > 1)) {
	      return tmp.substring(0, tmp.length() - 1);
	    }
	    return null;
	  }

	  public static String buildInStrNoDH(Object[] str)
	  {
	    if ((str == null) || (str.length == 0))
	      return null;
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < str.length; i++) {
	      if ((str[i] == null) || (str[i].toString().trim().length() == 0))
	        continue;
	      sb.append(str[i]);
	      sb.append(",");
	    }
	    String tmp = sb.toString();
	    if ((tmp != null) && (tmp.length() > 1)) {
	      return tmp.substring(0, tmp.length() - 1);
	    }
	    return null;
	  }

	  public static String buildInStr(String str, String spliter)
	  {
	    if ((str == null) || (str.trim().length() == 0))
	      return null;
	    return buildInStr(str.split(spliter));
	  }

	  public static String buildInStr(List list)
	  {
	    if ((list == null) || (list.size() == 0))
	      return null;
	    return buildInStr(list.toArray());
	  }

	  public static String buildInStr(String str, char spliter)
	  {
	    if ((str == null) || (str.trim().length() == 0))
	      return null;
	    return buildInStr(str.split(String.valueOf(spliter)));
	  }

	  public static Date getTimeOffset(Date date, long offset)
	  {
	    if (date == null)
	      return null;
	    return new Date(date.getTime() + offset);
	  }

	  public static boolean isEmpty(Object[] val)
	  {
	    if ((val == null) || (val.length == 0)) {
	      return true;
	    }
	    for (int i = 0; i < val.length; i++) {
	      if ((val[i] != null) && (!"".equals(val[i]))) {
	        return false;
	      }
	    }
	    return true;
	  }

	  public static boolean checkMobile(String mobile)
	  {
	    if (isEmpty(mobile)) {
	      return true;
	    }
	    Pattern pattern = Pattern.compile("^(1[3|4|5|8|9])\\d{9}$");
	    Matcher matcher = pattern.matcher(mobile);
	    return matcher.matches();
	  }

	  public static boolean removeFile(File path)
	  {
	    boolean result = false;
	    System.out.println("删除文件:" + path.getPath());
	    if (path.isDirectory()) {
	      File[] child = path.listFiles();
	      if ((child != null) && (child.length != 0)) {
	        for (int i = 0; i < child.length; i++) {
	          if (!removeFile(child[i])) {
	            System.out.println("删除文件:" + child[i] + " 失败");
	            return false;
	          }
	          child[i].delete();
	        }
	      }
	    }
	    path.delete();
	    result = true;
	    return result;
	  }

	  public static void copyFile(File sourceFile, File targetFile) throws IOException
	  {
	    BufferedInputStream inBuff = null;
	    BufferedOutputStream outBuff = null;
	    try
	    {
	      inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

	      outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

	      byte[] b = new byte[5120];
	      int len;
	      while ((len = inBuff.read(b)) != -1)
	      {
	        outBuff.write(b, 0, len);
	      }

	      outBuff.flush();
	    }
	    finally {
	      if (inBuff != null)
	        inBuff.close();
	      if (outBuff != null)
	        outBuff.close();
	    }
	  }

	  public static String formatNumber(BigDecimal number)
	  {
	    return formatNumber(number.doubleValue());
	  }

	  public static String formatNumber(double number)
	  {
	    DecimalFormat df1 = (DecimalFormat)DecimalFormat.getInstance();
	    df1.applyPattern("0.00");
	    return df1.format(number);
	  }

	  public static String formatNumber1(BigDecimal number, String pattern)
	  {
	    return formatNumber1(number.doubleValue(), pattern);
	  }

	  public static String formatNumber1(double number, String pattern)
	  {
	    DecimalFormat df1 = (DecimalFormat)DecimalFormat.getInstance();
	    if (isEmpty(pattern)) {
	      pattern = "#,##0.00";
	    }
	    df1.applyPattern(pattern);
	    return df1.format(number);
	  }

	  public static BigDecimal parseString(String source, String pattern) throws ParseException {
	    DecimalFormat df1 = (DecimalFormat)DecimalFormat.getInstance();
	    if (isEmpty(pattern)) {
	      pattern = "#,##0.00";
	    }
	    df1.applyPattern(pattern);
	    return new BigDecimal(df1.parse(source).doubleValue());
	  }

	  public static String shieldAccount(int head, int end, boolean isshield, String account)
	  {
	    if ((account == null) || ("".equals(account))) {
	      return "";
	    }
	    int total = head + end;
	    if (account.length() <= total) {
	      if (isshield) {
	        int length = account.length();
	        StringBuilder sb = new StringBuilder();
	        while (length > 0) {
	          sb.append("*");
	          length--;
	        }
	        return sb.toString();
	      }
	      return account;
	    }

	    int sAccount = 4;
	    StringBuilder sb = new StringBuilder();
	    while (sAccount > 0) {
	      sb.append("*");
	      sAccount--;
	    }
	    return account.substring(0, head) + sb.toString() + account.substring(account.length() - end);
	  }

	  public static boolean checkIdcard(String idcard)
	  {
	    boolean valid = false;
	    if ((idcard != null) && (!"".equals(idcard.trim()))) {
	      if (idcard.length() == 18) {
	        valid = isNumber(idcard.substring(0, idcard.length() - 1));
	        if (valid) {
	          valid = isNumber(idcard.substring(idcard.length() - 1));
	          if ((!valid) && (
	            (idcard.endsWith("x")) || (idcard.endsWith("X")))) {
	            valid = true;
	          }
	        }
	      }
	      else if (idcard.length() == 15) {
	        valid = isNumber(idcard);
	      }
	    }
	    return valid;
	  }

	  public static String join(String[] array) {
	    if ((array == null) || (array.length == 0)) {
	      return "";
	    }
	    StringBuilder sb = new StringBuilder();
	    String[] arrayOfString = array; int j = array.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
	      if (sb.length() > 0) {
	        sb.append(",");
	      }
	      sb.append(s);
	    }
	    return sb.toString();
	  }

	 public static String getCellValue2(HSSFCell cell) {
	    String value = "";
	    if (cell == null)
	      return "";
	    switch (cell.getCellType()) {
	    case 1:
	      value = cell.getStringCellValue();
	      break;
	    case 0:
	      double tp = Double.valueOf(cell.getNumericCellValue()).doubleValue();
	      value = String.format("%.0f", new Object[] { Double.valueOf(tp) });
	      break;
	    case 2:
	      value = cell.getCellFormula();
	      break;
	    case 5:
	      value = String.valueOf(cell.getErrorCellValue());
	      break;
	    case 4:
	      value = String.valueOf(cell.getBooleanCellValue());
	      break;
	    case 3:
	      value = "";
	      break;
	    }

	    return value;
	  }

	  public static boolean isChinese(String strVal)
	  {
	    int iRnt = 0;
	    boolean rs = false;
	    for (int i = 0; i < strVal.length(); i++) {
	      String strHanzi = strVal.substring(i, i + 1);
	      byte[] bytes = (byte[])null;
	      try {
	        bytes = strHanzi.getBytes("gbk");
	      } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	        return false;
	      }
	      if ((bytes == null) || (bytes.length > 2) || (bytes.length <= 0) || 
	        (bytes.length == 1))
	      {
	        iRnt++;
	      }
	      if (bytes.length == 2) {
	        return true;
	      }
	    }
	    rs = false;
	    return rs;
	  }
	  
	  public static int bgCompare(BigDecimal val1, BigDecimal val2)
	  {
	    return val1.compareTo(val2);
	  }

	  public static byte[] getBytesFromIS(InputStream is) throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int b = 0;

	    while ((b = is.read()) != -1) {
	      baos.write(b);
	    }
	    return baos.toByteArray();
	  }

	  public static String Round(BigDecimal num)
	  {
	    DecimalFormat df = new DecimalFormat("0.00");
	    String str = df.format(num);
	    return str;
	  }

	  public static String RoundAndScale(BigDecimal num)
	  {
	    num = num.divide(new BigDecimal("100.00"));
	    DecimalFormat df = new DecimalFormat("0.00");
	    String str = df.format(num);
	    return str;
	  }

	  public static String RoundAndScalePer(BigDecimal num)
	  {
	    DecimalFormat df = new DecimalFormat("0.00");
	    String str = df.format(num.multiply(new BigDecimal("100.00")));
	    return str;
	  }

	  public static String getSha1Str(File file)
	  {
	    String str = "";
	    try {
	      MessageDigest md = MessageDigest.getInstance("SHA-1");
	      FileInputStream fis = new FileInputStream(file);
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      byte[] buf = new byte[2048];
	      int c;
	      while ((c = fis.read(buf)) > 0)
	      {
	        baos.write(buf, 0, c);
	      }
	      str = new BASE64Encoder().encode(md.digest(baos.toByteArray()));
	      fis.close();
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	    }
	    return str;
	  }

	  public static String bytesToHexString(byte[] src) {
	    StringBuilder stringBuilder = new StringBuilder("");
	    if ((src == null) || (src.length <= 0)) {
	      return null;
	    }
	    for (int i = 0; i < src.length; i++) {
	      int v = src[i] < 0 ? src[i] + 256 : src[i];
	      String hv = Integer.toHexString(v);

	      stringBuilder.append(hv);
	    }
	    return stringBuilder.toString();
	  }

 
	  public static <T> T formMaptoBean(T obj, Map<String, Object> map, Class<T> clazz) throws Exception {
	    return formMaptoBean(obj, map, clazz, null);
	  }

	  public static <T> T formMaptoBean(T obj, Map<String, Object> map, Class<T> clazz, String timeformatter) throws Exception {
	    if (obj == null) {
	      obj = clazz.newInstance();
	    }

	    Map fieldMap = new HashMap();
	    Field[] fields = getAllDeclaredFields(clazz);

	    if (fields == null) return obj;

	    for (Field field : fields) {
	      fieldMap.put(field.getName().toLowerCase(), field);
	    }

	    for (Map.Entry entry : map.entrySet()) {
	      String key = (String)entry.getKey();
	      Object value = entry.getValue();
	      try {
	        String mothedName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase();
	        Class valueClass = fieldMap.get(key.toLowerCase()) == null ? null : ((Field)fieldMap.get(key.toLowerCase())).getType();
	        if (valueClass != null) {
	          Method method = clazz.getMethod(mothedName, new Class[] { valueClass });

	          if (method != null)
	            method.invoke(obj, new Object[] { getObject(valueClass, value, timeformatter) });
	        }
	      } catch (Exception localException) {
	      }
	    }
	    return obj;
	  }

	  private static Field[] getAllDeclaredFields(Class c)
	  {
	    if (c.equals(Object.class)) {
	      return null;
	    }

	    Field[] parentFields = getAllDeclaredFields(c.getSuperclass());
	    Field[] thisFields = c.getDeclaredFields();

	    if ((parentFields != null) && (parentFields.length > 0)) {
	      Field[] allFields = 
	        new Field[parentFields.length + thisFields.length];
	      System.arraycopy(parentFields, 0, allFields, 0, 
	        parentFields.length);
	      System.arraycopy(thisFields, 0, allFields, parentFields.length, 
	        thisFields.length);

	      thisFields = allFields;
	    }

	    return thisFields;
	  }

	  private static Object getObject(Class<?> clzz, Object value, String timeformatter)
	    throws Exception
	  {
	    if (value == null) return null;
	    if (clzz == Long.class)
	    {
	      return new Long(value.toString());
	    }
	    if (clzz == Boolean.class) {
	      return new Boolean(value.toString());
	    }
	    if (clzz == Byte.class) {
	      return new Byte(value.toString());
	    }
	    if (clzz == Short.class) {
	      return new Short(value.toString());
	    }
	    if (clzz == Integer.class) {
	      return new Integer(value.toString());
	    }
	    if (clzz == Float.class) {
	      return new Float(value.toString());
	    }
	    if (clzz == Double.class) {
	      return new Double(value.toString());
	    }
	    if (clzz == String.class) {
	      return new String(value.toString());
	    }

	    if (clzz == Byte.TYPE) {
	      return Byte.valueOf(Byte.parseByte(value.toString()));
	    }
	    if (clzz == Short.TYPE) {
	      return Short.valueOf(Short.parseShort(value.toString()));
	    }
	    if (clzz == Integer.TYPE) {
	      return Integer.valueOf(Integer.parseInt(value.toString()));
	    }
	    if (clzz == Long.TYPE) {
	      return Long.valueOf(Long.parseLong(value.toString()));
	    }
	    if (clzz == Float.TYPE) {
	      return Float.valueOf(Float.parseFloat(value.toString()));
	    }
	    if (clzz == Double.TYPE) {
	      return Double.valueOf(Double.parseDouble(value.toString()));
	    }
	    if (clzz == Boolean.TYPE) {
	      return Boolean.valueOf(Boolean.parseBoolean(value.toString()));
	    }

	    if (clzz == Timestamp.class) {
	      return parseStampTime(value.toString(), timeformatter);
	    }

	    if (clzz == Date.class) {
	      return parseTime(value.toString(), timeformatter);
	    }
	    return value;
	  }

	  public static String toXML(Object obj) {
	    XStream xstream = new XStream();
	    return xstream.toXML(obj);
	  }

	  public static Object fromXML(String xml) {
	    Object obj = null;
	    try {
	      XStream xstream = new XStream();
	      obj = xstream.fromXML(xml);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return obj;
	  }

	  public static String subStr(String str, int subSLength)
	  {
	    if (str == null)
	      return "";
	    try
	    {
	      byte[] strbyte = str.getBytes("GBK");
	      int subStrByetsL = strbyte.length;
	      if (subStrByetsL <= subSLength) {
	        return str;
	      }
	      String subStr = new String(strbyte, 0, subSLength + 1, "GBK");
	      subStr = subStr.substring(0, subStr.length() - 1);
	      return subStr;
	    } catch (Exception e) {
	      e.printStackTrace();
	    }return null;
	  }
	  
	  
	  /**
	 * 功能：最严格的身份证验证
	 * 2014-7-6
	 */
	public static boolean idcardVerify(String certificateNo){
//		  String certificateNo = "61072919761109762X";//身份证号码
          if(certificateNo.length() == 15){
              System.err.println("身份证号码无效，请使用第二代身份证！！！");
              return false;
          }else if(certificateNo.length() == 18){
              String address = certificateNo.substring(0,6);//6位，地区代码
              String birthday = certificateNo.substring(6,14);//8位，出生日期
              String sequenceCode =  certificateNo.substring(14,17);//3位，顺序码：奇为男，偶为女
              String checkCode =  certificateNo.substring(17);//1位，校验码：检验位
              System.out.println("身份证号码:"+certificateNo+"、地区代码:"+address+"、出生日期:"+birthday+"、顺序码:"+sequenceCode+"、校验码:"+checkCode+"\n");            
              String [] provinceArray = {"11:北京","12:天津","13:河北","14:山西","15:内蒙古","21:辽宁","22:吉林","23:黑龙江","31:上海","32:江苏","33:浙江","34:安徽", "35:福建","36:江西","37:山东","41:河南","42:湖北 ","43:湖南","44:广东","45:广西","46:海南","50:重庆","51:四川","52:贵州","53:云南","54:西藏 ","61:陕西","62:甘肃","63:青海","64:宁夏", "65:新疆","71:台湾","81:香港","82:澳门","91:国外"};
              boolean valideAddress =false;
              for (int i = 0; i < provinceArray.length; i++) {
                  String provinceKey =provinceArray[i].split(":")[0];
                  if (provinceKey.equals(address.substring(0,2))){
                          valideAddress = true;
                  }
              }
              if (valideAddress) {
                  String year =  birthday.substring(0,4);  
                  String month =birthday.substring(4,6);  
                  String day =birthday.substring(6);
                  Date tempDate = new Date(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                  if((tempDate.getYear()!=Integer.parseInt(year)|| tempDate.getMonth()!=Integer.parseInt(month)-1 || tempDate.getDate()!=Integer.parseInt(day))){//避免千年虫问题
                      System.err.println("身份证号码无效，请重新输入！！！");
                  }else{
                      int [] weightedFactors = { 7, 9, 10, 5, 8, 4, 2,1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };//加权因子  
                      int [] valideCode = { 1, 0, 10, 9, 8, 7, 6, 5,4, 3, 2 };//身份证验证位值，其中10代表X
                      int sum = 0;//声明加权求和变量
                      String []certificateNoArray =new String[certificateNo.length()];
                      for (int i = 0; i < certificateNo.length(); i++) {
                              certificateNoArray[i] =String.valueOf(certificateNo.charAt(i));
                      }                     
                      if ("x".equals(certificateNoArray[17].toLowerCase())) {
	                      certificateNoArray[17] ="10";//将最后位为x的验证码替换为10 
	                  }
	                  for (int i = 0; i < 17; i++) {
	                          sum += weightedFactors[i]* Integer.parseInt(certificateNoArray[i]);//加权求和  
	                  }
	                  int valCodePosition = sum % 11;//得到验证码所在位置
	                  if (Integer.parseInt(certificateNoArray[17])== valideCode[valCodePosition]) {
	                          String sex = "男";
	                          if(Integer.parseInt(sequenceCode)%2==0){
	                                  sex = "女";
	                          }
	                          System.out.println("身份证号码有效，性别为："+sex+"！");
	                          return true;
	                   } else {
	                        System.err.println("身份证号码无效，请重新输入！！！");
	                  }
	              }
	          } else {
                  System.err.println("身份证号码无效，请重新输入！！！");
	          }  
          }else{
              System.err.println("非身份证号码，请输入身份证号码！！！");
          }
		  return false;
	  }
	
	/**
	 * 功能：通过浏览器下载资源文件
	 * 2014-7-16
	 */
	public void downloadFiles(HttpServletRequest request, HttpServletResponse response,String filename,byte[] bytes) throws IOException{
		String agent = request.getHeader("USER-AGENT");
		if(agent != null && agent.indexOf("MSIE") != -1) {
			filename = URLEncoder.encode(filename, "UTF8");
			filename = filename.replaceAll("\\+", "%20");
		}else {
			filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
	    }
		response.setHeader("Content-Disposition","attachment;filename=" + filename);
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
	}
}
