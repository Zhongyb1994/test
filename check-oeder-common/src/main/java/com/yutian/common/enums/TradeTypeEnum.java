/**
 * fshows.com
 * Copyright (C) 2013-2019All Rights Reserved.
 */
package com.yutian.common.enums;

/**
 *
 * @author wangyz
 * @version TradeTypeEnum.java, v 0.1 2019-09-16 22:00
 */
public enum TradeTypeEnum {

    TRADE(1,"SUCCESS"),
    REFUND(4,"REFUND");


    private int value;
    private String name;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    TradeTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}