/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.service;

import com.google.common.collect.Sets;
import com.yutian.service.impl.CheckOrderServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author wangyz
 * @version CheckServiceTest.java, v 0.1 2019-09-12 15:38
 */
public class CheckServiceTest {

//    private CheckOrderServiceImpl checkOrderService = new CheckOrderServiceImpl();


//    @Test
//    public void  test(){
//        checkOrderService.checkOrder("20190911");
//    }

    @Test
    public void test1(){
        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);


        Set<Integer> set2 = new HashSet<>();
        set2.add(1);
        set2.add(2);
        set2.add(4);

        Sets.SetView<Integer> difference = Sets.difference(set1, set2);
        Sets.SetView<Integer> difference1 = Sets.difference(set2, set1);
        System.out.println(difference);
        System.out.println(difference1);

    }

    @Test
    public void test2(){
        String s = "2019091123531201154012774290N|63.00|0.13|SUCCESS";
        String substring = s.substring(0, 35);
        System.out.println(substring);
    }

    @Test
    public void test3(){
        ArrayList<String> list = new ArrayList<>();
        for (String s : list) {
            System.out.println(s);
        }
    }
}