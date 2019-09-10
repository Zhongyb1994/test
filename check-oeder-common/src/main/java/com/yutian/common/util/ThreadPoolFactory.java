/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  线程池工厂
 * @author wangyz
 * @version ThreadPoolFactory.java, v 0.1 2019-09-10 20:03
 */
public class ThreadPoolFactory {

    private ThreadPoolFactory(){};

    private static volatile ExecutorService pool;

    public static ExecutorService getExecutorService(){
        if (pool == null){
            synchronized (ThreadPoolFactory.class){
                int cpuNum = Runtime.getRuntime().availableProcessors();
                int threadNum = cpuNum * 2 + 1;
                pool = new ThreadPoolExecutor(threadNum,threadNum,10L, TimeUnit.MICROSECONDS,new LinkedBlockingDeque<>());
            }
        }
        return pool;
    }

}