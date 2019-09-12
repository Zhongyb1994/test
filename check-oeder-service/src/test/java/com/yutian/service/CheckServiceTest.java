/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.service;

import com.yutian.service.impl.CheckOrderServiceImpl;
import org.junit.Test;

/**
 *
 * @author wangyz
 * @version CheckServiceTest.java, v 0.1 2019-09-12 15:38
 */
public class CheckServiceTest {

    private CheckOrderServiceImpl checkOrderService = new CheckOrderServiceImpl();


    @Test
    public void  test(){
        checkOrderService.checkOrder("20190911");
    }
}