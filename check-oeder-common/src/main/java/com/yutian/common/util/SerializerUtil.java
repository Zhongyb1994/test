/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.yutian.common.util;

import org.nustaq.serialization.FSTConfiguration;

/**
 * 序列化工具类
 * @author wangyz
 * @version SerializerUtil.java, v 0.1 2019-09-11 11:45
 */
public class SerializerUtil {
    static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();

    private SerializerUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) {
        return (T) configuration.asObject(data);
    }

    public static <T> byte[] serialize(T obj) {
        return configuration.asByteArray(obj);
    }
}