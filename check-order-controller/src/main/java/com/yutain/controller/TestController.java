/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutain.controller;

import com.yutian.service.CheckOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author wangyz
 * @version TestController.java, v 0.1 2019-09-08 15:27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private CheckOrderService checkOrderService;

    @RequestMapping("/demo")
    public String demo(@RequestParam(value = "payDay")String payday){
        logger.info("demo >> 测试工程环境");
        checkOrderService.checkOrder(payday);
        return "success";
    }


}