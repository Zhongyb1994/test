/**
 * fshows.com
 * Copyright (C) 2013-2019All Rights Reserved.
 */
package com.yutian.service;

/**
 *
 * @author wangyz
 * @version CheckOrderService.java, v 0.1 2019-09-12 14:45
 */
public interface CheckOrderService {

    /**
     * 对账核心方法
     * @param payDay
     * @return
     */
    boolean checkOrder(String payDay);
}