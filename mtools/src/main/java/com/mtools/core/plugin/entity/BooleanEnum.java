/**
 * 通联支付-研发中心
 * BooleanEnum.java
 * 2014-4-25
 */
package com.mtools.core.plugin.entity;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-25
 */
public enum BooleanEnum {
    TRUE(Boolean.TRUE, "是"), FALSE(Boolean.FALSE, "否");

    private final Boolean value;
    private final String info;

    private BooleanEnum(Boolean value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public Boolean getValue() {
        return value;
    }
}
