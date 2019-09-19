/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author wangyz
 * @version CSvUtilTest.java, v 0.1 2019-09-12 10:33
 */
public class CSvUtilTest {


//    @Test
//    public void  test1(){
//        String fileName = "/Users/wengyuzhu/Desktop/WX_bank1_20190912030008的副本/WX_bank1_20190912030008_refund.csv";
//        CsvData read = CsvUtil.getReader().read(new File(fileName), Charset.forName("GBK"));
//        List<CsvRow> rows = read.getRows();
//        for (int i = 0; i < 10; i++) {
//            CsvRow row = rows.get(i);
//            List<String> rawList = row.getRawList();
//            String orderSn = rawList.get(0);
//            System.out.println(orderSn);
//        }
//    }


    @Test
    public void test2(){
        String orderNo = "2019091101164801526042919446N";
        String hour = orderNo.substring(8, 10);
        System.out.println(hour);
    }
}