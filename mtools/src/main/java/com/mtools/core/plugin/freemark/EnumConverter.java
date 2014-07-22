/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-6-24
 * version 1.0
 * 说明：
 */
package com.mtools.core.plugin.freemark;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter extends AbstractConverter
{
  private final Class<?> enumClass;

  public EnumConverter(Class<?> enumClass)
  {
    this(enumClass, null);
  }

  public EnumConverter(Class<?> enumClass, Object defaultValue)
  {
    super(defaultValue);
    this.enumClass = enumClass;
  }

  protected Class<?> getDefaultType()
  {
    return this.enumClass;
  }

  protected Object convertToType(Class type, Object value)
  {
    String str = value.toString().trim();
    return Enum.valueOf(type, str);
  }

  protected String convertToString(Object value)
  {
    return value.toString();
  }
}