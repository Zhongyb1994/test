/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.yutian.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author wangyz
 * @version CSVUtil.java, v 0.1 2016-09-18 21:12 wangyz
 */
public class CSVUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);
    public static File createCSVFile(List exportData, LinkedHashMap rowMapper,
                                     String outPutPath, String filename) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFile = new File(outPutPath + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            // GB2312使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GBK"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream.write("\""
                        + propertyEntry.getValue().toString() + "\"");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                LinkedHashMap row = (LinkedHashMap) iterator.next();

                for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext(); ) {
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                    csvFileOutputStream.write("\""
                            + propertyEntry.getValue().toString() + "\"");
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return csvFile;
    }

    public static void main(String[] args) {
        List exportData = new ArrayList<Map>();
        Map row1 = new LinkedHashMap<String, String>();
        row1.put("1", "11");
        row1.put("2", "12");
        row1.put("3", "13");
        row1.put("4", "14");
        exportData.add(row1);
        row1 = new LinkedHashMap<String, String>();
        row1.put("1", "21");
        row1.put("2", "22");
        row1.put("3", "23");
        row1.put("4", "24");
        exportData.add(row1);
        List propertyNames = new ArrayList();
        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");
        CSVUtil.createCSVFile(exportData, map, "d:/", "12");
    }

    /**
     * 读取csv文件
     */
    public static List<String[]> parseCsvFile(String filePath) {
        List<String[]> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK"));
            //第一行信息，为标题信息，不用,如果需要，注释掉
            reader.readLine();
            String line = null;
            String[] items = null;
            while ((line = reader.readLine()) != null) {
                //去掉\t
                line = line.replace("\t", "");
                //去掉"
                line = line.replace("\"", "");
                // 去掉 '
                line = line.replace("`", "");
                //CSV格式文件为逗号分隔符文件，这里根据逗号切分
                items = line.split(",");
                list.add(items);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;

    }

}
