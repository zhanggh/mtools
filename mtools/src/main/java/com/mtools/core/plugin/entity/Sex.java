/**
 * 通联支付-研发中心
 * Sex.java
 * 2014-4-25
 */
package com.mtools.core.plugin.entity;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-25
 */
public enum Sex {
    male("男"), female("女");

    private final String info;

    private Sex(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
