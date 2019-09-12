/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common;

import com.google.common.collect.Sets;
import com.yutian.common.util.RocksDbUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author wangyz
 * @version RocksDbTest.java, v 0.1 2019-09-10 20:41
 */
public class RocksDbTest {

    @Test
    public void  test(){
        RocksDbUtils db = RocksDbUtils.getInstance();
//        HashSet<String> set = Sets.newHashSet();
//        set.add("rose");
//        set.add("jack");
//        db.put("name",set);
//
//
//        HashSet<String> set1 = Sets.newHashSet();
//        set1.add("lucy");
//        set1.add("lili");
//
//        db.put("name",set1);

        Set<String> set2 = db.get("name");
        System.out.println(set2);
    }

    @Test
    public void test2(){
        String s = "201909011733012980050295222FS";
        String mold = String.valueOf(s.charAt(22));
        System.out.println(mold);
    }
}