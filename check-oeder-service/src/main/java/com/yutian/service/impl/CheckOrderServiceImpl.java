/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.yutian.common.constant.Constant;
import com.yutian.common.enums.OrderTypeEnum;
import com.yutian.common.thread.LoadDataThread;
import com.yutian.common.util.RocksDbUtils;
import com.yutian.common.util.SetUtils;
import com.yutian.common.util.ThreadPoolFactory;
import com.yutian.service.CheckOrderService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            long ss = System.currentTimeMillis();
            logger.info("对账开始 >> payday = {}",payDay);
            long start = System.currentTimeMillis();
            logger.info("加载内部数据开始");
            List<String> innerMolds = loadInnerData(payDay, OrderTypeEnum.INNER.getValue());
            long end = System.currentTimeMillis();
            logger.info("加载内部数据完成,usetime = {}秒",(end - start) / 1000);

            start = System.currentTimeMillis();
            logger.info("加载外部部数据开始");
            List<String> outMolds =  loadInnerData(payDay, OrderTypeEnum.OUT.getValue());
            end = System.currentTimeMillis();
            logger.info("加载外部数据完成,usetime = {}秒",(end - start) / 1000);

            // 按膜取出数据集合
            Set<String> innerDatas = new HashSet<>(10000);
            Set<String> outDatas = new HashSet<>(10000);

            // 差异数据集合
            Set<String> innerDiffer = new HashSet<>();
            Set<String> outDiffer = new HashSet<>();
            int innerNum = 0;
            int outNum = 0;
            start = System.currentTimeMillis();
            logger.info("对账开始 >> ");
            for (int i = 0; i < innerMolds.size(); i++) {
                innerDatas = RocksDbUtils.getInstance().get(innerMolds.get(i));
                innerNum += innerDatas.size();
                outDatas = RocksDbUtils.getInstance().get(outMolds.get(i));
                outNum += outDatas.size();
                // 外部比内部少的订单
                innerDiffer.addAll(Sets.difference(innerDatas,outDatas));
                // 内部比外部少的订单
                outDiffer.addAll(Sets.difference(outDatas,innerDatas));
            }
            end = System.currentTimeMillis();
            logger.info("对账完成,usetime = {}秒",(end - start) / 1000);
            logger.info("订单数目核对，内部订单数 innerNum = {} 外部订单数目 outNum = {}",innerNum,outNum);
            logger.info("对账完成 >> 内部订单存在，外部不存在的订单数据 num = {} innerNoExist = {}", innerDiffer.size(),JSON.toJSON(innerDiffer));
            logger.info("对账完成 >> 外部订单存在，内部不存在的订单数据 num = {} outNoExist = {}", outDiffer.size(),JSON.toJSON(outDiffer));
            
            //外部订单撤销 会有正反交易
//            outDiffer.stream().filter(r -> !r.contains("REVOKED")).filter();
//            outDiffer.stream().filter(s -> )

            String cancleTrade = "";
            Iterator<String> iterator = outDiffer.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                if (next.contains("REVOKED")){
                    cancleTrade = next.split("REVOKED")[0];
                    iterator.remove();
                    continue;
                }
                if (next.contains(cancleTrade)){
                    iterator.remove();
                }
            }

            logger.info("对账完成 >> 内部订单存在，外部不存在的订单数据 num = {} innerNoExist = {}", innerDiffer.size(),JSON.toJSON(innerDiffer));
            logger.info("对账完成 >> 外部订单存在，内部不存在的订单数据 num = {} outNoExist = {}", outDiffer.size(),JSON.toJSON(outDiffer));


            //比较内部差异集合和外部差异集合是否存在一样的订单 即双方都存在此订单 但此订单的手续费不一致


        }catch (Exception e){
            logger.error("对账出现异常 >> error = {}", ExceptionUtils.getStackTrace(e));
        }

        return false;
    }

    private List<String> loadInnerData(String payDay,Integer orderType) throws InterruptedException {
        Map<String, Set<String>> setMap = new HashMap<>();
        // 1.解压文件
        File filePath;
        int noUseFile;
        String dbName = null;
        if (orderType == OrderTypeEnum.INNER.getValue()){
            String srcPath = "/Users/wengyuzhu/Desktop/check-order/WX_bank1_20190912030008.zip";
            String dePath = "/Users/wengyuzhu/Desktop/check-order/" + payDay + "/inner";
            filePath = ZipUtil.unzip(srcPath, dePath);
            noUseFile = 1;
            dbName = Constant.INNER_DB_NAME;
        }else {
            String srcPath = "/Users/wengyuzhu/Desktop/check-order/ylwx_trade_20190911.csv.zip";
            String dePath = "/Users/wengyuzhu/Desktop/check-order/" + payDay + "/out";
            filePath = ZipUtil.unzip(srcPath, dePath);
            noUseFile = 0;
            dbName = Constant.OUT_DB_NAME;
        }

        File[] files = filePath.listFiles();
        if(null == files || files.length == 0){
            throw new RuntimeException("账单数据为空");
        }
        logger.info("数据加载开始 >> fileSize = {}",files.length);
        // 2.多线程加载数据
        ExecutorService executorService = ThreadPoolFactory.getExecutorService();
        CountDownLatch latch = new CountDownLatch(files.length - noUseFile);
        for (File file : files) {
            if (file.getName().contains("gather")){
                continue;
            }
            LoadDataThread thread = new LoadDataThread(latch, dbName,file,payDay,setMap);
            executorService.execute(thread);
        }
        if (!latch.await(30, TimeUnit.MINUTES)){
            logger.error("加载数据超过30分钟");
        }
        logger.info("数据加载完成 >> moldnum = {}",setMap.size());

        // 3.数据写入rocksdb
        setMap.forEach((k,v) ->{
            RocksDbUtils.getInstance().put(k,v);
        });
        List<String> collect = setMap.keySet().stream().sorted().collect(Collectors.toList());
        setMap.clear();
        return collect;
    }
}