/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc;

import com.solid4j.xmen.common.extension.Adaptive;
import com.solid4j.xmen.common.extension.SPI;
import com.solid4j.xmen.common.rpc.URL;

import java.util.Map;

/**
 * @author solidwang
 * @since 1.0
 */
@SPI("xmen")
public interface Protocol<T> {

    @Adaptive
    Exporter<T> export(Invoker<T> invoker, Map<String, Object> map);
    
    @Adaptive
    Invoker<T> refer(Class<T> type, URL url);

    void destroy();
}
