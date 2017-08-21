/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc;
import com.solid4j.xmen.common.extension.Adaptive;
import com.solid4j.xmen.common.extension.SPI;
import com.solid4j.xmen.common.rpc.URL;
/**
 * @author solidwang
 * @since 1.0
 */
@SPI("jdk")
public interface ProxyFactory {

    @Adaptive("proxy")
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url);

    @Adaptive("proxy")
    <T> T getProxy(Invoker<T> invoker);
}
