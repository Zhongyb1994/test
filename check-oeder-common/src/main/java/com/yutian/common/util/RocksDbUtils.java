/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common.util;

import org.apache.commons.lang3.SystemUtils;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.Set;

/**
 *
 * @author wangyz
 * @version RocksDbUtils.java, v 0.1 2019-09-10 20:19
 */
public class RocksDbUtils {

    private static volatile RocksDbUtils instance;

    private static String BD_FILE = SystemUtils.getUserHome() + SystemUtils.FILE_SEPARATOR + "rocksDb"
            + SystemUtils.FILE_SEPARATOR + "check-order";

    private RocksDB rocksDB;

    private RocksDbUtils(){
        openDb();
    }

    /**
     * 实力化工具类
     * @return
     */
    public static RocksDbUtils getInstance(){
        if (instance == null){
            synchronized (RocksDbUtils.class){
                instance = new RocksDbUtils();
            }
        }
        return instance;
    }

    /**
     * 打开数据库
     */
    public void  openDb(){
        try {
            rocksDB = RocksDB.open(BD_FILE);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库
     */
    public void close(){
        rocksDB.close();
    }

    /**
     * 存储数据
     * @param key
     * @param stringSet
     */
    public void  put(String key, Set<String> stringSet) {
        byte[] keyB = SerializerUtil.serialize(key);
        byte[] value = SerializerUtil.serialize(stringSet);
        try {
            rocksDB.put(keyB,value);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储数据
     * @param date 交易日期
     * @param dbName 按外部对账单和内部对账单不同存储
     * @param mold 取订单支付的小时为mold 将订单数据尽量均匀的拆分成小集合
     * @param stringSet 订单数据集合
     */
    public void  put(String date,String dbName,String mold,Set<String> stringSet){
        put(date + dbName + mold,stringSet);
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public Set<String> get(String key){
        byte[] keyb = SerializerUtil.serialize(key);
        Set<String> set = null;
        try {
            byte[] bytes = rocksDB.get(keyb);
            set = SerializerUtil.deserialize(bytes);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return set;
    }

    /**
     * 删除数据
     * @param key
     */
    public void delete(String key){
        byte[] keyb = SerializerUtil.serialize(key);
        try {
            rocksDB.delete(keyb);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}