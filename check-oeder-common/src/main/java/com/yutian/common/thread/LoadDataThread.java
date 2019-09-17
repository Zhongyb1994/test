/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common.thread;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.yutian.common.constant.Constant;
import com.yutian.common.enums.OrderTypeEnum;
import com.yutian.common.enums.TradeTypeEnum;
import com.yutian.common.util.CSVUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 加载账单数据的线程
 *
 * @author wangyz
 * @version LoadDataThread.java, v 0.1 2019-09-12 09:03
 */
public class LoadDataThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(LoadDataThread.class);

    /**
     * 线程计数器
     */
    private CountDownLatch latch;

    /**
     * 对账单数据库
     */
    private String dbName;

    /**
     * 加载文件
     */
    private File file;

    /**
     * 交易日期
     */
    private String payDay;

    /**
     * 加载的所有数据存在此map中
     */
    private Map<String, Set<String>> setMap;

    public LoadDataThread(CountDownLatch latch, String dbName, File file, String payDay, Map<String, Set<String>> setMap) {
        this.latch = latch;
        this.dbName = dbName;
        this.file = file;
        this.payDay = payDay;
        this.setMap = setMap;
    }

    @Override
    public void run() {
        // 内部对账单和外部对账单格式可能不一样 所以要分开加载
        if (dbName.equals(Constant.INNER_DB_NAME)) {
            loadData(Constant.INNER_COLUMN_NO, OrderTypeEnum.INNER.getValue());
        }
        if (dbName.equals(Constant.OUT_DB_NAME)) {
            loadData(Constant.OUT_COLUMN_NO,OrderTypeEnum.OUT.getValue());
        }
    }

    /**
     * 加载数据
     *
     * @param columns 所需加载元数据的指定列集合
     */
    private void loadData(int[] columns,int type) {
        try {
            String[] row;
            List<String[]> rows = CSVUtil.parseCsvFile(file.getPath());
            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
                String orderNo = row[columns[0]];
                String mold = getMold(orderNo);
                Set<String> set = getSet(mold);
                // 一条账单数据 订单号|交易金额|平台方手续费|交易类型
                if (type == OrderTypeEnum.INNER.getValue()){
                    set.add(row[columns[0]] + "|"
                            + row[columns[1]] +"|" + row[columns[2]] + "|" + getTradeType(Integer.valueOf(row[columns[3]])));
                }else {
                    if (i == 1){
                        logger.info("外部数据 订单号 ordersn = {}",row[columns[0]]);
                    }
                    set.add(row[columns[0]] + "|"
                            + row[columns[1]] +"|" + row[columns[2]] + "|" + row[columns[3]]);
                }

            }
        } catch (Exception e) {
            logger.error("数据加载异常 payday = {},dbName = {},errorInfo = {}",payDay,dbName, ExceptionUtils.getStackTrace(e));
        } finally {
            latch.countDown();
        }

    }

    private String getTradeType(int value){
        if (value == TradeTypeEnum.TRADE.getValue()){
            return TradeTypeEnum.TRADE.getName();
        }

        return TradeTypeEnum.REFUND.getName();
    }


    /**
     * 根据订单创建时间进行取膜
     *
     * @param orderNo
     * @return
     */
    private String getMold(String orderNo) {
        String hour = orderNo.substring(8, 10);
        return dbName + payDay + hour;
    }


    /**
     * 根据膜取获取对账数据所在set
     *
     * @param mold
     * @return
     */
    private Set<String> getSet(String mold) {
        Set<String> set = setMap.get(mold);
        if (null == set) {
            synchronized (LoadDataThread.class){
                set = new HashSet<String>();
                setMap.put(mold, set);
            }
        }
        return set;
    }
}