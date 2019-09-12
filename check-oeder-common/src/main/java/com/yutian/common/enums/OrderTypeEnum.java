/**
 * fshows.com
 * Copyright (C) 2013-2019All Rights Reserved.
 */
package com.yutian.common.enums;

/**
 *
 * @author wangyz
 * @version OrderTypeEnum.java, v 0.1 2019-09-12 15:32
 */
public enum OrderTypeEnum {

    OUT(1,"外部账单"),
    INNER(2,"内部账单");

    private int value;
    private String name;

    OrderTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}