/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.yutian.common.constant.Constant;
import com.yutian.common.enums.OrderTypeEnum;
import com.yutian.common.thread.LoadDataThread;
import com.yutian.common.util.ThreadPoolFactory;
import com.yutian.service.CheckOrderService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author wangyz
 * @version CheckOrderServiceImpl.java, v 0.1 2019-09-12 14:47
 */
@Service
public class CheckOrderServiceImpl implements CheckOrderService {

    private static Logger logger = LoggerFactory.getLogger(CheckOrderServiceImpl.class);

    /**
     * 对账核心方法
     * @param payDay
     * @return
     */
    @Override
    public boolean checkOrder(String payDay) {
        try {
            Map<String, Set<String>> innerMap = new HashMap<>();
            long start = System.currentTimeMillis();
            logger.info("加载内部数据开始");
            loadInnerData(payDay, OrderTypeEnum.INNER.getValue(),innerMap);
            long end = System.currentTimeMillis();
            logger.info("加载内部数据完成,usetime = {}秒",(end - start) / 1000);
            int num = 0;
            for (String s : innerMap.keySet()) {
                Set<String> set = innerMap.get(s);
                num += set.size();
            }
            logger.info("内部数据总量 num = {}",num);
        }catch (Exception e){
            logger.error("对账出现异常 >> error = {}", ExceptionUtils.getStackTrace(e));
        }

        return false;
    }

    private void loadInnerData(String payDay,Integer orderType,Map<String, Set<String>> setMap) throws InterruptedException {
        // 1.解压文件
        File filePath;
        if (orderType == OrderTypeEnum.INNER.getValue()){
            String srcPath = "/Users/wengyuzhu/Desktop/check-order/WX_bank1_20190912030008.zip";
            String dePath = "/Users/wengyuzhu/Desktop/check-order/" + payDay + "/inner";
            filePath = ZipUtil.unzip(srcPath, dePath);
        }else {
            String srcPath = "/Users/wengyuzhu/Desktop/check-order/ylwx_trade_20190911.csv.zip";
            String dePath = "/Users/wengyuzhu/Desktop/check-order/" + payDay + "/out";
            filePath = ZipUtil.unzip(srcPath, dePath);
        }

        File[] files = filePath.listFiles();
        if(null == files || files.length == 0){
            throw new RuntimeException("账单数据为空");
        }
        logger.info("数据加载开始 >> fileSize = {}",files.length);
        // 2.多线程加载数据
        ExecutorService executorService = ThreadPoolFactory.getExecutorService();
        CountDownLatch latch = new CountDownLatch(files.length - 1);
        for (File file : files) {
            if (file.getName().contains("gather")){
                continue;
            }
            LoadDataThread thread = new LoadDataThread(latch, Constant.INNER_DB_NAME,file,payDay,setMap);
            executorService.execute(thread);
        }
        if (!latch.await(30, TimeUnit.MINUTES)){
            logger.error("加载数据超过30分钟");
        }
        logger.info("数据加载完成 >> moldnum = {}",setMap.size());

    }
}