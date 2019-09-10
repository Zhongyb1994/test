/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common.util;

import org.apache.commons.lang3.SystemUtils;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

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

    public static RocksDbUtils getInstance(){
        if (instance == null){
            synchronized (RocksDbUtils.class){
                instance = new RocksDbUtils();
            }
        }
        return instance;
    }

    public void  openDb(){
        try {
            rocksDB = RocksDB.open(BD_FILE);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}